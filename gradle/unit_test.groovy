void call(){
  stage('Testing') {
    println "gradle: unit_test()"
    echo "Running ${env.BUILD_ID} on ${env.JENKINS_URL}"
    sh 'docker run --rm -u gradle -v ${env.WORKSPACE}:/app -w /app  sisglusdevops/gradle:4.10.3 gradle test'
  }
}
