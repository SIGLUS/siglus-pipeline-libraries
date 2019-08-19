void call(app_env){
  stage("Integrated with ReferenceUI: ${app_env.long_name}") {
    node{ 
      build job: 'openlmis-reference-ui/master', wait: false
    }
  }
}

@AfterStep
void notify_build_reference_ui(context){
    if (context.step.equals("build_app")){
        build job: 'openlmis-reference-ui/master', wait: false
    }
}