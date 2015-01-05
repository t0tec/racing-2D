# This is the main part of the project.

## This project is divided in 5 submodules

1. racingAPI: this is the REST API using Jersey Framework 1.18, Jersey is built upon 
the JAX-RS API. Jersey provides it's own API which has additional features and 
utilities. We make use of the JSON media type for our data representation.

2. racingConfig: this is a module for storing Configuration regarding the Spring 
framework and JDBC credentials

3. racingDomain: this is our Domain Layer where we get and put (persitence) our data 
to the MySQL database. It makes use of JDBC and is divided in separate Data Access Objects. 

4. racingUtil: this can be used to store constants, enums and other common classes 
that are used throughout the project

5. racingWeb: This is the client web application where the users will be logging in to 
create circuits, also new users can register. This is a Java JSP & Servlet based 
web application

* To start the web application you can simply deploy this on your local tomcat server
* Test user: *username:t0tec *password:t0tec

