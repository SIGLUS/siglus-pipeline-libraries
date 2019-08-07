def call(){
  stage "Building Docker Image", {
    node{
      sh '''
      GIT_REVISION=$(git rev-parse HEAD)
      IMAGE_VERSION=${BUILD_NUMBER}-${GIT_REVISION}
      PROJECT_NAME=${JOB_NAME%/*}
      IMAGE_NAME=siglusdevops/${PROJECT_NAME#*-}:${IMAGE_VERSION}
      function finish {
        docker-compose -f docker-compose.builder.yml down --volumes
      }
      trap finish EXIT

      sudo rm -f .env
      cp $ENV_FILE .env
      if [ "$GIT_BRANCH" != "master" ]; then
          sed -i '' -e "s#^TRANSIFEX_PUSH=.*#TRANSIFEX_PUSH=false#" .env  2>/dev/null || true
      fi

      docker-compose -f docker-compose.builder.yml run -e BUILD_NUMBER=$BUILD_NUMBER -e GIT_BRANCH=$GIT_BRANCH builder
      docker-compose -f docker-compose.builder.yml build image
      docker tag openlmis/${PROJECT_NAME#*-}:latest ${IMAGE_NAME}
      docker push ${IMAGE_NAME}
      docker rmi ${IMAGE_NAME}
      echo "*************Image name is [${IMAGE_NAME}]*************"
      '''
    }
  }
}
