@AfterStep
def call(){
    switch(currentBuild.result.status){
        case null: // no result set yet means success
        case "SUCCESS":
          slackSend color: "good", message: "Build Successful: ${env.JOB_URL}"
          break;
        case "FAILURE":
          slackSend color: '#ff0000', message: "Build Failure: ${env.JOB_URL}"
          break;
        default:
          echo "Slack Notifier doing nothing: ${currentBuild.result}"
    }
}