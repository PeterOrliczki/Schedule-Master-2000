function onSchedulesClicked() {
    const xhr = new XMLHttpRequest();
    xhr.addEventListener('load', onSchedulesLoad);
    xhr.addEventListener('error', onNetworkError);
    xhr.open('GET', 'protected/schedules');
    xhr.send();
}

function onSchedulesLoad() {
    if (this.status === OK) {
        const schedulesDto = JSON.parse(this.responseText);
        const schedules = schedulesDto.mySchedules;
        createSchedulesDisplay(schedules);
        showContents(['my-schedules-content']);
    } else {
        onOtherResponse(mySchedulesDivEl, this);
    }
}

function createSchedulesDisplay(schedules) {
    const buttonEl = createNewScheduleButton();
    buttonEl.addEventListener('click', addNewSchedule);
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
    titleAEl.dataset.id = schedule.id;
    titleAEl.onclick = onScheduleTitleClicked;
    titleAEl.textContent = schedule.title;
    titleTdEl.appendChild(titleAEl);

    const visibilityTdEl = document.createElement('td');
    if (schedule.visibility === 'true') {
      visibilityTdEl.textContent = 'Public';
    } else {
      visibilityTdEl.textContent = 'Private';
    }

    const buttonEditEl = document.createElement('i');
    buttonEditEl.classList.add('icon-edit');
    buttonEditEl.dataset.scheduleId = schedule.id;
    buttonEditEl.addEventListener('click', onScheduleEditClicked);

    const buttonDeleteEl = document.createElement('i');
    buttonDeleteEl.classList.add('icon-trash');
    buttonDeleteEl.dataset.scheduleId = schedule.id;
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
    const id = this.dataset.id;
    const params = new URLSearchParams();
    params.append('schedule-id', id);

    const xhr = new XMLHttpRequest();
    xhr.addEventListener('load', onScheduleResponse);
    xhr.addEventListener('error', onNetworkError);
    xhr.open('GET', 'protected/schedule?' + params.toString());
    xhr.send();
}

function onScheduleResponse() {
    if (this.status === OK) {
        const scheduleDto = JSON.parse(this.responseText);
        const schedule = scheduleDto.schedule;
        const tasks = scheduleDto.tasks;
        onScheduleLoad(schedule, tasks);
    } else {
        onOtherResponse(mySchedulesDivEl, this);
    }
}

function onScheduleLoad(schedule, tasks) {
    const tableEl = document.createElement('table');
    tableEl.setAttribute('id', 'schedules-table');
    const theadEl = createScheduleTableHeader(schedule);
    const tbodyEl = createScheduleTableBody(schedule, tasks);
    tableEl.appendChild(theadEl);
    tableEl.appendChild(tbodyEl);
    removeAllChildren(mySchedulesDivEl);
    mySchedulesDivEl.appendChild(tableEl);
    addTasksToSchedule(tasks);
}

function createScheduleTableHeader(schedule) {
    const daysList = ['Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday'];
    const duration = schedule.duration;
    const theadEl = document.createElement('thead');
    const trEl = document.createElement('tr');
    const timeThEl = document.createElement('th');
    timeThEl.textContent = 'Time';
    trEl.appendChild(timeThEl);
    for (let i = 0; i < duration; i++) {
        const thEl = document.createElement('th');
        thEl.textContent = daysList[i];
        trEl.appendChild(thEl);
    }
    theadEl.appendChild(trEl);
    return theadEl;
}

function createScheduleTableBody(schedule) {
    const tbodyEl = document.createElement('tbody');
    const duration = schedule.duration;
    for (let i = 0; i < 24; i++) {
        const trEl = document.createElement('tr');
        const hourTdEl = document.createElement('td');
        hourTdEl.textContent = i + ':00';
        trEl.appendChild(hourTdEl);
        for (let j = 0; j < duration; j++) {
            const tdEl = document.createElement('td');
            tdEl.classList.add('schedule-cell');
            tdEl.setAttribute('id', j + 1 + ':' + i);
            trEl.appendChild(tdEl);
        }
        tbodyEl.appendChild(trEl);
    }
    return tbodyEl;
}

function addTasksToSchedule(tasks) {
    for (let i = 0; i < tasks.length; i++) {
        const task = tasks[i];
        const duration = task.end - task.start;
        for (let j = 0; j < duration; j++) {
            const hour = task.start + j;
            tdEl = document.getElementById(task.columnNumber + ':' + hour);
            tdEl.textContent = task.title;
        }
    }
}

function onScheduleEditClicked() {
 // TODO: function
}

function onScheduleDeleteClicked() {
    const result = confirm('Click OK to confirm.');
    if (result) {
        const scheduleId = this.dataset.scheduleId;
        const params = new URLSearchParams();
        params.append('schedule-id', scheduleId);
        const xhr = new XMLHttpRequest();
        xhr.addEventListener('load', onScheduleDeleteResponse);
        xhr.addEventListener('error', onNetworkError);
        xhr.open('DELETE', 'protected/schedule?' + params.toString());
        xhr.send();
    } else {
        return;
    }
}

function onScheduleDeleteResponse() {
    if(this.status === OK) {
        alert(JSON.parse(this.responseText).message);
        onSchedulesClicked();
    } else {
        onOtherResponse(mySchedulesDivEl, this);
    }
}

function addNewSchedule() {
    removeAllChildren(mySchedulesDivEl);
    createNewScheduleForm();
}

function createNewScheduleForm() {
    const formEl = document.createElement('form');
    formEl.setAttribute('id', 'new-schedule-form');
    formEl.classList.add('menu-form');
    formEl.onSubmit = 'return false;';

    const titleEl = document.createElement('input');
    titleEl.classList.add('text-input');
    titleEl.placeholder = 'Schedule title';
    titleEl.setAttribute('name', 'title');

    const durationEl = document.createElement('select');
    durationEl.setAttribute('name', 'duration');
    for (let i = 1; i < 8; i ++) {
        const optionEl = document.createElement('option');
        optionEl.value = i;
        optionEl.textContent = i;
        durationEl.appendChild(optionEl);
    }

    const buttonEl = createNewScheduleButton();
    buttonEl.addEventListener('click', onCreateNewButtonClicked);

    formEl.appendChild(titleEl);
    formEl.appendChild(durationEl);
    formEl.appendChild(document.createElement('br'));
    formEl.appendChild(buttonEl);

    mySchedulesDivEl.appendChild(formEl);
}

function onCreateNewButtonClicked() {
    debugger;
    const newScheduleFormEl = document.forms['new-schedule-form'];

    const titleInputEl = newScheduleFormEl.querySelector('input[name="title"]');
    const durationInputEl = newScheduleFormEl.querySelector('select[name="duration"]');

    removeAllChildren(mySchedulesDivEl);
    const title = titleInputEl.value;
    const duration = durationInputEl.value;
    const params = new URLSearchParams();
    params.append('title', title);
    params.append('duration', duration);

    const xhr = new XMLHttpRequest();
    xhr.addEventListener('load', onNewScheduleResponse);
    xhr.addEventListener('error', onNetworkError);
    xhr.open('POST', 'protected/schedule');
    xhr.send(params);
}

function onNewScheduleResponse() {
    if (this.status === OK) {
        const response = JSON.parse(this.responseText);
        alert(response.message)
        onSchedulesClicked();
    } else {
        onOtherResponse(mySchedulesDivEl, this);
    }
}
