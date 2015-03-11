# WebServerInJavaAuthor  : Muhammad Farjad Malik
Reg No. : 02694
Class : BSCS - 2A

Advanced Programming - Lab 04 - Web Server in Java

A simple web server in java which receive a file request from client and displays it on the browser which is the client. The server runs on port 8080. And the browser can invoke it by the url "localhost:8080".

Status Codes catered for: 
200 - Request okay 
404 - File not found - 
500 - Internal server Error
400 - Bad Syntax
505 - Http Version Not supported
414 - URI too large

Requests handled:
Get requests 
and
Head requests

To receive a file placed on server from the browser you can call it using the following syntax:
localhost:8080/FileData.txt

where FileData.txt is our default file

The rest of the txt files presnt in the project are catering for and displaying the response messages in case of other status codes than 200.

If no file is specified on the client the server says hello.
