function onLogoutClicked() {
    const xhr = new XMLHttpRequest();
    xhr.addEventListener('load', onLogoutResponse);
    xhr.addEventListener('error', onNetworkError);
    xhr.open('GET', 'logout');
    xhr.send();
}

function onLogoutResponse() {
    if (this.status === OK) {
        const response = JSON.parse(this.responseText);
        alert(response.message);
        onLoad();
    } else {
        const contentEls = document.getElementsByClassName('content');
        for (let i = 0; i < contentEls.length; i++) {
            const contentEl = contentEls[i];
            if (contentEl.style.display === 'block') {
                onOtherResponse(contentEl);
            } 
        }
    }
}