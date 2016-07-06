# Architecture

This page should give you on overview about the used technology stack and the architecture of the application and database. 

## Technology stack
- Java JDK 1.8.0
- Eclipse Mars 
- Maven 3.3.9
- Postgres 9.5.2.1
- GIT 2.8.0
- Source Tree (optional)
- NodeJS 5.10
- Bower 1.7.9
- SpringBoot 1.3.5
- Tomcat (embedded)
- AngularJS 1.5.2

## Overview

The application is split into a classical three tier architecture consisting of a database for persistence, a server and a client.

### Database

![Database Model](./resources/dbmodel.tiff)

#### Tables

* Users - a concrete user with username and password to sign in
* Authority - a role which can be assigend to a user
* User_Authority - the n:m mapping table for the assigned roles for a userId
* Persistent_token - stores the created tokens for a user
* Vote - a concrete vote with a topic
* Vote_Options - vote options for a vote
* User_Votings - stores user votes for a concrete vote and the chosen option

### Server

The server was created as a Spring Boot Application which implements the business logic and managed the persistent layer. The integrated role concept prevent manipulations on entities, there the user has no permissions on. A REST interface is provided to all potential customers to create / read / update / delete the entities, handles authentication and authorization. Each of the mentioned sections are managed in own packages to ensure a clean separation and easy expandability with additional functional extensions.

### Client

