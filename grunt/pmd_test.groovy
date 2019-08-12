void call(){
  stage('Testing'){
    node{
      println "grunt: pmd_test()"
      sh 'mkdir -p /ebs/node-caches/${JOB_NAME}'
      sh 'mkdir -p /ebs/node-caches/.global'
      sh 'docker run --rm  -u nodejs -v ${PWD}:/app -v /ebs/node-caches/.global:/home/nodejs/.npm -v /ebs/node-caches/${JOB_NAME}:/app/node_modules -w /app siglusdevops/dev-ui:latest /dev-ui/build.sh'
    }
  }
}
