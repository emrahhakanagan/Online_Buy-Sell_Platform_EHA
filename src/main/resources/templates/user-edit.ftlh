<#import "blocks/template.ftlh" as t>
<@t.template admin>
    <h1 style="text-align: center">Editing User ${editUser.name}</h1><hr>
    <form action="/admin/user/edit" method="post">
        <#list roles as role>
            <div>
                <label><input type="checkbox" name="roles" value="${role}" ${editUser.roles?seq_contains(role)?string("checked", "")}>${role}</label>
            </div>
        </#list>
        <input type="hidden" value="${editUser.id}" name="userId">
        <input type="hidden" value="${_csrf.token}" name="_csrf">
        <button type="submit" class="btn btn-dark">Save</button>
    </form>

</@t.template>