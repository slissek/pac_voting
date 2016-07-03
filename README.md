# Voting Application

This documentation should help to setup all necessary third party components to run the application.

## Developer Documentation

### Third party components

To be able to run the application, we need the following software:
- Java 1.8
- PostgreSQL
- Maven

#### Database

Please connect to the PostgreSQL database as admin and copy the content of the database setup script: /configuration/prepare_database.sql and execute all commands. Please check if a new user: pac_app and a database pac was created. The user should be the owner of the database. The database should contain the the following tables:

Table Name | Default values
-----------|-----------------
authoriy | 'ROLE_ADMIN' and 'ROLE_USER'
user_authority | 1;"ROLE_ADMIN" , 1;"ROLE_USER" , 2;"ROLE_USER"
user_votings | none
users | 1;"";"admin" , "admin" 2;"test";"user", "test"
vote | none
vote_options | none

#### Maven

In your maven settings file, the maven central repository should be configured.