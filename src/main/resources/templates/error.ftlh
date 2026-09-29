<#import "layout.ftlh" as layout>
<@layout.page title="dirsync - error">
    <div class="card">
        <p class="card-title">Error</p>
        <p class="card-value error-message">${error!"Unknown error"}</p>
    </div>
    <div class="actions">
        <button type="button" class="btn" onclick="window.location.href='/'">Go back</button>
    </div>
</@layout.page>
