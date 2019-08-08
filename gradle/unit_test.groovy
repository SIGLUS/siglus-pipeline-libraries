void call(){
  stage('Testing'){
    node{
      println "gradle: unit_test()"
      sh 'mkdir -p /efs/gradle-caches/${JOB_NAME}'
      sh 'docker run --rm  -u gradle -v /efs/gradle-caches/${JOB_NAME}:/home/gradle/.gradle/caches -v ${PWD}:/app -w /app siglusdevops/gradle:4.10.3 gradle test'
    }
  }
}
