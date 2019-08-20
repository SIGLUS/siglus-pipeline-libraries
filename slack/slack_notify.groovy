@AfterStep
def call(context){
    switch(context.status){
        case null: // no result set yet means success
        case "SUCCESS":
            def icons = [":unicorn_face:", ":beer:", ":bee:", ":man_dancing:",
                ":party_parrot:", ":ghost:", ":dancer:", ":scream_cat:"]
            def randomIndex = (new Random()).nextInt(icons.size())
            def message = "@here Build ${env.JOB_NAME} ${env.STAGE_NAME} <${env.BUILD_URL}|${currentBuild.displayName}> " +
                "Build Success. ${icons[randomIndex]}"
            slackSend message: "${message}", color: 'good'
          break;
        case "FAILURE":
            def icons = [":unicorn_face:", ":beer:", ":bee:", ":man_dancing:",
                ":party_parrot:", ":ghost:", ":dancer:", ":scream_cat:"]
            def randomIndex = (new Random()).nextInt(icons.size())
            def message = "@here Build ${env.JOB_NAME} ${env.STAGE_NAME} <${env.BUILD_URL}|${currentBuild.displayName}> " +
                "Build Failure. ${icons[randomIndex]}"
            slackSend message: "${message}", color: '#ff0000'
          break;
        default:
          echo "Slack Notifier doing nothing: ${context.status}"
    }
}
