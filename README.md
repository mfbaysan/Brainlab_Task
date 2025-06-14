A tiny Spring Boot app that does basic math on a list of numbers you send it—addition, multiplication, average, and (population) standard deviation—all powered by Java’s BigDecimal for rock-solid precision.

1- Clone the repo
git clone https://github.com/yourusername/calculator-service.git
cd calculator-service
Build & test (requires Maven):

2- mvn clean install
mvn test

3- Run the service:

In IntelliJ IDEA Ultimate 2025.1 (the IDE of my choice when developing):

 1.1 Open the project folder.

 1.2 Let it import the Maven settings.

 1.3 Run from interface
 
On the command line:

 mvn spring-boot:run
 
4- Try it out in your browser or with curl:

http://localhost:8080/calculator/add?operands=1.2,3.4,5


What’s Inside:

Java 17+ & Spring Boot 3.x
Maven for building (pom.xml defines all dependencies)
SLF4J + Logback for logging
All math uses BigDecimal with MathContext.DECIMAL128 so you never lose precision
A simple global exception handler to turn bad inputs into clean 400 errors

Tests:

Parsing valid & invalid inputs
All four operations logically(add, multiply, average, stdev)
Error cases (missing or bad operands)

Run them with:

mvn test
