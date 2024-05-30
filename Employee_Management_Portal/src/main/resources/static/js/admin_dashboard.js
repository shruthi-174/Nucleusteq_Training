document.addEventListener('DOMContentLoaded', () => {
    const token = localStorage.getItem('token');

    const userTableContainer = document.getElementById('userTableContainer');
    const addUserModal = document.getElementById('addUserModal');
    const addUserForm = document.getElementById('addUserForm');
    const projectTableContainer = document.getElementById('projectTableContainer');
    const editUserModal = document.getElementById('editUserModal');
    const editUserForm = document.getElementById('editUserForm');
    const assignProjectModal = document.getElementById('assignProjectModal');
    const assignProjectForm = document.getElementById('assignProjectForm');
    const manageUsersLink = document.getElementById('manageUsersLink');
    const manageUsersContainer = document.getElementById('manageUsersContainer');
    const manageUsersTable = document.getElementById('manageUsersTable');

    function hideAllSections() {
        userTableContainer.style.display = 'none';
        projectTableContainer.style.display = 'none';
        addUserModal.style.display = 'none';
        editUserModal.style.display = 'none';
        assignProjectModal.style.display = 'none';
        manageUsersContainer.style.display = 'none';
        requestsTableContainer.style.display = 'none';    }
    
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
                const response = await fetch('/api/admin/add-user', {
                    method: 'POST',
                    headers: {
                        'Authorization': `Bearer ${token}`,
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify(userPayload)
                });

                if (!response.ok) {
                    const errorMessage = await response.text();
                    if (errorMessage === 'Email already exists') {
                        alert('Email already exists');
                    } else {
                        alert(`Error: ${errorMessage}`);
                    }
                    return;
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
    const managerSelect = document.getElementById('managerId'); 

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
                    managerId:  managerId
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

    // ASSIGN PROJECT
    const assignProjectButton = document.getElementById('assignProjectButton');

    if (assignProjectButton && assignProjectModal && assignProjectForm) {
        assignProjectButton.addEventListener('click', async () => {
            try {
                const [projectsResponse, employeesResponse] = await Promise.all([
                    fetch('/api/admin/projects', {
                        headers: {
                            'Authorization': `Bearer ${token}`
                        }
                    }),
                    fetch('/api/admin/employees', {
                        headers: {
                            'Authorization': `Bearer ${token}`
                        }
                    })
                ]);

                const projects = await projectsResponse.json();
                const employees = await employeesResponse.json();

                const projectIdSelect = document.getElementById('projectId');
                const employeeIdSelect = document.getElementById('employeeId');

                projectIdSelect.innerHTML = projects.map(project => `<option value="${project.projectId}">${project.name}</option>`).join('');
                employeeIdSelect.innerHTML = employees.map(employee => `<option value="${employee.userId}">${employee.firstname} ${employee.lastname}</option>`).join('');

                assignProjectModal.style.display = 'block';
            } catch (error) {
                console.error('Error fetching projects or employees:', error);
                alert('Failed to load projects or employees');
            }
        });

        assignProjectModal.querySelector('.close-btn').addEventListener('click', () => {
            assignProjectModal.style.display = 'none';
        });

        assignProjectForm.addEventListener('submit', async (e) => {
            e.preventDefault();
            const formData = new FormData(assignProjectForm);
            const projectId = formData.get('projectId');
            const employeeId = formData.get('employeeId');

            try {
                const response = await fetch(`/api/admin/assignProject/${projectId}/${employeeId}`, {
                    method: 'POST',
                    headers: {
                        'Authorization': `Bearer ${token}`,
                        'Content-Type': 'application/json'
                    }
                });

                if (!response.ok) {
                    const errorText = await response.text();
                    if (errorText === 'This employee is already assigned to a project.') {
                        throw new Error(errorText);
                    } else {
                        throw new Error('Failed to assign project');
                    }
                }

                alert('Project assigned successfully');
                assignProjectForm.reset();
                assignProjectModal.style.display = 'none';
            } catch (error) {
                if (error.message === 'This employee is already assigned to a project.') {
                    alert('Error: This employee is already assigned to a project.');
                } else {
                    alert(`Error: ${error.message}`);
                }
            }
        });
    }

    // VIEW PROJECTS
    const viewProjectsLink = document.getElementById('viewProjectsLink');
    const projectTableBody = document.querySelector('#projectTable tbody');

    if (viewProjectsLink) {
        viewProjectsLink.addEventListener('click', async () => {
            hideAllSections();
            try {
                const response = await fetch('/api/admin/projectDetails', {
                    headers: {
                        'Authorization': `Bearer ${token}`,
                        'Content-Type': 'application/json'
                    }
                });

                if (response.ok) {
                    const projectDetails = await response.json();
                    projectTableBody.innerHTML = '';

                    projectDetails.forEach((project, index) => {
                        const row = document.createElement('tr');
                        row.innerHTML = `
                            <td>${index + 1}</td>
                            <td>${project.projectName}</td>
                            <td>${project.managerName}</td>
                            <td>${project.employeeNames.join(', ')}</td>
                            <td>
                                <button class="unassign-btn">Unassign</button>
                            </td>
                        `;
                        projectTableBody.appendChild(row);
                    });

                    const unassignButtons = document.querySelectorAll('.unassign-btn');
                    unassignButtons.forEach((button, index) => {
                        button.addEventListener('click', () => {
                            const projectId = projectDetails[index].projectId;
                            const employeeNames = projectDetails[index].employeeNames;

                            if (employeeNames.length === 1) {
                                unassignEmployee(projectId, employeeNames[0]);
                            } else {
                                showUnassignModal(projectId, employeeNames);
                            }
                        });
                    });

                    projectTableContainer.style.display = 'block';
                } else {
                    throw new Error('Failed to fetch project details');
                }
            } catch (error) {
                console.error('Error:', error);
                alert(`Error: ${error.message}`);
            }
        });
    }
 
    //UNASSIGN PROJECT  
    async function unassignEmployee(projectId, employeeName) {
        try {
            const employeeId = await getEmployeeIdFromName(employeeName);
            console.log('Employee ID:', employeeId);

            const response = await fetch(`/api/admin/unassignProject/${projectId}/${employeeId}`, {
                method: 'POST',
                headers: {
                    'Authorization': `Bearer ${token}`,
                    'Content-Type': 'application/json'
                }
            });

            if (!response.ok) {
                const errorText = await response.text();
                if (errorText === 'Assignment not found.') {
                    alert('This employee is already unassigned from the project.');
                } else {
                    throw new Error('Failed to unassign employee');
                }
            } else {
                alert('Employee unassigned successfully');
                location.reload();
            }
        } catch (error) {
            console.error('Error unassigning employee:', error);
            alert(`Error: ${error.message}`);
        }
    }
    async function getEmployeeIdFromName(employeeName) {
        try {
            console.log('Fetching employees...');
            const response = await fetch('/api/admin/employees', {
                headers: {
                    'Authorization': `Bearer ${token}`
                }
            });

            console.log('Response status:', response.status);

            if (!response.ok) {
                const errorMessage = await response.text();
                console.error('Failed to fetch employees:', errorMessage);
                throw new Error(`Failed to fetch employees: ${errorMessage}`);
            }

            const employees = await response.json();
            console.log('Employees:', employees);

            const employee = employees.find(emp => `${emp.firstname} ${emp.lastname}` === employeeName);

            if (employee) {
                return employee.userId;
            } else {
                throw new Error(`Employee with name "${employeeName}" not found`);
            }
        } catch (error) {
            console.error('Error fetching employees:', error);
            throw error;
        }
    }
    
    function showUnassignModal(projectId, employeeNames) {
        const unassignModal = document.getElementById('unassignModal');
        const employeeSelect = document.getElementById('employeeSelect');
        const confirmUnassignBtn = document.getElementById('confirmUnassignBtn');

        employeeSelect.innerHTML = employeeNames.map(name => `<option value="${name}">${name}</option>`).join('');

        unassignModal.style.display = 'block';

        const closeBtn = unassignModal.querySelector('.close-btn');
        closeBtn.addEventListener('click', () => {
            unassignModal.style.display = 'none';
        });

        confirmUnassignBtn.addEventListener('click', async () => {
            const selectedEmployeeName = employeeSelect.value;
            try {
                await unassignEmployee(projectId, selectedEmployeeName);
                unassignModal.style.display = 'none';
            } catch (error) {
                console.error('Error unassigning employee:', error);
                alert(`Error: ${error.message}`);
            }
        });
    }

 // Manage Users
    if (manageUsersLink) {
        manageUsersLink.addEventListener('click', async () => {
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
                populateManageUsersTable(users);
                manageUsersContainer.style.display = 'block';
            } catch (error) {
                console.error('Error fetching users:', error);
                alert('Failed to fetch users');
            }
        });
    }

    function populateManageUsersTable(users) {
        const tableBody = manageUsersTable.getElementsByTagName('tbody')[0];
        tableBody.innerHTML = '';

        users.forEach((user, index) => {
            const row = document.createElement('tr');
            const serialNumberCell = document.createElement('td');
            const firstNameCell = document.createElement('td');
            const lastNameCell = document.createElement('td');
            const emailCell = document.createElement('td');
            const roleCell = document.createElement('td');
            const actionsCell = document.createElement('td');

            serialNumberCell.textContent = index + 1;
            firstNameCell.textContent = user.firstname;
            lastNameCell.textContent = user.lastname;
            emailCell.textContent = user.email;
            roleCell.textContent = user.role;

            const editButton = document.createElement('button');
            editButton.textContent = 'Edit';
            editButton.classList.add('edit-btn'); 
            editButton.addEventListener('click', () => showEditUserModal(user));

            const deleteButton = document.createElement('button');
            deleteButton.textContent = 'Delete';
            deleteButton.classList.add('delete-btn'); 
            deleteButton.addEventListener('click', () => deleteUser(user.userId));


            actionsCell.appendChild(editButton);
            actionsCell.appendChild(deleteButton);

            row.appendChild(serialNumberCell);
            row.appendChild(firstNameCell);
            row.appendChild(lastNameCell);
            row.appendChild(emailCell);
            row.appendChild(roleCell);
            row.appendChild(actionsCell);

            tableBody.appendChild(row);
        });
    }

    function showEditUserModal(user) {
        editUserModal.style.display = 'block';
        document.getElementById('editUserId').value = user.userId;
        document.getElementById('editFirstname').value = user.firstname;
        document.getElementById('editLastname').value = user.lastname;
        document.getElementById('editEmail').value = user.email;
        document.getElementById('editPassword').value = ''; 

        const closeBtn = editUserModal.querySelector('.close-btn');
        closeBtn.addEventListener('click', () => {
            editUserModal.style.display = 'none';
        });

        editUserForm.addEventListener('submit', async (e) => {
            e.preventDefault();
            const formData = new FormData(editUserForm);
            const userId = formData.get('editUserId');
            const firstname = formData.get('editFirstname');
            const lastname = formData.get('editLastname');
            const email = formData.get('editEmail');
            const password = formData.get('editPassword');

            const userPayload = { firstname, lastname, email, password };

            try {
                const response = await fetch(`/api/admin/updateEmployee/${userId}`, {
                    method: 'PUT',
                    headers: {
                        'Authorization': `Bearer ${token}`,
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify(userPayload)
                });

                if (!response.ok) {
                    throw new Error('Failed to update user');
                }

                alert('User updated successfully');
                editUserModal.style.display = 'none';
                location.reload(); 
            } catch (error) {
                console.error('Error updating user:', error);
                alert(`Error: ${error.message}`);
            }
        });
    }
    function deleteUser(userId) {
        if (confirm('Are you sure you want to delete this user?')) {
            fetch(`/api/admin/deleteEmployees/${userId}`, {
                method: 'DELETE',
                headers: {
                    'Authorization': `Bearer ${token}`
                }
            })
            .then(response => {
                if (response.ok) {
                    alert('User deleted successfully');
                    location.reload(); 
                } else if (response.status === 409) {
                    return response.text().then(message => {
                        alert(message);
                    });
                } else {
                    throw new Error('Failed to delete user');
                }
            })
            .catch(error => {
                console.error('Error deleting user:', error);
                alert(`Error: ${error.message}`);
            });
        }
    }
    
    // VIEW REQUESTS
    const viewRequestsLink = document.getElementById('viewRequestsLink');
    const requestsTableContainer = document.getElementById('requestsTableContainer');
    const requestsTable = document.getElementById('requestsTable');
    
    if (viewRequestsLink) {
        viewRequestsLink.addEventListener('click', async () => {
            hideAllSections();
            try {
                const response = await fetch('/api/admin/requests', {
                    headers: {
                        'Authorization': `Bearer ${token}`
                    }
                });

                if (!response.ok) {
                    throw new Error('Failed to fetch resource requests');
                }

                const requests = await response.json();
                populateRequestsTable(requests);
                requestsTableContainer.style.display = 'block';
            } catch (error) {
                console.error('Error fetching resource requests:', error);
                alert('Failed to fetch resource requests');
            }
        });
    }

    function populateRequestsTable(requests) {
        const tableBody = requestsTable.getElementsByTagName('tbody')[0];
        tableBody.innerHTML = '';

        requests.forEach((request, index) => {
            const row = document.createElement('tr');
            const serialNumberCell = document.createElement('td');
            const projectNameCell = document.createElement('td');
            const employeeEmailsCell = document.createElement('td');
            const statusCell = document.createElement('td');
            const actionsCell = document.createElement('td');

            serialNumberCell.textContent = index + 1;
            projectNameCell.textContent = request.project.name;
            employeeEmailsCell.textContent = request.employee.email;
            statusCell.textContent = request.status;

            const approveButton = document.createElement('button');
            approveButton.textContent = 'Approve';
            approveButton.classList.add('approve-btn');
            approveButton.addEventListener('click', () => approveRequest(request.requestId));

            const rejectButton = document.createElement('button');
            rejectButton.textContent = 'Reject';
            rejectButton.classList.add('reject-btn');
            rejectButton.addEventListener('click', () => rejectRequest(request.requestId));

            actionsCell.appendChild(approveButton);
            actionsCell.appendChild(rejectButton);

            row.appendChild(serialNumberCell);
            row.appendChild(projectNameCell);
            row.appendChild(employeeEmailsCell);
            row.appendChild(statusCell);
            row.appendChild(actionsCell);

            tableBody.appendChild(row);
        });
    }

    async function approveRequest(requestId) {
        try {
            const response = await fetch(`/api/admin/requests/${requestId}/approve`, {
                method: 'PUT',
                headers: {
                    'Authorization': `Bearer ${token}`
                }
            });

            if (!response.ok) {
                throw new Error('Failed to approve request');
            }

            alert('Request approved successfully');
            location.reload(); 
        } catch (error) {
            console.error('Error approving request:', error);
            alert(`Error: ${error.message}`);
        }
    }

    async function rejectRequest(requestId) {
        try {
            const response = await fetch(`/api/admin/requests/${requestId}/reject`, {
                method: 'PUT',
                headers: {
                    'Authorization': `Bearer ${token}`
                }
            });

            if (!response.ok) {
                throw new Error('Failed to reject request');
            }

            alert('Request rejected successfully');
            location.reload(); 
        } catch (error) {
            console.error('Error rejecting request:', error);
            alert(`Error: ${error.message}`);
        }
    }

   
    
    // LOGOUT
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