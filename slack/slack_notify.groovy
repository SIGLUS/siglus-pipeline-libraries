@AfterStep
def call(context){
    node{
        def commitUser = sh(returnStdout: true, script: "git show -s --pretty=%an").trim()
        def commitChangeset = sh(returnStdout: true, script: 'git diff-tree --no-commit-id --name-status -r HEAD').trim()
        def icons = [":unicorn_face:", ":beer:", ":bee:", ":man_dancing:",
            ":party_parrot:", ":ghost:", ":dancer:", ":scream_cat:"]
        def randomIndex = (new Random()).nextInt(icons.size())
        switch(context.status){
            case null: // no result set yet means success
            case "SUCCESS":
                def message = "@here Build ${env.JOB_NAME} ${env.STAGE_NAME} <${env.BUILD_URL}|${currentBuild.displayName}> " +
                    "Build Success. ${icons[randomIndex]}\n" + "```${commitChangeset}```"
                slackSend message: "${message}", color: 'good'
            break;
            case "FAILURE":
                def message = "@here Build ${env.JOB_NAME} ${env.STAGE_NAME} <${env.BUILD_URL}|${currentBuild.displayName}> " +
                    "Build Failure. ${icons[randomIndex]}\n" + "```${commitChangeset}```"
                slackSend message: "${message}", color: '#ff0000'
            break;
            default:
            echo "Slack Notifier doing nothing: ${context.status}"
        }
    }
}
