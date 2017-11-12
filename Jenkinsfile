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

        stage('Prepare') {
            steps {
                checkout([$class                           : 'GitSCM',
                          branches                         : [[name: "origin/${$env.BRANCH_NAME}"]],
                          doGenerateSubmoduleConfigurations: false,
                          extensions                       : [[$class: 'LocalBranch']]])
            }
        }

        stage('Build') {
            agent any
            tools {
                maven 'Maven'
            }
            steps {
                withSonarQubeEnv('My SonarQube Server') {
                    script {
                        //sh 'git checkout $BRANCHE_NAME'
                        sh 'git reset --hard origin/$BRANCH_NAME'
                        if (env.BRANCH_NAME == "feature/jenkins") {
                            def pom = readMavenPom file: 'usef-build/pom.xml'
                            env.devVersion = pom.version
                            env.version = pom.version.replace("-SNAPSHOT", ".${currentBuild.number}")
                            sh "mvn -f usef-build/pom.xml versions:set -DnewVersion=$version"
                            sh 'mvn -f usef-build/pom.xml clean deploy -DskipTests'
                            sh 'git commit -am "New release $version"'
                            sh 'git tag $version'
                            //sh "git push origin $version"
                        } else {
                            sh 'mvn -f usef-build/pom.xml clean deploy -DskipTests'
                        }
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
