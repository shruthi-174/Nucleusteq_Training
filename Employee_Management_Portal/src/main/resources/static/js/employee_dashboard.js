// Employee Dashboard
const editProfileLinkEmployee = document.getElementById('editProfileLinkEmployee'); // Change the variable name
const editProfileModalEmployee = document.getElementById('editProfileModalEmployee'); // Change the variable name
const closeEditProfileBtnEmployee = document.querySelector('.close-btn-employee');

if (editProfileLinkEmployee && editProfileModalEmployee && closeEditProfileBtnEmployee) {
    editProfileLinkEmployee.addEventListener('click', function() {
        editProfileModalEmployee.style.display = 'block';
    });

    closeEditProfileBtnEmployee.addEventListener('click', function() {
        editProfileModalEmployee.style.display = 'none';
    });

    window.addEventListener('click', function(event) {
        if (event.target === editProfileModalEmployee) {
            editProfileModalEmployee.style.display = 'none';
        }
    });

    const editProfileFormEmployee = document.getElementById('editProfileFormEmployee');

    if (editProfileFormEmployee) {
        editProfileFormEmployee.addEventListener('submit', async function(event) {
            event.preventDefault();

            const firstname = document.getElementById('firstname').value;
            const lastname = document.getElementById('lastname').value;
            const email = document.getElementById('email').value;
            const password = document.getElementById('password').value;

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

            if (isValid) {
                const userId = sessionStorage.getItem('userId') || localStorage.getItem('userId');

                if (!userId) {
                    console.error('User ID is not available.');
                    return;
                }

                const url = `/api/users/employee/${userId}`;

                const requestBody = {
                    firstname: firstname,
                    lastname: lastname,
                    email: email,
                    password: password
                };

                const response = await fetch(url, {
                    method: 'PUT',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify(requestBody)
                });

                if (response.ok) {
                    alert('Profile updated successfully!');
                } else {
                    const errorMessage = await response.text();
                    alert('Failed to update profile: ' + errorMessage);
                }
            }
        });
    }
}

//Logout functionality
const logoutBtnEmployee = document.querySelector('.logout-btn');

if (logoutBtnEmployee) {
    logoutBtnEmployee.addEventListener('click', function(event) {
        event.preventDefault();
        window.location.href = '/html/index.html';
    });
}