<%@tag pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>

<%@attribute name="scripts" fragment="true" %>
<%@attribute name="header" fragment="true" %>
<%@attribute name="footer" fragment="true" %>
<%@attribute name="head_scripts" fragment="true" %>

<%@attribute name="title" required="true" %>

<html>
    <head>
        <meta charset="utf-8">
        <meta http-equiv="x-ua-compatible" content="ie=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Spotted - ${title}</title>
        <link rel="stylesheet" href="/assets/css/foundation.min.css">
        <%-- FIREBASE INITIALIZATION --%>
        <script src="https://www.gstatic.com/firebasejs/5.2.0/firebase.js"></script>
        <script>
            // Initialize Firebase
            var config = {
                apiKey: "AIzaSyBGhNmShExjxbyFU5awcfMzw6qeXpt5LKk",
                authDomain: "spotted-209117.firebaseapp.com",
                databaseURL: "https://spotted-209117.firebaseio.com",
                projectId: "spotted-209117",
                storageBucket: "spotted-209117.appspot.com",
                messagingSenderId: "536993615158"
            };
            firebase.initializeApp(config);
        </script>
        <link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.1.0/css/solid.css" integrity="sha384-TbilV5Lbhlwdyc4RuIV/JhD8NR+BfMrvz4BL5QFa2we1hQu6wvREr3v6XSRfCTRp" crossorigin="anonymous">
        <link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.1.0/css/regular.css" integrity="sha384-avJt9MoJH2rB4PKRsJRHZv7yiFZn8LrnXuzvmZoD3fh1aL6aM6s0BBcnCvBe6XSD" crossorigin="anonymous">
        <link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.1.0/css/fontawesome.css" integrity="sha384-ozJwkrqb90Oa3ZNb+yKFW2lToAWYdTiF1vt8JiH5ptTGHTGcN7qdoR1F95e0kYyG" crossorigin="anonymous">
        <jsp:invoke fragment="head_scripts"/>
    </head>
    <body>
    <jsp:invoke fragment="header"/>
    <jsp:doBody/>
    <jsp:invoke fragment="footer"/>
    <script src="/assets/js/jquery.js"></script>
    <script src="/assets/js/what-input.js"></script>
    <script src="/assets/js/foundation.min.js"></script>
    <script src="/assets/js/app.js"></script>
    <jsp:invoke fragment="scripts"/>
    </body>
</html>