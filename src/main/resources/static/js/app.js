document.addEventListener('DOMContentLoaded', function () {

  /* ELIMINACIÓN AUTOMÁTICA DE ALERTAS */
  document.querySelectorAll('.alert[data-autodismiss]').forEach(function (el) {
    setTimeout(function () {
      el.style.transition = 'opacity .5s';
      el.style.opacity = '0';
      setTimeout(function () { el.remove(); }, 500);
    }, 3500);
  });

  /* CONFIRMAR ACCIÓN DE BORRADO */
  document.querySelectorAll('form[data-confirm]').forEach(function (form) {
    form.addEventListener('submit', function (e) {
      if (!confirm(form.getAttribute('data-confirm'))) {
        e.preventDefault();
      }
    });
  });

  /* ACTUALIZACIÓN DE ESTADO AJAX DESDE EL TABLERO */
  document.querySelectorAll('.status-select').forEach(function (sel) {
    sel.addEventListener('change', function () {
      const taskId = sel.getAttribute('data-task-id');
      const status = sel.value;
      const csrf   = document.querySelector('meta[name="_csrf"]');
      const csrfHeader = document.querySelector('meta[name="_csrf_header"]');

      const headers = { 'Content-Type': 'application/x-www-form-urlencoded' };
      if (csrf && csrfHeader) {
        headers[csrfHeader.content] = csrf.content;
      }

      fetch('/tasks/' + taskId + '/status', {
        method: 'POST',
        headers: headers,
        body: 'status=' + encodeURIComponent(status)
      }).then(function (r) {
        if (r.ok) {
          window.location.reload();
        }
      }).catch(function () {
        window.location.reload();
      });
    });
  });

  /* APERTURA Y CIERRE DEL MENÚ LATERAL EN MÓVIL */
  const mobileMenuToggle = document.getElementById('mobileMenuToggle');
  const mobileCloseBtn   = document.getElementById('mobileCloseBtn');
  const sidebar          = document.getElementById('sidebar');
  const sidebarOverlay   = document.getElementById('sidebarOverlay');

  function openSidebar() {
    if (sidebar) sidebar.classList.add('show');
    if (sidebarOverlay) sidebarOverlay.classList.add('show');
  }

  function closeSidebar() {
    if (sidebar) sidebar.classList.remove('show');
    if (sidebarOverlay) sidebarOverlay.classList.remove('show');
  }

  if (mobileMenuToggle) {
    mobileMenuToggle.addEventListener('click', openSidebar);
  }
  if (mobileCloseBtn) {
    mobileCloseBtn.addEventListener('click', closeSidebar);
  }
  if (sidebarOverlay) {
    sidebarOverlay.addEventListener('click', closeSidebar);
  }
});