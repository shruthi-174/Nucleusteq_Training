document.addEventListener('DOMContentLoaded', () => {
    const token = localStorage.getItem('token');

    const userTableContainer = document.getElementById('userTableContainer');
    const addUserModal = document.getElementById('addUserModal');
    const addUserForm = document.getElementById('addUserForm');
    const projectTableContainer = document.getElementById('projectTableContainer');


    function hideAllSections() {
        userTableContainer.style.display = 'none';
        projectTableContainer.style.display = 'none';
        addUserModal.style.display = 'none';
    }
    
    // VIEW USERS
    const viewUsersLink = document.getElementById('viewUsersLink');
    if (viewUsersLink) {
        viewUsersLink.addEventListener('click', async () => {
            hideAllSections();
            try {
                const response = await fetch('/api/admin/users', {
                    headers: {
                        'Authorization': `Bearer ${token}`
                    }
                });

                if (!response.ok) {
                    throw new Error('Failed to fetch users');
                }

                const users = await response.json();
                populateUserTable(users);
                userTableContainer.style.display = 'block';
            } catch (error) {
                console.error('Error fetching users:', error);
                alert('Failed to fetch users');
            }
        });
    }

    function populateUserTable(users) {
        const userTable = document.getElementById('userTable');
        const tableBody = userTable.getElementsByTagName('tbody')[0];
        tableBody.innerHTML = '';

        users.forEach((user, index) => {
            const row = document.createElement('tr');
            const serialNumberCell = document.createElement('td');
            const firstNameCell = document.createElement('td');
            const lastNameCell = document.createElement('td');
            const emailCell = document.createElement('td');
            const roleCell = document.createElement('td');

            serialNumberCell.textContent = index + 1;
            firstNameCell.textContent = user.firstname;
            lastNameCell.textContent = user.lastname;
            emailCell.textContent = user.email;
            roleCell.textContent = user.role;

            row.appendChild(serialNumberCell);
            row.appendChild(firstNameCell);
            row.appendChild(lastNameCell);
            row.appendChild(emailCell);
            row.appendChild(roleCell);

            tableBody.appendChild(row);
        });
    }

    // ADD USER
    const createUserButton = document.getElementById('createUserButton');
    if (createUserButton && addUserModal && addUserForm) {
        createUserButton.addEventListener('click', () => {
            addUserModal.style.display = 'block';
        });

        addUserModal.querySelector('.close-btn').addEventListener('click', () => {
            addUserModal.style.display = 'none';
        });

        addUserForm.addEventListener('submit', async (e) => {
            e.preventDefault();
            const formData = new FormData(addUserForm);
            const firstname = formData.get('firstname');
            const lastname = formData.get('lastname');
            const email = formData.get('email');
            const password = formData.get('password');
            const role = formData.get('role');

            // Validations
            if (!email.endsWith('@nucleusteq.com')) {
                alert('Email must end with @nucleusteq.com');
                return;
            }

         
            if (password.length < 3) {
                alert('Password must be at least 3 characters long.');
                return;
            }

            const containsLetter = /[a-zA-Z]/.test(password);
            const containsNumber = /\d/.test(password);

            if (!containsLetter || !containsNumber) {
                alert('Password must contain at least one letter and one number.');
                return;
            }

            const userPayload = { firstname, lastname, email, password, role };
            console.log('User Payload:', JSON.stringify(userPayload));

            try {
                const response = await fetch('/api/admin/register', {
                    method: 'POST',
                    headers: {
                        'Authorization': `Bearer ${token}`,
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify(userPayload)
                });

                if (!response.ok) {
                    throw new Error('Failed to add user');
                }

                alert('User added successfully');
                addUserForm.reset();
                addUserModal.style.display = 'none';
            } catch (error) {
                console.error('Error adding user:', error);
                alert(`Error: ${error.message}`);
            }
        });
    }



 // ADD PROJECT
    const addProjectButton = document.getElementById('addProjectButton');
    const addProjectModal = document.getElementById('addProjectModal');
    const addProjectForm = document.getElementById('addProjectForm');
    const managerSelect = document.getElementById('managerId'); // Assuming this is the ID of the manager dropdown

    if (addProjectButton && addProjectModal && addProjectForm) {
        addProjectButton.addEventListener('click', async () => {
            addProjectModal.style.display = 'block';

            try {
                
                const managersResponse = await fetch('/api/admin/managers', {
                    headers: {
                        'Authorization': `Bearer ${token}`
                    }
                });

                const managers = await managersResponse.json();
                populateManagerDropdown(managers);
            } catch (error) {
                console.error('Error fetching managers:', error);
                alert('Failed to fetch managers');
            }
        });

        addProjectModal.querySelector('.close-btn').addEventListener('click', () => {
            addProjectModal.style.display = 'none';
        });

        addProjectForm.addEventListener('submit', async (e) => {
            e.preventDefault();
            const formData = new FormData(addProjectForm);
            const projectName = formData.get('projectName');
            const projectDescription = formData.get('projectDescription');
            const selectedManagerName = managerSelect.value;

            try {
               
                const managerId = await getManagerIdFromName(selectedManagerName);

                const projectPayload = {
                    name: projectName,
                    description: projectDescription,
                    manager: { userId: managerId }
                };

                const response = await fetch('/api/admin/createProject', {
                    method: 'POST',
                    headers: {
                        'Authorization': `Bearer ${token}`,
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify(projectPayload)
                });

                if (!response.ok) {
                    throw new Error('Failed to add project');
                }

                const responseData = await response.json();
                if (responseData && responseData.error) {
                    throw new Error(responseData.error); 
                }

                alert('Project added successfully');
                addProjectForm.reset();
                addProjectModal.style.display = 'none';
            } catch (error) {
                alert(`Error: ${error.message}`);
            }
        });
    }

    function populateManagerDropdown(managers) {
        managerSelect.innerHTML = managers.map(manager => `<option value="${manager.firstname} ${manager.lastname}">${manager.firstname} ${manager.lastname}</option>`).join('');
    }

    async function getManagerIdFromName(managerName) {
        try {
            const response = await fetch('/api/admin/managers', {
                headers: {
                    'Authorization': `Bearer ${token}`
                }
            });

            if (!response.ok) {
                throw new Error('Failed to fetch managers');
            }

            const managers = await response.json();
            const manager = managers.find(mgr => `${mgr.firstname} ${mgr.lastname}` === managerName);

            if (manager) {
                return manager.userId;
            } else {
                throw new Error(`Manager with name "${managerName}" not found`);
            }
        } catch (error) {
            console.error('Error fetching managers:', error);
            throw error;
        }
    }

    
    // LOGOUT
    const logoutBtn = document.querySelector('.logout-btn');
    if (logoutBtn) {
        logoutBtn.addEventListener('click', function(event) {
            event.preventDefault();
            window.location.href = '/html/index.html';
        });
    }
});