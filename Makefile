JavaServer:
	mvn clean compile
	mvn clean package
regular:
	mvn compile
	mvn package
run:
	make regular
	java -jar target/JavaServer-1.0.jar
