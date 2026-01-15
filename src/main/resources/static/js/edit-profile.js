// Wait for DOM to be fully loaded
document.addEventListener('DOMContentLoaded', function() {
    // ========== SIDEBAR FUNCTIONALITY ==========
    const menuToggle = document.getElementById('menuToggle');
    const closeSidebar = document.getElementById('closeSidebar');
    const sidebar = document.getElementById('sidebar');
    
    if (menuToggle) {
        menuToggle.addEventListener('click', function() {
            sidebar.classList.add('active');
            document.body.style.overflow = 'hidden';
        });
    }
    
    if (closeSidebar) {
        closeSidebar.addEventListener('click', function() {
            sidebar.classList.remove('active');
            document.body.style.overflow = 'auto';
        });
    }
    
    // Close sidebar when clicking outside on mobile
    document.addEventListener('click', function(event) {
        if (window.innerWidth <= 992 &&
            sidebar &&
            !sidebar.contains(event.target) &&
            menuToggle &&
            !menuToggle.contains(event.target) &&
            sidebar.classList.contains('active')) {
            sidebar.classList.remove('active');
            document.body.style.overflow = 'auto';
        }
    });
    
    // Close sidebar on escape key
    document.addEventListener('keydown', function(event) {
        if (event.key === 'Escape' && sidebar && sidebar.classList.contains('active')) {
            sidebar.classList.remove('active');
            document.body.style.overflow = 'auto';
        }
    });
    
    // ========== FORM VALIDATION ==========
    const editProfileForm = document.getElementById('editProfileForm');
    
    if (editProfileForm) {
        // Real-time validation for required fields
        const requiredFields = editProfileForm.querySelectorAll('[required]');
        
        requiredFields.forEach(field => {
            field.addEventListener('blur', function() {
                validateField(this);
            });
            
            field.addEventListener('input', function() {
                // Clear validation on input
                if (this.value.trim() !== '') {
                    clearFieldError(this);
                }
            });
        });
        
        // Email validation
        const emailField = document.getElementById('email');
        if (emailField) {
            emailField.addEventListener('blur', function() {
                validateEmail(this);
            });
        }
        
        // Bio character counter
        const bioField = document.getElementById('bio');
        if (bioField) {
            // Create character counter element
            const charCounter = document.createElement('div');
            charCounter.className = 'form-text text-end';
            charCounter.id = 'bioCharCounter';
            bioField.parentNode.appendChild(charCounter);
            
            bioField.addEventListener('input', function() {
                const currentLength = this.value.length;
                const maxLength = 500;
                charCounter.textContent = `${currentLength}/${maxLength} characters`;
                
                if (currentLength > maxLength) {
                    charCounter.classList.add('text-danger');
                    this.classList.add('is-invalid');
                } else {
                    charCounter.classList.remove('text-danger');
                    this.classList.remove('is-invalid');
                }
            });
            
            // Initialize counter
            bioField.dispatchEvent(new Event('input'));
        }
        
        // Form submission validation
        editProfileForm.addEventListener('submit', function(event) {
            if (!validateForm()) {
                event.preventDefault();
                highlightInvalidFields();
                scrollToFirstInvalidField();
            }
        });
    }
    
    // ========== DATE VALIDATION ==========
    const dateOfBirthField = document.getElementById('dateOfBirth');
    if (dateOfBirthField) {
        dateOfBirthField.addEventListener('change', function() {
            validateDateOfBirth(this);
        });
    }
    
    const enrollmentDateField = document.getElementById('enrollmentDate');
    if (enrollmentDateField) {
        enrollmentDateField.addEventListener('change', function() {
            validateEnrollmentDate(this);
        });
    }
    
    // ========== HELPER FUNCTIONS ==========
    function validateField(field) {
        if (field.hasAttribute('required') && field.value.trim() === '') {
            setFieldError(field, 'This field is required');
            return false;
        }
        
        clearFieldError(field);
        return true;
    }
    
    function validateEmail(emailField) {
        const email = emailField.value.trim();
        const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        
        if (email && !emailRegex.test(email)) {
            setFieldError(emailField, 'Please enter a valid email address');
            return false;
        }
        
        clearFieldError(emailField);
        return true;
    }
    
    function validateDateOfBirth(dateField) {
        const dateValue = dateField.value;
        if (!dateValue) return true;
        
        const selectedDate = new Date(dateValue);
        const today = new Date();
        const minDate = new Date();
        minDate.setFullYear(today.getFullYear() - 100); // Max 100 years old
        const maxDate = new Date();
        maxDate.setFullYear(today.getFullYear() - 16); // Min 16 years old
        
        if (selectedDate < minDate) {
            setFieldError(dateField, 'Date of birth cannot be more than 100 years ago');
            return false;
        }
        
        if (selectedDate > maxDate) {
            setFieldError(dateField, 'You must be at least 16 years old');
            return false;
        }
        
        clearFieldError(dateField);
        return true;
    }
    
    function validateEnrollmentDate(dateField) {
        const dateValue = dateField.value;
        if (!dateValue) return true;
        
        const selectedDate = new Date(dateValue);
        const today = new Date();
        const maxDate = new Date();
        maxDate.setDate(today.getDate() + 30); // Max 30 days in future
        
        if (selectedDate > maxDate) {
            setFieldError(dateField, 'Enrollment date cannot be more than 30 days in the future');
            return false;
        }
        
        clearFieldError(dateField);
        return true;
    }
    
    function setFieldError(field, message) {
        field.classList.add('is-invalid');
        field.classList.remove('is-valid');
        
        // Create or update error message
        let errorElement = field.parentNode.querySelector('.invalid-feedback:not(.d-block)');
        if (!errorElement) {
            errorElement = document.createElement('div');
            errorElement.className = 'invalid-feedback';
            field.parentNode.appendChild(errorElement);
        }
        errorElement.textContent = message;
        errorElement.style.display = 'block';
    }
    
    function clearFieldError(field) {
        field.classList.remove('is-invalid');
        field.classList.add('is-valid');
        
        // Hide error message
        const errorElement = field.parentNode.querySelector('.invalid-feedback');
        if (errorElement) {
            errorElement.style.display = 'none';
        }
    }
    
    function validateForm() {
        let isValid = true;
        const requiredFields = editProfileForm.querySelectorAll('[required]');
        
        requiredFields.forEach(field => {
            if (!validateField(field)) {
                isValid = false;
            }
        });
        
        // Validate email
        const emailField = document.getElementById('email');
        if (emailField && !validateEmail(emailField)) {
            isValid = false;
        }
        
        // Validate dates
        const dateOfBirthField = document.getElementById('dateOfBirth');
        if (dateOfBirthField && !validateDateOfBirth(dateOfBirthField)) {
            isValid = false;
        }
        
        const enrollmentDateField = document.getElementById('enrollmentDate');
        if (enrollmentDateField && !validateEnrollmentDate(enrollmentDateField)) {
            isValid = false;
        }
        
        // Validate bio length
        const bioField = document.getElementById('bio');
        if (bioField && bioField.value.length > 500) {
            setFieldError(bioField, 'Bio cannot exceed 500 characters');
            isValid = false;
        }
        
        return isValid;
    }
    
    function highlightInvalidFields() {
        const invalidFields = editProfileForm.querySelectorAll('.is-invalid');
        invalidFields.forEach(field => {
            field.scrollIntoView({ behavior: 'smooth', block: 'center' });
        });
    }
    
    function scrollToFirstInvalidField() {
        const firstInvalidField = editProfileForm.querySelector('.is-invalid');
        if (firstInvalidField) {
            firstInvalidField.scrollIntoView({ behavior: 'smooth', block: 'center' });
            firstInvalidField.focus();
        }
    }
    
    // ========== FORM RESET FUNCTIONALITY ==========
    const resetButton = editProfileForm.querySelector('button[type="reset"]');
    if (resetButton) {
        resetButton.addEventListener('click', function() {
            // Clear all validation states
            const formFields = editProfileForm.querySelectorAll('.form-control, .form-select');
            formFields.forEach(field => {
                field.classList.remove('is-invalid', 'is-valid');
                
                const errorElement = field.parentNode.querySelector('.invalid-feedback:not(.d-block)');
                if (errorElement) {
                    errorElement.style.display = 'none';
                }
            });
            
            // Reset bio character counter
            const bioField = document.getElementById('bio');
            const charCounter = document.getElementById('bioCharCounter');
            if (bioField && charCounter) {
                charCounter.textContent = `0/500 characters`;
                charCounter.classList.remove('text-danger');
            }
            
            // Show confirmation
            setTimeout(() => {
                alert('Form has been reset to its original values');
            }, 100);
        });
    }
    
    // ========== DEGREE SELECTION ENHANCEMENT ==========
    const degreeSelect = document.getElementById('degree');
    if (degreeSelect) {
        degreeSelect.addEventListener('change', function() {
            const selectedOption = this.options[this.selectedIndex];
            if (selectedOption.value) {
                // You could add additional logic here, like:
                // - Fetch additional degree details via AJAX
                // - Show degree description
                // - Update related fields
                console.log('Selected degree ID:', selectedOption.value);
            }
        });
    }
    
    // ========== AUTO-SAVE DRAFT (OPTIONAL) ==========
    let autoSaveTimeout;
    const autoSaveFields = editProfileForm.querySelectorAll('input, textarea, select');
    
    autoSaveFields.forEach(field => {
        field.addEventListener('input', function() {
            clearTimeout(autoSaveTimeout);
            autoSaveTimeout = setTimeout(saveDraft, 2000); // Save after 2 seconds of inactivity
        });
    });
    
    function saveDraft() {
        // This would typically save to localStorage or send via AJAX
        console.log('Auto-saving form data...');
        
        // Show saving indicator (optional)
        const submitButton = editProfileForm.querySelector('button[type="submit"]');
        const originalText = submitButton.innerHTML;
        submitButton.innerHTML = '<i class="bi bi-save me-2"></i>Saving...';
        submitButton.disabled = true;
        
        setTimeout(() => {
            submitButton.innerHTML = originalText;
            submitButton.disabled = false;
            console.log('Draft saved successfully');
        }, 500);
    }
    
    // Load draft on page load (optional)
    function loadDraft() {
        // Load from localStorage if available
        // This is just a placeholder for actual implementation
        console.log('Checking for saved draft...');
    }
    
    // Initialize draft loading
    loadDraft();
    
    // ========== HANDLE WINDOW RESIZE ==========
    let resizeTimer;
    window.addEventListener('resize', function() {
        clearTimeout(resizeTimer);
        resizeTimer = setTimeout(function() {
            // Close sidebar if open on mobile when resizing to desktop
            if (window.innerWidth > 992 && sidebar && sidebar.classList.contains('active')) {
                sidebar.classList.remove('active');
                document.body.style.overflow = 'auto';
            }
        }, 250);
    });
    
    // ========== INITIAL FORM VALIDATION ==========
    // Validate form on page load to show any server-side errors
    setTimeout(() => {
        validateForm();
    }, 100);
});