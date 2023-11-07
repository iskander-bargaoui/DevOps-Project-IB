pipeline {
    // Defining Agents
    agent any
    // Environment Variables
    environment {
        DOCKERHUB_USERNAME = "iskander20"
        BACKTAG = "${DOCKERHUB_USERNAME}/devopsproject:backapp"
        FRONTAG = "${DOCKERHUB_USERNAME}/devopsproject:frontapp"
    }
    // Defining Tools To be Used - Declarative
    tools {
        jdk 'java1.8'
        nodejs 'NodeJS'
    }
    // Defining Stages
    stages {
        stage('SCM Checkout - Backend') {
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
        stage('Cleaning Project') {
            steps {
                script {
                    // Stage 2: Compile the project into a .jar file
                    sh "mvn clean install"
                }
            }
        }
        stage('Backend Compilation') {
            steps {
                script {
                    // Stage 2: Compile the project into a .jar file
                    sh "mvn compile"
                }
            }
        }
        stage('Unitary Tests') {
            steps {
                script {
                    // Stage 3: Run tests
                    sh "mvn test"
                }
            }
        }
        stage('Building Backend Application') {
            steps {
                script {
                    // Stage 4: Build the application
                    sh "mvn package"
                }
            }
        }
        stage('SonarQube - Analysis') {
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
        stage('Nexus - Deploying Artifacts') {
            steps {
                script {
                    // Execute mvn deploy skipping tests
                    sh "mvn deploy -DskipTests" //-U
                }
            }
        }
        // Building Docker Image for Backend
        stage('Docker Image Build - Backend') {
            steps {
                script {
                    sh "docker build -t $BACKTAG ."
                }
            }
        }
        stage('Docker Login Backend') {
            steps {
                script {
                    withCredentials([usernamePassword(credentialsId: 'DockerHubCreds', usernameVariable: 'DOCKERHUB_USERNAME', passwordVariable: 'DOCKERHUB_PASSWORD')]) {
                        sh "docker login -u $DOCKERHUB_USERNAME -p $DOCKERHUB_PASSWORD"
                    }
                }
            }
        }
        stage('Docker Push - Backend Application') {
            steps {
                script {
                    sh "docker push $BACKTAG"
                }
            }
        }
        // Frontend Stages (Always executed)
        stage('SCM Checkout - Frontend') {
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
        stage('Docker Image Build - Frontend') {
            steps {
                script {
                    sh "docker build -t $FRONTAG ."
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
        stage('Docker Push - Frontend Image') {
            steps {
                script {
                    sh "docker push $FRONTAG"
                }
            }
        }
        // Deploying Docker Compose
        stage('Docker Compose - Deployment') {
            steps {
                sh 'docker-compose up -d'  // Deploy Docker Compose services
            }
        }
        // Sending Email Notification (NgRok + Cron Job)
        stage('Email Alerting Notification') {
            steps {
                script {
                    // Reading the contents of the README.md file
                    def contenuReadMe = readFile('README.md') 
                    def subject = 'New DevOps Project Pipeline Commit - Iskander BARGAOUI'
                    def buildStartTime = new Date(currentBuild.startTimeInMillis)
                    def formattedDate = buildStartTime.format('yyyy-MM-dd HH:mm:ss')
                    def body = "A new commit has been made to the repository on ${formattedDate}.\n\n${contenuReadMe}"
                    def to = 'iskanderbargaouitest@gmail.com'
                    mail(
                        subject: subject,
                        body: body,
                        to: to,
                    )
                }
            }
        }
    }
    post {
        success {
            emailext subject: 'Successful Deployment',
                      body: 'Your pipeline was successfully deployed.',
                      to: 'iskanderbargaouitest@gmail.com'
        }
        failure {
            emailext subject: 'Deployment Failed',
                      body: 'Your pipeline was not successfully deployed. Verify file logs at /var/log/jenkins.log for more information.',
                      to: 'iskanderbargaouitest@gmail.com'
        }
    }
}
