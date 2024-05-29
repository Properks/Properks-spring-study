document.write('<script src="/js/background.js"></script>') // import background.js with write method

// fetch delete request from article view page
const deleteButton = document.getElementById('article-view-delete-article-btn');

if (deleteButton) {
    deleteButton.addEventListener('click', ()=> {
        if (isSameAuthor(userNickname)) {
            if (confirm('Delete this article?')) {
                let articleId = document.getElementById('article-view-id').value;
                fetch('/api/article/' + articleId, {method: 'DELETE'})
                    .then(response => {
                        if (response.status === 200) {
                            alert('Delete article successfully');
                            location.replace('/home');
                        }
                        else if (response.status === 400) {
                            alert('Fail to delete article (Bad Request)');
                            location.replace('/article/' + articleId);
                        }
                        else {
                            alert('Fail to delete article (Unknown error: ' + response.status + ')');
                            location.replace('/article/' + articleId);
                        }
                    })
            }
        } else {alert("Cannot delete someone else's");}
    })
}

// check you can go to modify page
const modifyButtonInArticleView = document.getElementById('article-view-modify-article-btn');
if (modifyButtonInArticleView) {
    modifyButtonInArticleView.addEventListener('click', ()=> {
        if(isSameAuthor(userNickname)) {
            let articleId = document.getElementById('article-view-id').value;
            location.replace('/new-article?id=' + articleId);
        } else {alert("Cannot modify someone else's")}
    })
}

// Fetch modify request from new article page
const modifyButton = document.getElementById('new-article-modify-btn');

if (modifyButton) {
    modifyButton.addEventListener('click', ()=> {
        let articleId = document.getElementById('new-article-id').value;
        let articleTitle = document.getElementById('new-article-title').value;
        let articleContent = document.getElementById('new-article-content').value;
        let categoryPath = document.getElementById('new-article-category').value;

        fetch('/api/article/' + articleId, {
            method : 'PUT',
            headers : {
                'Content-type' : 'application/json'
            },
            body : JSON.stringify({
                title: articleTitle,
                content: articleContent,
                category: categoryPath
            })
        })
            .then(response => {
                if (response.status === 200) {
                    alert('Modify article successfully');
                    location.replace('/article/' + articleId);
                }
                else if (response.status === 400) {
                    alert('Fail to modify article (Bad Request)');
                    location.replace('/article/' + articleId);
                }
                else {
                    alert('Fail to modify article (Unknown error: ' + response.status + ')');
                    location.replace('/article/' + articleId);
                }
            })
    })
}

const createButton = document.getElementById('new-article-create-btn');

// Create article function in new article page
if (createButton) {
    createButton.addEventListener('click', ()=> {
        let title = document.getElementById('new-article-title').value;
        let content = document.getElementById('new-article-content').value;
        let category = document.getElementById('new-article-category').value;

        fetch('/api/article', {
            method: 'POST',
            headers: {
                'Content-type': 'application/json'
            },
            body: JSON.stringify({
                title: title,
                content: content,
                category: category
            })
        })
            .then(response => {
                if (response.status === 201) {
                    alert('Create article successfully');
                    location.replace('/home');
                }
                else if (response.status === 400) {
                    alert('Fail to create article (Bad Request)')
                    location.replace('/new-article');
                }
                else {
                    alert('Fail to create article (Unknown error: ' + response.status + ')');
                    location.replace('/new-article');
                }
            })
    })
}

// check nickname is same as author. return boolean
function isSameAuthor(nickname) {
    let author = document.getElementById('article-view-author').textContent.replace("Writer: ", "");
    return author === nickname;
}

// Change page
const beforePage = document.getElementById('article-list-page-decrease');
const afterPage = document.getElementById('article-list-page-increase');
const totalPage = document.getElementById('article-list-total-page');
const currentPage = document.querySelector('.current-page');
const otherPage = document.querySelectorAll('.other-page')

if (beforePage && currentPage.textContent !== '1') {
    beforePage.addEventListener('click', () => {
        location.replace('/home?page=' + (parseInt(currentPage.textContent) - 1));
    })
}

if (afterPage && currentPage.textContent !== totalPage.value) {
    afterPage.addEventListener('click', () => {
        location.replace('/home?page=' + (parseInt(currentPage.textContent) + 1));
    })
}

if (otherPage) {
    otherPage.forEach(element => {
        element.addEventListener('click', () => {
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

    articlesPerPage.addEventListener('change', () => {
        location.replace("/home?page=" + currentPage.textContent + "&size=" + articlesPerPage.value);
    })
}

// Add Category with btn
const categoryButton = document.getElementById("create-sidebar-btn");
if (categoryButton) {
    categoryButton.addEventListener('click', () => {
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
                if (response.status === 201) {
                    alert("Success to create category");
                    location.replace('/home');
                }
                else if (response.status === 400) {
                    alert('Fail to create category (Bad Request)');
                    location.replace('/home');
                }
                else {
                    alert('Fail to create category (Unknown error: ' + response.status + ')');
                    location.replace('/home');
                }
            })

    })
}

//category select
const sidebarElements = document.querySelectorAll(".sidebar");
if (sidebarElements) {
    for (const sidebarElement of sidebarElements) {
        sidebarElement.addEventListener('click', () => {
            let categoryId = sidebarElement.querySelector(".category-id").value;
            location.replace("/home?size=" + articlesPerPage.value + "&categoryId=" +
                categoryId)
        })
    }
}

//All category
const allCategory = document.getElementById('sidebar-all-category');
if (allCategory) {
    allCategory.addEventListener('click', () => {
        let url = new URLSearchParams(location.search);
        let query = 'categoryId';
        if (url.has(query)) {
            url.delete(query)
        }
        location.replace('/home?' + url.toString());
    })
}

//Show category in article list
const categoryInUrl = new URLSearchParams(location.search).get('categoryId');
const showCategoryInList = document.getElementById('show-selected-category');
if (showCategoryInList) {
    if (categoryInUrl) {
        let categoryId =
            Array.from(sidebarElements).map(item => item.querySelector('.category-id'))
                .find(item => item.value === categoryInUrl)
        showCategoryInList.textContent = 'Category: ' + categoryId.getAttribute('category-name');
    } else {
        showCategoryInList.textContent = 'Category: All';
    }
}

//Search article
const searchButton = document.getElementById('article-list-search-btn');
if (searchButton) {
    searchButton.addEventListener('click', () => {
        let searchCategory = document.getElementById('article-list-search-category').value;
        let searchQuery = document.getElementById('article-list-search-parameter').value;
        let url = new URLSearchParams(location.search);
        if (searchQuery !== "") {
            location.replace('/home?' + searchCategory + '=' + searchQuery);
        }
        else if (url.has('titleContent') || url.has('writer')) {
            url.delete((url.has('titleContent')) ? 'titleContent' : 'writer');
            location.replace('/home?' + url.toString())
        }
    })
}

//Show searched word
const searchedWordInURL = new URLSearchParams(location.search);
const searchedWord = document.getElementById('article-list-searched-word');
if (searchedWord) {
    if (searchedWordInURL.has('titleContent')) {
        searchedWord.textContent = searchedWordInURL.get('titleContent');
    }
    else if (searchedWordInURL.has('writer')) {
        searchedWord.textContent = searchedWordInURL.get('writer');
    }
    else {
        searchedWord.textContent = 'none';
    }
}

// Check Special characters
function containsSpecialCharacters(str) {
    let regex = /[!@#$%^&*()_+\-=\[\]{};':"\\|,.<>\/?]+/;
    return regex.test(str);
}