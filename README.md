# JavaServer
A Server made in Java with as few external dependencies as possible

Dependencies used

Postgres Java Connector - To connect to Postgres: https://jdbc.postgresql.org/

Java-dotenv - To hide my Postgresql username and password: https://github.com/cdimascio/java-dotenv

Classes: 

Server - main class mostly used to tell the server which routes to accept

Router - actually directing the request and sending either html/css/javascript/json and creating threads to interact with the database

Database - the part where we interact with the database by using functions called based on the API endpoint with the /users prefix

JSONObject - used to turn Strings into JSON objects and back into JSON strings

Database used: PostgresSQL


To compile and run:

mvn clean compile

mvn clean package

java -jar target/JavaServer-1.0.jar
