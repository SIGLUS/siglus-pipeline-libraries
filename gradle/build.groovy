void call(){
  stage('Building'){
    node{
      println "gradle: build()"
      sh 'mkdir -p /ebs/gradle-caches/${JOB_NAME}'
      sh 'docker run --rm  -u gradle -v /ebs/gradle-caches/${JOB_NAME}:/home/gradle/.gradle/caches -v ${PWD}:/app -w /app siglusdevops/gradle:4.10.3 gradle clean build'
    }
  }
}