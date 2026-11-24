
pipeline {
    agent any

    options {
        skipDefaultCheckout(true)
    }

    stages {

        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build and Test') {
            steps {
                sh 'mvn clean verify'
            }

            post {
                always {
                    junit '**/target/surefire-reports/*.xml'
                }
            }
        }

        stage('SonarQube Code Inspection') {
            steps {
                withCredentials([
                    string(
                        credentialsId: 'sonar-token',
                        variable: 'SONAR_TOKEN'
                    )
                ]) {

                    // Verify the exact credential stored in Jenkins
                    sh '''
                        set +x

                        echo "Checking Jenkins SonarQube authentication..."

                        AUTH_RESPONSE=$(curl -sS \
                            -u "$SONAR_TOKEN:" \
                            http://localhost:9000/api/authentication/validate)

                        echo "Authentication response: $AUTH_RESPONSE"

                        if ! echo "$AUTH_RESPONSE" | grep -q '"valid":true'; then
                            echo "SonarQube authentication failed!"
                            exit 1
                        fi

                        echo "SonarQube authentication successful!"
                    '''

                    // Run SonarQube analysis
                    sh '''
                        set +x

                        mvn org.sonarsource.scanner.maven:sonar-maven-plugin:3.11.0.3922:sonar \
                            -Dsonar.projectKey=leave-service-2024sl70011 \
                            -Dsonar.host.url=http://localhost:9000 \
                            -Dsonar.coverage.jacoco.xmlReportPaths=target/site/jacoco/jacoco.xml \
                            -Dsonar.token="$SONAR_TOKEN"
                    '''
                }
            }
        }
    }
}

