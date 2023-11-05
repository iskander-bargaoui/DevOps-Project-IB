pipeline {
    agent any
    environment {
        DOCKERHUB_USERNAME = "iskander20"
        BACKTAG = "${DOCKERHUB_USERNAME}/devopsproject:backapp"
        FRONTAG = "${DOCKERHUB_USERNAME}/devopsproject:frontapp"
    }
    tools {
        jdk 'java1.8'
        nodejs 'NodeJS'
    }
    stages {
        stage('SCM Checkout') {
            steps {
                script {
                    // Stage 1: Git Checkout
                    checkout([$class: 'GitSCM',
                        branches: [[name: '*/Back']],  // Match any branch with "Back" in its name
                        userRemoteConfigs: [[
                            url: 'https://github.com/iskander-bargaoui/DevOps-Project-IB.git',
                            credentialsId: 'GithubJenkinsToken'
                        ]]
                    ])
                }
            }
        }
        stage('Clean Install') {
            steps {
                script {
                    // Stage 2: Compile the project into a .jar file
                    sh "mvn clean install"
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
                script {
                    withSonarQubeEnv('SonarQube') {
                        sh 'mvn sonar:sonar'
                    }
                }
            }
        }

           // Nexus

        stage('Deploy Artifacts') {
            steps {
                script {
                    // Execute mvn deploy skipping tests
                    sh "mvn deploy -DskipTests" //-U
                }
            }
        }

        // Building Docker Image for Backend
        stage('Docker Build Image') {
            steps {
                script {
                    sh "docker build -t $BACKTAG ."
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
                    sh "docker push $BACKTAG"
                }
            }
        }
     
        // Frontend Stages (Always executed)
        stage('Frontend Stages') {
            steps {
                script {
                    // Stage: Git Checkout to the 'Front' branch
                    checkout([$class: 'GitSCM',
                        branches: [[name: '*/Front']],  // Match any branch with "Front" in its name
                        userRemoteConfigs: [[
                            url: 'https://github.com/iskander-bargaoui/DevOps-Project-IB.git',
                            credentialsId: 'GithubJenkinsToken'
                        ]]
                    ])

                    // Change to the directory containing your Angular frontend code
                    dir('Front') {
                        // Build the Angular frontend
                        sh 'npm install'
                        sh 'npm run build'
                    }
                }
            }
        }
        // Building Docker Image for Frontend
        stage('Docker Build Image Front') {
            steps {
                script {
                    sh "docker build -t $FRONTAG ."
                }
            }
        }
        stage('Docker Login Front') {
            steps {
                script {
                    withCredentials([usernamePassword(credentialsId: 'DockerHubCreds', usernameVariable: 'DOCKERHUB_USERNAME', passwordVariable: 'DOCKERHUB_PASSWORD')]) {
                        sh "docker login -u $DOCKERHUB_USERNAME -p $DOCKERHUB_PASSWORD"
                    }
                }
            }
        }
        stage('Docker Push Front Image') {
            steps {
                script {
                    sh "docker push $FRONTAG"
                }
            }
        }


        stage('Deploy Docker Compose') {
            steps {
                sh 'docker-compose up -d'  // Deploy Docker Compose services
            }
        }
        // Testing Front UI
      /*  stage('Docker Test Front UI') {
            steps {
                script {
                    sh "docker run -p 4200:4200 $FRONTAG"
                }
            }
        }*/ 

        // Grafana + Prometheus
    }
}
