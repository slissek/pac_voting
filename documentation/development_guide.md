# Developer Documentation

Please have a look into the [Architecture overview](./architecture_overview.md) section to study the list of third party components / software which are used for this project.

The following sections cover the configuration of your local environment and the development process itself. 

## Setup

### Source Code Repository

The source code will be managed in Git and the repository location is:
https://github.com/slissek/pac_voting

### Java

Please install a Java JDK 1.8.x and set the *JAVA_HOME* environment variable and the *bin* directory to the path.  

### Database

Please install and connect to the PostgreSQL database as root and copy the content of the database setup script located at: */configuration/prepare_database.sql* and execute all commands. Please check if a new user: *pac_app* and a database *pac* were created. The *pac_app*-user must have the ownership of the database. The database should contain the the following tables:

![Database Model](./resources/dbmodel.tiff)

and the mentioned tables should contain these default values:

Table Name | Default values
-----------|-----------------
authoriy | 'ROLE_ADMIN' and 'ROLE_USER'
user_authority | 1;"ROLE_ADMIN" , 1;"ROLE_USER" , 2;"ROLE_USER"
users | 1;"";"admin" , "admin" 2;"test";"user", "test"

### Maven

In your maven settings file, the maven central repository should be configured.

### GIT

Please install git and provide a name and your email address in the *git config*, if not already present:

    git config --global user.name "John Doe"
    git config --global user.email johndoe@example.com

### Eclipse

Please execute the following configurations of your workspace:

#### Editors
**Text**
- Eclipse menu "Window" > "Preferences" search for "Text Editors"

    Displayed tab width: 4
    Insert spaces for tabs: checked

**XML, HTML, CSS**
- Eclipse menu "Window" > "Preferences" > "XML" > "XML Files"

    Encoding = UTF-8

- Eclipse menu "Window" > "Preferences" > "XML" > "XML Files" > "Editor"

    Line width = 144
    Indent using Spaces = Checked
    Indentation size = 4
    repeat for "HTML" and "CSS"

**Workspace**
- Eclipse menu "Window" > "Preferences" search for "workspace"

    Text file encoding: Other - UTF-8
    New text file line delimiter: Other - Windows

**Java**
- Eclipse menu "Window" > "Preferences" > "Java" > "Code Style" > "Code Templates"

    Please personalize @author tag at „Comments => Types“ (e.g. “@author <a href="mailto:sven.lissek@prodyna.com">Sven Lissek</a>, <a href="http://www.prodyna.de">PRODYNA AG</a>”)

**Save Actions**
Eclipse menu "Window" > "Preferences" search for "Save Actions" for Java > Editor enable following additional actions:
- Add 'this' qualifier to unqualified field accesses
- Add 'this' qualifier to unqualified method accesses
- Change non static accesses to static members using declaring type
- Change indirect accesses to static members to direct accesses (accesses through subtypes)
- Add final modifier to private fields
- Add final modifier to method parameters
- Add final modifier to local variables
- Remove unused imports
- Add missing '@Override' annotations
- Add missing '@Override' annotations to implementations of interface methods
- Add missing '@Deprecated' annotations
- Remove unnecessary casts
- Remove unnecessary '$NON-NLS$' tags
- Remove trailing white spaces on all lines
- Correct indentation

**Templates**

Within the workspace, a folder configuration is provided which contains formatter files. Import them as follows:
- Eclipse menu "Window" > "Preferences" > "Java" > "Code Style" > "Formatter"
- Eclipse menu "Window" > "Preferences" > "JavaScript" > "Code Style" > "Formatter"

## Development

The following sections should introduce you to the development approaches and describe the structure of the project.

### Definition of done

An enhancement of the project is **done** if the following rules are fulfilled:
* the code compiles
* the code is commented
* tests are created or adjusted
* all tests passing
* the build finishes successfully
* this documentation is updated 

### Server

### Client

To become familiar with the client application, it is recommended to read the following guides and concepts first before you start to develop new features.
* The AngularJs frontend follows the [AngularJS style guide](https://github.com/johnpapa/angular-styleguide/blob/master/a1/README.md). This guide is endorsed by the AngularJS Team, and gives the guarantee to have a clear update path to AngularJS 2.
* The client uses (angular-ui-router)[https://angular-ui.github.io/ui-router/] to add routing to the application.
* Alerts and progress bars from (UI Bootstrap)[https://angular-ui.github.io/bootstrap/] are used in the client

## Tests

### Server

Tests are done with the Spring Test Context framework, and are located in the *src/test/java* folder. It is recommended that you have a test class for every rest resource, service and converter. To be able to focus the test on the class implementation, it is best practice to use [Mockito](http://mockito.org) to mock e.g. a service or a repository.



### Client

Tests for the frontend are still on the TODO list, therefore we could not provide much information here.