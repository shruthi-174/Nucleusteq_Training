// Admin Dashboard
const addUserLink = document.getElementById('addUserLink');
const addUserModal = document.getElementById('addUserModal');
const closeBtn = document.querySelector('.close-btn');

if (addUserLink && addUserModal && closeBtn) {
    addUserLink.addEventListener('click', function() {
        addUserModal.style.display = 'block';
    });

    closeBtn.addEventListener('click', function() {
        addUserModal.style.display = 'none';
    });

    window.addEventListener('click', function(event) {
        if (event.target === addUserModal) {
            addUserModal.style.display = 'none';
        }
    });

    const addUserForm = document.getElementById('addUserForm');
    if (addUserForm) {
        addUserForm.addEventListener('submit', async function(event) {
            event.preventDefault();

            const firstname = document.getElementById('firstname').value;
            const lastname = document.getElementById('lastname').value;
            const email = document.getElementById('email').value;
            const password = document.getElementById('password').value;
            const role = document.getElementById('role').value;

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

            if (isValid) {
                const response = await fetch('/api/register', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify({ firstname, lastname, email, password, role })
                });

                if (response.ok) {
                   alert('User added successfully!');
                    // Refresh the user list or perform any other actions
                    addUserModal.style.display = 'none';
                } else {
                    const errorMessage = await response.text();
                    alert('Failed to add user: ' + errorMessage);
                }
            }
        });
    }
}

// JavaScript to toggle the sidebar visibility and handle sidebar link selection
//const sidebar = document.getElementById('sidebar');
//const sidebarLinks = document.querySelectorAll('.nav-links a');
//
//sidebar.style.display = 'block'; // Ensure the sidebar is initially visible
//
//sidebarLinks.forEach(link => {
//    link.addEventListener('click', function() {
//        sidebarLinks.forEach(otherLink => {
//            otherLink.classList.remove('selected');
//        });
//        this.classList.add('selected');
//    });
//});

// Employee and Manager Dashboard
const editProfileLink = document.getElementById('editProfileLink');
const editProfileModal = document.getElementById('editProfileModal');
const closeEditProfileBtn = document.querySelector('.close-btn');

if (editProfileLink && editProfileModal && closeEditProfileBtn) {
    editProfileLink.addEventListener('click', function() {
        editProfileModal.style.display = 'block';
    });

    closeEditProfileBtn.addEventListener('click', function() {
        editProfileModal.style.display = 'none';
    });

    window.addEventListener('click', function(event) {
        if (event.target === editProfileModal) {
            editProfileModal.style.display = 'none';
        }
    });

    // Edit Profile functionality
    const editProfileForm = document.getElementById('editProfileForm');

    if (editProfileForm) {
        editProfileForm.addEventListener('submit', async function(event) {
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

            if (isValid) {
                // Get the user ID from the session or localStorage
                const userId = sessionStorage.getItem('userId') || localStorage.getItem('userId');

                // Check if userId is valid before making the request
                if (!userId) {
                    console.error('User ID is not available.');
                    return;
                }

                console.log('User ID:', userId); // Debugging statement

                const url = `/api/users/${userId}`; // Construct the request URL

                const requestBody = {
                    firstname: firstname,
                    lastname: lastname,
                    email: email,
                    password: password
                    // No need to include the role property for updating the user profile
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
                    // Refresh the profile details or perform any other actions
                } else {
                    const errorMessage = await response.text();
                    alert('Failed to update profile: ' + errorMessage);
                }
            }
        });
    }
}

// Logout functionality
const logoutBtn = document.querySelector('.logout-btn');

if (logoutBtn) {
    logoutBtn.addEventListener('click', function(event) {
        event.preventDefault();
        window.location.href = '/html/index.html';
    });
}
