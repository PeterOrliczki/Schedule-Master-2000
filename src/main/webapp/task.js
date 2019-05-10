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
        tableEl.setAttribute('id', 'edit-task-table');
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
    const titleTdEl = document.createElement('th');
    titleTdEl.textContent = 'Title';

    const visibilityTdEl = document.createElement('th');
    visibilityTdEl.textContent = 'Content';

    const startTdEl = document.createElement('th');
    startTdEl.textContent = 'Start';

    const endTdEl = document.createElement('th');
    endTdEl.textContent = 'End';

    const buttonOneTdEl = document.createElement('th');
    buttonOneTdEl.textContent = 'Edit';

    const buttonTwoTdEl = document.createElement('th');
    buttonTwoTdEl.textContent = 'Delete';

    const trEl = document.createElement('tr');
    trEl.appendChild(titleTdEl);
    trEl.appendChild(visibilityTdEl);
    trEl.appendChild(startTdEl);
    trEl.appendChild(endTdEl);
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
        contentTdEl.textContent = task.content;
        contentTdEl.setAttribute('id', task.id);

        const startTdEl = document.createElement('td');
        startTdEl.textContent = task.start;
        startTdEl.setAttribute('id', task.id);

        const endTdEl = document.createElement('td');
        endTdEl.textContent = task.end;
        endTdEl.setAttribute('id', task.id);

        const buttonEditEl = document.createElement('i');
        buttonEditEl.classList.add('icon-edit');

        buttonEditEl.dataset.taskEditId = task.id;
        buttonEditEl.addEventListener('click', onTaskEditButtonClicked);

        const buttonDeleteEl = document.createElement('i');
        buttonDeleteEl.classList.add('icon-trash');
        buttonDeleteEl.setAttribute('id', task.id);
        buttonDeleteEl.dataset.taskDeleteId = task.id;
        buttonDeleteEl.addEventListener('click', onTaskDeleteClicked);

        const buttonOneTdEl = document.createElement('td');
        buttonOneTdEl.appendChild(buttonEditEl);
        buttonOneTdEl.setAttribute('id', 'task-edit-button-' + task.id);
        const buttonTwoTdEl = document.createElement('td');
        buttonTwoTdEl.appendChild(buttonDeleteEl);

        const trEl = document.createElement('tr');
        trEl.setAttribute('id', 'row-task-id-' + task.id);
        trEl.appendChild(titleTdEl);
        trEl.appendChild(contentTdEl);
        trEl.appendChild(startTdEl);
        trEl.appendChild(endTdEl);
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
    //formEl.onSubmit = 'return false;';

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
    buttonEl.setAttribute('type', 'button');
    buttonEl.classList.add('form-button');
    buttonEl.textContent = 'Create new task';

    return buttonEl;
}

function onSubmitNewTask() {
    const loginFormEl = document.forms['new-task-form'];

    const titleInputEl = loginFormEl.querySelector('input[name="task-title"]');
    const contentInputEl = loginFormEl.querySelector('input[name="task-content"]');
    const beginInputEl = loginFormEl.querySelector('input[name="task-begin"]');
    const endInputEl = loginFormEl.querySelector('input[name="task-end"]');
    console.log(contentInputEl.value);

    removeAllChildren(myTasksDivEl);
    const title = titleInputEl.value;
    const content = contentInputEl.value;
    const begin = beginInputEl.value;
    const end = endInputEl.value;

    const params = new URLSearchParams();
    params.append('task-title', title);
    params.append('task-content', content);
    params.append('task-begin', begin);
    params.append('task-end', end);

    const xhr = new XMLHttpRequest();
    xhr.addEventListener('load', onSubmissionResponse);
    xhr.addEventListener('error', onNetworkError);
    xhr.open('POST', 'protected/tasks');
    xhr.send(params);
}

function onSubmissionResponse() {
    if (this.status === OK) {
        const task = JSON.parse(this.responseText);
        alert(task.message);
        onTasksClicked();
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
    xhr.open('DELETE', 'protected/tasks?' + params.toString());
    xhr.send();
}

function onDeleteResponse() {
    if(this.status === OK) {
        const response = JSON.parse(this.responseText);
        alert(response.message);
        onTasksClicked();
    } else {
        onOtherResponse(myTasksDivEl, this);
    }
}

function onTaskEditClicked() {
    removeAllChildren(myTasksDivEl);
    const taskId = this.dataset.taskEditId;
    const params = new URLSearchParams();
    params.append('id', taskId);

    const xhr = new XMLHttpRequest();
    xhr.addEventListener('load', onTaskEditResponse);
    xhr.addEventListener('error', onNetworkError);
    xhr.open('GET', 'protected/task?' + params.toString());
    xhr.send();

}

function onTaskEditResponse() {
    if (this.status === OK) {
        onTaskTitleEditLoad(JSON.parse(this.responseText));
    } else {
        onOtherResponse(myTasksDivEl, this);
    }
}


function onTaskEditButtonClicked() {
    const id = this.dataset.taskEditId;
    const tableEl = document.getElementById('edit-task-table');
    const cells = tableEl.rows.namedItem('row-task-id-' + id).cells;

    for (let i = 0; i < cells.length - 2; i++) {
        const tdEl = cells[i];
        const oldValue = tdEl.textContent;
        tdEl.textContent = '';
        tdEl.appendChild(createPopUpInput(i, oldValue));
    }

    const buttonEditTdEl = document.getElementById('task-edit-button-' + id);
    const saveButtonEl = document.createElement('i');
    saveButtonEl.classList.add('icon-save');
    saveButtonEl.dataset.taskId = id;
    saveButtonEl.addEventListener('click', onSaveButtonClicked);
    buttonEditTdEl.innerHTML = '';
    buttonEditTdEl.appendChild(saveButtonEl);
}

function createPopUpInput(id, textContent) {
    const inputEl = document.createElement('input');
    inputEl.classList.add('pop-up-box');
    inputEl.name = 'input-task-id-' + id;
    inputEl.setAttribute('id', 'task-input-' + id);
    inputEl.value = textContent;
    return inputEl;
}

function onSaveButtonClicked() {
    const id = this.dataset.taskId;
    const user = getCurrentUser();

    const inputs = document.getElementsByClassName('pop-up-box');

    const data = {};
    data.id = id;
    data.title = inputs[0].value;
    data.content = inputs[1].value;
    data.start = inputs[2].value;
    data.end = inputs[3].value
    data.userId = user.id;
    const json = JSON.stringify(data);

    const xhr = new XMLHttpRequest();
    xhr.addEventListener('load', onTaskEditSubmitResponse);
    xhr.addEventListener('error', onNetworkError);
    xhr.open('PUT', 'protected/tasks');
    xhr.setRequestHeader('Content-type','application/json; charset=utf-8');
    xhr.send(json);
}


function onTaskEditSubmitResponse() {
   if (this.status === OK) {
      const response = JSON.parse(this.responseText);
      alert(response.message);
      onTasksClicked();
   } else {
       onOtherResponse(mySchedulesDivEl, this);
   }
}
