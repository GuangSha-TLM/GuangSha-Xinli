// chat.js
// 聊天页面自动加载历史内容
function getQueryParam(name) {
    const url = window.location.search;
    const params = new URLSearchParams(url);
    return params.get(name);
}

function renderHistoryMessages(messages) {
    const main = document.getElementById('chat-main');
    if (!Array.isArray(messages) || messages.length === 0) {
        main.innerHTML = '<div style="color:#aaa;text-align:center;margin-top:30px;">暂无历史内容</div>';
        return;
    }
    main.innerHTML = messages.map(msg => `<div style="margin:8px 0;"><b>${msg.role === 'user' ? '我' : 'AI'}：</b>${msg.content}</div>`).join('');
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
                    } catch(e) {
                        renderHistoryMessages([]);
                    }
                } else {
                    renderHistoryMessages([]);
                }
            })
            .catch(() => renderHistoryMessages([]));
    }
});
