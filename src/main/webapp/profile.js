function onShopsClicked() {
    const xhr = new XMLHttpRequest();
    xhr.addEventListener('load', onShopsResponse);
    xhr.addEventListener('error', onNetworkError);
    xhr.open('GET', 'protected/shops');
    xhr.send();
}

function onCouponsClicked() {
    const xhr = new XMLHttpRequest();
    xhr.addEventListener('load', onCouponsResponse);
    xhr.addEventListener('error', onNetworkError);
    xhr.open('GET', 'protected/coupons');
    xhr.send();
}

function onProfileLoad(user) {
    clearMessages();
    showContents(['profile-content', 'logout-content']);

    const userEmailSpandEl = document.getElementById('user-email');
    const userPasswordSpanEl = document.getElementById('user-password');

    userEmailSpandEl.textContent = user.email;
    userPasswordSpanEl.textContent = user.password;
}