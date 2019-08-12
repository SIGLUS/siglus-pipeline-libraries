void call(){
  stage('Building'){
    node{
      println "grunt: build()"
      sh '''
          NODE_CACHE_ROOT=/ebs/node-caches/
          export "UID=`id -u jenkins`"
          export DOCKERHUB_ORG=siglusdevops
          docker-compose build image
          docker-compose down --volumes
          GIT_REVISION=$(git rev-parse HEAD)
          IMAGE_VERSION=${BUILD_NUMBER}-${GIT_REVISION}
          PROJECT_NAME=${JOB_NAME%/*}
          IMAGE_NAME=${DOCKERHUB_ORG}/${PROJECT_NAME#*-}:${IMAGE_VERSION}
          docker tag ${DOCKERHUB_ORG}/${PROJECT_NAME#*-}:latest ${IMAGE_NAME}
          docker push ${IMAGE_NAME}
      '''
    }
  }
}