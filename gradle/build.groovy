void call(){
    println "gradle: build()"
    sh 'docker run --rm -u gradle -v "$PWD":/app -w /app  sisglusdevops/gradle:4.10.3 gradle clean build'
}