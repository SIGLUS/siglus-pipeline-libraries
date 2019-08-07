void call(){
  stage('Gradle Build') {
    node{
      println "gradle: build()"
      sh 'docker run --rm  -u gradle -v ${PWD}:/app -w /app  siglusdevops/gradle:4.10.3 gradle clean build'
    }
  }
  post {
    success {
        archiveArtifacts artifacts: 'build/libs/**/*.jar', fingerprint: true
    }
  }
}