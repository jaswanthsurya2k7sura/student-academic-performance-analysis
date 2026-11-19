/* -------------------------------------------------------------
 * VCET Student Academic Performance Portal - Client Script
 * Author: Jaswanth surya S (Reg No: 732925ITR038)
 * ------------------------------------------------------------- */

// Toggle Mobile Sidebar
document.addEventListener('DOMContentLoaded', () => {
    const toggleBtn = document.getElementById('toggleSidebar');
    const sidebar = document.getElementById('sidebar');
    if (toggleBtn && sidebar) {
        toggleBtn.addEventListener('click', () => {
            sidebar.classList.toggle('show');
        });
    }
});

// Toast Notification Helper
function showToast(message, type = 'success') {
    const container = document.getElementById('toastContainer');
    if (!container) return;

    const id = 'toast-' + Date.now();
    const bgClass = type === 'success' ? 'bg-success' : (type === 'danger' ? 'bg-danger' : 'bg-primary');
    const icon = type === 'success' ? 'fa-check-circle' : (type === 'danger' ? 'fa-exclamation-triangle' : 'fa-info-circle');

    const html = `
        <div id="${id}" class="toast align-items-center text-white ${bgClass} border-0 show shadow-lg" role="alert" aria-live="assertive" aria-atomic="true">
            <div class="d-flex">
                <div class="toast-body d-flex align-items-center gap-2">
                    <i class="fa-solid ${icon}"></i>
                    <span>${message}</span>
                </div>
                <button type="button" class="btn-close btn-close-white me-2 m-auto" data-bs-dismiss="toast" aria-label="Close"></button>
            </div>
        </div>
    `;

    container.insertAdjacentHTML('beforeend', html);
    const toastElem = document.getElementById(id);
    setTimeout(() => {
        if (toastElem) toastElem.remove();
    }, 4500);
}

// Client-Side Grade Calculation Preview
function previewGrade(internal, external) {
    const total = internal + external;
    if (external < 27.0 || total < 50.0) {
        return { total, grade: 'RA', gp: 0, status: 'FAIL', badgeClass: 'badge-grade-ra' };
    } else if (total >= 90.0) {
        return { total, grade: 'O', gp: 10, status: 'PASS', badgeClass: 'badge-grade-o' };
    } else if (total >= 80.0) {
        return { total, grade: 'A+', gp: 9, status: 'PASS', badgeClass: 'badge-grade-aplus' };
    } else if (total >= 70.0) {
        return { total, grade: 'A', gp: 8, status: 'PASS', badgeClass: 'badge-grade-a' };
    } else if (total >= 60.0) {
        return { total, grade: 'B+', gp: 7, status: 'PASS', badgeClass: 'badge-grade-bplus' };
    } else {
        return { total, grade: 'B', gp: 6, status: 'PASS', badgeClass: 'badge-grade-b' };
    }
}

// Table Filter Utility
function filterTable(inputId, tableBodyId) {
    const input = document.getElementById(inputId);
    const tbody = document.getElementById(tableBodyId);
    if (!input || !tbody) return;

    input.addEventListener('keyup', () => {
        const query = input.value.toLowerCase();
        const rows = tbody.getElementsByTagName('tr');
        for (let row of rows) {
            const text = row.textContent.toLowerCase();
            row.style.display = text.includes(query) ? '' : 'none';
        }
    });
}

// Print Element Helper
function printElement(elementId) {
    const elem = document.getElementById(elementId);
    if (!elem) return;
    const originalContents = document.body.innerHTML;
    const printContents = elem.innerHTML;
    document.body.innerHTML = printContents;
    window.print();
    document.body.innerHTML = originalContents;
    window.location.reload();
}
