# restro_reservation
Restaurant Reservation Management System

### Running Application Procedure

### Repo Link To Clone
~~~~
https://github.com/Pritamthing/restro_reservation.git
~~~~
###JDK
Min JDK 17 or above

#### Git branch
1. Checkout to master branch

###Configuration
1. goto `application.properties` file and update the database configuration
~~~~
spring.application.name=Restaurant Management System
spring.datasource.url=jdbc:mysql://localhost:3306/your_db_name
spring.datasource.username=mysqldbuser
spring.datasource.password=mysqldbuserpassword

#spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect
spring.jpa.hibernate.ddl-auto=update
~~~~
Or want to use H2 DB? use this config 
~~~~
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=password
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
~~~~
And add the H2 DB driver dependency in `pom.xml`
~~~~
<dependency>
    <groupId>com.h2database</groupId>
    <artifactId>h2</artifactId>
    <scope>runtime</scope>
</dependency>
~~~~

###Running Application
###### Go to `RestaurantManagementSystemApplication` right click and click run

After successfully running application open the browser and paste url 
```http://localhost:8080```

###Sign up & Login
1. Create a user and login
2. Start using app
