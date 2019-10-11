void call(){
  stage('Preparation'){
    node{
      script {
          def properties = readProperties file: 'project.properties'
          if (!properties.version) {
              error("version property not found")
          }
          env.VERSION = properties.version
          env.GIT_REVISION=sh(returnStdout: true, script: '''echo $(git rev-parse HEAD)''').trim()
          env.PROJECT_NAME=sh(returnStdout: true, script: '''echo ${JOB_NAME%/*}''').trim()
          env.PROJECT_SHORT_NAME=sh(returnStdout: true, script: '''echo ${PROJECT_NAME#*-}''').trim()

          DOCKER_ORG="siglusdevops"
          env.IMAGE_REPO= DOCKER_ORG + "/" + env.PROJECT_SHORT_NAME
          currentBuild.displayName += " - " + VERSION
          sh "printenv"
      }
      withCredentials([file(credentialsId: '8da5ba56-8ebb-4a6a-bdb5-43c9d0efb120', variable: 'ENV_FILE')]) {
          script {
              sh '''
                  rm -f .env
                  cp $ENV_FILE .env
                  if [ "$GIT_BRANCH" != "master" ]; then
                      sed -i '' -e "s#^TRANSIFEX_PUSH=.*#TRANSIFEX_PUSH=false#" .env  2>/dev/null || true
                  fi
              '''
          }
      }
    }
  }
}