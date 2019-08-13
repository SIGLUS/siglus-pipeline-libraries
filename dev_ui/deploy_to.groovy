void call(app_env){
  stage "Integrated with ReferenceUI: ${app_env.long_name}", {
      node{
        build job: 'openlmis-reference-ui/master', wait: false
      }
  }
}