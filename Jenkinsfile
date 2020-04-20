#!/usr/bin/env groovy

node('iasset-jenkins-slave') {

    // -----------------------------------------------
    // --------------- Staging Branch ----------------
    // -----------------------------------------------
    if (env.BRANCH_NAME == 'staging') {

        stage('Clone and Update') {
            git(url: 'https://github.com/i-Asset/data-channel-service.git', branch: env.BRANCH_NAME)
            sh 'git submodule init'
            sh 'git submodule update'
        }

        stage('Build Dependencies') {
            sh 'rm -rf common-1'
            sh 'git clone https://github.com/i-Asset/common-1.git'
            dir('common-1') {
                sh 'git checkout ' + env.BRANCH_NAME
                sh 'mvn clean install'
            }
        }

        stage('Build Java') {
            sh 'mvn clean install -DskipTests'
        }

        stage('Build Docker') {
            sh 'mvn -f data-channel-service/pom.xml docker:build -DdockerImageTag=staging'
        }

        stage('Push Docker') {
            sh 'docker push iassetplatform/data-channel-service:staging'
        }

        stage('Deploy') {
            sh 'ssh staging "cd /srv/docker-setup/staging/ && ./run-staging.sh restart-single data-channel-service"'
        }
    }

    // -----------------------------------------------
    // ---------------- Master Branch ----------------
    // -----------------------------------------------
    if (env.BRANCH_NAME == 'master') {

        stage('Clone and Update') {
            git(url: 'https://github.com/i-Asset/data-channel-service.git', branch: env.BRANCH_NAME)
            sh 'git submodule init'
            sh 'git submodule update'
        }

        stage('Build Dependencies') {
            sh 'rm -rf common-1'
            sh 'git clone https://github.com/i-Asset/common-1.git'
            dir('common-1') {
                sh 'git checkout ' + env.BRANCH_NAME
                sh 'mvn clean install'
            }
        }

        stage('Build Java') {
            sh 'mvn clean install -DskipTests'
        }
    }

    // -----------------------------------------------
    // ---------------- Release Tags -----------------
    // -----------------------------------------------
    if( env.TAG_NAME ==~ /^\d+.\d+.\d+$/) {

        stage('Clone and Update') {
            git(url: 'https://github.com/nimble-platform/data-channel-service.git', branch: 'master')
            sh 'git submodule init'
            sh 'git submodule update'
        }

        stage('Build Dependencies') {
            sh 'rm -rf common'
            sh 'git clone https://github.com/nimble-platform/common'
            dir('common') {
                sh 'git checkout master'
                sh 'mvn clean install'
            }
        }

        stage('Set version') {
            sh 'mvn org.codehaus.mojo:versions-maven-plugin:2.1:set -DnewVersion=' + env.TAG_NAME
            sh 'mvn -f data-channel-service/pom.xml org.codehaus.mojo:versions-maven-plugin:2.1:set -DnewVersion=' + env.TAG_NAME
        }

        stage('Build Java') {
            sh 'mvn clean install -DskipTests'
        }

        stage('Build Docker') {
            sh 'mvn -f data-channel-service/pom.xml docker:build'
        }

        stage('Push Docker') {
            sh 'docker push nimbleplatform/data-channel-service:' + env.TAG_NAME
            sh 'docker push nimbleplatform/data-channel-service:latest'
        }

        stage('Deploy MVP') {
            sh 'ssh nimble "cd /data/deployment_setup/prod/ && sudo ./run-prod.sh restart-single data-channel-service"'
        }

        stage('Deploy FMP') {
            sh 'ssh fmp-prod "cd /srv/nimble-fmp/ && ./run-fmp-prod.sh restart-single data-channel-service"'
        }
    }
}
