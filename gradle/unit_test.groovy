void call(){
  stage('Testing') {
        node{
        println "gradle: unit_test()"
        sh 'docker run -rm  -u gradle -v ${PWD}:/app -w /app  siglusdevops/gradle:4.10.3 gradle test'
        }
  }
}
