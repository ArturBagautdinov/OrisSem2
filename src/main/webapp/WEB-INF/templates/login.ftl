<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Вход</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background: #f5f5f5;
            margin: 0;
            padding: 0;
        }

        .container {
            width: 360px;
            margin: 80px auto;
            background: white;
            padding: 30px;
            border-radius: 12px;
            box-shadow: 0 4px 16px rgba(0, 0, 0, 0.1);
        }

        h1 {
            margin-top: 0;
            text-align: center;
        }

        form {
            display: flex;
            flex-direction: column;
            gap: 12px;
        }

        input {
            padding: 12px;
            font-size: 14px;
            border: 1px solid #ccc;
            border-radius: 8px;
        }

        button {
            padding: 12px;
            font-size: 14px;
            border: none;
            border-radius: 8px;
            background: #2d6cdf;
            color: white;
            cursor: pointer;
        }

        button:hover {
            background: #1f57b8;
        }

        .message {
            margin-bottom: 15px;
            padding: 10px;
            border-radius: 8px;
            font-size: 14px;
        }

        .error {
            background: #ffe5e5;
            color: #b30000;
        }

        .success {
            background: #e7f8ea;
            color: #1f7a1f;
        }

        .link {
            margin-top: 16px;
            text-align: center;
        }

        .link a {
            color: #2d6cdf;
            text-decoration: none;
        }

        .link a:hover {
            text-decoration: underline;
        }
    </style>
</head>
<body>
<div class="container">
    <h1>Вход</h1>

    <#if error??>
        <div class="message error">${error}</div>
    </#if>

    <#if success??>
        <div class="message success">${success}</div>
    </#if>

    <#if RequestParameters.error??>
        <div class="message error">Неверный логин или пароль</div>
    </#if>

    <#if RequestParameters.logout??>
        <div class="message success">Вы успешно вышли из аккаунта</div>
    </#if>

    <form action="/login" method="post">
        <input type="text" name="username" placeholder="Логин" required>
        <input type="password" name="password" placeholder="Пароль" required>
        <button type="submit">Войти</button>
    </form>

    <div class="link">
        <a href="/register">Нет аккаунта? Зарегистрироваться</a>
    </div>
</div>
</body>
</html>