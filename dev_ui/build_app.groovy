void call(){
  stage('Build and Test App'){
    node{
        script {
            try {
                sh "printenv"
                sh '''
                    export "UID=`id -u jenkins`"
                    docker-compose down --volumes
                    docker-compose pull
                    docker-compose run --entrypoint /dev-ui/build.sh ${PROJECT_SHORT_NAME}
                '''
                sh "printenv"
                junit '**/build/test/test-results/*.xml'
            }
            catch (exc) {
                currentBuild.result = 'UNSTABLE'
            }
        }
    }
    // archive 'build/styleguide/*, build/styleguide/**/*, build/docs/*, build/docs/**/*, build/messages/*'
    
  }
}