// chat-breakpoint.js
// 断点继续聊天页面自动加载历史内容和继续聊天
console.log('chat-breakpoint.js loaded');
function getQueryParam(name) {
    const url = window.location.search;
    const params = new URLSearchParams(url);
    return params.get(name);
}
function appendMessage(role, text, streaming = false) {
    const chatMain = document.getElementById('chat-main');
    const msgDiv = document.createElement('div');
    msgDiv.className = `message ${role}`;
    const bubble = document.createElement('div');
    bubble.className = `bubble ${role}`;
    bubble.textContent = text;
    msgDiv.appendChild(bubble);
    chatMain.appendChild(msgDiv);
    setTimeout(() => { chatMain.scrollTop = chatMain.scrollHeight; }, 0);
    return bubble;
}

function renderHistoryMessages(messages) {
    const chatMain = document.getElementById('chat-main');
    chatMain.innerHTML = '';
    if (!Array.isArray(messages) || messages.length === 0) {
        chatMain.innerHTML = '<div style="color:#aaa;text-align:center;margin-top:30px;">暂无历史内容</div>';
        return;
    }
    messages.forEach(msg => {
        appendMessage(msg.role === 'user' ? 'user' : 'ai', msg.content);
    });
    // 历史只渲染一次，不再追加
}
window.addEventListener('DOMContentLoaded', function() {
    const tid = getQueryParam('tid');
    if (tid) {
        fetch(`/chat/message?tid=${encodeURIComponent(tid)}`)
            .then(r => r.json())
            .then(data => {
                if (data && data.messagesJson) {
                    try {
                        const arr = JSON.parse(data.messagesJson);
                        renderHistoryMessages(arr);
                        window._chatHistory = arr;
                        window._chatTid = tid;
                    } catch(e) {
                        renderHistoryMessages([]);
                        window._chatHistory = [];
                        window._chatTid = tid;
                    }
                } else {
                    renderHistoryMessages([]);
                    window._chatHistory = [];
                    window._chatTid = tid;
                }
            })
            .catch(() => {
                renderHistoryMessages([]);
                window._chatHistory = [];
                window._chatTid = tid;
            });
    } else {
        window._chatHistory = [];
        window._chatTid = null;
    }
});
// 发送消息时带上 tid，AI流式回复，气泡样式一致
const chatForm = document.getElementById('chat-form');
const chatInput = document.getElementById('chat-input');
if (chatForm) {
    chatForm.addEventListener('submit', function(e) {
        e.preventDefault();
        const userText = chatInput.value.trim();
        if (!userText) return;
        chatInput.value = '';
        appendMessage('user', userText);
        sendToAI(userText);
    });
}

function sendToAI(text) {
    const aiBubble = appendMessage('ai', '', true);
    aiBubble.textContent = '...';
    // 构造历史消息
    // 收集所有已渲染消息，保证顺序和内容与页面一致
    const messages = Array.from(document.querySelectorAll('.message')).map(msg => {
        const role = msg.classList.contains('user') ? 'user' : 'assistant';
        const content = msg.querySelector('.bubble').textContent;
        return { role, content };
    });
    const payload = { messages };
    if (window._chatTid) payload.transactionId = window._chatTid;
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
                            setTimeout(() => { document.getElementById('chat-main').scrollTop = document.getElementById('chat-main').scrollHeight; }, 0);
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
                    // AI回复结束，不再追加到任何历史，只保留DOM
                    return;
                }
                buffer += decoder.decode(value, { stream: true });
                processBuffer();
                read();
            });
        }
        read();
    });
}
