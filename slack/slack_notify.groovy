@AfterStep
def call(context){
    switch(context.status){
        case null: // no result set yet means success
        case "SUCCESS":
          notifyStage('good', "Build Successful.")
          break;
        case "FAILURE":
          notifyStage('#ff0000', "Build Failure.")
          break;
        default:
          echo "Slack Notifier doing nothing: ${context.status}"
    }
}

def notifyStage(color, status_message) {
  def icons = [":unicorn_face:", ":beer:", ":bee:", ":man_dancing:",
    ":party_parrot:", ":ghost:", ":dancer:", ":scream_cat:"]
  def randomIndex = (new Random()).nextInt(icons.size())
  def message = "@here Build <${env.BUILD_URL}|${currentBuild.displayName}> " +
      "${status_message} ${icons[randomIndex]}"
  slackSend message: ${message}, color: ${color}
}
