let couponsTableEl;
let couponsTableBodyEl;

function onCouponClicked() {
    const couponId = this.dataset.couponId;

    const params = new URLSearchParams();
    params.append('id', couponId);

    const xhr = new XMLHttpRequest();
    xhr.addEventListener('load', onCouponResponse);
    xhr.addEventListener('error', onNetworkError);
    xhr.open('GET', 'protected/coupon?' + params.toString());
    xhr.send();
}

function onCouponAddResponse() {
    clearMessages();
    if (this.status === OK) {
        appendCoupon(JSON.parse(this.responseText));
    } else {
        onOtherResponse(couponsContentDivEl, this);
    }
}

function onCouponAddClicked() {
    const couponFormEl = document.forms['coupon-form'];

    const nameInputEl = couponFormEl.querySelector('input[name="name"]');
    const percentageInputEl = couponFormEl.querySelector('input[name="percentage"]');

    const name = nameInputEl.value;
    const percentage = percentageInputEl.value;

    const params = new URLSearchParams();
    params.append('name', name);
    params.append('percentage', percentage);

    const xhr = new XMLHttpRequest();
    xhr.addEventListener('load', onCouponAddResponse);
    xhr.addEventListener('error', onNetworkError);
    xhr.open('POST', 'protected/coupons');
    xhr.send(params);
}

function appendCoupon(coupon) {
    const idTdEl = document.createElement('td');
    idTdEl.textContent = coupon.id;

    const aEl = document.createElement('a');
    aEl.textContent = coupon.name;
    aEl.href = 'javascript:void(0);';
    aEl.dataset.couponId = coupon.id;
    aEl.addEventListener('click', onCouponClicked);

    const nameTdEl = document.createElement('td');
    nameTdEl.appendChild(aEl);

    const percentageTdEl = document.createElement('td');
    percentageTdEl.textContent = coupon.percentage;

    const trEl = document.createElement('tr');
    trEl.appendChild(idTdEl);
    trEl.appendChild(nameTdEl);
    trEl.appendChild(percentageTdEl);
    couponsTableBodyEl.appendChild(trEl);
}

function appendCoupons(coupons) {
    removeAllChildren(couponsTableBodyEl);

    for (let i = 0; i < coupons.length; i++) {
        const coupon = coupons[i];
        appendCoupon(coupon);
    }
}

function onCouponsLoad(coupons) {
    couponsTableEl = document.getElementById('coupons');
    couponsTableBodyEl = couponsTableEl.querySelector('tbody');

    appendCoupons(coupons);
}

function onCouponsResponse() {
    if (this.status === OK) {
        showContents(['coupons-content', 'back-to-profile-content', 'logout-content']);
        onCouponsLoad(JSON.parse(this.responseText));
    } else {
        onOtherResponse(couponsContentDivEl, this);
    }
}