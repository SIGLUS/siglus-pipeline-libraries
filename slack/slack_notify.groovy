@AfterStep
void call(context){
    if (context.step.equals("build") && context.status.equals("FAILURE")){
        slackSend color: '#ff0000', message: "Build Failure: ${env.JOB_URL}"
    }
}