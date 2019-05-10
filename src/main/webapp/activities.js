function onActivityClicked() {
    const xhr = new XMLHttpRequest();
    xhr.addEventListener('load', onActivitiesLoad);
    xhr.addEventListener('error', onNetworkError);
    xhr.open('GET', 'protected/activities');
    xhr.send();
}

function onActivitiesLoad() {
  if(this.status === OK) {
  const activities = JSON.parse(this.responseText);
  createActivitiesDisplay(activities);
  showContents(['activity-content']);
  } else {
    onOtherResponse(myActivitiesDivEl, this);
  }
}

function createActivitiesDisplay(activities) {
  if (activities.length === 0) {
    removeAllChildren(myActivitiesDivEl);
    const pEl = document.createElement('p');
    pEl.setAttribute('id', 'activity-info');
    pEl.textContent = 'The activity log is empty, seems like nothing happened throughout the application yet'
    myActivitiesDivEl.appendChild(pEl);
    myActivitiesDivEl.appendChild(buttonEl);
  } else {
    removeAllChildren(myActivitiesDivEl);
    const tableEl = document.createElement('table');
    const theadEl = createActivitiesTableHeader();
    const tbodyEl = createActivitiesTableBody(activities);
    tableEl.appendChild(theadEl);
    tableEl.appendChild(tbodyEl);
    myActivitiesDivEl.appendChild(tableEl);
  }
}

function createActivitiesTableBody(activities) {
  const tbodyEl = document.createElement('tbody');

  for (let i = 0; i < activities.length; i++) {
    const activity = activities[i];

    const titleTdEl = document.createElement('td');
    titleTdEl.classList.add('schedule-cell');
    titleTdEl.textContent = activity.eventName;

    const tableTdEl = document.createElement('td');
    tableTdEl.classList.add('schedule-cell');
    tableTdEl.textContent = activity.tableName;

    const userTdEl = document.createElement('td');
    userTdEl.classList.add('schedule-cell');
    userTdEl.textContent = activity.userName;

    const dateTdEl = document.createElement('td');
    dateTdEl.classList.add('schedule-cell');
    dateTdEl.textContent = activity.eventDate;

    const trEl = document.createElement('tr');
    trEl.appendChild(titleTdEl);
    trEl.appendChild(tableTdEl);
    trEl.appendChild(userTdEl);
    trEl.appendChild(dateTdEl);

    tbodyEl.appendChild(trEl);
  }

  return tbodyEl;
}

function createActivitiesTableHeader() {
    const titleThEl = document.createElement('th');
    titleThEl.classList.add('schedule-th');
    titleThEl.textContent = 'Event';

    const tableThEl = document.createElement('th');
    tableThEl.classList.add('schedule-th');
    tableThEl.textContent = 'Table';

    const userThEl = document.createElement('th');
    userThEl.classList.add('schedule-th');
    userThEl.textContent = 'User';

    const dateThEl = document.createElement('th');
    dateThEl.classList.add('schedule-th');
    dateThEl.textContent = 'Date';

    const trEl = document.createElement('tr');

    trEl.appendChild(titleThEl);
    trEl.appendChild(tableThEl);
    trEl.appendChild(userThEl);
    trEl.appendChild(dateThEl);

    const theadEl = document.createElement('thead');
    theadEl.appendChild(trEl);
    return theadEl;
}

function onActivityResponse() {
    if (this.status === OK) {
        const activity = JSON.parse(this.responseText);
        onActivityLoad(activity);
    } else {
        onOtherResponse(myActivitiesDivEl, this);
    }
}

function onActivityLoad(activity) {
    const tableEl = document.createElement('table');
    tableEl.setAttribute('id', 'activities-table');
    tableEl.appendChild(theadEl);
    tableEl.appendChild(tbodyEl);
    removeAllChildren(myActivitiesDivEl);
    myActivitiesDivEl.appendChild(tableEl);
}
