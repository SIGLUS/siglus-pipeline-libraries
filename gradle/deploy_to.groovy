void call(app_env){
    stage "Deploy to ${app_env.long_name}", {
      node {
        if (app_env.short_name == 'dev') {
            echo "******This is deploy for ${app_env.long_name}******"
            deploy app_env
        } else {   
            try {
                timeout ( time: 30, unit: "MINUTES" ) {
                    input(message: 'Deploy this build ?')
                }
                echo "******This is deploy for ${app_env.long_name}******"
                deploy app_env
            } catch (err) {
              def user = err.getCauses()[0].getUser()
              if('SYSTEM' == user.toString()) { //timeout
                currentBuild.result = "SUCCESS"
              }
            }
        }
      }
    }     
}

def deploy(app_env){
    withCredentials([file(credentialsId: "setting_env_${app_env.short_name}", variable: 'SETTING_ENV')]) {
      withEnv(["APP_ENV=${app_env.short_name}", "DOCKER_HOST=tcp://${app_env.hosts}:2376"]) {
        sh '''
            rm -f docker-compose*
            rm -f .env
            rm -f settings.env
            wget https://raw.githubusercontent.com/SIGLUS/openlmis-ref-distro/master/docker-compose-aws-${APP_ENV}.yml
            sed -i "s#openlmis#siglusdevops#g" docker-compose-aws-${APP_ENV}.yml
            SHORT_GIT_REVISION=$(git rev-parse --short HEAD)
            IMAGE_VERSION=${BUILD_NUMBER}-${SHORT_GIT_REVISION}
            PROJECT_NAME=${JOB_NAME%/*}
            SERVICE_NAME=${PROJECT_NAME#*-}
            IMAGE_NAME=`echo "OL_${SERVICE_NAME}_VERSION" |  tr '[:lower:]' '[:upper:]'`
            IMAGE_NAME=${IMAGE_NAME}-${BRANCH_NAME}
            cp $SETTING_ENV settings.env
            sed -i "s#<APP_ENV>#${APP_ENV}#g" settings.env
            echo "${IMAGE_NAME}=${IMAGE_VERSION}" > .env
            echo "Start deregister ${SERVICE_NAME} on ${APP_ENV} consul"
            curl -s http://${APP_ENV}.siglus.us:8500/v1/health/service/${SERVICE_NAME} | \
            jq -r '.[] | "curl -XPUT http://${APP_ENV}.siglus.us:8500/v1/agent/service/deregister/" + .Service.ID' > clear.sh
            chmod a+x clear.sh && ./clear.sh
            echo "Start deploy ${SERVICE_NAME} on ${APP_ENV}"
            docker-compose -H ${DOCKER_HOST} -f docker-compose-aws-${APP_ENV}.yml -p openlmis-ref-distro up --no-deps --force-recreate -d ${SERVICE_NAME}
        '''
      }
    }
}