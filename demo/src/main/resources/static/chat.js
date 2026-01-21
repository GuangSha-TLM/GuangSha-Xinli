const chatMain = document.getElementById('chat-main');
const chatForm = document.getElementById('chat-form');
const chatInput = document.getElementById('chat-input');

let messages = [];

function addBubble(role, content) {
    const wrapper = document.createElement('div');
    wrapper.className = 'bubble-row ' + (role === 'user' ? 'row-user' : 'row-ai');
    const avatar = document.createElement('img');
    avatar.className = 'avatar';
    avatar.src = role === 'user' ? '/image/user.png' : '/image/623.wegp';
    avatar.alt = role === 'user' ? '用户' : 'AI';
    const bubble = document.createElement('div');
    bubble.className = 'chat-bubble ' + (role === 'user' ? 'bubble-user' : 'bubble-ai');
    bubble.innerHTML = content;
    if (role === 'user') {
        wrapper.appendChild(bubble);
        wrapper.appendChild(avatar);
    } else {
        wrapper.appendChild(avatar);
        wrapper.appendChild(bubble);
    }
    chatMain.appendChild(wrapper);
    chatMain.scrollTop = chatMain.scrollHeight;
}

function renderHistory() {
    chatMain.innerHTML = '';
    messages.forEach(msg => addBubble(msg.role, msg.content));
}




chatForm.addEventListener('submit', async function(e) {
    e.preventDefault();
    const userMsg = chatInput.value.trim();
    if (!userMsg) return;
    messages.push({ role: 'user', content: userMsg });
    renderHistory();
    chatInput.value = '';
    chatInput.focus();
    await fetchAI();
});


async function fetchAI() {
    let aiContent = '';
    // 先渲染AI思考气泡
    const aiMsg = { role: 'assistant', content: '<span class="typing">AI正在思考...</span>' };
    messages.push(aiMsg);
    renderHistory();
    const aiIndex = messages.length - 1;
    try {
        const response = await fetch('/deepseek/chat', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ messages: messages.slice(0, aiIndex) })
        });
        if (!response.body || !response.ok) throw new Error('网络错误');
        const reader = response.body.getReader();
        const decoder = new TextDecoder('utf-8');
        let done = false;
        while (!done) {
            const { value, done: doneReading } = await reader.read();
            done = doneReading;
            if (value) {
                const chunk = decoder.decode(value);
                chunk.split('\n').forEach(line => {
                    if (line.startsWith('data:')) {
                        const data = line.replace('data:', '').trim();
                        if (data && data !== '[DONE]') {
                            try {
                                const json = JSON.parse(data);
                                // 只拼接choices[0].delta.content
                                const content = json.choices && json.choices[0] && json.choices[0].delta && json.choices[0].delta.content;
                                if (content) {
                                    aiContent += content;
                                    messages[aiIndex].content = aiContent;
                                    renderHistory();
                                }
                            } catch (e) {
                                // 非JSON直接忽略
                            }
                        }
                    }
                });
            }
        }
    } catch (e) {
        messages[aiIndex].content = '<span style="color:red">AI回复失败，请重试</span>';
        renderHistory();
    }
}

// 兼容 EventSource POST（需引入 polyfill）
(function() {
    if (!window.EventSourcePolyfill) {
        const script = document.createElement('script');
        script.src = 'https://cdn.jsdelivr.net/npm/event-source-polyfill@1.0.32/eventsource.min.js';
        script.onload = () => { window.EventSourcePolyfill = window.EventSourcePolyfill || window.EventSource; };
        document.head.appendChild(script);
    }
})();
