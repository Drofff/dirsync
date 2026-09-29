<#macro page title>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>${title}</title>
    <link rel="stylesheet" href="/css/dirsync.css">
</head>
<body>
<header class="header">
    <h1><a href="/server">dirsync</a></h1>
</header>
<main>
    <#nested>
</main>
</body>
</html>
</#macro>
