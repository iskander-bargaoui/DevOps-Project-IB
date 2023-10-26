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
                    withMaven(jdk: 'JDK8') {
                        sh "mvn compile"
                    }
                }
            }
        }
        stage('Building Back Application') {
            steps {
                script {
                    // Stage 3: Run tests
                    withMaven(jdk: 'JDK8') {
                        sh "mvn test"
                    }
                }
            }
        }
        stage('Build') {
            steps {
                script {
                    // Stage 4: Build the application
                    withMaven(jdk: 'JDK8') {
                        sh "mvn package"
                    }
                }
            }
        }
    }
}
