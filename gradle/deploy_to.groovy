void call(app_env){
    stage "Deploy to ${app_env.long_name}", {
      node {
        if (app_env.short_name ==~ 'dev|integ') {
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
      withEnv(["APP_ENV=${app_env.short_name}", "CONSUL_HOST=${app_env.hosts}:8500","DOCKER_HOST=tcp://${app_env.hosts}:2376"]) {
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
            cp $SETTING_ENV settings.env
            sed -i "s#<APP_ENV>#${APP_ENV}#g" settings.env
            echo "${IMAGE_NAME}=${IMAGE_VERSION}" > .env
            CONTAINER_NAME=$(docker ps -a| grep ${SERVICE_NAME} | awk '{print $NF}')
            echo "Start deregister ${SERVICE_NAME} on ${APP_ENV} consul"
            docker -H ${DOCKER_HOST} exec ${CONTAINER_NAME} node consul/registration.js -c deregister -f consul/config.json -r consul/api-definition.yaml 
            docker -H ${DOCKER_HOST} stop ${CONTAINER_NAME}

            echo "Start deploy ${SERVICE_NAME} on ${APP_ENV}"
            docker-compose -H ${DOCKER_HOST} -f docker-compose-aws-${APP_ENV}.yml -p openlmis-ref-distro up --no-deps --force-recreate -d ${SERVICE_NAME}
        '''
      }
    }
}