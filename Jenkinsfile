pipeline {
    agent any
    environment {
        JDK8 = tool name: 'java1.8', type: 'jdk'
        JDK11 = tool name: 'java11.1', type: 'jdk'
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
                    withEnv(["JAVA_HOME=${JDK8}"]) {
                        sh "mvn compile"
                    }
                }
            }
        }
        stage('Building Back Application') {
            steps {
                script {
                    // Stage 3: Run tests
                    withEnv(["JAVA_HOME=${JDK8}"]) {
                        sh "mvn test"
                    }
                }
            }
        }
        stage('Build') {
            steps {
                script {
                    // Stage 4: Build the application
                    withEnv(["JAVA_HOME=${JDK8}"]) {
                        sh "mvn package"
                    }
                }
            }
        }
        stage('SonarQube Analysis') {
            steps {
                script {
                    // Stage 5: Execute SonarQube analysis with Java 11
                    withEnv(["JAVA_HOME=${JDK11}"]) {
                        sh 'mvn sonar:sonar'
                    }
                }
            }
        }
    }
    tools {
        jdk 'java1.8'
        jdk11 'java11.1'
    }
}
