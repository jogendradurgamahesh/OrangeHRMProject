pipeline {
    agent any  

    tools {
        maven 'TestMaven' 
    }



   environment {
        COMPOSE_PATH = "${WORKSPACE}/docker" // 🔁 Adjust if compose file is elsewhere
        SELENIUM_GRID = "true"
    }














    stages {
		
		  stage('Checkout') {
            steps {
                git branch: 'main', url: 'https://github.com/jogendradurgamahesh/OrangeHRMProject.git'
            }
        }

		
		 stage('Start Selenium Grid via Docker Compose') {
            steps {
                script {
                    echo "Starting Selenium Grid with Docker Compose..."
                       bat """
                        cd "${WORKSPACE}/docker"
                        docker compose down
                        docker compose up -d
                        """    

                    echo "Waiting for Selenium Grid to be ready..."
                    sleep 30 // Add a wait if needed
                }
            }
        }
		
		

      
        stage('Build & Test') {
            steps {
                bat 'mvn clean test -DseleniumGrid=true'
            }
        }



  stage('Stop Selenium Grid') {
            steps {
                script {
                    echo "Stopping Selenium Grid..."
                     bat """
                    cd "${WORKSPACE}/docker"
                    docker compose down
                    """
                }
            }
        }
        
        
        stage('Publish Reports') {
            steps {
                publishHTML(target: [
                    reportDir: 'test-output',
                    reportFiles: 'ExtentReport.html',
                    reportName: 'Extent Report',
                    keepAll: true,
                    alwaysLinkToLastBuild: true,
                    allowMissing: false
                ])
            }
        }
    }

    post {

        always {
            archiveArtifacts artifacts: '**/test-output/**/*.html, **/screenshots/*.png', fingerprint: true
            junit 'target/surefire-reports/*.xml'
        }

        success {
            emailext (
                to: 'jogendramahesh123@gmail.com',
                subject: "✅ Build SUCCESS: ${env.JOB_NAME} #${env.BUILD_NUMBER}",
                body: """
                <html>
                <body>
                <p>Hello Team,</p>
                <p><b>Build Status:</b> <span style="color: green;">SUCCESS</span></p>

                <p><b>Project:</b> ${env.JOB_NAME}</p>
                <p><b>Build Number:</b> ${env.BUILD_NUMBER}</p>

                <p><b>Build URL:</b><br>
                <a href="${env.BUILD_URL}">${env.BUILD_URL}</a></p>

                <p><b>Extent Report:</b><br>
                <a href="${env.BUILD_URL}HTML_20Extent_20Report/">
                View Report
                </a></p>

                <p>Regards,<br>Automation Team</p>
                </body>
                </html>
                """,
                mimeType: 'text/html',
                attachLog: true
            )
        }

        failure {
            emailext (
                to: 'jogendramahesh123@gmail.com',
                subject: "❌ Build FAILED: ${env.JOB_NAME} #${env.BUILD_NUMBER}",
                body: """
                <html>
                <body>
                <p>Hello Team,</p>

                <p><b style="color:red;">Build FAILED</b></p>

                <p><b>Build URL:</b><br>
                <a href="${env.BUILD_URL}">${env.BUILD_URL}</a></p>

                <p><b>Extent Report:</b><br>
                <a href="${env.BUILD_URL}HTML_20Extent_20Report/">
                View Report
                </a></p>

                <p>Please check logs and fix issues.</p>

                <p>Regards,<br>Automation Team</p>
                </body>
                </html>
                """,
                mimeType: 'text/html',
                attachLog: true
            )
        }
    }
}    
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        