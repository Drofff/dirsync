<#import "layout.ftlh" as layout>
<#assign sharing = sharedDir?has_content>
<@layout.page title="dirsync">
    <a class="back-link" href="/">&larr; Back to home</a>
    <div class="card">
        <p class="card-title">Sharing directory</p>
        <#if sharing>
            <p class="card-value">${sharedDir}</p>
        <#else>
            <p class="card-value empty">Not sharing any directory</p>
        </#if>
    </div>
    <div class="actions">
        <button type="button" class="btn" id="open-modal">${sharing?then("Change directory", "Share directory")}</button>
        <#if sharing>
            <button type="button" class="btn danger" id="stop-sharing">Stop sharing</button>
        </#if>
    </div>

    <div class="modal-backdrop" id="modal">
        <div class="modal" role="dialog" aria-modal="true" aria-labelledby="modal-title">
            <h2 id="modal-title">Select directory</h2>
            <div class="field">
                <label for="dir-path">Directory path</label>
                <input type="text" id="dir-path" placeholder="/Users/me/shared" autocomplete="off">
            </div>
            <div class="actions">
                <button type="button" class="btn secondary" id="cancel">Cancel</button>
                <button type="button" class="btn" id="confirm" disabled>Confirm</button>
            </div>
        </div>
    </div>

    <script>
        const modal = document.getElementById('modal');
        const pathInput = document.getElementById('dir-path');
        const confirmBtn = document.getElementById('confirm');

        async function send(method, url) {
            try {
                // Redirects must not be followed, otherwise fetch would repeat the same method against /server
                const res = await fetch(url, { method, redirect: 'manual' });
                if (res.type === 'opaqueredirect') {
                    window.location.href = '/server';
                    return;
                }
                const html = await res.text();
                document.open();
                document.write(html);
                document.close();
            } catch (e) {
                alert('Request failed: ' + e.message);
            }
        }

        function closeModal() {
            modal.classList.remove('open');
            pathInput.value = '';
            confirmBtn.disabled = true;
        }

        document.getElementById('open-modal').addEventListener('click', () => {
            modal.classList.add('open');
            pathInput.focus();
        });
        document.getElementById('cancel').addEventListener('click', closeModal);
        modal.addEventListener('click', e => { if (e.target === modal) closeModal(); });
        document.addEventListener('keydown', e => { if (e.key === 'Escape') closeModal(); });

        pathInput.addEventListener('input', () => {
            confirmBtn.disabled = pathInput.value.trim() === '';
        });

        confirmBtn.addEventListener('click', () => {
            const params = new URLSearchParams({ directoryPath: pathInput.value.trim() });
            confirmBtn.disabled = true;
            send('PUT', '/server/directory?' + params);
        });

        const stopBtn = document.getElementById('stop-sharing');
        if (stopBtn) {
            stopBtn.addEventListener('click', () => {
                stopBtn.disabled = true;
                send('DELETE', '/server/directory');
            });
        }
    </script>
</@layout.page>
