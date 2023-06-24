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
                sh '/var/jenkins_home/tools/hudson.tasks.Maven_MavenInstallation/mvn/bin/mvn package'
            }
        }
        
        stage('Execute JAR') {
            steps {
                sh 'java -jar target/*.jar'
            }
        }

        stage('Deploy to AWS Server') {
            steps {
                withCredentials([file(credentialsId: 'aws-ec2', variable: 'PEM_FILE')]) {
                    // Copy JAR file to AWS server
                    sh "scp -i $PEM_FILE -o StrictHostKeyChecking=no target/*.jar admin@ec2-18-233-157-228.compute-1.amazonaws.com:/app/deployed.jar"
                    
                    // SSH into AWS server and deploy the JAR file
                    sh "ssh -i $PEM_FILE -o StrictHostKeyChecking=no admin@ec2-18-233-157-228.compute-1.amazonaws.com 'java -jar /app/deployed.jar'"
                }
            }
        }
    }
}
