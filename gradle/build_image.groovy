def call(){
  stage "Building Docker Image", {
    node{
      sh '''
      SHORT_GIT_REVISION=$(git rev-parse --short HEAD)
      IMAGE_VERSION=${BUILD_NUMBER}-${SHORT_GIT_REVISION}
      PROJECT_NAME=${JOB_NAME%/*}
      IMAGE_REPO=siglusdevops/${PROJECT_NAME#*-}
      IMAGE_NAME=${IMAGE_REPO}-${BRANCH_NAME}:${IMAGE_VERSION}
      docker build -t ${IMAGE_NAME} .
      docker tag ${IMAGE_NAME} ${IMAGE_REPO}-${BRANCH_NAME}:latest
      docker push ${IMAGE_NAME}
      docker push ${IMAGE_REPO}-${BRANCH_NAME}:latest
      docker rmi ${IMAGE_NAME} ${IMAGE_REPO}-${BRANCH_NAME}:latest
      echo "*************Image name is [${IMAGE_NAME}]*************"
      '''
    }
  }
}