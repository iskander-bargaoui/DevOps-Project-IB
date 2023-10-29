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
        stage('Compile') {
            steps {
                // Stage 2: Compile the project into a .jar file
                sh "mvn compile"
            }
        }
        // ... Other backend stages

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
        // ... Docker and Nexus stages for backend

        // Parallel execution of backend and frontend stages
        stage('Build and Deploy') {
            parallel {
                backend: {
                    stage('Backend Stages') {
                        // ... Backend stages
                    }
                }
                frontend: {
                    stage('Frontend Stages') {
                        when {
                            expression { env.BRANCH_NAME ==~ /.*Front.*/ }  // Run if the branch name contains "Front"
                        }
                        stages {
                            stage('Build Frontend') {
                                // Stage : Git Checkout to the 'Front' branch
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
                            // Testing Front UI
                            stage('Docker Test Front UI') {
                                steps {
                                    script {
                                        sh "docker run -p 4200:4200 $FRONTAG"
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // Grafana + Prometheus
    }
}
