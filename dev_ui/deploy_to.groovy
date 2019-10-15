void call(app_env){
  node{
    stage("Notify to build ReferenceUI: ${app_env.short_name}") {
      if (app_env.short_name == 'dev') {
        steps.build job: '../openlmis-reference-ui/master', wait: false
      } else if(app_env.short_name == 'integ') {
        steps.build job: '../openlmis-reference-ui/release_phase1', wait: false 
      } else {
        println "******  Do nothing! ******"
        // steps.build job: '../openlmis-reference-ui/release_phase1', wait: false
      }
    }
  }
}

