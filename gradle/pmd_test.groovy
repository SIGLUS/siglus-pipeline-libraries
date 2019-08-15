void call(){
  stage('PMD Testing'){
    node{
      println "gradle: pmd_test()"
      script {
          CURRENT_BRANCH = env.GIT_BRANCH // needed for agent-less stages
          def properties = readProperties file: 'gradle.properties'
          if (!properties.serviceVersion) {
              error("serviceVersion property not found")
          }
          VERSION = properties.serviceVersion
          STAGING_VERSION = properties.serviceVersion
          if (CURRENT_BRANCH != 'master' || (CURRENT_BRANCH == 'master' && !VERSION.endsWith("SNAPSHOT"))) {
              STAGING_VERSION += "-STAGING"
          }
          currentBuild.displayName += " - " + VERSION
      }

      sh 'mkdir -p /ebs/gradle-caches/${JOB_NAME}'
      sh 'mkdir -p /ebs/node-caches/${JOB_NAME}'
      sh 'docker run --rm -u gradle -v /ebs/gradle-caches/${JOB_NAME}:/home/gradle/.gradle/caches -v ${PWD}:/app -v /ebs/node-caches/${JOB_NAME}:/app/node_modules -w /app siglusdevops/gradle:4.10.3 gradle pmdMain pmdTest checkstyleMain checkstyleTest'
    }
  }
}