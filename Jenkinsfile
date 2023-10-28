pipeline {
    agent any
    environment {
        DOCKERHUB_USERNAME = "iskander20"
        TAG = "${DOCKERHUB_USERNAME}/devopsproject:backapp"
    }
    tools {
        jdk 'java1.8'
    }
    stages {
        stage('SCM Checkout') {
            steps {
                script {
                    // Stage 1: Git Checkout
                    checkout([$class: 'GitSCM',
                        branches: [[name: 'Back']],
                        userRemoteConfigs: [[
                            url: 'https://github.com/iskander-bargaoui/DevOps-Project-IB.git',
                            credentialsId: 'GithubJenkinsToken'
                        ]]
                    ])
                }
            }
        }
        stage('Compile') {
            steps {
                script {
                    // Stage 2: Compile the project into a .jar file
                    sh "mvn compile"
                }
            }
        }
        stage('Building Back Application') {
            steps {
                script {
                    // Stage 3: Run tests
                    sh "mvn test"
                }
            }
        }
        stage('Build') {
            steps {
                script {
                    // Stage 4: Build the application
                    sh "mvn package"
                }
            }
        }
        stage('SonarQube Analysis') {
            tools {
                jdk 'java11.1'
            }
            steps {
                withSonarQubeEnv('SonarQube') {
                    sh 'mvn sonar:sonar'
                }
            }
        }
        // Dockerhub
         stage('Docker Build Image') {
            steps {
                    sh 'chmod +777 /var/run/docker.sock'
                    sh 'docker build -t $TAG .'
            }
        }
         stage('Docker Login') {
      steps {
        withCredentials([usernamePassword(credentialsId: 'DockerHubCreds', usernameVariable: 'DOCKERHUB_USERNAME', passwordVariable: 'DOCKERHUB_PASSWORD')]) {
          sh 'docker login -u $DOCKERHUB_USERNAME -p $DOCKERHUB_PASSWORD'
        }
      }
    }
        stage('Docker Push') {
            steps {
                    sh 'docker push $TAG'
            }
        }

        // Nexus + Front + Grafan prom

    }
}

