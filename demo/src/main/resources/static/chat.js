// 检查登录状态，未登录则跳转到登录页
fetch('/auth/me').then(res => res.json()).then(data => {
    if (!data || data.code !== 0) {
        window.location.href = '/auth.html';
    }
});

const chatMain = document.getElementById('chat-main');
const chatForm = document.getElementById('chat-form');
const chatInput = document.getElementById('chat-input');


function getTidFromUrl() {
    const params = new URLSearchParams(window.location.search);
    return params.get('tid');
}
const transactionId = getTidFromUrl();

// 页面加载时，如果有tid参数，则拉取历史消息并渲染
if (transactionId) {
    fetch(`/chat/message?tid=${transactionId}`)
        .then(res => res.json())
        .then(data => {
            if (data && data.messagesJson) {
                try {
                    const arr = JSON.parse(data.messagesJson);
                    if (Array.isArray(arr)) {
                        arr.forEach(msg => {
                            appendMessage(msg.role === 'user' ? 'user' : 'ai', msg.content);
                        });
                    }
                } catch (e) {}
            }
        });
}

function appendMessage(role, text, streaming = false) {
    const msgDiv = document.createElement('div');
    msgDiv.className = `message ${role}`;
    const bubble = document.createElement('div');
    bubble.className = `bubble ${role}`;
    bubble.textContent = text;
    msgDiv.appendChild(bubble);
    chatMain.appendChild(msgDiv);
    chatMain.scrollTop = chatMain.scrollHeight;
    return bubble;
}

chatForm.addEventListener('submit', function(e) {
    e.preventDefault();
    const userText = chatInput.value.trim();
    if (!userText) return;
    chatInput.value = '';
    const userBubble = appendMessage('user', userText);
    userBubble.classList.add('sending');
    sendToAI(userText);
});

function sendToAI(text) {
    const aiBubble = appendMessage('ai', '', true);
    aiBubble.textContent = '...';
    const messages = Array.from(document.querySelectorAll('.message')).map(msg => {
        const role = msg.classList.contains('user') ? 'user' : 'assistant';
        const content = msg.querySelector('.bubble').textContent;
        return { role, content };
    });
    messages.push({ role: 'user', content: text });
    const payload = { messages, transactionId };
    let aiText = '';
    fetch('/deepseek/chat', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(payload)
    }).then(response => {
        if (!response.body) {
            aiBubble.textContent = 'AI助手暂时无法响应，请稍后再试。';
            return;
        }
        const reader = response.body.getReader();
        const decoder = new TextDecoder('utf-8');
        let buffer = '';
        function processBuffer() {
            // 处理所有完整的 data: 行
            let lines = buffer.split('\n');
            for (let i = 0; i < lines.length; i++) {
                let line = lines[i];
                if (line.startsWith('data:')) {
                    let data = line.slice(5).trim();
                    if (data === '[DONE]') continue;
                    try {
                        const json = JSON.parse(data);
                        const delta = json.choices?.[0]?.delta;
                        if (delta && 'content' in delta) {
                            aiText += delta.content;
                            aiBubble.textContent = aiText;
                            chatMain.scrollTop = chatMain.scrollHeight;
                        }
                    } catch (e) {
                        // 忽略解析失败的行
                    }
                }
            }
        }
        function read() {
            reader.read().then(({ done, value }) => {
                if (done) {
                    processBuffer();
                    if (!aiText) aiBubble.textContent = 'AI助手暂时无法响应，请稍后再试。';
                    // 流式传输结束后，保存聊天记录
                    const saveMessages = Array.from(document.querySelectorAll('.message')).map(msg => {
                        const role = msg.classList.contains('user') ? 'user' : 'assistant';
                        const content = msg.querySelector('.bubble').textContent;
                        return { role, content };
                    });
                    fetch('/deepseek/saveChatHistory', {
                        method: 'POST',
                        headers: { 'Content-Type': 'application/json' },
                        body: JSON.stringify({ messages: saveMessages, transactionId })
                    });
                    return;
                }
                buffer += decoder.decode(value);
                // 只处理完整的 data: 行，剩下的留到下次
                let lastNewline = buffer.lastIndexOf('\n');
                if (lastNewline !== -1) {
                    let processPart = buffer.slice(0, lastNewline);
                    buffer = buffer.slice(lastNewline + 1);
                    processPart.split('\n').forEach(line => {
                        if (line.startsWith('data:')) {
                            let data = line.slice(5).trim();
                            if (data === '[DONE]') return;
                            try {
                                const json = JSON.parse(data);
                                const delta = json.choices?.[0]?.delta;
                                if (delta && 'content' in delta) {
                                    aiText += delta.content;
                                    aiBubble.textContent = aiText;
                                    chatMain.scrollTop = chatMain.scrollHeight;
                                }
                            } catch (e) {
                                // 忽略解析失败的行
                            }
                        }
                    });
                }
                read();
            });
        }
        read();
    }).catch(() => {
        aiBubble.textContent = 'AI助手暂时无法响应，请稍后再试。';
    });
}

