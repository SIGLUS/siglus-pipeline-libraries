void call(){
  stage('Integration Testing'){
    node{
      withCredentials([file(credentialsId: 'setting_env', variable: 'SETTING_ENV')]) {
        println "gradle: integration_test()"
        sh 'rm -f .env'
        sh 'cp $SETTING_ENV .env'
        sh 'docker rm -f ${JOB_NAME}_inttest_db'
        sh 'docker rm -f ${JOB_NAME}_inttest_redis'
        sh 'docker rm -f ${JOB_NAME}_inttest_log'
        sh 'docker network rm -f ${JOB_NAME}_inttest'
        sh 'docker network create --driver bridge ${JOB_NAME}_inttest'
        sh 'docker run -d --network ${JOB_NAME}_inttest --env-file .env --name ${JOB_NAME}_inttest_db openlmis/postgres:9.6'
        sh 'docker run -d --network ${JOB_NAME}_inttest --name ${JOB_NAME}_inttest_redis redis:3.2.12'
        sh 'docker run -d --network ${JOB_NAME}_inttest --name ${JOB_NAME}_inttest_log openlmis/rsyslog:1'
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
        sh 'docker rm -f ${JOB_NAME}_inttest_db'
        sh 'docker rm -f ${JOB_NAME}_inttest_redis'
        sh 'docker rm -f ${JOB_NAME}_inttest_log'
      }
    }
  }
}