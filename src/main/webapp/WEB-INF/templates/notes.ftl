<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Мои заметки</title>
</head>
<body>
<h1>Мои заметки</h1>

<#if successMessage??>
    <p>${successMessage}</p>
</#if>

<form method="get" action="/notes">
    <input type="text" name="q" value="${query!''}" placeholder="Поиск по моим заметкам">
    <button type="submit">Найти</button>
</form>

<p>
    <a href="/notes/create">Создать заметку</a> |
    <a href="/notes/public">Публичные заметки</a>
</p>

<table border="1" cellpadding="8" cellspacing="0">
    <tr>
        <th>ID</th>
        <th>Заголовок</th>
        <th>Содержимое</th>
        <th>Создана</th>
        <th>Публичная</th>
        <th>Действия</th>
    </tr>
    <#list notes as note>
        <tr>
            <td>${note.id}</td>
            <td>${note.title}</td>
            <td>${note.content}</td>
            <td>${note.createdAt}</td>
            <td><#if note.public>Да<#else>Нет</#if></td>
            <td>
                <a href="/notes/${note.id}/edit">Редактировать</a>
                <form method="post" action="/notes/${note.id}/delete" style="display:inline;">
                    <button type="submit">Удалить</button>
                </form>
            </td>
        </tr>
    </#list>
</table>
</body>
</html>
