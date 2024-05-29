const allSignUpErrorMessage = document.querySelectorAll("[id $= error-msg]");
function checkSignUpCondition(){
    let email = document.getElementById('sign-up-email').value;
    let nickname = document.getElementById('sign-up-nickname').value;
    let password = document.getElementById('sign-up-password').value;
    let passwordCheck = document.getElementById('sign-up-password-check').value;
    let validEmail = document.getElementById('sign-up-email-valid-value').value;

    allSignUpErrorMessage.forEach(element =>{
        if (element.style.color !== "green") {
            element.style.display = "none";
        }
    });
    if (email === "" || nickname ==="" || password ==="" || passwordCheck ==="") {
        alert("Fill out all field");
    }
    else if (validEmail === "none") {
        errorMessage('sign-up-email-error-msg', "Please click Duplicate check");
    }
    else if (validEmail === "invalid") {
        errorMessage('sign-up-email-error-msg', 'This email already exists');
    }
    else if (password.length < 8) {
        errorMessage('sign-up-password-error-msg', "Please input password over 8 digits");
    }
    else if (password !== passwordCheck) {
        errorMessage('sign-up-password-check-error-msg', "Password Check is different from password");
    }
    else {
        alert("Sign Up SuccessFully");
        return true;
    }
    return false;
}

const duplicateCheckButton = document.getElementById('duplicate-check-btn');

duplicateCheckButton.addEventListener('click', () => {
    let email = document.getElementById('sign-up-email').value;

    if (isValidEmail(email)) {
        fetch('/api/email/' + email, { method: 'GET' }) // Use GET method
            .then(response => {
                if (response.status === 200) {
                    errorMessage('sign-up-email-error-msg', "You can use it", "green");

                    document.getElementById('sign-up-email-valid-value').value = "valid";
                    document.getElementById('sign-up-valid-img').style.display = "block";
                    document.getElementById('sign-up-invalid-img').style.display = "none";
                } else if (response.status === 302) { // Check for 404 status
                    errorMessage('sign-up-email-error-msg', "This email already exists");

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
        errorMessage('sign-up-email-error-msg', "Enter a email");
    } else {
        errorMessage('sign-up-email-error-msg', "You have entered an invalid email address!");
    }
});

function isValidEmail(mail) {
    let mailFormat = /^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,3})+$/;
    return !!mail.match(mailFormat);
}

function errorMessage(elementName, msg, color = "indianred") {
    let ErrorElement = document.getElementById(elementName);
    ErrorElement.style.display = "block";
    ErrorElement.style.color = color;
    ErrorElement.textContent = msg;
}
