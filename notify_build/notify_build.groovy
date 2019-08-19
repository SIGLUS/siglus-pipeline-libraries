@AfterStep
void call(context){
    if (context.step.equals("build_app")){
        build job: 'openlmis-reference-ui/master', wait: false
    }
}