/**
 * Login page specific JavaScript
 */

// DOM Elements
let togglePassword, passwordInput, loginForm, loginBtn, forgotPasswordForm;
let studentNoInput, rememberCheckbox;

/**
 * Initialize login page elements
 */
function initLoginElements() {
    togglePassword = document.getElementById('togglePassword');
    passwordInput = document.getElementById('password');
    loginForm = document.getElementById('loginForm');
    loginBtn = document.getElementById('loginBtn');
    forgotPasswordForm = document.getElementById('forgotPasswordForm');
    studentNoInput = document.getElementById('studentNo');
    rememberCheckbox = document.getElementById('remember');
}

/**
 * Toggle password visibility
 */
function initPasswordToggle() {
    if (!togglePassword || !passwordInput) return;
    
    togglePassword.addEventListener('click', function() {
        const type = passwordInput.getAttribute('type') === 'password' ? 'text' : 'password';
        passwordInput.setAttribute('type', type);
        
        // Toggle icon
        const icon = this.querySelector('i');
        if (type === 'password') {
            icon.classList.remove('bi-eye-slash');
            icon.classList.add('bi-eye');
        } else {
            icon.classList.remove('bi-eye');
            icon.classList.add('bi-eye-slash');
        }
    });
}

/**
 * Initialize login form submission
 */
function initLoginForm() {
    if (!loginForm || !loginBtn) return;
    
    loginForm.addEventListener('submit', function(e) {
        // Validate form
        const studentNo = studentNoInput ? studentNoInput.value.trim() : '';
        const password = passwordInput ? passwordInput.value.trim() : '';
        
        if (!studentNo || !password) {
            return; // Let browser handle validation
        }
        
        // Show loading state
        setButtonLoading(loginBtn, true);
        
        // Save remember me preference
        saveRememberMePreference();
        
        // In production, remove the setTimeout - it's just for demo
        setTimeout(() => {
            setButtonLoading(loginBtn, false);
        }, 2000);
    });
}

/**
 * Initialize forgot password form
 */
function initForgotPasswordForm() {
    if (!forgotPasswordForm) return;
    
    forgotPasswordForm.addEventListener('submit', function(e) {
        e.preventDefault();
        const resetStudentNoInput = document.getElementById('resetStudentNo');
        const studentNo = resetStudentNoInput ? resetStudentNoInput.value.trim() : '';
        
        if (!studentNo) {
            alert('Please enter your student number');
            return;
        }
        
        // Show success message (in production, this would make an API call)
        alert(`Password reset link has been sent for student number: ${studentNo}\n\n(Note: This is a demo. In production, this would send an email.)`);
        
        // Close modal
        const modal = bootstrap.Modal.getInstance(document.getElementById('forgotPasswordModal'));
        if (modal) {
            modal.hide();
        }
        
        // Reset form
        forgotPasswordForm.reset();
    });
}

/**
 * Save remember me preference to localStorage
 */
function saveRememberMePreference() {
    if (!rememberCheckbox || !studentNoInput) return;
    
    if (rememberCheckbox.checked) {
        localStorage.setItem('rememberedStudentNo', studentNoInput.value);
    } else {
        localStorage.removeItem('rememberedStudentNo');
    }
}

/**
 * Load remember me preference from localStorage
 */
function loadRememberMePreference() {
    if (!rememberCheckbox || !studentNoInput) return;
    
    const storedStudentNo = localStorage.getItem('rememberedStudentNo');
    if (storedStudentNo) {
        studentNoInput.value = storedStudentNo;
        rememberCheckbox.checked = true;
    }
}

/**
 * Initialize student number input validation
 */
function initStudentNoValidation() {
    if (!studentNoInput) return;
    
    studentNoInput.addEventListener('input', function() {
        const value = this.value;
        // Allow only alphanumeric and common student number formats
        this.value = value.replace(/[^a-zA-Z0-9\-_]/g, '');
    });
}

/**
 * Auto-focus on student number input
 */
function initAutoFocus() {
    if (studentNoInput) {
        studentNoInput.focus();
    }
}

/**
 * Initialize modal close handlers
 */
function initModalHandlers() {
    const forgotPasswordModal = document.getElementById('forgotPasswordModal');
    if (forgotPasswordModal) {
        forgotPasswordModal.addEventListener('hidden.bs.modal', function() {
            if (forgotPasswordForm) {
                forgotPasswordForm.reset();
            }
        });
    }
}

/**
 * Initialize all login page functionality
 */
function initLoginPage() {
    // Initialize elements
    initLoginElements();
    
    // Set up event listeners
    initPasswordToggle();
    initLoginForm();
    initForgotPasswordForm();
    initStudentNoValidation();
    initModalHandlers();
    
    // Load preferences and set focus
    loadRememberMePreference();
    initAutoFocus();
}

// Initialize when DOM is loaded
document.addEventListener('DOMContentLoaded', initLoginPage);