void call(){
  stage('Building'){
    node{
      println "grunt: build()"
      sh 'printenv'
    }
  }
}