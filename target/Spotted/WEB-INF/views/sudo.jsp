<%@ page contentType="text/html;charset=UTF-8" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<t:template title="Sudo">

    <jsp:attribute name="head_scripts">
        <script src='https://www.google.com/recaptcha/api.js'></script>
    </jsp:attribute>

    <jsp:attribute name="scripts">

    </jsp:attribute>

    <jsp:body>
        <div class="grid-container">
            <form action="/give-sudo" method="POST" class="grid-container">
                <label>
                    User identifier:
                    <input type="text" name="identifier" minlength="3">
                </label>
                <div class="g-recaptcha" data-sitekey="6LdDvGMUAAAAAFwP1YAPQvMf2yBjkUso2RqQ2-Jy"></div>
                <button type="submit" class="alert button">Give sudo</button>
            </form>
        </div>
    </jsp:body>

</t:template>