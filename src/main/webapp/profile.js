function onProfileClicked() {
  onProfileLoad(getCurrentUser());
}

function onProfileLoad(user) {
    if (user.role === 'ADMIN') {
      showMenu();
    } else if (user.role === 'REGULAR') {
      const activityLiEl = document.getElementById('link-activity');
      activityLiEl.style.display = 'none';
      showMenu();
    } else if (user.role === 'GUEST') {
      hideMenu();
    }
    showProfileContent(user);
}

function showProfileContent(user) {
    const pEl = document.createElement('p');
    if (user.role === 'GUEST') {
      const textOne = document.createTextNode('Welcome! You are currently browsing the site as a guest. To access additional content, click ');
      pEl.appendChild(textOne);
      const registerAEl = document.createElement('a');
      registerAEl.textContent = 'here';
      registerAEl.href = 'javascript:void(0)';
      registerAEl.onclick = onRegisterRedirectClicked;
      pEl.appendChild(registerAEl);
      const textTwo = document.createTextNode(' to register.');
      pEl.appendChild(textTwo);
      showContents(['profile-content', 'all-schedules-content']);
    } else {
      pEl.textContent = 'Welcome ' + user.name + '!'
      showContents(['profile-content'])
    }
    removeAllChildren(profileContentDivEl);
    profileContentDivEl.appendChild(pEl);

}
