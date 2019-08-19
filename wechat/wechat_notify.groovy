@AfterStep
def call(context){
  node{
    switch(context.status){
        case null: // no result set yet means success
        case "SUCCESS":
          this.post_message("info", "BUILD SUCCESS")
          break;
        case "FAILURE":
          this.post_message("warning", "Build Failure")
          break;
        default:
          echo "WeChat Notifier doing nothing: ${context.status}"
    }
  }
}

void post_message(color, message){
  withCredentials([string(credentialsId: 'wechat_token', variable: 'WECHAT_TOKEN')]){
    sh '''
    USER_NAME=$(git show -s --pretty=%an)
    cat <<EOT >> send.sh
    curl -s 'http://qyapi.weixin.qq.com/cgi-bin/webhook/send?key=${env.WECHAT_TOKEN}' \
          -H 'Content-Type: application/json' \
          -d '
          {
            "msgtype": "markdown",
            "markdown": {
                "content": "<font color=\"${color}\">${message}: by ${USER_NAME}</font>\n
                >JOB_NAME: ${env.JOB_NAME} \n
                >[JOB_URL](${env.JOB_URL}) \n"
            }
          }'
    EOT
    chmod a+x send.sh && send.sh
    '''
  }
}