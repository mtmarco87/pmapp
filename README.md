# Projects Management App
This application is a simple Project Management tool built in Java and React. 
The app features user authentication through JWT tokens and stateless server session, exposes RESTful APIs protected by RBAC, and allows managing of Projects, Tasks and Users through a FE web application.

# Prerequisites

In order to successfully use/test this application you need the following software:

- [Java](https://www.java.com/en/download/) - version 8 or above
- [Node](https://nodejs.org/it/download/) - LTS version
- [Yarn](https://yarnpkg.com/lang/en/docs/install/) - optional (the scripts, the FE project, and lockfile have been built with it, but you may use NPM)
- [Git](https://git-scm.com/) 
- [Docker](https://www.docker.com/get-started)

# Getting Started
## Create and run the application
Clone this repository and make the 'start.sh' script executable:
```bash
git clone https://github.com/mtmarco87/pmapp.git
cd pmapp
chmod +x ./start.sh
```

Launch the 'start.sh' script to automatically install deps, test, build, and run the application:
```bash
./start.sh
```
Wait a couple of minutes the process to finalize. If everything goes well, a browser window should automatically open up pointing to the [PmApp FE homepage](http://localhost:3000).

If you have trouble with the script or you want to manually install and build the app, here are the needed steps:
```bash
#1) BE deps install, test, build and docker image creation
cd pmapp-be
./mvnw package
docker build -t ricardo/pmapp-be .

#1) FE deps install, test, build and docker image creation
cd ../pmapp-fe
yarn install
yarn test --watchAll=false
yarn build
docker build -t ricardo/pmapp-fe .

#3) Run Docker Compose
cd ..
docker-compose up -d
```

## Testing the application
The application launches an InitScript at startup time, that creates an initial database structure and some test data.

Hence, you can access the web application at http://localhost:3000/ using a pre-configured user profile to login:
    - Admin profile => Username: dev , Password: Test123
    - Project Manager profile => Username: projman , Password: Test123
    - Admin profile => Username: admin , Password: Test123

Furthermore a Swagger API Documentation is generated by the BE application, and you can access it at the following link:

```bash
http://localhost:8080/swagger-ui.htm
```

From here you can test all the APIs. Anyway, since the application needs authentication, you will first need to execute a POST call to the login endpoint with valid credentials to get a JWT token:
```bash
http://localhost:8080/auth/login
```

The response of this API will contain a valid access token that you can use from now on in the Authorization header of your requests as a Bearer token.

Usage example with curl:
```bash
curl -X GET "http://localhost:8080/user/all" -H "accept: */*" -H "Authorization: Bearer jwt_token_here"
```

Or you can put it in Swagger clicking on the Authorize button prepending the word Bearer to the token (Bearer yourToken).

## Accessing to the DB
A simple and lightweight UI has been provided to access the DB data. Access the following link with your browser:

```bash
http://localhost:9000/
```

Use the following credentials to log-in:
- System: PostgreSQL
- Server: db
- User: postgres
- Password: pmapp
- Database: pmapp

In case you want a more advanced experience to access the database, you may download [PgAdmin](https://www.pgadmin.org/download/).

# Improvements
The application has been developed for educational purposes, so some important points haven't been addressed and could still be improved, such as:

- Add a proper Java/TypeScript documentation: classes have not been always commented
- Increase test coverage: usually a good software has at least 80%/90% of testing coverage
- Add meaningful logging: it is quite hard to find out what it is going on in the application if something goes wrong
- Add more FE validation: more front-end side validations can be done, to save some unnecessary api call to the back-end relying on the back-end side validation
- Improve BE validation: a cleaner validation layer may be put in place for the REST API models
- Improve usage of Redux store: for the moment the Redux store have been only used for the Session/application state, essentialy to store logged in user information and to dispatch notifications/errors through the application. It can be also used to manage other application data
- Improve UI
- Avoid hardcoded credentials/endpoints in repository: these information may be fetched from the environment variables, to prepare the application to CI/CD 
- Security: for an improved application security in production it would be better to serve the application in HTTPS (i.e.: to avoid man in the middle attacks)
- OAuth2: a modern OAuth2 login system may be put in place. The back-end is ready for OAuth2 support, and small work need to be done on the FE to support it. Also an OAuth2 provider account must be created and the BE configuration must be updated accordingly
- Server-side paginated/optimized API calls: the API calls feeding Projects/Taks/Users views are for now retrieving extra unneeded data in some occasions. For sure an optimization may be done in this sense, and also server side pagination may be implemented
