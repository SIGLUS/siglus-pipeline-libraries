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

      sh 'mkdir -p /ebs2/gradle-caches/${JOB_NAME}'
      sh 'mkdir -p /ebs2/node-caches/${JOB_NAME}'
      sh 'docker run --rm -u gradle -v ${PWD}:/app -w /app siglusdevops/gradle:4.10.3 gradle checkstyleMain checkstyleTest checkstyleIntegrationTest pmdMain pmdTest pmdIntegrationTest'
    }
  }
}