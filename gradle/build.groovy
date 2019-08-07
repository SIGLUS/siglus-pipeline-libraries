def call(){
  stage "Building", {
    node{
       withCredentials([file(credentialsId: '8da5ba56-8ebb-4a6a-bdb5-43c9d0efb120', variable: 'ENV_FILE')]) {
          sh '''
          GIT_REVISION=$(git rev-parse HEAD)
          IMAGE_VERSION=${BUILD_NUMBER}-${GIT_REVISION}
          PROJECT_NAME=${JOB_NAME%/*}
          IMAGE_NAME=siglusdevops/${PROJECT_NAME#*-}:${IMAGE_VERSION}
          trap $(docker-compose -f docker-compose.builder.yml down --volumes) EXIT

          rm -f .env
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
       currentBuild.result = processTestResults('SUCCESS')
    }
  }
}

def processTestResults(status) {
    checkstyle pattern: '**/build/reports/checkstyle/*.xml'
    pmd pattern: '**/build/reports/pmd/*.xml'
    junit '**/build/test-results/*/*.xml'

    AbstractTestResultAction testResultAction = currentBuild.rawBuild.getAction(AbstractTestResultAction.class)
    if (testResultAction != null) {
        failuresCount = testResultAction.failCount
        echo "Failed tests count: ${failuresCount}"
        if (failuresCount > 0) {
            echo "Setting build unstable due to test failures"
            status = 'UNSTABLE'
        }
    }

    return status
}