<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>

	<meta charset="utf-8">
    <meta http-equiv="x-ua-compatible" content="ie=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="resources/css/foundation.css">
    <link rel="stylesheet" href="resources/css/app.css">

	<script src="resources/js/vendor/jquery.js"></script>
    <script src="resources/js/vendor/what-input.js"></script>
    <script src="resources/js/vendor/foundation.js"></script>
    <script src="resources/js/app.js"></script>

	<link href="https://fonts.googleapis.com/css?family=Sanchez" rel="stylesheet">

	<title>
		Middlesex Clubs
	</title>

</head>

<body>

	<h1>Middlesex Clubs</h1>

	<h4 id="pageOne">

		<form action="action_page.php">
			<div class="container">
				<label><b>Email</b></label>
				<input type="text" placeholder="Enter Email" name="email" required>
				<label><b>Password</b></label>
				<input type="password" placeholder="Enter Password" name="psw" required>
				<button type="submit">Login</button>
			</div>
			<div class="container">
				<span class="psw">Forgot <a href="#">password?</a></span>
			</div>

		</form>
	</h4>
</body>

</html>