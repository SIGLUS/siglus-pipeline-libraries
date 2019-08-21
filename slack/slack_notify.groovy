@AfterStep
def call(context){
    node{
        def commitUser = sh(returnStdout: true, script: "git show -s --pretty=%an").trim()
        def commitChangeset = this.getChangeString()
        def icons = [":unicorn_face:", ":beer:", ":bee:", ":man_dancing:",
            ":party_parrot:", ":ghost:", ":dancer:", ":scream_cat:"]
        def randomIndex = (new Random()).nextInt(icons.size())
        switch(context.status){
            case null: // no result set yet means success
            case "SUCCESS":
            break;
            case "FAILURE":
                def message = "@here ${env.JOB_NAME} <${env.BUILD_URL}|${currentBuild.displayName}> commit by ${commitUser}; " +
                    "Build Failure. ${icons[randomIndex]}\n" + "```${commitChangeset}```"
                slackSend message: "${message}", color: '#ff0000'
            break;
            default:
                echo "Slack Notifier doing nothing: ${context.status}"
        }
       if (currentBuild.result == null) {
        currentBuild.result = 'SUCCESS'
       }
    }
}

@NonCPS
def getChangeString() {
    MAX_MSG_LEN = 100
    def changeString = ""

    echo "Gathering SCM changes"
    def changeLogSets = currentBuild.changeSets
    for (int i = 0; i < changeLogSets.size(); i++) {
        def entries = changeLogSets[i].items
        for (int j = 0; j < entries.length; j++) {
            def entry = entries[j]
            truncated_msg = entry.msg.take(MAX_MSG_LEN)
            changeString += " - ${truncated_msg} [${entry.author}]\n"
        }
    }
    if (!changeString) {
        changeString = " - No new changes"
    }
    return changeString
}