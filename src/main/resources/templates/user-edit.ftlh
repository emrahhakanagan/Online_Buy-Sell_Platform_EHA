<!DOCTYPE html>
<html>
<head>
    <img src="/static/images/logo.jpg" alt="Logo" style="width:100px;height:auto;">
    <title>ONLINE BUY-SELL PLATFORM EHA</title>
</head>
<body>
<H1> BUY-SELL EHA! </H1>
<H3>Edit User ${user.name}</H3>
<form action="/admin/user/edit" method="post">
    <#list roles as role>
        <div>
            <label>
                <input type="checkbox" name="${role}" ${user.roles?seq_contains(role)?string("checked", "")}>${role}
            </label>
        </div>
    </#list>
    <input type="hidden" value="${user.id}" name="user.id">
    <input type="hidden" name="_csrf" value="_csrf.token">
    <button type="submit">Save</button>
</form>
</body>
</html>