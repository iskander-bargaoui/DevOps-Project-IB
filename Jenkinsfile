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
                script {
                    sh "docker build -t $TAG ."
                }
            }
        }
        stage('Docker Login') {
            steps {
                script {
                    withCredentials([usernamePassword(credentialsId: 'DockerHubCreds', usernameVariable: 'DOCKERHUB_USERNAME', passwordVariable: 'DOCKERHUB_PASSWORD')]) {
                        sh "docker login -u $DOCKERHUB_USERNAME -p $DOCKERHUB_PASSWORD"
                    }
                }
            }
        }
        stage('Docker Push') {
            steps {
                script {
                    sh "docker push $TAG"
                }
            }
        }

        // Nexus

        stage('Deploy Artifacts') {
            steps {
                script {
                    // Exécuter mvn deploy en sautant les tests
                    sh "mvn deploy -DskipTests"
                }
            }
        }

        // FRONT //

        stage('Build Frontend') {
            steps {
                script {
                    // Stage : Git Checkout to the 'Front' branch
                    checkout([$class: 'GitSCM',
                        branches: [[name: 'Front']],
                        userRemoteConfigs: [[
                            url: 'https://github.com/iskander-bargaoui/DevOps-Project-IB.git',
                            credentialsId: 'GithubJenkinsToken'
                        ]]
                    ])

                    // Change to the directory containing your Angular frontend code
                    // Adjust the path as needed
                    dir('Front') {
                        // Build the Angular frontend
                        sh 'npm install'
                        sh 'npm run build'  
                    }
                }
            }
        }

        // Grafana + Prometheus
    }
}
