# Notes Application
### Notes Application with HTTP Basic Authentication
- User can perform CRUD operations on Notes
- Only owner of the notes can perform CRUD operations
- One use can have multiple notes
- Application can run on tomcat server as well and using docker as well

### Steps to run stand-alone application
- Install postgres database and create user_notes database
- Run spring boot application from Main class
- Create user and perform CRUD operations on notes

### Steps to run application inside docker
- Install docker inside your machine
- Open terminal/ command prompt inside your application where Dockerfile and docker-compose.yml is placed 
- Run following command to run your application
  - docker-compose up --build
- Create user and perform CRUD operations on notes

### Find the cURL commands for creating user and perform CRUD operations on notes.

#### Create User

curl --location 'http://localhost:8080/api/users' \
--header 'Content-Type: application/json' \
--data-raw '{
"email": "dhrupal.prajapati@gmail.com",
"password": "123456789"
}'

#### Create Note

curl --location 'http://localhost:8080/api/notes' \
--header 'Content-Type: application/json' \
--header 'Authorization: Basic ZGhydXBhbC5wcmFqYXBhdGlAZ21haWwuY29tOjEyMzQ1Njc4OQ==' \
--data '{
"title": "Create CRUD For notes application",
"note": "Implement basic auth, crud for notes, provide basic validations."
}'

#### Update Note

curl --location --request PUT 'http://localhost:8080/api/notes/1' \
--header 'Content-Type: application/json' \
--header 'Authorization: Basic ZGhydXBhbC5wcmFqYXBhdGlAZ21haWwuY29tOjEyMzQ1Njc4OQ==' \
--data '{
"title": "Create CRUD For notes application",
"note": "Implement basic auth, crud for notes, provide basic validations, create one user by default."
}'

#### Get Note

curl --location 'http://localhost:8080/api/notes/1' \
--header 'Authorization: Basic ZGhydXBhbC5wcmFqYXBhdGlAZ21haWwuY29tOjEyMzQ1Njc4OQ=='

#### Get All Notes

curl --location 'http://localhost:8080/api/notes' \
--header 'Authorization: Basic ZGhydXBhbC5wcmFqYXBhdGlAZ21haWwuY29tOjEyMzQ1Njc4OQ==' \
--header 'Cookie: JSESSIONID=A5B9BB22DAC02AC7DC355A5ABCE2F6C5; JSESSIONID=A5B9BB22DAC02AC7DC355A5ABCE2F6C5'

#### Delete Note

curl --location --request DELETE 'http://localhost:8080/api/notes/1' \
--header 'Content-Type: application/json' \
--header 'Authorization: Basic ZGhydXBhbC5wcmFqYXBhdGlAZ21haWwuY29tOjEyMzQ1Njc4OQ=='