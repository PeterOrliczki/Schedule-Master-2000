function onSchedulesClicked() {
    const user = getCurrentUser();

    const xhr = new XMLHttpRequest();
    xhr.addEventListener('load', onSchedulesLoad);
    xhr.addEventListener('error', onNetworkError);
    xhr.open('GET', 'protected/myschedules');
}

function onSchedulesLoad() {
    if (this.status === OK) {
        const schedules = JSON.parse(this.responseText);
        createSchedulesDisplay(schedules);
    } else {
        onOtherResponse(mySchedulesDivEl, this);
    }
}

function createSchedulesDisplay(schedules) {
    const buttonEl = createNewScheduleButton();
    if (schedules.length === 0) {
      const pEl = document.createElement('p');
      pEl.textContent = 'You have no schedules yet. Click the button below to create one.'
      mySchedulesDivEl.appendChild(pEl);
    } else {
      const tableEl = document.createElement('table');
      const theadEl = createSchedulesTableHeader();
      const tbodyEl = createSchedulesTableBody(schedules);
      tableEl.appendChild(theadEl);
      tableEl.appendChild(tbodyEl);
    }

    while (mySchedulesDivEl.childNodes.length > 2) {
      mySchedulesDivEl.removeChild(mySchedulesDivEl.lastChild);
    }
    mySchedulesDivEl.appendChild(buttonEl);
    mySchedulesDivEl.appendChild(tableEl);
}

function createNewScheduleButton() {
    const buttonEl = document.createElement('button');
    buttonEl.classList.add('form-button');
    buttonEl.textContent = 'Create new schedule';
    return buttonEl;
}

function createSchedulesTableBody(schedules) {
  const tbodyEl = document.createElement('tbody');

  for (let i = 0; i < schedules.length; i++) {
    const schedule = schedules[i];

    const titleTdEl = document.createElement('td');
    const titleAEl = document.createElement('a');
    titleAEl.href = 'javascript:void(0)';
    titleAEl.onclick = onScheduleTitleClicked;
    titleAEl.textContent = schedule.title;
    titleAEl.setAttribute('id', schedule.id);
    titleTdEl.appendChild(titleAEl);

    const visibilityTdEl = document.createElement('td');
    if (schedule.visibility === 'true') {
      visibilityTdEl.textContent = 'Public';
    } else {
      visibilityTdEl.textContent = 'Private';
    }

    const buttonEditEl = document.createElement('i');
    buttonEditEl.classList.add('fa fa-pencil');
    buttonEditEl.setAttribute('id', schedule.id);
    buttonEditEl.addEventListener('click', onScheduleEditClicked);

    const buttonDeleteEl = document.createElement('i');
    buttonDeleteEl.classList.add('fa fa-trash');
    buttonDeleteEl.setAttribute('id', schedule.id);
    buttonDeleteEl.addEventListener('click', onScheduleDeleteClicked);

    const buttonOneTdEl = document.createElement('td');
    buttonOneTdEl.appendChild(buttonEditEl);
    const buttonTwoTdEl = document.createElement('td');
    buttonTwoTdEl.appendChild(buttonDeleteEl);

    const trEl = document.createElement('tr');
    trEl.appendChild(idTdEl);
    trEl.appendChild(usernameTdEl);
    trEl.appendChild(buttonOneTdEl);
    trEl.appendChild(buttonTwoTdEl);

    tbodyEl.appendChild(trEl);
  }

  return tbodyEl;
}

function createSchedulesTableHeader() {
    const titleTdEl = document.createElement('td');
    titleTdEl.textContent = 'Title';

    const visibilityTdEl = document.createElement('td');
    visibilityTdEl.textContent = 'Visibility';

    const buttonOneTdEl = document.createElement('td');
    buttonOneTdEl.textContent = '';

    const buttonTwoTdEl = document.createElement('td');
    buttonTwoTdEl.textContent = '';

    const trEl = document.createElement('tr');
    trEl.appendChild(titleTdEl);
    trEl.appendChild(visibilityTdEl);
    trEl.appendChild(buttonOneTdEl);
    trEl.appendChild(buttonTwoTdEl);

    const theadEl = document.createElement('thead');
    theadEl.appendChild(trEl);
    return theadEl;
}
