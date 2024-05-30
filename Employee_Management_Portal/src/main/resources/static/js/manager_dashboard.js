document.addEventListener('DOMContentLoaded', () => {
    function hideAll() {
        editProfileModalManager.style.display = 'none';
        userListSection.style.display = 'none';
        filterOptions.style.display = 'none';
        projectListSection.style.display = 'none';
    }

    // VIEW USERS
    const viewUsersLink = document.getElementById('viewUsersLink');
    const userListSection = document.getElementById('userList');
    const filterOptions = document.getElementById('filterOptions');
    const filterBySkillsLink = document.getElementById('filterBySkills');
    const unassignedEmployeesLink = document.getElementById('unassignedEmployees');

    function showUserList() {
        hideAll();
        userListSection.style.display = 'block';
        filterOptions.style.display = 'block';
        loadUserList();
    }

    function hideUserList() {
        userListSection.style.display = 'none';
        filterOptions.style.display = 'none';
    }

    if (viewUsersLink) {
        viewUsersLink.addEventListener('click', showUserList);
    }

    const filterBySkillsCheckbox = document.getElementById('filterBySkills');
    const filterByUnassignedCheckbox = document.getElementById('filterByUnassigned');
    const showAllUsersCheckbox = document.getElementById('showAllUsers');

    filterBySkillsCheckbox.addEventListener('change', handleFilterChange);
    filterByUnassignedCheckbox.addEventListener('change', handleFilterChange);
    showAllUsersCheckbox.addEventListener('change', handleFilterChange);

    async function handleFilterChange() {
        const isFilterBySkillsChecked = filterBySkillsCheckbox.checked;
        const isFilterByUnassignedChecked = filterByUnassignedCheckbox.checked;
        const isShowAllUsersChecked = showAllUsersCheckbox.checked;

        try {
            let filteredUsers = [];

            if (isShowAllUsersChecked) {
                filteredUsers = await fetchAllUsers();
            } else if (isFilterBySkillsChecked && isFilterByUnassignedChecked) {
                filteredUsers = await fetchFilteredUsers('skills', 'unassigned');
            } else if (isFilterBySkillsChecked) {
                filteredUsers = await fetchFilteredUsers('skills');
            } else if (isFilterByUnassignedChecked) {
                filteredUsers = await fetchFilteredUsers('unassigned');
            }

            displayUsers(filteredUsers);
        } catch (error) {
            console.error('Error:', error);
            alert('An error occurred while filtering users');
        }
    }

    async function fetchFilteredUsers(...filters) {
        const skillsPromise = filters.includes('skills')
            ? new Promise((resolve, reject) => {
                const skills = prompt('Enter skills:');
                if (skills) {
                    resolve(
                        fetch(`/api/manager/employees/filter?skills=${skills.replace(/\s/g, '')}`, {
                            headers: {
                                'Authorization': `Bearer ${localStorage.getItem('token')}`,
                            },
                        })
                    );
                } else {
                    reject(new Error('No skills provided'));
                }
            })
            : null;

        const unassignedPromise = filters.includes('unassigned')
            ? fetch('/api/manager/filter/unassigned-employees', {
                headers: {
                    'Authorization': `Bearer ${localStorage.getItem('token')}`,
                },
            })
            : null;

        const promises = [skillsPromise, unassignedPromise].filter(Boolean);

        const responses = await Promise.all(promises);
        const users = await Promise.all(responses.map((response) => response.json()));

        if (filters.length === 2) {
            const skillsUsers = users[0];
            const unassignedUsers = users[1];
            return unassignedUsers.filter((user) =>
                skillsUsers.some((skillsUser) => skillsUser.userId === user.userId)
            ).map((user) => ({ ...user, role: 'EMPLOYEE' }));
        } else {
            return users.flat().map((user) => ({ ...user, role: 'EMPLOYEE' }));
        }
    }

    async function fetchAllUsers() {
        const [employeeResponse, managerResponse] = await Promise.all([
            fetch('/api/manager/employees', {
                headers: {
                    'Authorization': `Bearer ${localStorage.getItem('token')}`,
                },
            }),
            fetch('/api/manager/managers', {
                headers: {
                    'Authorization': `Bearer ${localStorage.getItem('token')}`,
                },
            }),
        ]);

        if (employeeResponse.ok && managerResponse.ok) {
            const employees = await employeeResponse.json();
            const managers = await managerResponse.json();

            return [
                ...employees.map((employee) => ({ ...employee, role: 'EMPLOYEE' })),
                ...managers.map((manager) => ({ ...manager, role: 'MANAGER' })),
            ];
        } else {
            const employeeError = await employeeResponse.text();
            const managerError = await managerResponse.text();
            throw new Error(`Error fetching users: ${employeeError}, ${managerError}`);
        }
    }

    async function loadUserList() {
        if (userListSection.style.display === 'none') {
            return;
        }

        try {
            const users = await fetchAllUsers();
            displayUsers(users);
        } catch (error) {
            console.error('Error:', error);
            alert('An error occurred while loading the user list');
        }
    }

    if (unassignedEmployeesLink) {
        unassignedEmployeesLink.addEventListener('click', async () => {
            try {
                const response = await fetch('/api/manager/filter/unassigned-employees', {
                    headers: {
                        'Authorization': `Bearer ${localStorage.getItem('token')}`,
                    },
                });

                if (response.ok) {
                    const employees = await response.json();
                    displayUsers(employees, 'EMPLOYEE');
                } else {
                    const error = await response.text();
                    alert(error);
                }
            } catch (error) {
                console.error('Error:', error);
                alert('An error occurred while fetching unassigned employees');
            }
        });
    }

    function displayUsers(users) {
        const userListTable = document.querySelector('#userList table tbody');
        userListTable.innerHTML = '';

        users.forEach((user, index) => {
            const row = document.createElement('tr');
            row.innerHTML = `
                <td>${index + 1}</td>
                <td>${user.firstname}</td>
                <td>${user.lastname}</td>
                <td>${user.email}</td>
                <td>${user.role}</td>
            `;
            userListTable.appendChild(row);
        });
    }

 // CREATE REQUEST
    const createRequestButton = document.getElementById('createRequestButton');
    const createRequestModal = document.getElementById('createRequestModal');
    const closeRequestModalBtn = document.querySelector('.close-btn-request');
    const createRequestForm = document.getElementById('createRequestForm');

    function showCreateRequestModal() {
        hideAll();
        createRequestModal.style.display = 'block';
        loadProjectsAndEmployees();
    }

    function hideCreateRequestModal() {
        createRequestModal.style.display = 'none';
    }

    if (createRequestButton) {
        createRequestButton.addEventListener('click', showCreateRequestModal);
    }

    if (closeRequestModalBtn) {
        closeRequestModalBtn.addEventListener('click', hideCreateRequestModal);
    }
   
 // Add Email
    const addEmailBtn = document.getElementById('addEmailBtn');
    const employeeEmailsContainer = document.getElementById('employeeEmailsContainer');

    addEmailBtn.addEventListener('click', () => {
        const newEmailInput = document.createElement('input');
        newEmailInput.type = 'email';
        newEmailInput.className = 'form-input employee-email';
        newEmailInput.placeholder = 'Enter an email';
        newEmailInput.required = true;
        employeeEmailsContainer.appendChild(newEmailInput);
    });
    
    async function loadProjectsAndEmployees() {
        try {
            const [projects, employees] = await Promise.all([
                fetch('/api/manager/projects', {
                    headers: {
                        'Authorization': `Bearer ${localStorage.getItem('token')}`,
                    },
                })
                .then(response => response.json()),
                fetchAllUsers(),
            ]);

            const projectSelect = document.getElementById('projectName');
            const managerId = document.getElementById('managerId');
            projectSelect.innerHTML = '';

            const defaultOption = document.createElement('option');
            defaultOption.value = '';
            defaultOption.text = 'Select Project';
            projectSelect.add(defaultOption);

            projects.forEach(project => {
                const option = document.createElement('option');
                option.value = project.id || project.projectId; 
                option.text = project.name;
                projectSelect.add(option);
            });

            const loggedInManagerId = await getUserId();
            console.log('Logged In Manager ID:', loggedInManagerId);
            managerId.value = loggedInManagerId;
        } catch (error) {
            console.error('Error:', error);
            alert('An error occurred while loading projects and employees');
        }
    }

    
    if (createRequestForm) {
        createRequestForm.addEventListener('submit', async (e) => {
            e.preventDefault();

            const projectNameSelect = document.getElementById('projectName');
            const selectedProjectOption = projectNameSelect.options[projectNameSelect.selectedIndex];
            const selectedProjectId = selectedProjectOption ? selectedProjectOption.value : undefined;
            console.log('Selected Project ID:', selectedProjectId);

            if (!selectedProjectId || selectedProjectId === '') {
                alert('Please select a project');
                return;
            }

            const employeeEmailInputs = document.querySelectorAll('.employee-email');
            const employeeEmails = Array.from(employeeEmailInputs).map(input => input.value.trim()).filter(email => email !== '');

            if (employeeEmails.length === 0) {
                alert('Please enter at least one employee email');
                return;
            }

            const managerId = await getUserId();
            console.log('Manager ID:', managerId);

            const requestBody = {
                projectId: selectedProjectId,
                employeeEmails,
                managerId,
            };

            console.log('Request Body:', JSON.stringify(requestBody));

            try {
                const response = await fetch('/api/manager/request-resources', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json',
                        'Authorization': `Bearer ${localStorage.getItem('token')}`,
                    },
                    body: JSON.stringify(requestBody),
                });

                if (response.ok) {
                    const message = await response.text();
                    alert('Request has been sent!');
                    hideCreateRequestModal();
                } else {
                    alert('This email does not belongs to employee');
                    const errorText = await response.text();                }
            } catch (error) {
                console.error('Error:', error);
                alert('An error occurred while creating the request');
            }
        });
    }
    
    // EDIT PROFILE
    const editProfileLinkManager = document.getElementById('editProfileLinkManager');
    const editProfileModalManager = document.getElementById('editProfileModalManager');
    const closeModalBtnManager = document.querySelector('.close-btn-manager');
    const editProfileFormManager = document.getElementById('editProfileFormManager');

    function showEditProfileModalManager() {
        hideAll();
        editProfileModalManager.style.display = 'block';
    }

    function hideEditProfileModalManager() {
        editProfileModalManager.style.display = 'none';
    }

    if (editProfileLinkManager) {
        editProfileLinkManager.addEventListener('click', showEditProfileModalManager);
    }

    if (closeModalBtnManager) {
        closeModalBtnManager.addEventListener('click', hideEditProfileModalManager);
    }

    if (editProfileFormManager) {
        editProfileFormManager.addEventListener('submit', async (e) => {
            e.preventDefault();

            const firstname = document.getElementById('firstname').value;
            const lastname = document.getElementById('lastname').value;
            const email = document.getElementById('email').value;
            const password = document.getElementById('password').value;

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

            try {
                const userId = await getUserId();

                const response = await fetch(`/api/manager/update-profile/${userId}`, {
                    method: 'PUT',
                    headers: {
                        'Content-Type': 'application/json',
                        'Authorization': `Bearer ${localStorage.getItem('token')}`,
                    },
                    body: JSON.stringify({ firstname, lastname, email, password }),
                });

                if (response.ok) {
                    const message = await response.text();
                    alert(message);
                    hideEditProfileModalManager();
                } else {
                    const error = await response.text();
                    alert(error);
                }
            } catch (error) {
                console.error('Error:', error);
                alert('An error occurred while updating the profile');
            }
        });
    }

    async function getUserId() {
        try {
            const token = localStorage.getItem('token');
            if (!token) {
                throw new Error('No token found');
            }

            const response = await fetch('/api/users/email', {
                headers: {
                    'Authorization': `Bearer ${token}`,
                },
            });
            if (response.ok) {
                const user = await response.json();
                const userId = user.userId;
                if (userId) {
                    localStorage.setItem('userId', userId);
                    return userId;
                } else {
                    throw new Error('User ID not found');
                }
            } else {
                const errorText = await response.text();
                throw new Error(`Failed to fetch user data: ${errorText}`);
            }
        } catch (error) {
            console.error('Error:', error);
            alert('An error occurred while fetching user data');
        }
    }

 // VIEW PROJECTS
    const viewProjectsLink = document.getElementById('viewProjectsLink');
    const projectListSection = document.getElementById('projectList');

    function showProjectList() {
        hideAll();
        projectListSection.style.display = 'block';
        loadProjectList();
    }

    function hideProjectList() {
        projectListSection.style.display = 'none';
    }

    if (viewProjectsLink) {
        viewProjectsLink.addEventListener('click', showProjectList);
    }

    async function loadProjectList() {
        if (projectListSection.style.display === 'none') {
            return;
        }

        try {
            const response = await fetch('/api/manager/projects', {
                headers: {
                    'Authorization': `Bearer ${localStorage.getItem('token')}`,
                },
            });

            if (response.ok) {
                const projects = await response.json();
                const projectListTable = document.querySelector('#projectList table tbody');
                projectListTable.innerHTML = '';

                projects.forEach((project, index) => {
                    const row = document.createElement('tr');
                    row.innerHTML = `
                        <td>${index + 1}</td>
                        <td>${project.name}</td>
                        <td>${project.description}</td>
                    `;
                    projectListTable.appendChild(row);
                });
            } else {
                const error = await response.text();
                alert(error);
            }
        } catch (error) {
            console.error('Error:', error);
            alert('An error occurred while loading the project list');
        }
    } 
    
    // Logout functionality
    const logoutButton = document.getElementById('logoutButton');
    logoutButton.addEventListener('click', logout);

    function logout() {
      fetch('/api/logout', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        }
      })
      .then(response => {
        if (response.ok) {
          console.log('Logged out successfully');
          window.location.href = '/html/index.html';
        } else {
          console.error('Failed to log out');
        }
      })
      .catch(error => {
        console.error('Error occurred while logging out:', error);
      });
    }
});
