function onSchedulesClicked() {
    const xhr = new XMLHttpRequest();
    xhr.addEventListener('load', onSchedulesLoad);
    xhr.addEventListener('error', onNetworkError);
    xhr.open('GET', 'protected/my-schedules');
    xhr.send();
}

function onSchedulesLoad() {
    if (this.status === OK) {
        const schedules = JSON.parse(this.responseText);
        createSchedulesDisplay(schedules);
        showContents(['my-schedules-content']);
    } else {
        onOtherResponse(mySchedulesDivEl, this);
    }
}

function createSchedulesDisplay(schedules) {
    const buttonEl = createNewScheduleButton();
    if (schedules.length === 0) {
      removeAllChildren(mySchedulesDivEl);
      const pEl = document.createElement('p');
      pEl.setAttribute('id', 'schedule-info');
      pEl.textContent = 'You have no schedules yet. Click the button below to create one.'
      mySchedulesDivEl.appendChild(pEl);
      mySchedulesDivEl.appendChild(buttonEl);
    } else {
      removeAllChildren(mySchedulesDivEl);
      const tableEl = document.createElement('table');
      const theadEl = createSchedulesTableHeader();
      const tbodyEl = createSchedulesTableBody(schedules);
      tableEl.appendChild(theadEl);
      tableEl.appendChild(tbodyEl);
      mySchedulesDivEl.appendChild(tableEl);
      mySchedulesDivEl.appendChild(buttonEl);
    }
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
    buttonEditEl.classList.add('icon-edit');
    buttonEditEl.setAttribute('id', schedule.id);
    buttonEditEl.addEventListener('click', onScheduleEditClicked);

    const buttonDeleteEl = document.createElement('i');
    buttonDeleteEl.classList.add('icon-trash');
    buttonDeleteEl.setAttribute('id', schedule.id);
    buttonDeleteEl.addEventListener('click', onScheduleDeleteClicked);

    const buttonOneTdEl = document.createElement('td');
    buttonOneTdEl.appendChild(buttonEditEl);
    const buttonTwoTdEl = document.createElement('td');
    buttonTwoTdEl.appendChild(buttonDeleteEl);

    const trEl = document.createElement('tr');
    trEl.appendChild(titleTdEl);
    trEl.appendChild(visibilityTdEl);
    trEl.appendChild(buttonOneTdEl);
    trEl.appendChild(buttonTwoTdEl);

    tbodyEl.appendChild(trEl);
  }

  return tbodyEl;
}

function createSchedulesTableHeader() {
    const titleThEl = document.createElement('th');
    titleThEl.textContent = 'Title';

    const visibilityThEl = document.createElement('th');
    visibilityThEl.textContent = 'Visibility';

    const buttonOneThEl = document.createElement('th');
    buttonOneThEl.textContent = 'Edit';

    const buttonTwoThEl = document.createElement('th');
    buttonTwoThEl.textContent = 'Delete';

    const trEl = document.createElement('tr');
    trEl.appendChild(titleThEl);
    trEl.appendChild(visibilityThEl);
    trEl.appendChild(buttonOneThEl);
    trEl.appendChild(buttonTwoThEl);

    const theadEl = document.createElement('thead');
    theadEl.appendChild(trEl);
    return theadEl;
}

function onScheduleTitleClicked() {
    const params = new URLSearchParams();
    params.append('schedule-id', this.id);

    const xhr = new XMLHttpRequest();
    xhr.addEventListener('load', onScheduleResponse);
    xhr.addEventListener('error', onNetworkError);
    xhr.open('POST', 'protected/edit-schedule');
    xhr.send(params);
}

function onScheduleResponse() {
    if (this.status === OK) {
        const schedule = JSON.parse(this.responseText);
        onScheduleLoad(schedule);
    } else {
        onOtherResponse(mySchedulesDivEl, this);
    }
}

function onScheduleLoad(schedule) {
    const theadEl = createScheduleTableHeader(schedule);
    const tbodyEl = createScheduleTableBody(schedule);
    const tableEl = document.createElement('table');
    tableEl.appendChild(theadEl);
    tableEl.appendChild(tbodyEl);
    removeAllChildren(mySchedulesDivEl);
    mySchedulesDivEl.appendChild(tableEl);
}

function createScheduleTableHeader(schedule) {
    const daysList = ['Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday'];

    const theadEl = document.createElement('thead');
    const trEl = document.createElement('tr');
    for (let i = 0; i < schedule.duration; i++) {
        const thEl = document.createElement('th');
        thEl.textContent = daysList[i];
        trEl.appendChild(thEl);
    }
    theadEl.appendChild(trEl);
    return theadEl;
}

function createScheduleTableBody(schedule) {
    const tbodyEl = document.createElement('tbody');

    for (let i = 0; i < schedule.duration; i++) {
      for (let j = 0; j < 24; i++) {
          const trEl = document.createElement('tr');
          const tdEl = document.createElement('td');
          tdEl.textContent = '';
          tdEl.setAttribute('id', i)
          trEl.appendChild(tdEl);
          tbodyEl.appendChild(trEl);
        }
    }
    return tbodyEl;
}

function onScheduleEditClicked() {
}

function onScheduleDeleteClicked() {
}
