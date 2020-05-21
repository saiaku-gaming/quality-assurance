pipeline {
    agent {
        node {
            label 'gungnir'
        }
    }
    stages {
        stage('Build'){
            steps {
                script {
                    docker.build("valhalla-qa")
                }
            }
        }
        stage('Deploy'){
            environment {
                SPRING_PROFILES_ACTIVE="prod"
                SPRING_SECURITY_OAUTH2_CLIENT_REGISTRATION_GITHUB_CLIENT_SECRET=credentials('github-oauth-valhalla-qa')
            }
            steps {
                script {
                    sh 'docker-compose up -d'
                }
            }
        }
    }
}