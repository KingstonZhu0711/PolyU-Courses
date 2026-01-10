// Get form elements
const regUsername = document.getElementById('regUsername');
const regEmail = document.getElementById('regEmail');
const regPassword = document.getElementById('regPassword');
const regConfirmPwd = document.getElementById('regConfirmPwd');
const registerForm = document.getElementById('registerForm');
const agreePolicy = document.getElementById('agreePolicy');

// Get error message elements
const usernameError = document.getElementById('usernameError');
const emailError = document.getElementById('emailError');
const passwordError = document.getElementById('passwordError');
const pwdMatchHint = document.getElementById('pwdMatchHint');

// Helper function to show error messages
const showError = (element, message) => {
    element.textContent = message;
    element.classList.remove('d-none');
};

// Helper function to hide error messages
const hideError = (element) => {
    element.classList.add('d-none');
};

// Validate username (3-16 alphanumeric characters)
const validateUsername = () => {
    const username = regUsername.value.trim();
    if (username.length < 3 || username.length > 16) {
        showError(usernameError, "Username must be 3-16 characters");
        return false;
    }
    if (!/^[a-zA-Z0-9]+$/.test(username)) {
        showError(usernameError, "Username can only contain letters and numbers");
        return false;
    }
    hideError(usernameError);
    return true;
};

// Validate corporate email format
const validateEmail = () => {
    const email = regEmail.value.trim();
    const emailRegex = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
    if (!emailRegex.test(email)) {
        showError(emailError, "Please enter a valid corporate email (e.g., name@company.com)");
        return false;
    }
    hideError(emailError);
    return true;
};

// Validate password strength
const validatePassword = () => {
    const password = regPassword.value;
    if (password.length < 8) {
        showError(passwordError, "Password must be at least 8 characters");
        return false;
    }
    if (!/[A-Za-z]/.test(password) || !/\d/.test(password)) {
        showError(passwordError, "Password must include both letters and numbers");
        return false;
    }
    if (!/[!@#$%^&*(),.?":{}|<>]/.test(password)) {
        showError(passwordError, "Password should include a special character (!@# etc.)");
        return false;
    }
    hideError(passwordError);
    return true;
};

// Validate password confirmation match
const validateConfirmPassword = () => {
    const isMatch = regPassword.value === regConfirmPwd.value;
    pwdMatchHint.classList.toggle('d-none', isMatch);
    return isMatch;
};

// Validate policy agreement
const validatePolicyAgreement = () => {
    return agreePolicy.checked;
};

// Real-time validation triggers
regUsername.addEventListener('input', validateUsername);
regEmail.addEventListener('input', validateEmail);
regPassword.addEventListener('input', () => {
    validatePassword();
    validateConfirmPassword();
});
regConfirmPwd.addEventListener('input', validateConfirmPassword);
agreePolicy.addEventListener('change', validatePolicyAgreement);

// Form submission handling
registerForm.addEventListener('submit', (e) => {
    e.preventDefault();

    // Run all validations
    const isUsernameValid = validateUsername();
    const isEmailValid = validateEmail();
    const isPasswordValid = validatePassword();
    const isConfirmValid = validateConfirmPassword();
    const isPolicyAgreed = validatePolicyAgreement();

    // If all validations pass
    if (isUsernameValid && isEmailValid && isPasswordValid && isConfirmValid && isPolicyAgreed) {
        const userData = {
            username: regUsername.value.trim(),
            email: regEmail.value.trim(),
            password: regPassword.value 
        };
        console.log("Registration data submitted:", userData);
        window.location.href = 'login.html'; 
    } else {
        if (!isPolicyAgreed) {
            agreePolicy.classList.add('border-danger');
            setTimeout(() => agreePolicy.classList.remove('border-danger'), 2000);
        }
    }
});