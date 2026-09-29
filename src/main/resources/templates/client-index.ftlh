<#import "layout.ftlh" as layout>
<@layout.page title="dirsync">
    <a class="back-link" href="/">&larr; Back to home</a>
    <div class="card">
        <p class="card-title">Remote directories</p>
        <#if dirCopies?has_content>
            <ul class="copy-list">
                <#list dirCopies as copy>
                    <li class="copy-row">
                        <div class="copy-info">
                            <span class="copy-item" title="Server IP">
                                <svg class="icon" viewBox="0 0 24 24" aria-hidden="true"><rect x="3" y="4" width="18" height="7" rx="1.5"/><rect x="3" y="13" width="18" height="7" rx="1.5"/><circle cx="7" cy="7.5" r="1"/><circle cx="7" cy="16.5" r="1"/></svg>
                                <span class="card-value">${copy.serverIp}</span>
                            </span>
                            <span class="copy-item" title="Local directory">
                                <svg class="icon" viewBox="0 0 24 24" aria-hidden="true"><path d="M3 6.5A1.5 1.5 0 0 1 4.5 5h4.2l2 2.2h8.8A1.5 1.5 0 0 1 21 8.7v9.8a1.5 1.5 0 0 1-1.5 1.5h-15A1.5 1.5 0 0 1 3 18.5z"/></svg>
                                <span class="card-value">${copy.localDirPath}</span>
                            </span>
                        </div>
                        <div class="copy-actions">
                            <button type="button" class="btn small" data-refresh="${copy.id?c}">Refresh</button>
                            <button type="button" class="btn small danger" data-delete="${copy.id?c}" data-server="${copy.serverIp}">Delete</button>
                        </div>
                    </li>
                </#list>
            </ul>
        <#else>
            <p class="card-value empty">No remote directories configured yet.</p>
        </#if>
    </div>
    <div class="actions">
        <button type="button" class="btn" id="open-modal">Add remote directory</button>
    </div>

    <div class="modal-backdrop" id="modal">
        <div class="modal" role="dialog" aria-modal="true" aria-labelledby="modal-title">
            <h2 id="modal-title">Add remote directory</h2>
            <div class="field">
                <label for="server-ip">Server IP Address</label>
                <input type="text" id="server-ip" placeholder="192.168.0.10:8080" autocomplete="off">
            </div>
            <div class="field">
                <label for="local-dir-path">Local Directory Path</label>
                <input type="text" id="local-dir-path" placeholder="/Users/me/copy" autocomplete="off">
            </div>
            <div class="actions">
                <button type="button" class="btn secondary" id="cancel">Cancel</button>
                <button type="button" class="btn" id="confirm" disabled>Confirm</button>
            </div>
        </div>
    </div>

    <div class="modal-backdrop" id="sync-overlay">
        <div class="modal sync-status" role="status" aria-live="polite">
            <div class="spinner"></div>
            <p>Synchronizing the files</p>
        </div>
    </div>

    <script>
        const modal = document.getElementById('modal');
        const serverIpInput = document.getElementById('server-ip');
        const localDirInput = document.getElementById('local-dir-path');
        const confirmBtn = document.getElementById('confirm');
        const syncOverlay = document.getElementById('sync-overlay');

        async function send(method, url) {
            try {
                // Redirects must not be followed, otherwise fetch would repeat the same method against /client
                const res = await fetch(url, { method, redirect: 'manual' });
                if (res.type === 'opaqueredirect') {
                    window.location.href = '/client';
                    return;
                }
                const html = await res.text();
                document.open();
                document.write(html);
                document.close();
            } catch (e) {
                syncOverlay.classList.remove('open');
                alert('Request failed: ' + e.message);
            }
        }

        function closeModal() {
            modal.classList.remove('open');
            serverIpInput.value = '';
            localDirInput.value = '';
            confirmBtn.disabled = true;
        }

        function updateConfirm() {
            confirmBtn.disabled = serverIpInput.value.trim() === '' || localDirInput.value.trim() === '';
        }

        document.getElementById('open-modal').addEventListener('click', () => {
            modal.classList.add('open');
            serverIpInput.focus();
        });
        document.getElementById('cancel').addEventListener('click', closeModal);
        modal.addEventListener('click', e => { if (e.target === modal) closeModal(); });
        document.addEventListener('keydown', e => { if (e.key === 'Escape') closeModal(); });

        serverIpInput.addEventListener('input', updateConfirm);
        localDirInput.addEventListener('input', updateConfirm);

        confirmBtn.addEventListener('click', () => {
            const params = new URLSearchParams({
                serverIp: serverIpInput.value.trim(),
                localDirPath: localDirInput.value.trim()
            });
            confirmBtn.disabled = true;
            send('POST', '/client/copy?' + params);
        });

        document.querySelectorAll('[data-refresh]').forEach(btn => {
            btn.addEventListener('click', () => {
                syncOverlay.classList.add('open');
                send('PUT', '/client/copy/' + btn.dataset.refresh + '/refresh');
            });
        });

        document.querySelectorAll('[data-delete]').forEach(btn => {
            btn.addEventListener('click', () => {
                if (!confirm('Delete remote directory ' + btn.dataset.server + '?')) {
                    return;
                }
                btn.disabled = true;
                send('DELETE', '/client/copy/' + btn.dataset.delete);
            });
        });
    </script>
</@layout.page>
