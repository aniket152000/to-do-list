document.getElementById('loginForm').addEventListener('submit', function(event) {
    event.preventDefault(); 
    const username = document.getElementById('username').value;
    const password = document.getElementById('password').value;
  
    if (username === 'admin' && password === 'password') {
      // Redirect to index.html
      window.location.href = 'index.html';
    } else {
      document.getElementById('loginMessage').textContent = 'Invalid username or password';
    }
  });
  