// Register Form
const registerForm = document.getElementById('registerForm');

if (registerForm) {
    registerForm.addEventListener('submit', async function(event) {
        event.preventDefault();

        const firstname = document.getElementById('firstname').value;
        const lastname = document.getElementById('lastname').value;
        const email = document.getElementById('email').value;
        const password = document.getElementById('password').value;

        // Validate inputs
        let isValid = true;
        document.querySelectorAll('.error').forEach(function(span) {
            span.textContent = '';
        });

        if (!firstname.trim()) {
            document.getElementById('firstnameError').textContent = 'First name is required';
            isValid = false;
        }

        if (!lastname.trim()) {
            document.getElementById('lastnameError').textContent = 'Last name is required';
            isValid = false;
        }

        if (!email.trim()) {
            document.getElementById('emailError').textContent = 'Email is required';
            isValid = false;
        } else if (!email.trim().endsWith('@nucleusteq.com')) {
            document.getElementById('emailError').textContent = 'Email must end with @nucleusteq.com';
            isValid = false;
        }

        if (!password.trim()) {
            document.getElementById('passwordError').textContent = 'Password is required';
            isValid = false;
        } else if (password.trim().length < 3) {
            document.getElementById('passwordError').textContent = 'Password must have at least 3 characters';
            isValid = false;
        } else if (!password.trim().match(/^(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d]{3,}$/)) {
            document.getElementById('passwordError').textContent = 'Password must contain letters and numbers';
            isValid = false;
        }

        const roleSelect = document.getElementById('role');
        const selectedRole = roleSelect.value;

        // Only allow registration if the selected role is 'ADMIN'

        if (isValid && selectedRole === 'ADMIN') {
            const response = await fetch('/api/register', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({ firstname, lastname, email, password, role: selectedRole })
            });

            if (response.ok) {
                console.log("Registration successful!");
                alert("Registration successful! You will now be redirected to the login page.");
                // Redirect to login page after successful registration
                window.location.href = "/html/login.html";
            } else {
                console.log("Registration failed. Status code:", response.status);
                const errorMessage = await response.text();
                console.log("Error message:", errorMessage);
                alert("Registration failed: " + (errorMessage ? errorMessage : "Unknown error"));
            }
        } else if (isValid && selectedRole !== 'ADMIN') {
            alert("Only admins can register.");
        }
    });
}


// Login Form
const loginForm = document.getElementById('loginForm');

if (loginForm) {
    loginForm.addEventListener('submit', async function(event) {
        event.preventDefault();
        const email = document.getElementById('email').value;
        const password = document.getElementById('password').value;

        // Validate inputs
        let isValid = true;
        document.querySelectorAll('.error').forEach(function(span) {
            span.textContent = ''; // Clear previous error messages
        });
        if (!email.trim()) {
            document.getElementById('emailError').textContent = 'Email is required';
            isValid = false;
        }

        if (!password.trim()) {
            document.getElementById('passwordError').textContent = 'Password is required';
            isValid = false;
        }

        if (isValid) {
            // Proceed with login
            const response = await fetch('/api/login', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({ email, password })
            });

            if (response.ok) {
                const data = await response.json();
                const userId = data.user_id; // Replace 'userIdPlaceholder' with 'user_id'
                const role = data.role; // Replace 'rolePlaceholder' with 'role'

                // Store the user ID in sessionStorage or localStorage
                sessionStorage.setItem('userId', userId);
                // OR
                // localStorage.setItem('userId', userId);

                if (role === 'ADMIN') {
                    window.location.href = '/html/dashboard/admin-dashboard.html'; 
                } else if (role === 'EMPLOYEE') {
                    window.location.href = '/html/dashboard/employee-dashboard.html';
                } else if (role === 'MANAGER') {
                    window.location.href = '/html/dashboard/manager-dashboard.html';
                } else {
                    alert("Invalid role");
                }
            } else {
                const errorMessage = await response.text();
                alert("Login failed: " + (errorMessage ? errorMessage : "Unknown error"));
            }
        }
    });
}
