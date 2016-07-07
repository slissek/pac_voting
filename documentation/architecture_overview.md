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

## Security

### Role concept

The database script will add some default users and roles which will be shortly introduced here:

* "admin" is an admin user, with the "ROLE_USER" and "ROLE_ADMIN" authorizations.
* "system" is used for auditing purposes, when an action is done automatically (the action is done by the system, not by a user). You should not be able to login with this user. It has the "ROLE_USER" and "ROLE_ADMIN" authorizations.
* "user" is a standard user, with the "ROLE_USER" authorization.
* "anonymousUser" is for non-authenticated users, so it doesn't have any authorization. This user can be useful for some Spring Security configurations, but we don't use it by default.

This concept can easily enhanced for business related roles in the future.

### Password handling

### Authentication

This is the "classical" Spring Security authentication mechanism, but we have improved it quite significantly. It uses the HTTP Session, so it is a stateful mechanism: if you plan to scale your application on multiple servers, you need to have a load balancer with sticky sessions so that each user stays on the same server.

#### Improved remember-me mechanism

We have modified the Spring Security remember-me mechanism so that you have a unique token, that is stored in your database. We also store more information than the standard implementation, so you have a better understanding of where those tokens come from: IP address, browser, date.

#### Cookie theft protection

We have added a very complete cookie theft protection mechanism: we store your security information in a cookie, as well as in the database, and each time a user logs in we modify those values and check if they have been altered. That way, if a user ever steals your cookie, he will be able to use only once, at most.

#### CSRF protection

Spring Security and AngularJS both have CSRF protection out-of-the-box, but unfortunately they don't use the same cookies or HTTP headers! In practice, you have in fact no protection at all for CSRF attacks. We re-configured both tools so that they correctly work together.