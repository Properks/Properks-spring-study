document.write('<script src="/js/background.js"></script>') // import background.js with write method

//Change nickname
const changeNickname = document.getElementById("information-change-nickname-btn");

if (changeNickname) {
    changeNickname.addEventListener("click", () => {
        let userId = document.getElementById("user-id").value;
        let nickname = document.getElementById("information-my-nickname").value;
        let code = document.getElementById("information-my-nickname-code").value;
        let originalNickname = document.getElementById("information-original-nickname").value;
        let errorMessage = document.getElementById("information-change-nickname-error-message");
        errorMessage.style.color = "crimson";
        if (originalNickname === (nickname + "#" + code)) {
            errorMessage.textContent = "Nickname isn't changed"
            return;
        }
        fetch("/user/nickname", {
            method: "PUT",
            headers: {"Content-type": "application/json"},
            body: JSON.stringify({
                id: userId,
                nickname: nickname,
                code: code
            })
        })
            .then((response) => {
                if (response.status === 409) {
                    errorMessage.textContent = nickname + "#" + code + " already exists";
                }
                else if (response.status === 200) {
                    errorMessage.textContent = "";
                    alert("Change nickname successfully");
                }
                else {
                    alert("Unknown error (" + response.status + ")")
                }
            })
    })
}

//Change password
const changePassword = document.getElementById('information-change-password-btn');

if (changePassword) {
    changePassword.addEventListener('click', () => {
        let userId = document.getElementById("user-id").value;
        let oldPassword = document.getElementById('information-current-password').value;
        let newPassword = document.getElementById('information-new-password').value;
        let checkNewPassword = document.getElementById('information-check-new-password').value;
        let errorMessage = document.getElementById('information-change-password-error-message');
        errorMessage.style.color = "crimson";
        if (newPassword !== checkNewPassword) {
            errorMessage.textContent = "Check New Password is different from New Password";
            return;
        }
        fetch('/user/password', {
            method: 'PUT',
            headers: {
                'Content-type': 'application/json'
            },
            body: JSON.stringify({
                id: userId,
                oldPassword: oldPassword,
                newPassword: newPassword
            })
        })
            .then(response => {
                if (response.status === 401) {
                    errorMessage.textContent = "Current Password is not correct";
                }
                else if (response.status === 200) {
                    errorMessage.textContent = "";
                    alert("Change Password Successfully\nYour account will be logged out.");
                    location.replace('/login');
                }
                else {
                    alert("Unknown error (" + response.status + ")");
                }
            })
    })
}

//Delete account
const deleteAccountBtnContainer= document.querySelector('.information-delete-account-btn-container');
const deleteAccountBtn = document.getElementById('information-delete-account-submit-btn');
const checkPasswordContainer = document.querySelector('.information-delete-account-check-password-container');
const checkPasswordBtn = document.getElementById('information-delete-account-check-password-btn');

if (deleteAccountBtn) {
    deleteAccountBtn.addEventListener('click', () => {
        deleteAccountBtnContainer.style.display = 'none';
        checkPasswordContainer.style.display = 'block';
    })
}


if (checkPasswordBtn) {
    checkPasswordBtn.addEventListener('click', () => {
        let password = document.getElementById('information-delete-account-check-password').value;
        if (confirm('Are you sure you want to delete the account?')) {
            fetch('/api/password', {
                method: 'POST',
                headers: {
                    'Content-type': 'application/json'
                },
                body: JSON.stringify({
                    password: password
                })
            })
                .then(response => {
                    if (response.status === 401) {
                        alert("Password is incorrect!");
                    }
                    else if (response.status !== 200) {
                        alert("Unknown error! (" + response.status+ ")");
                    }
                    else {
                        let userId = document.getElementById('user-id').value;
                        fetch('/user/' + userId, {method: 'DELETE'})
                            .then(response => {
                                if (response.status === 400) {
                                    alert("Fail to delete account (" + response.status + ")");
                                }
                                else if (response.status === 200) {
                                    alert("Delete account successfully");
                                    location.replace("login");
                                }
                                else {
                                    alert("Unknown error! (" + response.status+ ")");
                                }
                            })
                    }
                })
        }
    })
}