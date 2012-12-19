Moonscapes
==========

Multiplayer Thrust-alike game in Java, from years ago, with primitive Java (1.3?), but still compiles and runs!

In fact, the main focus in this project was implementing the Netplay library/framework for writing client-server programs in java. The game was just a demonstration.

Game graphics are bit clunky, because hardware accelerated graphics in Java was missing way back then.

Some Netplay library features:
* Pure java
* Multithreaded
* TCP and UDP sockets
* Abstracts sockets away and gives programmer serializable Java classes/objets to play with


Compile with: 

    ant jar

Run server:

    java -jar msserver.jar

Run client:

    java -jar msclient.jar
  
    java -jar msclient.jar -help
  
    java -jar msclient.jar -host <hostname> -name MyNick
    
Control in game:

    A - Turn left
    S - Turn right 
    Right shift - Thrust
    Enter - Fire
    T - Chat
