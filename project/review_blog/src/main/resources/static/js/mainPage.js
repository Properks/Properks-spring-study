const userInfo =  document.querySelector('.user-info-btn');
const userNickname = (userInfo) ? userInfo.textContent.replace("Username: ", "") : null;

// fetch delete request from article view page
const deleteButton = document.getElementById('article-view-delete-article-btn');

if (deleteButton) {
    deleteButton.addEventListener('click', event => {
        if (isSameAuthor(userNickname)) {
            if (confirm('Delete this article?')) {
                let articleId = document.getElementById('article-view-id').value;
                fetch('/api/article/' + articleId, {method: 'DELETE'})
                    .then(() => {
                        alert('Delete article successfully');
                        location.replace('/home');
                    })
            }
        } else {alert("Cannot delete someone else's");}
    })
}

// check you can go to modify page
const modifyButtonInArticleView = document.getElementById('article-view-modify-article-btn');
if (modifyButtonInArticleView) {
    modifyButtonInArticleView.addEventListener('click', event => {
        if(isSameAuthor(userNickname)) {
            let articleId = document.getElementById('article-view-id').value;
            location.replace('/new-article?id=' + articleId);
        } else {alert("Cannot modify someone else's")}
    })
}

// Fetch modify request from new article page
const modifyButton = document.getElementById('new-article-modify-btn');

if (modifyButton) {
    modifyButton.addEventListener('click', event => {
        let articleId = document.getElementById('new-article-id').value;
        let articleTitle = document.getElementById('new-article-title').value;
        let articleContent = document.getElementById('new-article-content').value;

        fetch('/api/article/' + articleId, {
            method : 'PUT',
            headers : {
                'Content-type' : 'application/json'
            },
            body : JSON.stringify({
                title: articleTitle,
                content: articleContent
            })
        })
            .then(() => {
                alert('Modify article successfully');
                location.replace('/article/' + articleId);
            })
    })
}

const createButton = document.getElementById('new-article-create-btn');

// Create article function in new article page
// TODO:Have to implement author on body after implementing user
if (createButton) {
    createButton.addEventListener('click', event => {
        let title = document.getElementById('new-article-title').value;
        let content = document.getElementById('new-article-content').value;

        fetch('/api/article', {
            method: 'POST',
            headers: {
                'Content-type': 'application/json'
            },
            body: JSON.stringify({
                title: title,
                content: content,
                category: "article" //FIXME: fix it to select category
            })
        })
            .then(() => {
                alert('Create article successfully');
                location.replace('/home');
            })
    })
}

// function that represent nickname with code
if (userInfo) {
    const userOriginalNickname = userInfo.textContent;
    userInfo.addEventListener('mouseover', event => {
        userInfo.textContent = 'Username: ' + userInfo.getAttribute('hover-text');
    })
    userInfo.addEventListener('mouseout', event => {
        userInfo.textContent = userOriginalNickname;
    })
}

// check nickname is same as author. return boolean
function isSameAuthor(nickname) {
    let author = document.getElementById('article-view-author').textContent.replace("Writer: ", "");
    return author === nickname;
}

// Change page
const beforePage = document.getElementById('page-decrease');
const afterPage = document.getElementById('page-increase');
const totalPage = document.getElementById('total-page');
const currentPage = document.querySelector('.current-page');
const otherPage = document.querySelectorAll('.other-page')

if (beforePage && currentPage.textContent !== '1') {
    beforePage.addEventListener('click', event => {
        location.replace('/home?page=' + (parseInt(currentPage.textContent) - 1));
    })
}

if (afterPage && currentPage.textContent !== totalPage.value) {
    afterPage.addEventListener('click', event => {
        location.replace('/home?page=' + (parseInt(currentPage.textContent) + 1));
    })
}

if (otherPage) {
    otherPage.forEach(element => {
        element.addEventListener('click', event => {
            location.replace('home?page=' + element.textContent);
        })
    })
}

//Change articles per page
const articlesPerPage = document.getElementById('article-list-page-size');
if (articlesPerPage) {
    let sizeParameter = new URLSearchParams(location.search).get('size');
    if (sizeParameter) {
        articlesPerPage.value = sizeParameter;
    } else {articlesPerPage.value = 10;}

    articlesPerPage.addEventListener('change', event => {
        location.replace("/home?page=" + currentPage.textContent + "&size=" + articlesPerPage.value);
    })
}

// Add Category with btn
const categoryButton = document.getElementById("create-sidebar-btn");
if (categoryButton) {
    categoryButton.addEventListener('click', event => {
        let inputPath = prompt("Enter a category ex)article/article1/article2");
        let path = inputPath.replace(/\//g, '_');
        fetch('/api/category', {
            method: 'POST',
            headers: {
                'Content-type': 'application/json'
            },
            body: JSON.stringify({
                pathOfCategory: path
            })
        })
        // fetch('/api/category/' + path, {method: 'POST'})
            .then(response => {
                alert("Success to create category");
                location.replace('/home');
            })

    })
}

// Error message
// const errorMessage = document.getElementById('error-message').value;
//
// if (errorMessage) {
//     alert(errorMessage);
// }