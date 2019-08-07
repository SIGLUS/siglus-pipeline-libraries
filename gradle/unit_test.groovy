import hudson.model.*
void call(){
  stage('Testing') {
    println "gradle: unit_test()"
withDockerContainer(args: ' -it  -u gradle -v ${env.WORKSPACE}:/app -w /app ', image: 'siglusdevops/gradle:4.10.3') {
    gradle test
}
  }
}
