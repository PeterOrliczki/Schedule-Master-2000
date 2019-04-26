function onShopLoad(shop) {
    const shopIdSpandEl = document.getElementById('shop-id');
    const shopNameSpanEl = document.getElementById('shop-name');

    shopIdSpandEl.textContent = shop.id;
    shopNameSpanEl.textContent = shop.name;
}

function onShopResponse() {
    if (this.status === OK) {
        clearMessages();
        showContents(['shop-content', 'back-to-profile-content', 'logout-content']);
        onShopLoad(JSON.parse(this.responseText));
    } else {
        onOtherResponse(shopsContentDivEl, this);
    }
}