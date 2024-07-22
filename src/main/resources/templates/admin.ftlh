<#import "blocks/template.ftlh" as t>
<@t.template user>
    `<h1 style="text-align: center">Admin Panel</h1><hr>
    <table class="table table-hover table-dark">
        <tr>
            <th scope="col">Email</th>
            <th scope="col">Phone Number</th>
            <th scope="col">Activity</th>
            <th scope="col">Roles</th>
            <th scope="col">Ban</th>
            <th scope="col">Edit</th>
            <th scope="col">Detailed Information</th>
        </tr>
        <#list users as user>
            <tr>
                <th>${user.email}</th>
                <th>${user.phoneNumber}</th>
                <th>${user.active?string('true', 'false')}</th>
                <th><#list user.roles as role>${role}</#list></th>
                <th>
                    <form action="/admin/user/ban/${user.id}" method="post">
                        <input type="hidden" name="_csrf" value="${_csrf.token}">
                        <input type="submit" class="btn btn-light" value="${user.active?string("Ban", "Unban")}">
                    </form>
                </th>
                <th><a href="/admin/user/edit/${user.id}">Edit</a></th>
                <th><a href="/user/${user.id}">Detailed Information</a></th>
            </tr>
        <#else>
            <h3>No users found!</h3>
        </#list>
    </table>
</@t.template>