void call(app_env){
  node{
    stage("Integrated with ReferenceUI: ${app_env.long_name}") {
      if (app_env.short_name == 'dev') {
        steps.build job: '../openlmis-reference-ui/master', wait: false
      } else if (app_env.short_name == 'qa'){
        steps.build job: '../openlmis-reference-ui/master', wait: false
      } else {
        steps.build job: '../openlmis-reference-ui/release_phase1', wait: false
      }
        // steps.build job: '../openlmis-reference-ui/master', wait: false
    }
  }
}

