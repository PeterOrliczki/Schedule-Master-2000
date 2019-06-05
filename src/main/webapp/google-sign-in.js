function onSignIn(googleUser) {
    const id_token = googleUser.getAuthResponse().id_token;
    const profile = googleUser.getBasicProfile();
    console.log('ID: ' + profile.getId()); // Do not send to your backend! Use an ID token instead.
    console.log('Name: ' + profile.getName());
    console.log('Image URL: ' + profile.getImageUrl());
    console.log('Email: ' + profile.getEmail()); // This is null if the 'email' scope is not present.

    var xhr = new XMLHttpRequest();
    xhr.open('POST', 'googlesignin');
    xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
    xhr.onload = onVerified;
    xhr.send('idtoken=' + id_token);
}

function onVerified() {
    if (this.status === OK) {
        const user = JSON.parse(this.responseText);
        alert("OK");
        onProfileLoad(user)
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
