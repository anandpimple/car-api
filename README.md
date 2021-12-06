# car-api
## Overview
The car api provide functionality to add, retrieve and delete cars through various endpoints. The details about endpoints published as swagger 2 documentation and can be be accessed on url http://localhost:8080/v2/api-docs?group=internal once application has been started.

Please note that currently the the api is secured using spring security basic authentication
## Guidelines
1. This is gradle application, and one can import this project in IDE as gradle project ( Gradle 6.8.x +). 
2. After importing project in IDE, we can build and run the application. 
3. Alternativly, we can clone the project and then, on command line go to project root. Then on comand line, run command 'gradle clean build' (Gradle 6.8.x+ should be installed on your system). Then, go to the build/lib library and run 'java -jar car-api-'1.0-SNAPSHOT'.jar'. 
4. If application is running succesfully on system, then accessing 'http://localhost:8080/v2/api-docs?group=internal' whould show a swagger documentation for API.
5. To access api's, we required to provide Authorization header to the request with basic authentication details (i.e. Authorization: Basic Y2FyLWFwaTpwYXNzdzByZA==) or use username 'car-api' and password 'passw0rd' when promted while calling api.

## Notes
Uses in memory DB and currently the data get reset when we starts the application.
