def call(){
  stage "Building Docker Image", {
    node{
      sh '''
      GIT_REVISION=$(git rev-parse HEAD)
      IMAGE_VERSION=${BUILD_NUMBER}-${GIT_REVISION}
      PROJECT_NAME=${JOB_NAME%/*}
      IMAGE_NAME=siglusdevops/${PROJECT_NAME}:${IMAGE_VERSION}
      docker build -t ${IMAGE_NAME} .
      docker push ${IMAGE_NAME}
      docker rmi ${IMAGE_NAME}
      echo "*************Image name is [${IMAGE_NAME}]*************"
      '''
    }
  }
}