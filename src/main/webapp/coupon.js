let couponId;

function onCouponShopsAddResponse() {
    clearMessages();
    if (this.status === OK) {
        onCouponLoad(JSON.parse(this.responseText));
    } else {
        onOtherResponse(couponContentDivEl, this);
    }
}

function onCouponShopsAddClicked() {
    const couponShopsForm = document.forms['coupon-shops-form'];

    const shopIdsSelectEl = couponShopsForm.querySelector('select[name="shopIds"]');
    
    const params = new URLSearchParams();
    params.append('id', couponId);
    for (let i = 0; i < shopIdsSelectEl.selectedOptions.length; i++) {
        params.append('shopIds', shopIdsSelectEl.selectedOptions[i].value);
    }

    const xhr = new XMLHttpRequest();
    xhr.addEventListener('load', onCouponShopsAddResponse);
    xhr.addEventListener('error', onNetworkError);
    xhr.open('POST', 'protected/coupon');
    xhr.send(params);
}

function createCouponShop(shop) {
    const liEl = document.createElement('li');
    liEl.textContent = `${shop.id} - ${shop.name}`;
    return liEl;
}

function addCouponShops(shops) {
    const couponShopsSpanEl = document.getElementById('coupon-shops');

    removeAllChildren(couponShopsSpanEl);

    if (shops.length === 0) {
        couponShopsSpanEl.textContent = 'No associated shops';
    } else {
        const ulEl = document.createElement('ul');
        for (let i= 0; i < shops.length; i++) {
            const shop = shops[i];
            ulEl.appendChild(createCouponShop(shop));
        }
        couponShopsSpanEl.appendChild(ulEl);
    }
}

function addAllShops(shops) {
    const selectEl = document.querySelector('#coupon-shops-form > select');

    removeAllChildren(selectEl);

    for (let i= 0; i < shops.length; i++) {
        const shop = shops[i];

        const optionEl = document.createElement('option');
        optionEl.value = shop.id;
        optionEl.textContent = `${shop.id} - ${shop.name}`;

        selectEl.appendChild(optionEl);
    }
}

function onCouponLoad(couponDto) {
    couponId = couponDto.coupon.id;

    const couponIdSpandEl = document.getElementById('coupon-id');
    const couponNameSpanEl = document.getElementById('coupon-name');
    const couponPercentageSpanEl = document.getElementById('coupon-percentage');

    couponIdSpandEl.textContent = couponDto.coupon.id;
    couponNameSpanEl.textContent = couponDto.coupon.name;
    couponPercentageSpanEl.textContent = couponDto.coupon.percentage;

    addCouponShops(couponDto.couponShops);
    addAllShops(couponDto.allShops);
}

function onCouponResponse() {
    if (this.status === OK) {
        clearMessages();
        showContents(['coupon-content', 'back-to-profile-content', 'logout-content']);
        onCouponLoad(JSON.parse(this.responseText));
    } else {
        onOtherResponse(couponsContentDivEl, this);
    }
}