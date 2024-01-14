document.addEventListener('DOMContentLoaded', function() {
    const loginForm = document.getElementById('loginForm');
    const signupForm = document.getElementById('signupForm');

    // Handle Login Form Submission
    loginForm.addEventListener('submit', function(event) {
        event.preventDefault(); // Prevent the default form submission

        // Collect form data
        const loginUsername = document.getElementById('login-username').value;
        const loginPassword = document.getElementById('login-password').value;

        // Send data to the servlet
        sendLoginData(loginUsername, loginPassword);
    });
    

    // Handle Signup Form Submission
    signupForm.addEventListener('submit', function(event) {
        event.preventDefault(); // Prevent the default form submission

		
        // Collect form data
        const signupEmail = document.getElementById('signup-email').value;
        const signupUsername = document.getElementById('signup-username').value;
        const signupPassword = document.getElementById('signup-password').value;
		const confirmPassword = document.getElementById('signup-password-confirm').value;
        // check if password and confirm password matches
        if (signupPassword !== confirmPassword) {
			console.log("password doesnt match");
            alert('Passwords do not match. Please re-enter.');
            return; // Prevent further execution of the form submission
        }

        // Send data to the servlet
        sendSignupData(signupEmail, signupUsername, signupPassword);
    });
});

// Function to send login data to the servlet
function sendLoginData(username, password) {
    fetch('LoginServlet', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({
            username: username,
            password: password
        })
    })
    .then(response => {
	    if (!response.ok) {
	        return response.json().then(error => {
	            // Display error message to the user
	            alert(error); 
	            throw new Error(error);
	        });
	    }
	    return response.json();
	})
    .then(data => {
		if(data === 'false') { //username or password incorrect
			alert('Invalid username or password');
			console.error('Login failed: ' + data.error);
		}
         else {
            localStorage.setItem('loggedIn', 'true');

            localStorage.setItem('username', username);
            console.log( "local storage: " + localStorage.getItem('username'));
            
            // Redirect to the home page upon successful login
            window.location.href = 'home.html';
        } 
    })
    .catch(error => {
        console.error('Error:', error);
    });
}

// Function to send signup data to the servlet
function sendSignupData(email, username, password) {
    fetch('SignupServlet', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({
            email: email,
            username: username,
            password: password
        })
    })
    .then(response => {
    if (!response.ok) {
        return response.json().then(error => {
            // Display error message to the user
            alert(error);
            throw new Error(error);
        });
    }
    return response.json();
	})
    .then(data => {
		if(data == -1) { //username is in use
			alert('Username is already in use');
		}
		else if (data == -2) { //email is in use
			alert('Email is already in use');
		} 
		else { //successful signup
			sendLoginData(username, password);
		}
    })
    .catch(error => {
        console.error('Error:', error);
        
    });
}