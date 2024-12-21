Multi-thread Web Server
=============

Installation and execution
------------
After Extracing the files, to start the program and use, remember to change the IP address according to the WIFI the user is going to use. For example: The POLYU WLAN IP address in Hung Hom Hall is 172.16.141.128 so when I am using the server, I set the IP address for the program to 172.16.141.128 which my other devices can also access the server when running the server.
Also, remember to add the files into the 'files' folder users would like to search for inside the file, for example: If the user would like to search for 1.txt using the server and would like to receive a OK response, a file named 1.txt should be created in the 'files' folder for the server to search for.
To execute the program, users can immediately run the Server program and then choose to execute the Client program or use the browser/website bar to search.

REMEMBER, IF THE USER CHOOSE TO USE Client.py for executing, CHANGE THE IP ADDRESS ACCORDING TO THE WLAN THE USER IS USING SAME TO THE SERVER 
Responses for browser will be shown on server and responses for client will be shown on client page.

For Client programs run python Client.py in the command line at the location of the client file, then enter 
command for GET: GET / filename.extension 
command for HEAD: HEAD / filename.extension

For website/browser search: Enter http://IP address/filename.extension

