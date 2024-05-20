//EDIT PROFILE 
const editProfileLinkManager = document.getElementById('editProfileLinkManager');
const editProfileModalManager = document.getElementById('editProfileModalManager');
const closeModalBtnManager = document.querySelector('.close-btn-manager');
const editProfileFormManager = document.getElementById('editProfileFormManager');

function showEditProfileModalManager() {
  editProfileModalManager.style.display = 'block';
}

function hideEditProfileModalManager() {
  editProfileModalManager.style.display = 'none';
}

editProfileLinkManager.addEventListener('click', showEditProfileModalManager);

closeModalBtnManager.addEventListener('click', hideEditProfileModalManager);

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

async function getUserId() {
  try {
    const token = localStorage.getItem('token');
    if (!token) {
      throw new Error('No token found');
    }

    const response = await fetch('/api/users/role', {
      headers: {
        'Authorization': `Bearer ${token}`,
      },
    });

    if (response.ok) {
      const role = await response.text();
      if (role === 'ROLE_MANAGER') {
        const userId = localStorage.getItem('userId');
        if (userId) {
          return userId;
        } else {
          const userResponse = await fetch('/api/users/email', {
            headers: {
              'Authorization': `Bearer ${token}`,
            },
          });

          if (userResponse.ok) {
            const user = await userResponse.json();
            localStorage.setItem('userId', user.userId);
            return user.userId;
          } else {
            throw new Error('Failed to fetch user data');
          }
        }
      } else {
        throw new Error('Unauthorized access');
      }
    } else {
      throw new Error('Failed to get user role');
    }
  } catch (error) {
    console.error('Error:', error);
    alert('An error occurred while fetching user data');
  }
}

// Logout functionality
const logoutBtn = document.querySelector('.logout-btn');
if (logoutBtn) {
  logoutBtn.addEventListener('click', function(event) {
    event.preventDefault();
    localStorage.removeItem('token');
    localStorage.removeItem('userId'); 
    window.location.href = '/html/index.html';
  });
}
