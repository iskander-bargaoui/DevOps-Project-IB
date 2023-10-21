pipeline {
    agent any
    stages {
        // Email Notification
        stage('Send Email Notification') {
            steps {
                script {
                    //
                    def contenuReadMe = readFile('README.md')
                    
                    def subject = 'New Project Commit - Iskander BARGAOUI'
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
}
