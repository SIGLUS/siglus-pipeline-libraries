void call(app_env){
  node{
    stage("Integrated with ReferenceUI: ${app_env.long_name}") {
        build job: '../openlmis-reference-ui/master', wait: false
    }
  }
}

