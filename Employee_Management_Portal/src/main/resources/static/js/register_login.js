const loginForm = document.getElementById('loginForm');

loginForm.addEventListener('submit', async (e) => {
  e.preventDefault();

  const email = document.getElementById('email').value;
  const password = document.getElementById('password').value;
  try {
    const response = await fetch('/authenticate', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({ email, password }),
    });

    if (response.ok) {
      const token = await response.text();
      localStorage.setItem('token', token);

      const roleResponse = await fetch('/api/users/role', {
        headers: {
          'Authorization': `Bearer ${token}`,
        },
      });

      if (roleResponse.ok) {
        const role = await roleResponse.text();

        if (role === 'ROLE_ADMIN') {
          window.location.href = '/html/dashboard/admin-dashboard.html';
        } else if (role === 'ROLE_EMPLOYEE') {
          window.location.href = '/html/dashboard/employee-dashboard.html';
        } else if (role === 'ROLE_MANAGER') {
          window.location.href = '/html/dashboard/manager-dashboard.html';
        } else {
          alert('Invalid user role');
        }
      } else {
        alert('Failed to get user role');
      }
    } else {
      alert('Incorrect email or password');
    }
  } catch (error) {
    console.error('Error:', error);
    alert('An error occurred during login');
  }
});
