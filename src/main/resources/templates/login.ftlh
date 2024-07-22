<#import "blocks/template.ftlh" as t>
<@t.template user=user!>
    <h1 style="text-align: center">Authorisation</h1>
    <hr>
    <form action="/login" method="post">
        <div class="form-group">
            <label for="email">Email</label>
            <input type="email" class="form-control <#if RequestParameters?? && RequestParameters.error??>is-invalid</#if>"
                   id="email" name="username">
            <div id="email" class="invalid-feedback">
                Invalid email
            </div>
        </div>
        <div class="form-group">
            <label for="password">Password</label>
            <input type="password" class="form-control <#if RequestParameters?? && RequestParameters.error??>is-invalid</#if>"
                   id="password" name="password">
            <div id="password" class="invalid-feedback">
                Invalid password
            </div>
        </div>
        <input type="hidden" name="_csrf" value="${_csrf.token}">
        <button type="submit" style="width: 100%" class="btn btn-dark">Sign in</button>
    </form>
    <hr>
    <h2>Don't have a ONLINE BUY-SELL PLATFORM EHA account? <a href="/registration">Sign up!</a></h2>
</@t.template>