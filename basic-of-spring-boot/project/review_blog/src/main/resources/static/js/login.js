const urlParams = new URLSearchParams(location.search);
const loginErrorContainer = document.getElementById('login-error-msg');

// inform authentication error on page
if (urlParams.has('error')) {
    let errorMsg = urlParams.get('error');

    loginErrorContainer.style.display = "block";
    loginErrorContainer.textContent = errorMsg;
} else {
    loginErrorContainer.style.display = "none";
}