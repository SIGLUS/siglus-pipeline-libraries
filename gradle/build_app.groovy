void call(){
  stage('Building'){
    node{
      fetch_setting_env
      ansiColor('xterm') {
        println "gradle: build()"
        sh 'mkdir -p /ebs2/gradle-caches/${JOB_NAME}'
        sh 'mkdir -p /ebs2/node-caches/${JOB_NAME}'
        sh 'docker run --rm  -u gradle --add-host=log:127.0.0.1 -v ${PWD}:/app -w /app siglusdevops/gradle:4.10.3 gradle clean build -x checkstyleMain -x checkstyleTest -x checkstyleIntegrationTest -x pmdMain -x pmdTest -x pmdIntegrationTest'
      }
      checkstyle pattern: '**/build/reports/checkstyle/*.xml'
      pmd pattern: '**/build/reports/pmd/*.xml'
      junit '**/build/test-results/*/*.xml'
    }
  }
}