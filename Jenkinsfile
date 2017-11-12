#!groovy
@Library('dynamo-workflow-libs') _

pipeline {
    agent none
    environment {
        MAVEN_OPTS = '-Dmaven.wagon.http.ssl.insecure=true -Dmaven.wagon.http.ssl.allowall=true -Dmaven.wagon.http.ssl.ignore.validity.dates=true'
    }
    options {
        // Only keep the 10 most recent builds
        buildDiscarder(logRotator(numToKeepStr: '10'))
    }
    stages {

        stage('Start') {
            agent any
            steps {
                sh 'env'
            }
        }

        stage('Checkout') {
            agent any
            steps {
                checkout([
                        $class           : 'GitSCM',
                        branches         : scm.branches,
                        extensions       : scm.extensions + [[$class: 'CleanCheckout']],
                        userRemoteConfigs: scm.userRemoteConfigs
                ])
            }
        }

        stage('Build') {
            agent any
            tools {
                maven 'Maven'
            }
            steps {
                withSonarQubeEnv('My SonarQube Server') {
//          sh 'cd usef-build && mvn clean verify sonar:sonar deploy -Dsonar.host.url=$SONARQUBE_URL && cd ..'
                    script {
                        def pom = readMavenPom file: 'usef-build/pom.xml'
                        env.devVersion = pom.version
                        env.version = pom.version.replace("-SNAPSHOT", ".${currentBuild.number}")
                        sh 'mvn -f usef-build/pom.xml -DreleaseVersion=${version} -DdevelopmentVersion=${devVersion} -DpushChanges=true -DlocalCheckout=false release:prepare release:perform -B'
                    }
                }
            }
        }

//    stage("Quality Gate"){
//      agent any
//      steps {
//        timeout(time: 1, unit: 'HOURS') { // Just in case something goes wrong, pipeline will be killed after a timeout
//          script {
//            def qg = waitForQualityGate() // Reuse taskId previously collected by withSonarQubeEnv
//            if (qg.status != 'OK') {
//              error "Pipeline aborted due to quality gate failure: ${qg.status}"
//            }
//          }
//        }
//      }
//    }

    }
    post {
        failure {
            sendNotifications 'FAILURE'
        }
        unstable {
            sendNotifications 'UNSTABLE'
        }
    }
}
