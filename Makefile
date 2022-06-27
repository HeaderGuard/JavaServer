JavaServer:
	mvn clean package
regular:
	mvn package
run:
	make regular
	java -jar target/JavaServer-1.0.jar
