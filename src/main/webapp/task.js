function onTasksClicked() {
    const xhr = new XMLHttpRequest();
    xhr.addEventListener('load', onTasksLoad);
    xhr.addEventListener('error', onNetworkError);
    xhr.open('GET', 'protected/tasks');
    xhr.send();
}

function onTasksLoad() {
    if(this.status === OK) {
        const tasks = JSON.parse(this.responseText);
        console.log(tasks);
        createTasksDisplay(tasks);
        showContents(['my-tasks-content']);
    } else {
        onOtherRespons(myTasksDivEl, this);
    }
}

function createTasksDisplay(tasks) {
    const buttonEl = createNewTaskButton();
    if (tasks.length === 0) {
        removeAllChildren(myTasksDivEl);
        const pEl = document.createElement('p');
        pEl.setAttribute('id', 'task-info');
        pEl.textContent = 'You have no tasks yet. Click the button below to create one.'
        myTasksDivEl.appendChild(pEl);
        myTasksDivEl.appendChild(buttonEl);
    } else {
        removeAllChildren(myTasksDivEl);
        const tableEl = document.createElement('table');
        const theadEl = createTasksTableHeader();
        const tbodyEl = createTasksTableBody(tasks);
        tableEl.appendChild(theadEl);
        tableEl.appendChild(tbodyEl);
        myTasksDivEl.appendChild(tableEl);
        myTasksDivEl.appendChild(buttonEl);
    }
}

function createNewTaskButton() {
    const buttonEl = document.createElement('button');
    buttonEl.classList.add('form-button');
    buttonEl.textContent = 'Create new task';
    return buttonEl;
}

function createTasksTableHeader() {
    const titleTdEl = document.createElement('td');
    titleTdEl.textContent = 'Title';

    const visibilityTdEl = document.createElement('td');
    visibilityTdEl.textContent = 'Content';

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

function createTasksTableBody(tasks) {
    const tbodyEl = document.createElement('tbody');

    for (let i = 0; i < tasks.length; i++) {
        const task = tasks[i];
        console.log(task);
        const titleTdEl = document.createElement('td');
        const titleAEl = document.createElement('a');
        titleAEl.href = 'javascript:void(0)';
        //titleAEl.onclick = onTaskTitleClicked;
        titleAEl.textContent = task.title;
        titleAEl.setAttribute('id', task.id);
        titleTdEl.appendChild(titleAEl);

        const contentTdEl = document.createElement('td');
        const contentAEl = document.createElement('a');
        contentAEl.textContent = task.content;
        contentAEl.setAttribute('id', task.id);
        contentTdEl.appendChild(contentAEl);

        const buttonEditEl = document.createElement('i');
        buttonEditEl.classList.add('fa');
        buttonEditEl.classList.add('fa-pencil');
        buttonEditEl.setAttribute('id', task.id);
        //buttonEditEl.addEventListener('click', onScheduleEditClicked);

        const buttonDeleteEl = document.createElement('i');
        buttonDeleteEl.classList.add('fa');
        buttonDeleteEl.classList.add('fa-trash');
        buttonDeleteEl.setAttribute('id', task.id);
        //buttonDeleteEl.addEventListener('click', onScheduleDeleteClicked);

        const buttonOneTdEl = document.createElement('td');
        buttonOneTdEl.appendChild(buttonEditEl);
        const buttonTwoTdEl = document.createElement('td');
        buttonTwoTdEl.appendChild(buttonDeleteEl);

        const trEl = document.createElement('tr');
        trEl.appendChild(titleTdEl);
        trEl.appendChild(contentTdEl);
        trEl.appendChild(buttonOneTdEl);
        trEl.appendChild(buttonTwoTdEl);

        tbodyEl.appendChild(trEl);
    }

    return tbodyEl;
}
