void call(app_env){
  stage "Deploy to ${app_env.long_name}", {
      node{
            withCredentials([file(credentialsId: 'setting_env', variable: 'SETTING_ENV')]) {
              withEnv(["APP_ENV=${app_env.short_name}", "DOCKER_HOST=tcp://${app_env.hosts}:2376"]) {
                sh '''
                    rm -f docker-compose*
                    rm -f .env
                    rm -f settings.env
                    wget https://raw.githubusercontent.com/SIGLUS/openlmis-ref-distro/master/docker-compose.yml
                    sed -i "s#openlmis#siglusdevops#g" docker-compose.yml
                    GIT_REVISION=$(git rev-parse HEAD)
                    IMAGE_VERSION=${BUILD_NUMBER}-${GIT_REVISION}
                    PROJECT_NAME=${JOB_NAME%/*}
                    SERVICE_NAME=${PROJECT_NAME#*-}
                    IMAGE_NAME=`echo "OL_${SERVICE_NAME}_VERSION" |  tr '[:lower:]' '[:upper:]'`
                    cp $SETTING_ENV settings.env
                    sed -i "s#<APP_ENV>#${APP_ENV}#g" settings.env
                    echo "${IMAGE_NAME}=${IMAGE_VERSION}" > .env
                    echo "Start deregister ${SERVICE_NAME} on ${APP_ENV} consul"
                    curl -s http://dev.siglus.us:8500/v1/health/service/${SERVICE_NAME} | \
                      jq -r '.[].Service.ID' | \
                      xargs curl -XPUT http://dev.siglus.us:8500/v1/agent/service/deregister/
                    echo "Start deploy ${SERVICE_NAME} on ${APP_ENV}"
                    docker-compose -H ${DOCKER_HOST} -f docker-compose.yml -p openlmis-ref-distro up --no-deps --force-recreate -d ${SERVICE_NAME}
                '''
              }
            }
        }
  }
}
