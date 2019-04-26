let shopsTableEl;
let shopsTableBodyEl;

function onShopClicked() {
    const shopId = this.dataset.shopId;

    const params = new URLSearchParams();
    params.append('id', shopId);

    const xhr = new XMLHttpRequest();
    xhr.addEventListener('load', onShopResponse);
    xhr.addEventListener('error', onNetworkError);
    xhr.open('GET', 'protected/shop?' + params.toString());
    xhr.send();
}

function onShopAddResponse() {
    clearMessages();
    if (this.status === OK) {
        appendShop(JSON.parse(this.responseText));
    } else {
        onOtherResponse(shopsContentDivEl, this);
    }
}

function onShopAddClicked() {
    const shopFormEl = document.forms['shop-form'];

    const nameInputEl = shopFormEl.querySelector('input[name="name"]');

    const name = nameInputEl.value;

    const params = new URLSearchParams();
    params.append('name', name);

    const xhr = new XMLHttpRequest();
    xhr.addEventListener('load', onShopAddResponse);
    xhr.addEventListener('error', onNetworkError);
    xhr.open('POST', 'protected/shops');
    xhr.send(params);
}

function appendShop(shop) {
    const idTdEl = document.createElement('td');
    idTdEl.textContent = shop.id;

    const aEl = document.createElement('a');
    aEl.textContent = shop.name;
    aEl.href = 'javascript:void(0);';
    aEl.dataset.shopId = shop.id;
    aEl.addEventListener('click', onShopClicked);

    const nameTdEl = document.createElement('td');
    nameTdEl.appendChild(aEl);

    const trEl = document.createElement('tr');
    trEl.appendChild(idTdEl);
    trEl.appendChild(nameTdEl);
    shopsTableBodyEl.appendChild(trEl);
}

function appendShops(shops) {
    removeAllChildren(shopsTableBodyEl);

    for (let i = 0; i < shops.length; i++) {
        const shop = shops[i];
        appendShop(shop);
    }
}

function onShopsLoad(shops) {
    shopsTableEl = document.getElementById('shops');
    shopsTableBodyEl = shopsTableEl.querySelector('tbody');

    appendShops(shops);
}

function onShopsResponse() {
    if (this.status === OK) {
        showContents(['shops-content', 'back-to-profile-content', 'logout-content']);
        onShopsLoad(JSON.parse(this.responseText));
    } else {
        onOtherResponse(shopsContentDivEl, this);
    }
}