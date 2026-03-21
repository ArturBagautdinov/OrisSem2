<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Публичные заметки</title>
</head>
<body>
<h1>Публичные заметки</h1>

<form method="get" action="/notes/public">
    <input type="text" name="q" value="${query!''}" placeholder="Поиск по публичным заметкам">
    <button type="submit">Найти</button>
</form>

<p><a href="/notes">Мои заметки</a></p>

<table border="1" cellpadding="8" cellspacing="0">
    <tr>
        <th>ID</th>
        <th>Заголовок</th>
        <th>Содержимое</th>
        <th>Автор</th>
        <th>Создана</th>
    </tr>
    <#list notes as note>
        <tr>
            <td>${note.id}</td>
            <td>${note.title}</td>
            <td>${note.content}</td>
            <td>${note.author.username}</td>
            <td>${note.createdAt}</td>
        </tr>
    </#list>
</table>
</body>
</html>
