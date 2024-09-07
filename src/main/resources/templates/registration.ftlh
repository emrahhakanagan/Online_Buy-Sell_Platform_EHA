<#import "blocks/template.ftlh" as t>
<@t.template user>
    <h1 style="text-align: center">Registration</h1>
    <hr>
    <form action="/registration" method="post">
        <div class="form-group">
            <label for="name">Name Surname</label>
            <input type="text" class="form-control" required id="name" name="name" placeholder="Name and surname">
        </div>
        <div class="form-group">
            <label for="email">Email</label>
            <input type="email" class="form-control" id="email" required name="email" placeholder="Email">
        </div>
        <div class="form-group">
            <label for="phoneNumber">Phone Number</label>
            <input type="text" class="form-control" id="phoneNumber" required name="phoneNumber" placeholder="Phone number">
        </div>
        <div class="form-group">
            <label for="password">Password</label>
            <input type="password" class="form-control" id="password" required name="password" placeholder="Password">
        </div>
        <div class="form-group">
            <label for="passwordConfirmation">Password Confirmation</label>
            <input type="password" class="form-control" id="passwordConfirmation" required name="passwordConfirmation" placeholder="Confirm your password">
        </div>
        <input type="hidden" name="_csrf" value="${_csrf.token}">
        <button type="submit" style="width: 100%" class="btn btn-dark">Sign up</button>
    </form>
    <#if errorMessage??>
        <h2 style="color: red">${errorMessage}</h2>
    </#if>
</@t.template>