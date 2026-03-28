<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Админка</title>
</head>
<body>

<h1>Админка</h1>

<h3>Удалить заметку по ID</h3>

<input type="number" id="noteId" placeholder="Введите ID">
<button onclick="deleteNote()">Удалить</button>

<p id="result"></p>

<script>
    function deleteNote() {
        const id = document.getElementById("noteId").value;

        if (!id) {
            alert("Введите ID");
            return;
        }

        fetch("/admin/notes/" + id, {
            method: "DELETE"
        })
            .then(response => {
                if (response.status === 204) {
                    document.getElementById("result").innerText = "Заметка удалена";
                } else if (response.status === 404) {
                    document.getElementById("result").innerText = "Заметка не найдена";
                } else {
                    document.getElementById("result").innerText = "Ошибка: " + response.status;
                }
            })
            .catch(error => {
                document.getElementById("result").innerText = "Ошибка запроса";
            });
    }
</script>

</body>
</html>