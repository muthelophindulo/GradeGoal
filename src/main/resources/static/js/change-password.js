document.addEventListener('DOMContentLoaded', function() {
    // DOM Elements
    const changePasswordForm = document.getElementById('changePasswordForm');
    const currentPassword = document.getElementById('currentPassword');
    const newPassword = document.getElementById('password');
    const confirmPassword = document.getElementById('confirmPassword');
    const toggleNewPassword = document.getElementById('toggleNewPassword');
    const toggleConfirmPassword = document.getElementById('toggleConfirmPassword');
    const submitButton = document.getElementById('submitButton');
    const passwordMatchText = document.getElementById('passwordMatchText');
    
    // Password toggle functionality
    function setupPasswordToggle(passwordField, toggleButton) {
        toggleButton.addEventListener('click', function() {
            const type = passwordField.getAttribute('type') === 'password' ? 'text' : 'password';
            passwordField.setAttribute('type', type);
            this.innerHTML = type === 'password' ? '<i class="bi bi-eye"></i>' : '<i class="bi bi-eye-slash"></i>';
        });
    }
    
    // Setup toggles
    if (toggleNewPassword && newPassword) {
        setupPasswordToggle(newPassword, toggleNewPassword);
    }
    
    if (toggleConfirmPassword && confirmPassword) {
        setupPasswordToggle(confirmPassword, toggleConfirmPassword);
    }
    
    // Check password strength
    function checkPasswordStrength(password) {
        const requirements = {
            length: password.length >= 8,
            uppercase: /[A-Z]/.test(password),
            lowercase: /[a-z]/.test(password),
            number: /[0-9]/.test(password),
            special: /[^A-Za-z0-9]/.test(password)
        };
        
        // Update requirement icons
        updateRequirement('reqLength', requirements.length);
        updateRequirement('reqUppercase', requirements.uppercase);
        updateRequirement('reqLowercase', requirements.lowercase);
        updateRequirement('reqNumber', requirements.number);
        updateRequirement('reqSpecial', requirements.special);
        
        // Calculate strength score
        let score = 0;
        if (requirements.length) score += 20;
        if (requirements.uppercase) score += 20;
        if (requirements.lowercase) score += 20;
        if (requirements.number) score += 20;
        if (requirements.special) score += 20;
        
        return {
            requirements: requirements,
            score: score,
            allMet: Object.values(requirements).every(req => req)
        };
    }
    
    // Update requirement display
    function updateRequirement(elementId, isValid) {
        const element = document.getElementById(elementId);
        if (element) {
            element.classList.remove('valid', 'invalid');
            element.classList.add(isValid ? 'valid' : 'invalid');
            
            const icon = element.querySelector('i');
            if (icon) {
                icon.className = isValid ? 'bi bi-check-circle' : 'bi bi-x-circle';
                icon.style.color = isValid ? '#28a745' : '#dc3545';
            }
        }
    }
    
    // Check if passwords match
    function checkPasswordMatch() {
        if (!newPassword || !confirmPassword) return false;
        
        const newPass = newPassword.value;
        const confirmPass = confirmPassword.value;
        
        if (newPass && confirmPass) {
            if (newPass === confirmPass) {
                passwordMatchText.innerHTML = '<span style="color:#28a745;"><i class="bi bi-check-circle"></i> Passwords match</span>';
                return true;
            } else {
                passwordMatchText.innerHTML = '<span style="color:#dc3545;"><i class="bi bi-x-circle"></i> Passwords do not match</span>';
                return false;
            }
        }
        
        passwordMatchText.textContent = 'Passwords must match.';
        return false;
    }
    
    // Update submit button state
    function updateSubmitButtonState() {
        if (!submitButton) return;
        
        //const currentPass = currentPassword ? currentPassword.value.trim() : '';
        const newPass = newPassword ? newPassword.value.trim() : '';
        const confirmPass = confirmPassword ? confirmPassword.value.trim() : '';
        
        // Check all conditions
        const strength = checkPasswordStrength(newPass);
        const passwordsMatch = checkPasswordMatch();
        
        // Enable button only if all conditions are met
        const allValid =newPass &&
                        confirmPass && 
                        strength.allMet && 
                        passwordsMatch;
        
        submitButton.disabled = !allValid;
        
        // Visual feedback
        if (submitButton.disabled) {
            submitButton.title = 'Please fill all fields and meet password requirements';
        } else {
            submitButton.title = 'Click to update your password';
        }
    }
    
    // Event listeners for real-time validation
    if (currentPassword) {
        currentPassword.addEventListener('input', updateSubmitButtonState);
    }
    
    if (newPassword) {
        newPassword.addEventListener('input', function() {
            checkPasswordStrength(this.value);
            updateSubmitButtonState();
        });
    }
    
    if (confirmPassword) {
        confirmPassword.addEventListener('input', updateSubmitButtonState);
    }
    
    // Form submission validation
    if (changePasswordForm) {
        changePasswordForm.addEventListener('submit', function(event) {
            // Final validation
            const newPass = newPassword ? newPassword.value : '';
            const confirmPass = confirmPassword ? confirmPassword.value : '';
            
            // Check if new password is the same as current
            if (currentPassword && currentPassword.value === newPass) {
                event.preventDefault();
                showAlert('New password cannot be the same as your current password.', 'warning');
                return;
            }
            
            // Check password match
            if (newPass !== confirmPass) {
                event.preventDefault();
                showAlert('Passwords do not match. Please confirm your new password.', 'warning');
                return;
            }
            
            // Check password requirements
            const strength = checkPasswordStrength(newPass);
            if (!strength.allMet) {
                event.preventDefault();
                showAlert('Please meet all password requirements before submitting.', 'warning');
                return;
            }
            
            // Show processing state
            if (submitButton) {
                submitButton.disabled = true;
                submitButton.innerHTML = '<i class="bi bi-arrow-repeat spin me-2"></i>Updating...';
            }
        });
    }
    
    // Show alert function
    function showAlert(message, type) {
        // Remove any existing alerts
        const existingAlert = document.querySelector('.custom-alert');
        if (existingAlert) {
            existingAlert.remove();
        }
        
        // Create alert element
        const alertEl = document.createElement('div');
        alertEl.className = `alert alert-${type} alert-dismissible fade show custom-alert`;
        alertEl.style.position = 'fixed';
        alertEl.style.top = '20px';
        alertEl.style.right = '20px';
        alertEl.style.zIndex = '9999';
        alertEl.style.maxWidth = '400px';
        alertEl.style.boxShadow = '0 5px 15px rgba(0,0,0,0.1)';
        alertEl.innerHTML = `
            <strong>${type === 'warning' ? 'Warning' : 'Error'}:</strong> ${message}
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        `;
        
        document.body.appendChild(alertEl);
        
        // Auto-dismiss after 5 seconds
        setTimeout(() => {
            if (alertEl.parentNode) {
                alertEl.classList.remove('show');
                setTimeout(() => alertEl.remove(), 150);
            }
        }, 5000);
    }
    
    // Add spin animation
    const style = document.createElement('style');
    style.textContent = `
        @keyframes spin {
            0% { transform: rotate(0deg); }
            100% { transform: rotate(360deg); }
        }
        .spin {
            animation: spin 1s linear infinite;
        }
    `;
    document.head.appendChild(style);
    
    // Initialize button state
    updateSubmitButtonState();
    
    // Check password match on load if there are values
    if (newPassword && newPassword.value && confirmPassword && confirmPassword.value) {
        checkPasswordMatch();
    }
});