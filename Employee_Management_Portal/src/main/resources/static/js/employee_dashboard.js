document.addEventListener('DOMContentLoaded', () => {
	function hideAll() {
        editProfileModalEmployee.style.display = 'none';
        profileSection.style.display = 'none';
        projectDetailsSection.style.display = 'none';
        userListSection.style.display = 'none';
    }
	
	
// VIEW PROFILE, AND PROJECT DETAILS
	const viewProfileLink = document.getElementById('viewProfileLink');
    const profileSection = document.getElementById('profileSection');
    const projectDetailsSection = document.getElementById('projectDetailsSection');
 
    function showViewProfile() {
        hideAll();
        profileSection.style.display = 'block';
        projectDetailsSection.style.display = 'block';
        loadEmployeeSkills();
        loadEmployeeProjectDetails();
    }
    function hideViewProfile() {
        profileSection.style.display = 'none';
        projectDetailsSection.style.display = 'none';
    }

    if (viewProfileLink) {
        viewProfileLink.addEventListener('click', showViewProfile);
    }
    async function loadEmployeeProjectDetails() {
        try {
            const userId = await getUserId();
            if (userId) {
                const response = await fetch(`/api/employee/projects/${userId}`, {
                    headers: {
                        'Authorization': `Bearer ${localStorage.getItem('token')}`,
                    },
                });

                if (response.ok) {
                    const projectDetails = await response.json();
                    const projectList = document.getElementById('projectList');
                    projectList.innerHTML = '';

                    projectDetails.forEach(project => {
                        const projectDiv = document.createElement('div');
                        projectDiv.innerHTML = `
                            <p>Project Name: ${project.projectName}</p>
                            <p>Project Manager: ${project.managerName}</p>
                            <p>Team Members:</p>
                            <ul>
                                ${project.employeeNames.map(name => `<li>${name}</li>`).join('')}
                            </ul>
                        `;
                        projectList.appendChild(projectDiv);
                    });
                } else {
                    const error = await response.text();
                    alert(error);
                }
            } else {
                alert('Failed to retrieve user ID');
            }
        } catch (error) {
            console.error('Error:', error);
            alert('An error occurred while loading project details');
        }
    }
	
// ADD SKILL	
    const addSkillFormElement = document.getElementById('addSkillFormElement');
   
    async function loadEmployeeSkills() {
        try {
            const userId = await getUserId();
            if (userId) {
                const response = await fetch(`/api/employee/skills/${userId}`, {
                    headers: {
                        'Authorization': `Bearer ${localStorage.getItem('token')}`,
                    },
                });

                if (response.ok) {
                    const skills = await response.json();
                    const skillsContainer = document.getElementById('skillsContainer');
                    skillsContainer.innerHTML = ''; 
                    skills.filter(skill => typeof skill === 'string').forEach(skill => {
                        const skillItem = document.createElement('li');
                        skillItem.textContent = skill;
                        skillsContainer.appendChild(skillItem);
                    });
                } else {
                    const error = await response.text();
                    alert(error);
                }
            } else {
                alert('Failed to retrieve user ID');
            }
        } catch (error) {
            console.error('Error:', error);
            alert('An error occurred while loading skills');
        }
    }

    async function addSkill(e) {
        e.preventDefault();
        const newSkill = document.getElementById('newSkill').value;

        try {
            const userId = await getUserId();
            if (userId) {
                const response = await fetch(`/api/employee/add-skills/${userId}`, {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json',
                        'Authorization': `Bearer ${localStorage.getItem('token')}`,
                    },
                    body: JSON.stringify({ skillName: newSkill }),
                });

                if (response.ok) {
                    alert('Skill added successfully');
                    loadEmployeeSkills();
                } else {
                    const error = await response.text();
                    alert(error);
                }
            } else {
                alert('Failed to retrieve user ID');
            }
        } catch (error) {
            console.error('Error:', error);
            alert('An error occurred while adding the skill');
        }
    }

    if (addSkillFormElement) {
        addSkillFormElement.addEventListener('submit', addSkill);
    }

// VIEW USERS
    const viewUsersLink = document.getElementById('viewUsersLink');
    const userListSection = document.getElementById('userList');
    function showUserList() {
        hideAll();
        userListSection.style.display = 'block';
        loadUserList();
    }

    function hideUserList() {
        userListSection.style.display = 'none';
    }    

    if (viewUsersLink) {
        viewUsersLink.addEventListener('click', showUserList);
    }
    async function loadUserList() {
        if (userListSection.style.display === 'none') {
            return;
        }

        try {
            const [employeeResponse, managerResponse] = await Promise.all([
                fetch('/api/employee/employees', {
                    headers: {
                        'Authorization': `Bearer ${localStorage.getItem('token')}`,
                    },
                }),
                fetch('/api/employee/managers', {
                    headers: {
                        'Authorization': `Bearer ${localStorage.getItem('token')}`,
                    },
                }),
            ]);

            if (employeeResponse.ok && managerResponse.ok) {
                const employees = await employeeResponse.json();
                const managers = await managerResponse.json();
                const userListTable = document.querySelector('#userList table tbody');
                userListTable.innerHTML = '';

                [...employees, ...managers].forEach((user, index) => {
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
            } else {
                const employeeError = await employeeResponse.text();
                const managerError = await managerResponse.text();
                alert(`Error fetching users: ${employeeError}, ${managerError}`);
            }
        } catch (error) {
            console.error('Error:', error);
            alert('An error occurred while loading the user list');
        }
    }
    
    // EDIT PROFILE
    const editProfileLinkEmployee = document.getElementById('editProfileLinkEmployee');
    const editProfileModalEmployee = document.getElementById('editProfileModalEmployee');
    const closeModalBtnEmployee = document.querySelector('.close-btn-employee');
    const editProfileFormEmployee = document.getElementById('editProfileFormEmployee');

    function showEditProfileModalEmployee() {
        hideAll();
        editProfileModalEmployee.style.display = 'block';
    }

    function hideEditProfileModalEmployee() {
        editProfileModalEmployee.style.display = 'none';
    }

    if (editProfileLinkEmployee) {
        editProfileLinkEmployee.addEventListener('click', showEditProfileModalEmployee);
    }

    if (closeModalBtnEmployee) {
        closeModalBtnEmployee.addEventListener('click', hideEditProfileModalEmployee);
    }

    if (editProfileFormEmployee) {
        editProfileFormEmployee.addEventListener('submit', async (e) => {
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
                if (userId) {
                    const response = await fetch(`/api/employee/update-profile/${userId}`, {
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
                        hideEditProfileModalEmployee();
                    } else {
                        const error = await response.text();
                        alert(error);
                    }
                } else {
                    alert('Failed to retrieve user ID');
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

    //LOGOUT FUNCTIONALITY
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
