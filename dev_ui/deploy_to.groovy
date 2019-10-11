void call(app_env){
  node{
    stage("Notify to build ReferenceUI: ${app_env.short_name}") {
      if (app_env.short_name == 'dev') {
        steps.build job: '../openlmis-reference-ui/master', wait: false
        currentBuild.result = "SUCCESS"
      } else if (app_env.short_name == 'qa'){
        steps.build job: '../openlmis-reference-ui/master', wait: false
        currentBuild.result = "SUCCESS"
      } else {
        steps.build job: '../openlmis-reference-ui/release_phase1', wait: false
        currentBuild.result = "SUCCESS"
      }
        // steps.build job: '../openlmis-reference-ui/master', wait: false
    }
  }
}

