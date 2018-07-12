<%@ page contentType="text/html;charset=UTF-8" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>

<t:template title="Authentication">
    <jsp:attribute name="head_scripts">
    <script src="https://cdn.firebase.com/libs/firebaseui/3.1.1/firebaseui.js"></script>
    <link type="text/css" rel="stylesheet" href="https://cdn.firebase.com/libs/firebaseui/3.1.1/firebaseui.css" />
    <script src="https://www.gstatic.com/firebasejs/ui/3.1.1/firebase-ui-auth__en.js"></script>
    <link type="text/css" rel="stylesheet" href="https://www.gstatic.com/firebasejs/ui/3.1.1/firebase-ui-auth.css" />
    <script>
        var uiConfig = {
            signInSuccessUrl: 'http://127.0.0.1:8080/authn',
            signInOptions: [
                // Leave the lines as is for the providers you want to offer your users.
                firebase.auth.GoogleAuthProvider.PROVIDER_ID,
                firebase.auth.FacebookAuthProvider.PROVIDER_ID,
                firebase.auth.EmailAuthProvider.PROVIDER_ID
            ]
        };

        initApp = function() {
            firebase.auth().onAuthStateChanged(function(user) {
                if (user) {
                    user.getIdToken(true).then(function (idToken) {
                        // FIXME!
                        // this operation needs to be done with HTTPS only!
                        const xhr = new XMLHttpRequest();
                        xhr.open("POST", "/signin", true);
                        xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");

                        xhr.onload = function() {
                            if (xhr.readyState === xhr.DONE) {
                                if (xhr.status === 304 || (xhr.status === 200 && xhr.responseURL)) {
                                    console.log("success!");
                                    console.log("doing a redirect, follow me :D");
                                    window.location.replace(xhr.responseURL);
                                } else {
                                    console.error("something went wrong with the request :(");
                                }
                            }
                        };
                        console.log("sending: " + idToken);
                        xhr.send("id_token=" + idToken);
                    }).catch(function (error) {
                        console.error(error);
                    });
                    // User is signed in.
                    const displayName = user.displayName;
                    const email = user.email;
                    const emailVerified = user.emailVerified;
                    const photoURL = user.photoURL;
                    const uid = user.uid;
                    const phoneNumber = user.phoneNumber;
                    const providerData = user.providerData;
                } else {
                    $("#authn-mask").remove();
                    // User is signed out.
                    // Initialize the FirebaseUI Widget using Firebase.
                    var ui = new firebaseui.auth.AuthUI(firebase.auth());
                    // The start method will wait until the DOM is loaded.
                    ui.start('#firebaseui-auth-container', uiConfig);
                }
            }, function(error) {
                console.log(error);
            });
        };

        window.addEventListener("load", function() {
            initApp();
        });
    </script>
    </jsp:attribute>
    <jsp:body>
        <div class="grid-container">
            <div class="grid-x grid-margin-x" id="authn-mask">
                <h1 class="cell" style="text-align: center; text-decoration: underline">Please, wait. Authenticating....</h1>
                <div style="margin: 0 auto;">
                    <img src="/assets/img/monkey.webp" alt="crazy monkey">
                    <img src="/assets/img/cat.webp" alt="douh! cat">
                </div>
                <div id="sign-in-status"></div>
                <div id="sign-in"></div>
                <div id="account-details"></div>
            </div>
            <div id="firebaseui-auth-container"></div>
        </div>

    </jsp:body>
</t:template>