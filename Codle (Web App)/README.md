# Introduction


Codle is a clone of the popular word guessing game [Wordle](https://www.nytimes.com/games/wordle/index.html) but with a twist.

This project makes use of:
* [Javalin](https://javalin.io/)
* [Thymeleaf](https://www.thymeleaf.org/)
* [SQLite](https://sqlite.org/index.html)
* [EoDSQL](http://eodsql.sourceforge.net/)
* [jQuery](https://jquery.com/)


# Installation


**Note that [Maven](https://maven.apache.org/) and [JDK 17](https://www.oracle.com/java/technologies/downloads) is required to compile and execute the server package.**

Once all necessary prerequisites are installed and you have navigated to the project's root directory `codle` run the following commands:

`mvn clean install`

This will test, compile, and package the project.

`java -jar target/codle-*-SNAPSHOT.jar `

This will run the server.

Then, open up your browser and navigate to `localhost:5000`
