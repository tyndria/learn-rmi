# university

Learning RMI technology

1. Add public interface Constants to the package wedding.database with the final string fields DB_URL, USER, PASS;
2. Add public interface NetworkConstants to the package wedding with the final string fields HOST, APP_PORT, REGISTRY_PORT;
*** To the field HOST you should write your EXTERNAL IP
3. Don't forget to write THIS EXTERNAL IP to file java.policy (common path: C:\Program Files\Java\jre1.8.0_101\lib\security or C:\Program Files\Java\jdk1.8.0_101\jre\lib\security)
Example: 
     permission java.net.SocketPermission "158.129.230.252:1099", "connect,resolve"; (port for app)
	   permission java.net.SocketPermission "158.129.230.252:3090", "connect,resolve"; (port for registry)

