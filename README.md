# Java-Server-Client-Chat
Multiclients chat application

<h3>Synopsis </h3>
<p>Application is written in Java and it uses concepts of Java multithreading and Java networking. It enables up to 10 clients to communicate with the possibility to send personal messages, messages that only sender and reciever can see, no matter how many clients exists. Server must be created first to enable connecting of clients.</p>

<h3>Reference </h3>
<p>Introduction to Java Programming - COMPREHENSIVE VERSION Eight Edition, Y. Daniel Liang

<h3>Tests </h3>
<p>The application can be run from the command line with specifying the argument which will be used as a port number. First, server must be created. If server is not created clients can not establish connections. If port is not specified from the command line, application uses default port. HomeScreen.java class is class that is calling corresponding classes - Server.java(class that handles server and client connections) if the user Sign In as "Server", and Chat.java if user Sign In as "Client". Chat.java and Client.java together are handling the communication between clients. If client enters /quit message, client will be out of the chat room. For personal messages use '@' character in front of the name of client you want to send the message.</p>
<p>Youtube video soon...</p>
