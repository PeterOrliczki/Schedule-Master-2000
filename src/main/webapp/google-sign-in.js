function onSignIn(googleUser) {
    const id_token = googleUser.getAuthResponse().id_token;
    const profile = googleUser.getBasicProfile();
    console.log('ID: ' + profile.getId()); // Do not send to your backend! Use an ID token instead.
    console.log('Name: ' + profile.getName());
    console.log('Image URL: ' + profile.getImageUrl());
    console.log('Email: ' + profile.getEmail()); // This is null if the 'email' scope is not present.

    const params = new URLSearchParams();
    params.append('idToken', id_token);

    var xhr = new XMLHttpRequest();
    xhr.addEventListener('load', onVerified);
    xhr.addEventListener('error', onNetworkError);
    xhr.open('POST', 'google-sign-in');
    xhr.onload = function() {
    console.log('Signed in as: ' + xhr.responseText);
    };
    xhr.send(params);
}

function onVerified() {
    if (this.status === OK) {
        alert("OK");
        onLoad();
    } else {
        onOtherResponse(loginContentDivEl, this);
    }
}

function signOut() {
    var auth2 = gapi.auth2.getAuthInstance();
    auth2.signOut().then(function () {
      console.log('User signed out.');
    });
}
