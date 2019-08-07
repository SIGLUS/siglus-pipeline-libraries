void call(){
  stage('Testing') {
    node{
      println "gradle: unit_test()"
      sh 'mkdir -p /tmp/gradle-caches/${env.JOB_BASE_NAME}'
      sh 'docker run --rm  -u gradle -v /tmp/gradle-caches/${env.JOB_BASE_NAME}:/home/gradle/.gradle/caches -v ${PWD}:/app -w /app  siglusdevops/gradle:4.10.3 gradle test'
    }
  }
  post {
    always {
        junit 'build/reports/**/*.xml'
    }
  }
}
