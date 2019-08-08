void call(app_env){
  stage "Deploy to ${app_env.long_name}", {
      node{
            withCredentials([file(credentialsId: 'setting_env', variable: 'SETTING_ENV')]) {
              withEnv(["APP_ENV=${app_env.short_name}", "DOCKER_HOST=tcp://${app_env.hosts}:2376"]) {
                sh '''
                    rm -f docker-compose*
                    wget https://raw.githubusercontent.com/SIGLUS/openlmis-ref-distro/master/docker-compose.yml
                    sed -i "s#openlmis#siglusdevops#g" docker-compose.yml
                    GIT_REVISION=$(git rev-parse HEAD)
                    IMAGE_VERSION=${BUILD_NUMBER}-${GIT_REVISION}
                    PROJECT_NAME=${JOB_NAME%/*}
                    SERVICE_NAME=${PROJECT_NAME#*-}
                    IMAGE_NAME=`echo "OL_${SERVICE_NAME}_VERSION" |  tr '[:lower:]' '[:upper:]'`
                    rm -f settings.env
                    cp $SETTING_ENV settings.env
                    sed -i "s#<APP_ENV>#${APP_ENV}#g" settings.env
                    eval ${IMAGE_NAME}=${IMAGE_VERSION}
                    docker-compose -H ${DOCKER_HOST} -f docker-compose.yml -p openlmis-ref-distro up --no-deps --force-recreate -d ${SERVICE_NAME}
                '''
              }
            }
        }
  }
}
