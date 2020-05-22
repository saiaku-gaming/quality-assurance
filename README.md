### Quality Assurance Service

This service main purpose is to handle automated feedback from the game valhalla.

Kotlin with spring boot is serving the backend. The frontend is using vuejs for rendering any graphical stuff needed.
It has a postgres database for storing and tracking QA issues. 
Files will be stored at a temp folder while testing and on /data path (later mounted at /opt/qa at gungnir)

## Develop

For developing, start the backend from QaApplication.kt and the frontend from the frontend folder with
```npm install``` and then ```npm run serve```

When started with the prod profile it uses spring boot oauth2 client to ensure you are logged in when 
accessing frontend code.

## Deploy

Pushing the master branch will deploy this to gungnir. 

The process is as follows:

Jenkins will use the Jenkinsfile as the build definition.

It will in turn build with docker using the Dockerfile

Once built it will run the docker-compose.yml to make sure everything is up and running. 