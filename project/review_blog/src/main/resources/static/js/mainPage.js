const deleteButton = document.getElementById('article-view-delete-article-btn');

if (deleteButton) {
    deleteButton.addEventListener('click', event => {
        if (confirm('Delete this article?')) {
            let articleId = document.getElementById('article-view-id').value;
            fetch('/api/article/' + articleId, {method: 'DELETE'})
                .then(() => {
                    alert('Delete article successfully');
                    location.replace('/home');
                })
        }
    })
}

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