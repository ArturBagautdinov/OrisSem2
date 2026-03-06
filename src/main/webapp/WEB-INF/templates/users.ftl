<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Users</title>

    <style>
        body {
            font-family: Arial;
            margin: 40px;
        }

        table {
            border-collapse: collapse;
            width: 400px;
        }

        th, td {
            border: 1px solid #ccc;
            padding: 8px;
        }

        th {
            background: #eee;
        }

        form {
            margin-top: 20px;
        }
    </style>

</head>
<body>

<h2>User List</h2>

<table>
    <tr>
        <th>ID</th>
        <th>Username</th>
    </tr>

    <#list users as user>
        <tr>
            <td>${user.id}</td>
            <td>${user.username}</td>
        </tr>
    </#list>

</table>

<h3>Add user</h3>

<form method="post" action="/users/create">
    <input type="text" name="username" placeholder="username" required/>
    <button type="submit">Create</button>
</form>

</body>
</html>