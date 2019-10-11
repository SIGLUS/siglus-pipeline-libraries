void call(){
  stage('Build and Push Image'){
    node{
        withCredentials([usernamePassword(
                      credentialsId: "cad2f741-7b1e-4ddd-b5ca-2959d40f62c2",
                      usernameVariable: "USER",
                      passwordVariable: "PASS"
                    )]) {
              sh 'set +x'
              sh 'docker login -u $USER -p $PASS'
        }
        script {
            try {
                sh '''
                    export "UID=`id -u jenkins`"
                    docker-compose build image
                    docker tag ${IMAGE_REPO}:latest ${IMAGE_REPO}:${VERSION}
                    docker push ${IMAGE_REPO}:${VERSION}
                    docker push ${IMAGE_REPO}:latest
                    echo "**********clean*********"
                    docker rmi ${IMAGE_REPO}:${VERSION}
                    docker-compose down --volumes
                '''
            }
            catch (exc) {
                currentBuild.result = 'UNSTABLE'
            }
        }
    }
  }
}