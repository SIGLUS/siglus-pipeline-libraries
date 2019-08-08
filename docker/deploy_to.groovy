void call(app_env){
  stage "Deploy to ${app_env.long_name}", {
    withCredentials([file(credentialsId: 'setting_env', variable: 'SETTING_ENV')]) {
      sh '''
        wget https://raw.githubusercontent.com/SIGLUS/openlmis-ref-distro/master/docker-compose.yml
        GIT_REVISION=$(git rev-parse HEAD)
        IMAGE_VERSION=${BUILD_NUMBER}-${GIT_REVISION}
        PROJECT_NAME=${JOB_NAME%/*}
        IMAGE_NAME=siglusdevops/${PROJECT_NAME#*-}:${IMAGE_VERSION}
        rm -f settings.env
        cp $SETTING_ENV settings.env
        sed -i "s#<APP_ENV>#${app_env.short_name}#g" settings.env
        DEPLOY_SERVICE=OL_${PROJECT_NAME#*-}_VERSION
        eval "${DEPLOY_SERVICE^^}"='${IMAGE_NAME}'
        printenv
      '''
    }
  }
}
