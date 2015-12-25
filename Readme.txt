
1. Installation

Frontend:
The frontend part uses npm for package management - under Windows one can simply start the provided "start_web_server.cmd" file which will automatically download all required dependencies and start a development http server. The application is the accessible under http://localhost:8000/app/#/

Backend:
Intellij has been used as IDE - one can easly use its import function to import the projects pom.xml file. The project uses Maven, so all dependencies should be automatically downloaded.

DB:
Sqlite Database file is provided as part of the project, so it can be directly used. Also it can be created from sratch using the CreateDb.sql file.


2. Current state

The application is not complete. A partial publisher web app has been provided - it allows to register an account and log in. When redirected to the landing page dummy journal data is presented which has not been coupled with the backend service. The web app uses the backend service for login and registration. When building the web app sample code from the internet has been used.

The backend service currently provides a single publisher account controller which allows for user login and registration. It stores data in Sqlite. Other controllers following the same patterns need to be added to cover the applications functional requirements. TDD aproach has been used, so unit and integration tests have been provided for the controller and corresponding services. As for known issues Cors handling needs to be refactored to take leverage of jersey built in mechnism and sqlite session handling improved as there are concurrency issues when starting all integration tests.