const deleteButton = document.getElementById("delete-btn");

if (deleteButton) {
    deleteButton.addEventListener('click', event => {
        let id = document.getElementById("article-id").value;
        fetch(`/api/articles/${id}`, {method: 'DELETE'})
            .then(() => {
                alert('Success to delete');
                location.replace("/articles");
                }
            )
    })
}

const modifyButton = document.getElementById("modify-btn");

if (modifyButton) {
    modifyButton.addEventListener('click', event => {
        let params = new URLSearchParams(location.search);
        let id = params.get('id');
        let title = document.getElementById("title").value;
        let content = document.getElementById("content").value;

        fetch(`/api/articles/${id}`, {
            method:'PUT',
            headers: {"Content-Type": "application/json"},
            body: JSON.stringify({title: title, content: content})
        })
            .then(() => {
                alert("Modified");
                location.replace(`/articles/${id}`);
            });
    });
}

const createButton = document.getElementById('create-btn');

if (createButton) {
    createButton.addEventListener('click', event => {
        fetch('/api/articles',{
            method: 'POST',
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify({
                title: document.getElementById('title').value,
                content: document.getElementById('content').value
            })
        })
            .then(() => {
                alert("Created");
                location.replace('/articles');
            })
    })
}