const deleteButton = document.getElementById("delete-btn");

if (deleteButton) {
    deleteButton.addEventListener('click', event => {
        let id = document.getElementById("article-id").value;
        fetch(`/api/delete/${id}`, {method: 'DELETE'})
            .then(() => {
                alert('Success to delete');
                location.replace("/articles");
                }
            )
    })
}