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