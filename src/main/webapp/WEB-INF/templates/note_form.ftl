<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>${pageTitle!"Форма заметки"}</title>
</head>
<body>
<h1>${pageTitle!"Форма заметки"}</h1>

<form method="post" action="${formAction}">
    <div>
        <label for="title">Заголовок:</label>
        <input id="title" type="text" name="title" value="${note.title!''}" required>
    </div>

    <div>
        <label for="content">Содержимое:</label><br>
        <textarea id="content" name="content" rows="10" cols="60" required>${note.content!''}</textarea>
    </div>

    <div>
        <label>
            <input type="checkbox" name="public" <#if note.public?? && note.public>checked</#if>>
            Публичная заметка
        </label>
    </div>

    <button type="submit">Сохранить</button>
    <a href="/notes">Назад</a>
</form>
</body>
</html>
