void call(){
  stage('Testing'){
    node{
      println "grunt: pmd_test()"
      sh '''
          NODE_CACHE_ROOT=/ebs/node-caches/
          wget https://raw.githubusercontent.com/SIGLUS/openlmis-ref-distro/master/settings-sample.env -O .env
          if [ "$GIT_BRANCH" != "master" ]; then
              sed -i '' -e "s#^TRANSIFEX_PUSH=.*#TRANSIFEX_PUSH=false#" .env  2>/dev/null || true
          fi
          export "UID=`id -u jenkins`"
          export DOCKERHUB_ORG=siglusdevops
          PROJECT_NAME=${JOB_NAME%/*}
          SERVICE_NAME=${PROJECT_NAME#*-}
          docker-compose pull
          docker-compose down --volumes
          docker-compose run --entrypoint /dev-ui/build.sh ${SERVICE_NAME}
      '''
    }
  }
}