/**
 * Base JavaScript functions used across the application
 */

/**
 * Utility function to show loading state on a button
 * @param {HTMLElement} button - The button element
 * @param {boolean} isLoading - Whether to show loading state
 */
function setButtonLoading(button, isLoading) {
    if (!button) return;
    
    if (isLoading) {
        button.classList.add('loading');
        button.disabled = true;
    } else {
        button.classList.remove('loading');
        button.disabled = false;
    }
}

/**
 * Auto-hide alerts after a specified time
 * @param {number} delay - Delay in milliseconds (default: 5000)
 */
function autoHideAlerts(delay = 5000) {
    setTimeout(function() {
        const alerts = document.querySelectorAll('.alert');
        alerts.forEach(alert => {
            const fadeAlert = new bootstrap.Alert(alert);
            fadeAlert.close();
        });
    }, delay);
}

/**
 * Initialize auto-hide for alerts on page load
 */
function initAutoHideAlerts() {
    if (typeof bootstrap !== 'undefined' && bootstrap.Alert) {
        autoHideAlerts();
    }
}

/**
 * Initialize when DOM is loaded
 */
document.addEventListener('DOMContentLoaded', function() {
    initAutoHideAlerts();
    
    // Add any other base initialization here
});