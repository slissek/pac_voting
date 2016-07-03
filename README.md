# Voting Application

This documentation should help to setup all necessary third party components to run the application.

## Developer Documentation

Third party components

To be able to run the application, we need the following software:
- Java JDK 1.8.0
- Eclipse Mars 
- Maven 3.3.9
- Postgres 9.5.2.1
- GIT 2.8.0
- Source Tree (optional)
- NodeJS 5.10
- Bower 1.7.9

### Database

Please connect to the PostgreSQL database as admin and copy the content of the database setup script: /configuration/prepare_database.sql and execute all commands. Please check if a new user: pac_app and a database pac was created. The user should be the owner of the database. The database should contain the the following tables:

Table Name | Default values
-----------|-----------------
authoriy | 'ROLE_ADMIN' and 'ROLE_USER'
user_authority | 1;"ROLE_ADMIN" , 1;"ROLE_USER" , 2;"ROLE_USER"
user_votings | none
users | 1;"";"admin" , "admin" 2;"test";"user", "test"
vote | none
vote_options | none

### Maven

In your maven settings file, the maven central repository should be configured.

### Eclipse

Please execute the following configurations of your workspace:

#### Editors
Text
Eclipse menu "Window" > "Preferences" search for "Text Editors"
Displayed tab width: 4
Insert spaces for tabs: checked

#### XML, HTML, CSS
Eclipse menu "Window" > "Preferences" > "XML" > "XML Files"
Encoding = UTF-8
Eclipse menu "Window" > "Preferences" > "XML" > "XML Files" > "Editor"
Line width = 144
Indent using Spaces = Checked
Indentation size = 4
repeat for "HTML" and "CSS"

#### Workspace
Eclipse menu "Window" > "Preferences" search for "workspace" 
Text file encoding: Other - UTF-8
New text file line delimiter: Other - Windows

#### Java
Eclipse menu "Window" > "Preferences" > "Java" > "Code Style" > "Code Templates"
Please personalize @author tag at „Comments => Types“ (e.g. “@author <a href="mailto:sven.lissek@prodyna.com">Sven Lissek</a>, <a href="http://www.prodyna.de">PRODYNA AG</a>”)

#### Save Actions
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

#### Templates

Within the workspace, a folder configuration is provided which contains formatter files. Import them as follows:
Eclipse menu "Window" > "Preferences" > "Java" > "Code Style" > "Formatter"
Eclipse menu "Window" > "Preferences" > "JavaScript" > "Code Style" > "Formatter"

## Run the application

Navigate to the git-clone directory and run
```
mvn clean install
bower install
```

