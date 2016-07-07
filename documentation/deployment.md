# Deployment and Production usage

## Monitoring

The application comes with full monitoring support from Metrics. The application send all the information to a Graphite server, which is configured in the YAML configuration file.

### Configuration

We want to use the "production" profile in order to build and package our application. To do so, use the pre-configured "prod" Maven profile:

    ./mvnw -Pprod

This profile will compile, test and package the application with all productions settings.
Please notice [Open Points](./open.md)

### Generating a WAR file
To package the application as a "production" WAR, type:

    ./mvnw -Pprod package

This will generate two files:

    target/voting-1.0.0.war
    target/voting-1.0.0.war.original

The first one is an executable WAR file (see next section to run it). It can also be deployed on an application server, but as it includes the Tomcat runtime libs, it will probably produce some warnings, that's why we recommend you use the second, ".original" file if you want to deploy the VotingApp on an application server.

Please note that when building a WAR file with the "prod" profile, the generated archive will not include the "dev" assets! This means that you will have to explicitly set the "prod" profile when running or deploying the WAR file, as well, because otherwise you will end up with an odd combination of production front-end with development back-end. 
There are several ways to trigger a Spring profile, for example you can add -Dspring.profiles.active=prod to your JAVA_OPTS when running your server.

### Executing the WAR file without an application server
Instead of deploying on an application server, we also provide an exectuable WAR file (which includes Tomcat 8, in fact).

The first WAR file generated in the previous step is such a WAR, so you can run it in "production" mode by typing:

    ./voting-1.0.0.war --spring.profiles.active=prod 

If you are on Windows, use: 
    java -jar voting-1.0.0.war --spring.profiles.active=prod

Please note that we use of the Spring "prod" profile, as explained in the previous section.