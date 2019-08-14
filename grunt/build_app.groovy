void call(){
  stage('Building'){
    node{
      println "grunt: build()"
      sh 'mkdir -p /ebs/node-caches/${JOB_NAME}'
      sh 'printenv'
    }
  }
}