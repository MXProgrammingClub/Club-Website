<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@ page import="java.io.*,java.util.*,javax.mail.*"%>
<%@ page import="javax.mail.internet.*,javax.activation.*"%>
<%@ page import="javax.servlet.http.*,javax.servlet.*" %>
<%
	//Adapted from https://www.tutorialspoint.com/jsp/jsp_sending_email.htm
	//with gmail help from http://stackoverflow.com/questions/15597616/sending-email-via-gmail-smtp-server-in-java

	//To do: Change to sender and email list
	String to = "juliamcclellan37@gmail.com", from = "prrpapm@gmail.com";
	
	Properties props = System.getProperties();
	props.put("mail.smtp.host", "smtp.gmail.com");
	props.put("mail.smtp.socketFactory.port", "465");
	props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
	props.put("mail.smtp.auth", "true");
	props.put("mail.smtp.port", "465");
	
	Session mailSession = Session.getInstance(props, new javax.mail.Authenticator()
	{
		protected PasswordAuthentication getPasswordAuthentication()
		{
			return new PasswordAuthentication("prrpapm", "mxclubwebsite");
		}
	});

	boolean sent = false;
	try
	{
		MimeMessage message = new MimeMessage(mailSession);
		message.setFrom(new InternetAddress(from));
		message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
		
		//Gets information from form used to create email
		message.setSubject(request.getParameter("subject"));
		message.setText(request.getParameter("body"));

		Transport.send(message);
		sent = true;
		}
	catch (MessagingException mex) {}
	
	String referer = request.getHeader("Referer");
	response.sendRedirect(referer);
%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
<center>
<h1>Send Email using JSP</h1>
</center>
<p align="center">
<% 
   out.println("Result: " + sent + "\n");
%>
</p>
</body>
</html>