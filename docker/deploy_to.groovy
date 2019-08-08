void call(app_env){
  stage "Deploy to ${app_env.long_name}", {
      node{
            withCredentials([file(credentialsId: 'setting_env', variable: 'SETTING_ENV')]) {
            sh '''
                rm -f docker-compose*
                wget https://raw.githubusercontent.com/SIGLUS/openlmis-ref-distro/master/docker-compose.yml
                GIT_REVISION=$(git rev-parse HEAD)
                IMAGE_VERSION=${BUILD_NUMBER}-${GIT_REVISION}
                PROJECT_NAME=${JOB_NAME%/*}
                IMAGE_NAME=siglusdevops/${PROJECT_NAME#*-}:${IMAGE_VERSION}
                rm -f settings.env
                cp $SETTING_ENV settings.env
                sed -i "s#<APP_ENV>#${app_env.short_name}#g" settings.env
                export OL_${PROJECT_NAME#*-}_VERSION=${IMAGE_NAME}
                printenv
                # docker-compose -f docker-compose.yml -p openlmis-ref-distro up -d --force-recreate ${PROJECT_NAME#*-}
            '''
            }
        }
  }
}
