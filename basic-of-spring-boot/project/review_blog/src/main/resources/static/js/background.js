
const userInfo = document.querySelector('.user-info-btn');
const userNickname = (userInfo) ? userInfo.textContent.replace("Username: ", "") : null;

// function that represent nickname with code
if (userInfo) {
    const userInfoContainer = document.querySelector(".user-info-container"); // For addEventListener
    const userOriginalNickname = userInfo.textContent;
    const hoverMenu = document.getElementById("home-page-user-hover-menu"); // Show menu
    userInfoContainer.addEventListener('mouseover', () => {
        userInfo.textContent = 'Username: ' + userInfo.getAttribute('hover-text');
        hoverMenu.style.display = "block";
    })
    userInfoContainer.addEventListener('mouseout', () => {
        userInfo.textContent = userOriginalNickname;
        hoverMenu.style.display = "none";
    })
}