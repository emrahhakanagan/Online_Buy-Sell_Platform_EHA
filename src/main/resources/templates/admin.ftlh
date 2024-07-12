<!DOCTYPE html>
<html>
<head>
    <img src="/images/logo.jpg" alt="Logo" style="width:100px;height:auto;">
    <title>ONLINE BUY-SELL PLATFORM EHA</title>
</head>
<body>
<H1> BUY-SELL EHA! </H1>
<#if user.email??>
    <form action="/logout" method="post" style="display: inline;">
        <input type="submit" value="Logout"/>
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
    </form>
<#else>
    <a href="/login">Sign in</a>
</#if>
<hr>
<H4>Administrator Panel</H4>
<table>
    <tr>
        <th>Email</th>
        <th>Phone number</th>
        <th>Activity</th>
        <th>Roles</th>
        <th>Ban</th>
        <th>Edit</th>
        <th>Detailed Information</th>
    </tr>
    <#list users as user>
        <th>${user.email}</th>
        <th>${user.phoneNumber}</th>
        <th>${user.active}</th>
        <th><#list user.roles as role><${role}<#sep>, </#list></th>
        <th>
            <form action="/admin/ban/${user.id}">
                <input type="hidden" name="_csrf" value="${_csrf.token}"><
                <input type="submit" name="Ban">
            </form>
        </th>
        <th><a href="/admin/user/edit/${user.id}">Edit</a></th>
        <th><a href="/user/${user.id}">Detailed Information</a></th>
        <#else>
            <H3>There are no user available at the moment.</H3>
    </#list>
</table>
</body>
</html>