const deleteButton = document.getElementById("delete-btn");

if (deleteButton) {
    deleteButton.addEventListener('click', event => {
        let id = document.getElementById("article-id").value;
        function success() {
            alert('Successfully deleted');
            location.replace('/articles');
        }
        function fail() {
            alert('fail in deleting');
            location.replace('/articles');
        }
        httpRequest('DELETE', '/api/articles/' + id, null, success, fail)
    })
}

const modifyButton = document.getElementById("modify-btn");

if (modifyButton) {
    modifyButton.addEventListener('click', event => {
        let params = new URLSearchParams(location.search);
        let id = params.get('id');
        let title = document.getElementById("title").value;
        let content = document.getElementById("content").value;

        body = JSON.stringify({
            title: title,
            content: content
        })

        function success() {
            alert('Successfully modified');
            location.replace('/articles/' + id);
        }

        function fail() {
            alert('Modification failed');
            location.replace('articles/' + id);
        }

        httpRequest('PUT', '/api/articles/'+ id, body, success, fail);
    });
}

const createButton = document.getElementById('create-btn');

if (createButton) {
    createButton.addEventListener('click', event => {
            body = JSON.stringify({
                title: document.getElementById('title').value,
                content: document.getElementById('content').value
            });
            function success() {
                alert('Successfully created');
                location.replace('/articles');
            }
            function fail() {
                alert('fail in creating');
                location.replace('/articles');
            }

            httpRequest('POST', '/api/articles', body, success, fail)
    })
}

function getCookie(key) {
    var result = null;
    var cookie = document.cookie.split(';');
    cookie.some(function (item) {
        item = item.replace(" ", "");

        var dic = item.split('=');

        if (key === dic[0]) {
            result = dic[1];
            return true;
        }
    });

    return result;
}

function httpRequest(method, url, body, success, fail) {
    fetch(url, {
        method: method,
        headers: {

            Authorization: "Bearer " + localStorage.getItem('access_token'),
            'Content-Type': 'application/json'
        },
        body: body
    })
        .then((response) => {
            if (response.status === 201 || response.status === 200) {
                return success();
            }
            const refreshToken = getCookie('refresh_token');
            if (response.status === 401 && refreshToken) {
                fetch ('/api/token', {
                    method: 'POST',
                    headers: {
                        Authorization: "Bearer " + localStorage.getItem('access_token'),
                        'Content-Type': 'application/json'
                    }
                    body: JSON.stringify({
                        refreshToken: getCookie('refresh_token')
                    })
                })
                    .then((result) => {
                        localStorage.setItem('access_token', result.accessToken);
                        httpRequest(method, url, body, success, fail);
                    })
                    .catch((error) => fail());
            } else {
                return fail();
            }
        })
}