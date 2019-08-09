void call(){
  stage('Testing'){
    node{
      println "grunt: pmd_test()"
      sh 'mkdir -p /ebs/node-caches/${JOB_NAME}'
      sh 'docker run --rm  -u nodejs -v ${PWD}:/app -v /ebs/node-caches/${JOB_NAME}:/app/node_modules -w /app siglusdevops/dev-ui:latest npm rebuild && npm install && grunt karma:unit'
    }
  }
}