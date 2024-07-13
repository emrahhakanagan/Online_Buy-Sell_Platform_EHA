<!DOCTYPE html>
<html>
<head>
    <img src="/images/logo.jpg" alt="Logo" style="width:100px;height:auto;">
    <title>ONLINE BUY-SELL PLATFORM EHA</title>
</head>
<body>
<H1> BUY-SELL EHA! </H1>
<hr>
<H4>Administrator Panel</H4>
<table border="1">
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
    <tr>
        <th>${user.email}</th>
        <th>${user.phoneNumber}</th>
        <th><#if user.active>true<#else>false</#if></th>
        <th><#list user.roles as role>${role}</#list></th>
        <th>
            <form action="/admin/ban/${user.id}" method="post">
                <input type="hidden" name="_csrf" value="${_csrf.token}">
                <input type="submit" name="Ban" value="Ban">
            </form>
        </th>
        <th><a href="/admin/user/edit/${user.id}">Edit</a></th>
        <th><a href="/user/${user.id}">Detailed Information</a></th>
        <#else>
            <H3>There are no user available at the moment.</H3>
    </tr>
    </#list>
</table>
</body>
</html>