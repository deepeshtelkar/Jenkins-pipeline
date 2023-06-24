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
                script {
                    // Copy JAR file to AWS server
                    //sh 'scp -i /path/to/key.pem target/*.jar ec2-user@your-aws-server-ip:/path/to/deployed.jar'
                    
                    // SSH into AWS server and deploy the JAR file
                    //sh 'ssh -i /path/to/key.pem ec2-user@your-aws-server-ip "java -jar /path/to/deployed.jar"'
                }
            }
        }
    }
}
