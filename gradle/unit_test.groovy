void call(){
    node{
      println "gradle: unit_test()"
      sh 'docker run --rm  -u gradle -v /tmp/gradle-caches:/home/gradle/.gradle/caches -v ${PWD}:/app -w /app siglusdevops/gradle:4.10.3 gradle test'
      junit 'build/reports/**/*.xml'
    }
}
