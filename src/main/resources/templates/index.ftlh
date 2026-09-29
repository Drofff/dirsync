<#import "layout.ftlh" as layout>
<@layout.page title="dirsync">
    <div class="card">
        <p class="card-title">Welcome</p>
        <p class="card-value">Share a directory from this machine, or keep a local copy of a directory shared by another one.</p>
    </div>
    <nav class="choices">
        <a class="choice" href="/server">
            <svg class="choice-icon" viewBox="0 0 24 24" aria-hidden="true"><path d="M3 6.5A1.5 1.5 0 0 1 4.5 5h4.2l2 2.2h8.8A1.5 1.5 0 0 1 21 8.7v9.8a1.5 1.5 0 0 1-1.5 1.5h-15A1.5 1.5 0 0 1 3 18.5z"/><path d="M12 17v-6.5M9.2 13.2 12 10.4l2.8 2.8"/></svg>
            <span class="choice-label">Configure directory sharing</span>
            <span class="choice-hint">Serve a local directory to other machines</span>
        </a>
        <a class="choice" href="/client">
            <svg class="choice-icon" viewBox="0 0 24 24" aria-hidden="true"><path d="M3 6.5A1.5 1.5 0 0 1 4.5 5h4.2l2 2.2h8.8A1.5 1.5 0 0 1 21 8.7v9.8a1.5 1.5 0 0 1-1.5 1.5h-15A1.5 1.5 0 0 1 3 18.5z"/><path d="M12 10.4v6.5M9.2 14.1l2.8 2.8 2.8-2.8"/></svg>
            <span class="choice-label">Use shared directory</span>
            <span class="choice-hint">Sync a remote directory to this machine</span>
        </a>
    </nav>
</@layout.page>
