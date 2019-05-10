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

    if (user.role !== 'GUEST') {
        onProfileDisplayClicked();
        //showProfileData();
    }
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
      pEl.textContent = 'Welcome ' + user.name + '!';
      showContents(['profile-content']);
    }
    removeAllChildren(profileContentDivEl);
    profileContentDivEl.appendChild(pEl);

}

function onProfileDisplayClicked() {
    const xhr = new XMLHttpRequest();
    xhr.addEventListener('load', onUserLoad);
    xhr.addEventListener('error', onNetworkError);
    xhr.open('GET', 'protected/profile');
    xhr.send();
}

function onUserLoad() {
    if(this.status === OK) {
        const user = JSON.parse(this.responseText);
        showProfileData(user);
    } else {
        onOtherRespons(myTasksDivEl, this);
    }
}

function showProfileData(user) {
    removeAllChildren(profileContentDivEl);

    const tableEl = document.createElement('table');
    tableEl.setAttribute('id', 'edit-profile-table');
    const theadEl = createProfileTableHeader();
    const tbodyEl = createProfileTableBody(user);
    tableEl.appendChild(theadEl);
    tableEl.appendChild(tbodyEl);
    profileContentDivEl.appendChild(tableEl);

}

function createProfileTableHeader() {
    const nameTdEl = document.createElement('th');
    nameTdEl.textContent = 'Name';

    const emailTdEl = document.createElement('th');
    emailTdEl.textContent = 'Email';

    const passwordTdEl = document.createElement('th');
    passwordTdEl.textContent = 'Password';

    const roleTdEl = document.createElement('th');
    roleTdEl.textContent = 'Role';

    const buttonOneTdEl = document.createElement('th');
    buttonOneTdEl.textContent = 'Edit';

    const trEl = document.createElement('tr');
    trEl.appendChild(nameTdEl);
    trEl.appendChild(emailTdEl);
    trEl.appendChild(passwordTdEl);
    trEl.appendChild(roleTdEl);
    trEl.appendChild(buttonOneTdEl);

    const theadEl = document.createElement('thead');
    theadEl.appendChild(trEl);

    return theadEl;
}

function createProfileTableBody(user) {
    const tbodyEl = document.createElement('tbody');

    const nameTdEl = document.createElement('td');
    nameTdEl.textContent = user.name;
    nameTdEl.setAttribute('id', user.id);

    const emailTdEl = document.createElement('td');
    emailTdEl.textContent = user.email;
    emailTdEl.setAttribute('id', user.id);

    const passwordTdEl = document.createElement('td');
    passwordTdEl.textContent = '*****';
    passwordTdEl.setAttribute('id', user.id);

    const roleTdEl = document.createElement('td');
    roleTdEl.textContent = user.role;
    roleTdEl.setAttribute('id', user.role);

    const buttonEditEl = document.createElement('i');
    buttonEditEl.classList.add('icon-edit');

    buttonEditEl.dataset.userEditId = user.id;
    buttonEditEl.addEventListener('click', onProfileEditButtonClicked);

    const buttonOneTdEl = document.createElement('td');
    buttonOneTdEl.appendChild(buttonEditEl);
    buttonOneTdEl.setAttribute('id', 'user-edit-button-' + user.id);

    const trEl = document.createElement('tr');
    trEl.setAttribute('id', 'row-user-id-' + user.id);
    trEl.appendChild(nameTdEl);
    trEl.appendChild(emailTdEl);
    trEl.appendChild(passwordTdEl);
    trEl.appendChild(roleTdEl);
    trEl.appendChild(buttonOneTdEl);

    tbodyEl.appendChild(trEl);


    return tbodyEl;
}

function onProfileEditButtonClicked() {
    const id = this.dataset.userEditId;
    const tableEl = document.getElementById('edit-profile-table');
    const cells = tableEl.rows.namedItem('row-user-id-' + id).cells;

    for (let i = 0; i < cells.length - 2; i++) {
        const tdEl = cells[i];
        const oldValue = tdEl.textContent;
        tdEl.textContent = '';
        tdEl.appendChild(createPopUpInput(i, oldValue));
    }

    const buttonEditTdEl = document.getElementById('user-edit-button-' + id);
    const saveButtonEl = document.createElement('i');
    saveButtonEl.classList.add('icon-save');
    saveButtonEl.dataset.userId = id;
    saveButtonEl.addEventListener('click', onProfileSaveButtonClicked);
    buttonEditTdEl.innerHTML = '';
    buttonEditTdEl.appendChild(saveButtonEl);
}

function createPopUpInput(id, textContent) {
    const inputEl = document.createElement('input');
    inputEl.classList.add('pop-up-box');
    inputEl.name = 'input-user-id-' + id;
    inputEl.setAttribute('id', 'user-input-' + id);
    inputEl.value = textContent;
    return inputEl;
}

function onProfileSaveButtonClicked() {
    const id = this.dataset.userId;
    const user = getCurrentUser();

    const inputs = document.getElementsByClassName('pop-up-box');

    const data = {};
    data.id = id;
    data.name = inputs[0].value;
    data.email = inputs[1].value;
    data.password = inputs[2].value;
    data.role = user.role;
    const json = JSON.stringify(data);

    const xhr = new XMLHttpRequest();
    xhr.addEventListener('load', onUserEditSubmitResponse);
    xhr.addEventListener('error', onNetworkError);
    xhr.open('PUT', 'protected/profile');
    xhr.setRequestHeader('Content-type','application/json; charset=utf-8');
    xhr.send(json);
}

function onUserEditSubmitResponse() {
   if (this.status === OK) {
      const response = JSON.parse(this.responseText);
      alert(response.message);
      onProfileClicked();
   } else {
       onOtherResponse(mySchedulesDivEl, this);
   }
}
