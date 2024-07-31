<#import "blocks/template.ftlh" as t>
<@t.template user>
    <h1 style="text-align: center">Confirmation</h1>
    <hr>
    <#if error??>
        <h2 style="color: red">${error}</h2>
    <#else>
        <h2 style="color: green">Your email has been confirmed successfully. ${user.name}</h2>
        <h3>You can now <a href="/login">log in</a>.</h3>
    </#if>
</@t.template>