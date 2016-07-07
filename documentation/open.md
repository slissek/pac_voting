# Open points and road map

Due to some time constraints, not all planed task could be accomplished until today. We would like to list some points, which are necessary to go on with the project.

## Javascript Build

Create a build process with Gulp for all static resources (CSS, JavaScript, HTML, JavaScript, images...) in order to generate an optimized client-side application for production mode.

## Profiles

The application currently run in development mode. Everything is prepared to add a *dev* and *prod* profile. The configuration values within the *application.yml* should be split into static and profile specific groups and *application-dev.yml* and *application-prod.yml* should be added.

## Splitting development artifacts

Currently, the client and the server code artifacts are both in the back-end SpringBoot code. For the initial development purpose, this setup was much easier to handle and work on. If the team grows and multiple developer work on the different parts of the application, the front- and back-end code artifacts should be split into own projects. 

## UI based tests

Add a testframework like Karma to establish user front-end testing and add test cases.

## Adding a code coverage tool

Understanding the test code coverage and optimize your code style by using a code coverage tool like sonar. Integrate it with its maven plugin into the maven build.