@AfterStep
void call(context){
    slackSend color: '#ff0000', message: "Build Failure: ${env.JOB_URL}"
}