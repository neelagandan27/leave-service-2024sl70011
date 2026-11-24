
pipeline {
    agent any

    options {
        skipDefaultCheckout(true)
    }

    environment {
        DOCKER_IMAGE = 'leave-service:1.0'
        CONTAINER_NAME = 'leave-service-staging'
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
                withSonarQubeEnv('SonarQube') {
                    sh '''
                        mvn org.sonarsource.scanner.maven:sonar-maven-plugin:3.11.0.3922:sonar \
                            -Dsonar.projectKey=leave-service-2024sl70011 \
                            -Dsonar.host.url=http://localhost:9000 \
                            -Dsonar.coverage.jacoco.xmlReportPaths=target/site/jacoco/jacoco.xml
                    '''
                }
            }
        }

        stage('Quality Gate') {
            steps {
                timeout(time: 5, unit: 'MINUTES') {
                    waitForQualityGate abortPipeline: true
                }
            }
        }

        stage('Build Docker Image') {
            steps {
                sh '''
                    docker build -t $DOCKER_IMAGE .
                '''
            }
        }

        stage('Deploy to Staging') {
            steps {
                sh '''
                    docker rm -f $CONTAINER_NAME || true

                    docker run -d \
                        --name $CONTAINER_NAME \
                        -e APP_ENV=staging \
                        -p 8085:8080 \
                        $DOCKER_IMAGE

                    sleep 5

                    curl --fail http://localhost:8085
                '''
            }
        }

        stage('Approval for Production') {
            steps {
                timeout(time: 30, unit: 'MINUTES') {
                    input message: 'Staging deployment verified. Approve production deployment?',
                          ok: 'Deploy to Production'
                }
            }
        }

        stage('Deploy to Production') {
            steps {
                sh '''
                    docker rm -f leave-service-production || true

                    docker run -d \
                        --name leave-service-production \
                        -e APP_ENV=production \
                        -p 8086:8080 \
                        $DOCKER_IMAGE

                    sleep 5

                    curl --fail http://localhost:8086
                '''
            }
        }
    }
}
