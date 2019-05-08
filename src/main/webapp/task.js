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
        createTasksDisplay(tasks);
        showContents(['my-tasks-content']);
    } else {
        onOtherRespons(myTasksDivEl, this);
    }
}

function createTasksDisplay(tasks) {
    const buttonEl = createNewTaskButton();
    buttonEl.addEventListener('click', addNewTask);
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
        const titleTdEl = document.createElement('td');
        const titleAEl = document.createElement('a');
        titleAEl.href = 'javascript:void(0)';
        titleAEl.dataset.taskId = task.id;
        titleAEl.onclick = onTaskTitleClicked;
        titleAEl.textContent = task.title;
        titleAEl.setAttribute('id', task.id);
        titleTdEl.appendChild(titleAEl);

        const contentTdEl = document.createElement('td');
        const contentAEl = document.createElement('a');
        contentAEl.textContent = task.content;
        contentAEl.setAttribute('id', task.id);
        contentTdEl.appendChild(contentAEl);

        const buttonEditEl = document.createElement('i');
        buttonEditEl.classList.add('icon-edit');
        buttonEditEl.setAttribute('id', task.id);
        buttonEditEl.addEventListener('click', onTaskEditClicked);

        const buttonDeleteEl = document.createElement('i');
        buttonDeleteEl.classList.add('icon-trash');
        buttonDeleteEl.setAttribute('id', task.id);
        buttonDeleteEl.dataset.taskDeleteId = task.id;
        buttonDeleteEl.addEventListener('click', onTaskDeleteClicked);

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

function onTaskTitleClicked() {
    const taskId = this.dataset.taskId;

    const params = new URLSearchParams();
    params.append('id', taskId);

    const xhr = new XMLHttpRequest();
    xhr.addEventListener('load', onTaskResponse);
    xhr.addEventListener('error', onNetworkError);
    xhr.open('GET', 'protected/task?' + params.toString());
    xhr.send();
}

function onTaskResponse() {
    if (this.status === OK) {
        onTaskLoad(JSON.parse(this.responseText));
    } else {
        onOtherResponse(myTasksDivEl, this);
    }
}

function onTaskLoad(task) {
    removeAllChildren(myTasksDivEl);
    const pTiEl = document.createElement('p');
    pTiEl.textContent = "Task title: " + task.title;

    const pCoEl = document.createElement('p');
    pCoEl.textContent = "Task content: " + task.content;

    const pStEl = document.createElement('p');
    pStEl.textContent = "Task start: " + task.start;

    const pEnEl = document.createElement('p');
    pEnEl.textContent = "Task end: " + task.end;

    myTasksDivEl.appendChild(pTiEl);
    myTasksDivEl.appendChild(pCoEl);
    myTasksDivEl.appendChild(pStEl);
    myTasksDivEl.appendChild(pEnEl);
}

function addNewTask() {
    removeAllChildren(myTasksDivEl);
    createNewTaskForm();
}

function createNewTaskForm() {
    const formEl = document.createElement('form');
    formEl.setAttribute('id','new-task-form');
    formEl.classList.add('menu-form');
    formEl.onSubmit = 'return false;';

    const inputTiEl = document.createElement("input"); //input element, text
    inputTiEl.setAttribute("type","text");
    inputTiEl.classList.add("text-input");
    inputTiEl.placeholder = "Title";
    inputTiEl.setAttribute("name","task-title");

    const inputCoEl = document.createElement("input"); //input element, text
    inputCoEl.setAttribute("typ","text");
    inputCoEl.classList.add("text-input");
    inputCoEl.placeholder = "Content";
    inputCoEl.setAttribute("name","task-content");

    const inputBEl = document.createElement("input"); //input element, text
    inputBEl.setAttribute("type","text");
    inputBEl.classList.add("text-input");
    inputBEl.placeholder = "Begin";
    inputBEl.setAttribute("name","task-begin");

    const inputEnEl = document.createElement("input"); //input element, text
    inputEnEl.setAttribute("type","text");
    inputEnEl.classList.add("text-input");
    inputEnEl.placeholder = "End";
    inputEnEl.setAttribute("name","task-end");

    const brEl = document.createElement("br");

    const sEl = createNewSubmitButton();
    sEl.classList.add("form-button");
    sEl.addEventListener('click', onSubmitNewTask);

    formEl.appendChild(inputTiEl);
    formEl.appendChild(inputCoEl);
    formEl.appendChild(inputBEl);
    formEl.appendChild(inputEnEl);
    formEl.appendChild(brEl);
    formEl.appendChild(sEl);

    myTasksDivEl.appendChild(formEl);
}

function createNewSubmitButton() {
    const buttonEl = document.createElement('button');
    buttonEl.setAttribute('id', 'new-task-button');
    buttonEl.classList.add('new-task-button');
    buttonEl.textContent = 'Create new task';

    return buttonEl;
}

function onSubmitNewTask() {
   const loginFormEl = document.forms['new-task-form'];

    const titleInputEl = loginFormEl.querySelector('input[name="task-title"]');
    const contentInputEl = loginFormEl.querySelector('input[name="task-content"]');
    const beginInputEl = loginFormEl.querySelector('input[name="task-begin"]');
    const endInputEl = loginFormEl.querySelector('input[name="task-end"]');

    const title = titleInputEl.value;
    const content = contentInputEl.value;
    const begin = beginInputEl.value;
    const end = endInputEl.value;
    console.log("Shit");
    const params = new URLSearchParams();
    params.append('title', title);
    params.append('content', content);
    params.append('begin', begin);
    params.append('end', end);

    const xhr = new XMLHttpRequest();
    xhr.addEventListener('load', onSubmissionResponse);
    xhr.addEventListener('error', onNetworkError);
    xhr.open('POST', 'protected/task');
    xhr.send(params);
}

function onSubmissionResponse() {
    if (this.status === OK) {
        const task = JSON.parse(this.responseText);
        console.log(task);
    } else {
        onOtherResponse(myTasksDivEl, this);
    }
}

function onTaskDeleteClicked() {
    removeAllChildren(myTasksDivEl);
    const taskId = this.dataset.taskDeleteId;

    const params = new URLSearchParams();
    params.append('id', taskId);
    const xhr = new XMLHttpRequest();
    xhr.addEventListener('load', onDeleteResponse);
    xhr.addEventListener('error', onNetworkError);
    xhr.open('GET', 'protected/deletetask?' + params.toString());
    xhr.send();
}

function onDeleteResponse() {
    if(this.status === OK) {
        newInfo(myTasksDivEl, JSON.parse(this.responseText).message);
    } else {
        onOtherResponse(myTasksDivEl, this);
    }
}

function onTaskEditClicked() {
    removeAllChildren(myTasksDivEl);
}
