pipeline {
    agent any
    
    stages {
        stage('Checkout') {
            steps {
                checkout([$class: 'GitSCM',
                          branches: [[name: '*/main']], // Specify the branch to checkout
                          userRemoteConfigs: [[url: 'https://github.com/deepeshtelkar/HelloWorld.git',
                                               credentialsId: 'github']]])
            }
        }
        
        stage('Build with Maven') {
            steps {
                sh 'mvn clean package'
            }
        }
        
        stage('Execute JAR') {
            steps {
                sh 'java -jar target/*.jar'
            }
        }
    }
}