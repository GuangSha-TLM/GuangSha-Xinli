const chatMain = document.getElementById('chat-main');
const chatForm = document.getElementById('chat-form');
const chatInput = document.getElementById('chat-input');

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
    const payload = { messages };
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

