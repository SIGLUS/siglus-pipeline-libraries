void call(){
  stage('PMD Testing'){
    node{
      println "gradle: pmd_test()"
      sh 'mkdir -p /ebs2/gradle-caches/${JOB_NAME}'
      sh 'mkdir -p /ebs2/node-caches/${JOB_NAME}'
      sh 'docker run --rm -u gradle -v ${PWD}:/app -w /app siglusdevops/gradle:4.10.3 gradle checkstyleMain checkstyleTest checkstyleIntegrationTest pmdMain pmdTest pmdIntegrationTest'
    }
  }
}