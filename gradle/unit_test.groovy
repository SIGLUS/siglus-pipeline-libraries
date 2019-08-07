void call(){
    node{
      println "gradle: unit_test()"
      sh 'mkdir -p /tmp/gradle-caches/${JOB_BASE_NAME}'
      sh 'docker run --rm  -u gradle -v /tmp/gradle-caches/${JOB_BASE_NAME}:/home/gradle/.gradle/caches -v ${PWD}:/app -w /app siglusdevops/gradle:4.10.3 gradle test'
    }
}
