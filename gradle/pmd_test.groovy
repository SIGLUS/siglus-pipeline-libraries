void call(){
  stage('PMD Testing'){
    node{
      println "gradle: pmd_test()"
      sh 'mkdir -p /ebs/gradle-caches/${JOB_NAME}'
      sh 'docker run --rm  -u gradle -v /ebs/gradle-caches/${JOB_NAME}:/home/gradle/.gradle/caches -v ${PWD}:/app -v /ebs/node-caches:/app/node_modules -w /app siglusdevops/gradle:4.10.3 gradle pmdMain pmdTest checkstyleMain checkstyleTest'
    }
  }
}