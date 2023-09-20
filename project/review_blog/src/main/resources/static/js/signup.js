const submitButton = document.getElementById('sign-up-submit-btn');
submitButton.addEventListener('click', event => {
    let email = document.getElementById('sign-up-email').value;
    let nickname = document.getElementById('sign-up-nickname').value;
    let password = document.getElementById('sign-up-password').value;
    let passwordCheck = document.getElementById('sign-up-password-check').value;
    let validEmail = document.getElementById('sign-up-email-valid-value').value;

    if (email === "" || nickname ==="" || password ==="" || passwordCheck ==="") {
        alert("Fill out all field");
    }
    else if (validEmail === "none") {
        alert("Please click Duplicate check");
    }
    else if (validEmail === "invalid") {
        alert('This email already exists');
    }
    else if (password.length < 8) {
        alert("Please input password over 8 digits");
    }
    else if (password !== passwordCheck) {
        alert("Password Check doesn't same as password");
    }
    else {
        fetch('/user', {
            method: 'POST',
            headers: {
                'Content-type': 'application/json'
            },
            body: JSON.stringify({
                email: email,
                nickname: nickname,
                password: password
            })
        })
            .then(() => {
                alert("Sign Up Successfully");
                location.replace("/login");
            })
    }

})

const duplicateCheckButton = document.getElementById('duplicate-check-btn');

duplicateCheckButton.addEventListener('click', event => {
    let email = document.getElementById('sign-up-email').value;

    if (isValidEmail(email)) {
        fetch('/api/email/' + email, { method: 'GET' }) // Use GET method
            .then(response => {
                if (response.status === 200) {
                    alert("You can use it");
                    document.getElementById('sign-up-email-valid-value').value = "valid";
                    document.getElementById('sign-up-valid-img').style.display = "block";
                    document.getElementById('sign-up-invalid-img').style.display = "none";
                } else if (response.status === 302) { // Check for 404 status
                    alert("This email already exists");
                    document.getElementById('sign-up-email-valid-value').value = "invalid";
                    document.getElementById('sign-up-invalid-img').style.display = "block";
                    document.getElementById('sign-up-valid-img').style.display = "none";
                } else {
                    alert("An error occurred");
                }
            })
            .catch(error => {
                console.error("Fetch error:", error);
                alert("An error occurred");
            });
    } else if (email === "") {
        alert("Enter a email");
    } else {
        alert("You have entered an invalid email address!");
    }
});

function isValidEmail(mail) {
    let mailFormat = /^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,3})+$/;
    return !!mail.match(mailFormat);
}
