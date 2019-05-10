function onAllSchedulesClicked() {
    const xhr = new XMLHttpRequest();
    xhr.addEventListener('load', onAllSchedulesLoad);
    xhr.addEventListener('error', onNetworkError);
    xhr.open('GET', 'protected/schedules');
    xhr.send();
}

function onAllSchedulesLoad() {
    if (this.status === OK) {
        const schedulesDto = JSON.parse(this.responseText);
        const schedules = schedulesDto.publicSchedules;
        createAllSchedulesDisplay(schedules);
        showContents(['all-schedules-content']);
    } else {
        onOtherResponse(allSchedulesDivEl, this);
    }
}

function createAllSchedulesDisplay(schedules) {
    if (schedules.length === 0) {
      removeAllChildren(allSchedulesDivEl);
      const pEl = document.createElement('p');
      pEl.setAttribute('id', 'schedule-info');
      pEl.textContent = 'There are no public schedules yet.'
      allSchedulesDivEl.appendChild(pEl);
    } else {
      removeAllChildren(allSchedulesDivEl);
      const tableEl = document.createElement('table');
      tableEl.setAttribute('id', 'edit-schedule-table');
      const theadEl = createAllSchedulesTableHeader();
      const tbodyEl = createAllSchedulesTableBody(schedules);
      tableEl.appendChild(theadEl);
      tableEl.appendChild(tbodyEl);
      allSchedulesDivEl.appendChild(tableEl);
    }
}

function createAllSchedulesTableHeader() {
    const titleThEl = document.createElement('th');
    titleThEl.textContent = 'Title';

    const durationThEl = document.createElement('th');
    durationThEl.textContent = 'Duration';

    const visibilityThEl = document.createElement('th');
    visibilityThEl.textContent = 'Published';

    const trEl = document.createElement('tr');
    trEl.appendChild(titleThEl);
    trEl.appendChild(durationThEl);
    trEl.appendChild(visibilityThEl);

    const theadEl = document.createElement('thead');
    theadEl.appendChild(trEl);
    return theadEl;
}

function createAllSchedulesTableBody(schedules) {
  const tbodyEl = document.createElement('tbody');

  for (let i = 0; i < schedules.length; i++) {
    const schedule = schedules[i];

    const trEl = document.createElement('tr');
    trEl.setAttribute('id', 'row-schedule-id-' + schedule.id);

    const titleTdEl = document.createElement('td');
    const titleAEl = document.createElement('a');
    titleAEl.href = 'javascript:void(0)';
    titleAEl.dataset.id = schedule.id;
    titleAEl.onclick = onScheduleTitleClicked;
    titleAEl.textContent = schedule.title;
    titleTdEl.appendChild(titleAEl);

    const durationTdEl = document.createElement('td');
    durationTdEl.textContent = schedule.duration;

    const visibilityTdEl = document.createElement('td');
    visibilityTdEl.textContent = schedule.visibility;

    trEl.appendChild(titleTdEl);
    trEl.appendChild(durationTdEl);
    trEl.appendChild(visibilityTdEl);

    tbodyEl.appendChild(trEl);
  }

  return tbodyEl;
}

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
      tableEl.setAttribute('id', 'edit-schedule-table');
      const theadEl = createSchedulesTableHeader();
      const tbodyEl = createSchedulesTableBody(schedules);
      tableEl.appendChild(theadEl);
      tableEl.appendChild(tbodyEl);
      const hiddenFormEl = document.createElement('form');
      hiddenFormEl.onSubmit = 'return false';
      hiddenFormEl.setAttribute('id', 'schedule-edit-form');
      hiddenFormEl.appendChild(tableEl);
      mySchedulesDivEl.appendChild(hiddenFormEl);
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

    const trEl = document.createElement('tr');
    trEl.setAttribute('id', 'row-schedule-id-' + schedule.id);

    const titleTdEl = document.createElement('td');
    const titleAEl = document.createElement('a');
    titleAEl.href = 'javascript:void(0)';
    titleAEl.dataset.id = schedule.id;
    titleAEl.onclick = onScheduleTitleClicked;
    titleAEl.textContent = schedule.title;
    titleTdEl.appendChild(titleAEl);

    const durationTdEl = document.createElement('td');
    durationTdEl.textContent = schedule.duration;

    const visibilityTdEl = document.createElement('td');
    visibilityTdEl.textContent = schedule.visibility;

    const buttonEditEl = document.createElement('i');
    buttonEditEl.classList.add('icon-edit');
    buttonEditEl.dataset.scheduleId = schedule.id;
    buttonEditEl.addEventListener('click', onScheduleEditButtonClicked);

    const buttonDeleteEl = document.createElement('i');
    buttonDeleteEl.classList.add('icon-trash');
    buttonDeleteEl.dataset.scheduleId = schedule.id;
    buttonDeleteEl.addEventListener('click', onScheduleDeleteClicked);

    const buttonEditTdEl = document.createElement('td');
    buttonEditTdEl.appendChild(buttonEditEl);
    buttonEditTdEl.setAttribute('id', 'schedule-edit-button-' + schedule.id);
    const buttonDelTdEl = document.createElement('td');
    buttonDelTdEl.appendChild(buttonDeleteEl);

    trEl.appendChild(titleTdEl);
    trEl.appendChild(durationTdEl);
    trEl.appendChild(visibilityTdEl);
    trEl.appendChild(buttonEditTdEl);
    trEl.appendChild(buttonDelTdEl);

    tbodyEl.appendChild(trEl);
  }

  return tbodyEl;
}

function createSchedulesTableHeader() {
    const titleThEl = document.createElement('th');
    titleThEl.textContent = 'Title';

    const durationThEl = document.createElement('th');
    durationThEl.textContent = 'Duration';

    const visibilityThEl = document.createElement('th');
    visibilityThEl.textContent = 'Published';

    const buttonOneThEl = document.createElement('th');
    buttonOneThEl.textContent = 'Edit';

    const buttonTwoThEl = document.createElement('th');
    buttonTwoThEl.textContent = 'Delete';

    const trEl = document.createElement('tr');

    trEl.appendChild(titleThEl);
    trEl.appendChild(durationThEl);
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
        const allTasks = scheduleDto.allTasks;
        onScheduleLoad(schedule, tasks, allTasks);
    } else {
        onOtherResponse(mySchedulesDivEl, this);
    }
}

function onScheduleLoad(schedule, tasks, allTasks) {
    const tableEl = document.createElement('table');
    tableEl.setAttribute('id', 'schedules-table');
    const theadEl = createScheduleTableHeader(schedule);
    const tbodyEl = createScheduleTableBody(schedule, tasks);
    tableEl.appendChild(theadEl);
    tableEl.appendChild(tbodyEl);
    removeAllChildren(mySchedulesDivEl);
    mySchedulesDivEl.appendChild(tableEl);
    addSchedulesToSchedule(tasks);
    debugger;
    const formEl = createNewTaskAddForm(schedule, tasks, allTasks);
    formEl.setAttribute('id', 'new-task-add-form');
    formEl.classList.add('menu-form');
    formEl.onSubmit = 'return false;';
    mySchedulesDivEl.insertBefore(formEl, tableEl);
    showContents(['my-schedules-content'])
}

function createScheduleTableHeader(schedule) {
    const daysList = ['Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday', 'Sunday'];
    const duration = schedule.duration;
    const theadEl = document.createElement('thead');
    const trEl = document.createElement('tr');
    trEl.setAttribute('id', 'row-schedule-header');
    const timeThEl = document.createElement('th');
    timeThEl.classList.add('schedule-th');
    timeThEl.textContent = 'Time';
    trEl.appendChild(timeThEl);
    for (let i = 0; i < duration; i++) {
        const thEl = document.createElement('th');
        thEl.textContent = daysList[i];
        thEl.classList.add('schedule-th');
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
        hourTdEl.classList.add('schedule-cell')
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

function addSchedulesToSchedule(tasks) {
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
    xhr.open('POST', 'protected/schedules');
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

function onScheduleEditButtonClicked() {
    const id = this.dataset.scheduleId;
    const tableEl = document.getElementById('edit-schedule-table');
    const cells = tableEl.rows.namedItem('row-schedule-id-' + id).cells;

    for (let i = 0; i < cells.length - 2; i++) {
        const tdEl = cells[i];
        const oldValue = tdEl.textContent;
        tdEl.textContent = '';
        tdEl.appendChild(createPopUpInput(i, oldValue));
    }

    const buttonEditTdEl = document.getElementById('schedule-edit-button-' + id);
    const saveButtonEl = document.createElement('i');
    saveButtonEl.classList.add('icon-save');
    saveButtonEl.dataset.scheduleId = id;
    saveButtonEl.addEventListener('click', onSaveButtonClicked);
    buttonEditTdEl.innerHTML = '';
    buttonEditTdEl.appendChild(saveButtonEl);
}

function createPopUpInput(id, textContent) {
    const inputEl = document.createElement('input');
    inputEl.classList.add('pop-up-box');
    inputEl.name = 'input-schedule-id-' + id;
    inputEl.setAttribute('id', 'schedule-input-' + id);
    inputEl.value = textContent;
    return inputEl;
}

function onSaveButtonClicked() {
    const id = this.dataset.scheduleId;
    const user = getCurrentUser();

    const inputs = document.getElementsByClassName('pop-up-box');

    const data = {};
    data.id = id;
    data.title = inputs[0].value;
    data.duration = inputs[1].value;
    data.visibility = inputs[2].value;
    data.userId = user.id;
    const json = JSON.stringify(data);

    const xhr = new XMLHttpRequest();
    xhr.addEventListener('load', onScheduleEditSubmitResponse);
    xhr.addEventListener('error', onNetworkError);
    xhr.open('PUT', 'protected/schedule');
    xhr.setRequestHeader('Content-type','application/json; charset=utf-8');
    xhr.send(json);
}

function onScheduleEditSubmitResponse() {
   if (this.status === OK) {
      const response = JSON.parse(this.responseText);
      alert(response.message);
      onSchedulesClicked();
   } else {
       onOtherResponse(mySchedulesDivEl, this);
   }
}

function createNewTaskAddForm(schedule, tasks, allTasks) {
    const formEl = document.createElement('form');

    const pEl = document.createElement('p');
    pEl.textContent = 'Select a task to add: ';
    formEl.appendChild(pEl);

    const taskSelectEl = document.createElement('select');
    taskSelectEl.setAttribute('name', 'task-select');
    taskSelectEl.classList.add('task-select');

    const tableEl = document.getElementById('schedules-table');
    const cells = tableEl.rows.namedItem('row-schedule-header').cells;

    for (let i = 0; i < allTasks.length; i++) {
        const task = allTasks[i];
        const optionEl = document.createElement('option');
        optionEl.value = task.id;
        optionEl.textContent = task.title + ' (' + task.start + ':00' + ' - ' + task.end + ':00' + ')';
        taskSelectEl.appendChild(optionEl);
    }

    const daySelectEl = document.createElement('select');
    daySelectEl.setAttribute('name', 'day-select');
    daySelectEl.classList.add('task-select');
    for (let i = 1; i < cells.length; i ++) {
        const optionEl = document.createElement('option');
        optionEl.value = i;
        optionEl.textContent = cells[i].textContent;
        daySelectEl.appendChild(optionEl);
    }

    const buttonEl = document.createElement('button');
    buttonEl.textContent = 'Add to schedule';
    buttonEl.classList.add('form-button');
    buttonEl.addEventListener('click', onAddTaskToScheduleClicked);
    buttonEl.dataset.scheduleId = schedule.id;

    formEl.appendChild(taskSelectEl);
    formEl.appendChild(document.createElement('br'));
    formEl.appendChild(daySelectEl);
    formEl.appendChild(document.createElement('br'));
    formEl.appendChild(buttonEl);
    formEl.appendChild(document.createElement('br'));
    return formEl;
}

function onAddTaskToScheduleClicked() {
    debugger;
    const formEl = document.forms['new-task-add-form'];
    const taskSelectEl = formEl.querySelector('select[name="task-select"]');
    const daySelectEl = formEl.querySelector('select[name="day-select"]');

    removeAllChildren(mySchedulesDivEl);

    const taskId = taskSelectEl.value;
    const columnNumber = daySelectEl.value;
    const params = new URLSearchParams();
    params.append('task-id', taskId);
    params.append('columnNumber', columnNumber);
    params.append('scheduleId', this.dataset.scheduleId);

    const xhr = new XMLHttpRequest();
    xhr.addEventListener('load', onAddTaskToScheduleResponse);
    xhr.addEventListener('error', onNetworkError);
    xhr.open('POST', 'protected/schedule');
    xhr.send(params);
}

function onAddTaskToScheduleResponse() {
    if (this.status === OK) {
       alert('Task added.');
       const scheduleDto = JSON.parse(this.responseText);
       const schedule = scheduleDto.schedule;
       const tasks = scheduleDto.tasks;
       const allTasks = scheduleDto.allTasks;
       onScheduleLoad(schedule, tasks, allTasks);
   } else {
       onOtherResponse(mySchedulesDivEl, this);
   }
}

