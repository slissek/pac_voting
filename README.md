# Voting Application

This application was created for the PRODYNA Architecture certification exercise.

Within the application you are able to create votes and vote other votings.

## Further information

For a better overview, further information are splitted into the following sub-topics:

[Architecture overview](./documentation/architecture_overview.md)  
[Development guide](./documentation/development_guide.md)  
[Release Management](./documentation/release_management.md)  
[Deployment and environment](./documentation/deployment.md)  
[Open points and roadmap](./documentation/open.md)  

## Run the application

### Maven startup

Navigate to the git-clone directory and run
```
mvn clean install
bower install
```
After the builds completed successfully you could easily launch the application by calling:

```
mvn
```
which is mapped to our default Maven task, spring-boot:run.

The application prints its access urls to the console:

    Application 'slissek-pac' is running! Access URLs:
    Local:      http://127.0.0.1:8080
    External:   http://192.168.178.20:8080

### IDE startup

From your IDE, right-click on the "Application" class at the root of your Java package hierarchy, and run it directly. You should also be able to debug it as easily.

The application will be available on http://localhost:8080.