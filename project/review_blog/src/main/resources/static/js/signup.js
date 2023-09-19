const submitButton = document.getElementById('sign-up-submit-btn');
submitButton.addEventListener('click', event => {
    let email = document.getElementById('sign-up-email').value;
    let nickname = document.getElementById('sign-up-nickname').value;
    let password = document.getElementById('sign-up-password').value;
    let passwordCheck = document.getElementById('sign-up-password-check').value;

    if (email === "" || nickname ==="" || password ==="" || passwordCheck ==="") {
        alert("Fill out all field");
    }
    else if (password.length < 8) {
        alert("Please input password over 8 digits");
    }
    else if (password !== passwordCheck) {
        alert("Password Check doesn't same as password");
    }
    else {
        fetch("/user", {
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