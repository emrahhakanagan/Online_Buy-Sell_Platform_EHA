<#import "blocks/template.ftlh" as t>
<@t.template user>
    <h1>Administrator Panel</h1>
    <table class="table table-hover table-dark">
        <tr>
            <th scope="col">Email</th>
            <th scope="col">Phone number</th>
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
                    <#if user.active>
                        <input type="submit" name="Ban" value="Ban">
                    <#else>
                        <input type="submit" name="Ban" value="Unban">
                    </#if>
                </form>
            </th>
            <th><a href="/admin/user/edit/${user.id}">Edit</a></th>
            <th><a href="/user/${user.id}">Detailed Information</a></th>
            <#else>
                <H3>There are no user available at the moment.</H3>
        </tr>
        </#list>
    </table>
</@t.template>