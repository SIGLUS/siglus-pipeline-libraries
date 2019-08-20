void call(){
  stage('Integration Testing'){
    node{
      withCredentials([file(credentialsId: 'setting_env', variable: 'SETTING_ENV')]) {
        println "gradle: integration_test()"
        println "integration_test(): Starting Clear testing env"
        sh '''
          echo "clear env file"
          rm -f .env
          echo "create .env file"
          cp $SETTING_ENV .env
          for CONTAINER in ${JOB_NAME}_inttest_db ${JOB_NAME}_inttest_redis ${JOB_NAME}_inttest_log; do
            RUNNING=$(docker inspect --format="{{ .State.Running }}" $CONTAINER 2> /dev/null)
            if [ $? -eq 1 ]; then
              echo "'$CONTAINER' does not exist."
            else
              /usr/bin/docker rm --force $CONTAINER
            fi
          done
          docker network rm -f ${JOB_NAME}_inttest
        '''
        println "integration_test(): Starting create testing env"
        sh 'docker network create --driver bridge ${JOB_NAME}_inttest'
        sh 'docker run -d --network ${JOB_NAME}_inttest --env-file .env --name ${JOB_NAME}_inttest_db openlmis/postgres:9.6'
        sh 'docker run -d --network ${JOB_NAME}_inttest --env-file .env --name ${JOB_NAME}_inttest_redis redis:3.2.12'
        sh 'docker run -d --network ${JOB_NAME}_inttest --env-file .env --name ${JOB_NAME}_inttest_log openlmis/rsyslog:1'
        println "integration_test(): Starting Running integration_test"
        sh '''
          docker run --rm -u gradle \
            --env-file .env \
            --network ${JOB_NAME}_inttest \
            --link ${JOB_NAME}_inttest_redis:redis \
            --link ${JOB_NAME}_inttest_db:db \
            --link ${JOB_NAME}_inttest_log:log \
            -v ${PWD}:/app -w /app \
            siglusdevops/gradle:4.10.3 \
            gradle integrationTest
        '''
        println "integration_test(): integration_test Done, clear env"
        sh '''
          for CONTAINER in ${JOB_NAME}_inttest_db ${JOB_NAME}_inttest_redis ${JOB_NAME}_inttest_log; do
            RUNNING=$(docker inspect --format="{{ .State.Running }}" $CONTAINER 2> /dev/null)
            if [ $? -eq 1 ]; then
              echo "'$CONTAINER' does not exist."
            else
              /usr/bin/docker rm --force $CONTAINER
            fi
          done
          docker network rm -f ${JOB_NAME}_inttest
        '''
      }
    }
  }
}