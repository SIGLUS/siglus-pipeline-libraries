void call(app_env){
  stage("Integrated with ReferenceUI: ${app_env.long_name}") {
    node{ 
      build job: 'openlmis-reference-ui/master', wait: false
    }
  }
}

@AfterStep
void call(context){
    if (context.step.equals("build_app")){
        build job: 'openlmis-reference-ui/master', wait: false
    }
}