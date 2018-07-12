<%@ page contentType="text/html;charset=UTF-8" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<t:template title="Home">

    <jsp:attribute name="head_scripts">
        <script src='https://www.google.com/recaptcha/api.js'></script>
    </jsp:attribute>

    <jsp:attribute name="scripts">
         <script>
             const reportInput = document.querySelector("#report-modal [name=post_id]");
             let _user;

             firebase.auth().onAuthStateChanged(function(user) {
                 if (user) {
                     _user = user;
                     document.querySelectorAll("[name=uid]").forEach(function(e) {
                         e.value = user.uid;
                     });
                 } else {
                     const xhr = new XMLHttpRequest();
                     xhr.open("GET", "/signout", true);

                     xhr.onload = function() {
                         if (this.readyState === XMLHttpRequest.DONE && this.status === 200) {
                             window.location.replace("/authn");
                         }
                     };

                     xhr.send(null);
                 }
             });

             $(document).ready(function() {
                 document.querySelectorAll(".btn-do-delete").forEach(function(e) {

                     e.addEventListener("click", function() {
                         const xhr = new XMLHttpRequest();
                         xhr.open("POST", "/delete", true);
                         xhr.setRequestHeader('Content-type', 'application/x-www-form-urlencoded');

                         xhr.onload = function() {
                             if (this.readyState === XMLHttpRequest.DONE && this.status === 200) {
                                 window.location.reload();
                             }
                         };

                         xhr.send("post_id=" + this.getAttribute("data-post_id"));
                     });

                 });

                 document.getElementById("btn-logout").addEventListener("click", function() {
                     const xhr = new XMLHttpRequest();
                     xhr.open("GET", "/signout", true);

                     xhr.onload = function() {
                         if (this.readyState === XMLHttpRequest.DONE && this.status === 200) {
                             firebase.auth().signOut().then(function() {
                                 console.log("signed out!");
                                 window.location.replace("/authn");
                             }).catch(function(error) {
                                 console.error(error);
                                 window.location.replace("/authn");
                             });
                         }
                     };

                     xhr.send(null);
                 });

                 document.querySelectorAll(".btn-do-like").forEach(function(e) {
                    // add full heart
                     let liked = false;
                     if (e.getAttribute("data-post-liked") === "true") {
                         liked = true;
                         const heart = e.querySelector("i");
                         heart.classList.remove("far");
                         heart.classList.add("fas");
                         heart.style.color = "#b23632";
                     }

                     // add listener
                     e.addEventListener("click", function() {
                         const xhr = new XMLHttpRequest();
                         let data = "post_id=" + this.getAttribute("data-post_id");

                         if (liked) {
                             data += "&delete=true"
                         }

                         xhr.open("POST", "/like", true);
                         xhr.setRequestHeader('Content-type', 'application/x-www-form-urlencoded');

                         xhr.onload = function() {
                            if (this.readyState === XMLHttpRequest.DONE && this.status === 200) {
                                window.location.reload();
                            }
                         };

                         xhr.send(data);
                     });
                 });

                 document.querySelectorAll(".btn-do-report").forEach(function(e) {
                    e.addEventListener("click", function () {
                        reportInput.value = this.getAttribute("data-post_id");
                        console.log(reportInput.value);
                    });
                 });

             });
         </script>
    </jsp:attribute>

    <jsp:body>
        <div class="reveal" id="report-modal" data-reveal>
            <form action="/report" class="grid-container" method="POST">
                <input type="hidden" name="post_id">
                <input type="hidden" name="uid">
                <label>
                    Reason:
                    <input type="text" name="reason" minlength="5" required>
                </label>
                <div class="g-recaptcha" data-sitekey="6LdDvGMUAAAAAFwP1YAPQvMf2yBjkUso2RqQ2-Jy"></div>
                <button id="do-report" type="submit" class="alert button">Report!</button>
            </form>
            <button class="close-button" data-close aria-label="Close modal" type="button">
                <span aria-hidden="true">&times;</span>
            </button>
        </div>

        <div class="reveal" id="post-modal" data-reveal>
            <form class="grid-container" action="/post" method="POST">
                <label>
                    Message:
                    <textarea name="message" rows="5" minlength="10" required></textarea>
                </label>
                <div class="g-recaptcha" data-sitekey="6LdDvGMUAAAAAFwP1YAPQvMf2yBjkUso2RqQ2-Jy"></div>
                <button id="do-post" type="submit" class="success button">Post!</button>
            </form>
            <button class="close-button" data-close aria-label="Close modal" type="button">
                <span aria-hidden="true">&times;</span>
            </button>
        </div>

        <div class="grid-container">

            <div class="top-bar">
                <div class="top-bar-left">
                    <ul class="dropdown menu" data-dropdown-menu>
                        <li class="menu-text">Spotted</li>
                        <li><a href="/home">Home</a></li>
                        <li><a href="/liked">Liked</a></li>
                        <c:if test="${admin}">
                            <li style="background: #DD433E;"><a style="color: white;" href="/reported">Reported</a></li>
                            <li style="background: #DD433E;"><a style="color: white;" href="/sudo">Give Sudo</a></li>
                        </c:if>
                    </ul>
                </div>
                <div class="top-bar-right">
                    <ul class="menu">
                        <li><button type="button" class="button" data-open="post-modal">Write a post</button></li>
                        <li><button id="btn-logout" type="button" class="alert button">Logout</button></li>
                    </ul>
                </div>
            </div>

            <br>

            <div class="grid-x grid-padding-x small-up-2 medium-up-3">
                <c:forEach var="post" items="${posts}">
                    <div class="cell">
                        <div class="card">
                            <div class="card-section">
                                <div>
                                    <p>${post.message}</p>
                                </div>

                                <c:if test="${reports != null and admin}">
                                    <p>Reason:</p>
                                    <ul style="color: #b23632; font-weight: bold; text-decoration: underline">
                                        <c:forEach items="${reports.get(post.id)}" var="report">
                                            <li>${report.reason}</li>
                                        </c:forEach>
                                    </ul>
                                </c:if>

                                <hr style="margin: 0 0 5px 0">
                                <div style="display: inline">

                                    <div style="float: left; display: inherit; text-align: left; font-size: .65rem; color: #212121; font-style: italic;">
                                        <p>${post.created_at}</p>
                                    </div>

                                    <div style="float: right; display: inherit;">
                                        <button class="btn-do-report" data-open="report-modal" style="cursor: pointer" data-post_id="${post.id}"><i class="far fa-flag"></i></button>
                                        <button class="btn-do-like" style="cursor: pointer" data-post_id="${post.id}" data-post-liked="${upvotes.containsKey(post.id) ? "true" : "false"}"><i class="far fa-heart"></i></button>
                                        <c:if test="${admin}">
                                            <button class="btn-do-delete" style="cursor: pointer; color: #b23632;" data-post_id="${post.id}"><i class="far fa-trash-alt"></i></button>
                                        </c:if>
                                    </div>

                                </div>

                            </div>
                        </div>
                    </div>
                </c:forEach>
            </div>
        </div>
    </jsp:body>

</t:template>