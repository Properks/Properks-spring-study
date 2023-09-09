let deleteButton = document.getElementById('article-view-delete-article-btn');
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