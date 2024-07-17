<#macro template user>
    <!doctype html>
    <html lang="ru">
    <head>
        <!-- Required meta tags -->
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
        <link rel="stylesheet" href="/static/css/style.css">

        <!-- Bootstrap CSS -->
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.6.1/dist/css/bootstrap.min.css"
              integrity="sha384-o2zRBfKNquMkCBs9lM5+3HYy3N97DRJlkqS0hZKfpqR+m5HRA5Ck9x2fPQ+Bb6U6"
              crossorigin="anonymous">
        <img src="/static/images/logo.jpg" alt="Logo" style="width:100px;height:auto;">
        <title>ONLINE BUY-SELL PLATFORM EHA</title>
    </head>
    <body>
    <nav class="navbar navbar-dark bg-dark">
        <div class="container-fluid">
            <a href="/">
                <span class="navbar-brand mb-0 h1">ONLINE BUY-SELL PLATFORM EHA</span>
            </a>
            <#if user.email??>
                <div class="dropdown text-end">
                    <a href="/profile">
                        <img src="/static/images/avatar.png" alt="mdo" width="32" height=auto class="rounded-circle">
                    </a>
                </div>
            <#else>
                <button class="btn btn-light my-2 my-sm-0" type="button" onclick="window.location.href = '/login';">Sign in</button>
            </#if>
        </div>
    </nav>
        <div class="container mt-4">
            <#nested/>
        </div>
    <script src="https://code.jquery.com/jquery-3.3.1.slim.min.js"
            integrity="sha384-q8i/X+965DzO0rT7abK41JStqIAqVgRVzpbzo5smXKp4yfRvH+8abtTE1Pi6jizo"
            crossorigin="anonymous"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.3/umd/popper.min.js"
            integrity="sha384-ZMP7rVo3mIyKv4+2p9J3U46jb6OWLuAdn689qCwog9BJIsnjAK/L8WCPVYFIm49"
            crossorigin="anonymous"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.1.1/js/bootstrap.min.js"
            integrity="sha384-smHYkdLAwkXOn1EmNqjk/HfnUCbV2ryYmZ4qpPea6sjB/pTJOevyQpOeMk8ck"
            crossorigin="anonymous"></script>
    </body>
    </html>
</#macro>