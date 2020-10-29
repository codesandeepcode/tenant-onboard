# API On-Boarding for Publisher Admin

This project consists of Spring Boot as backend service, that interact with 
PostgreSQL database

The view/frontend is developed using React.js library

## Generating war file
To create war files, ensure you have these libraries available in your classpath -
- Java 8+ (tested for Java 11)
- Maven 3.6.3+ 


### Command to produce war file

Run the below command in same location where you see pom.xml. You'll get the build 
as war file in target folder  
*mvn clean package -DskipTests -Pprod*

#### Note -
 - *'-DskipTests'* flag : for skipping broken tests till all is fixed
 - *'-Pprod'* flag : for production version (to include in react and spring boot build)