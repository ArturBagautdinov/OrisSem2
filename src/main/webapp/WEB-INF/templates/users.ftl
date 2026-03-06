<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Users</title>

    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 40px;
        }

        table {
            border-collapse: collapse;
            width: 700px;
            margin-top: 20px;
        }

        th, td {
            border: 1px solid #ccc;
            padding: 8px;
            text-align: left;
            vertical-align: middle;
        }

        th {
            background: #eee;
        }

        form {
            margin: 0;
        }

        .inline-form {
            display: inline-block;
            margin-right: 10px;
        }

        input[type="text"] {
            padding: 6px;
            width: 180px;
        }

        button {
            padding: 6px 12px;
            cursor: pointer;
        }

        .section {
            margin-top: 30px;
        }
    </style>
</head>
<body>

<h2>User List</h2>

<table>
    <tr>
        <th>ID</th>
        <th>Username</th>
        <th>Actions</th>
    </tr>

    <#list users as user>
        <tr>
            <td>${user.id}</td>
            <td>${user.username}</td>
            <td>
                <form class="inline-form" method="post" action="/users/update">
                    <input type="hidden" name="id" value="${user.id}">
                    <input type="text" name="username" value="${user.username}" required>
                    <button type="submit">Update</button>
                </form>

                <form class="inline-form" method="post" action="/users/delete">
                    <input type="hidden" name="id" value="${user.id}">
                    <button type="submit" onclick="return confirm('Delete user ${user.username}?')">
                        Delete
                    </button>
                </form>
            </td>
        </tr>
    </#list>
</table>

<div class="section">
    <h3>Add user</h3>

    <form method="post" action="/users/create">
        <input type="text" name="username" placeholder="username" required>
        <button type="submit">Create</button>
    </form>
</div>

</body>
</html>