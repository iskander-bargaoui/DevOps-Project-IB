pipeline {
    agent any
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
        }
        stage('Building Back Application') {
            steps {
                script {
                    // Stage 3: Run tests
                        sh "mvn test"
                    }
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
        }
        stage('SonarQube Analysis') {
            tools{
                    jdk 'java11.1'
                }
            steps {
                script {
                    // Stage 5: Execute SonarQube analysis with Java 11
                    
                        sh 'mvn sonar:sonar'
                    }
                }
            }
        }
    }
    tools {
        jdk 'java1.8'
    }
}
