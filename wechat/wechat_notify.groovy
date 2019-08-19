@AfterStep
def call(context){
    withCredentials([string(credentialsId: 'wechat_token', variable: 'WECHAT_TOKEN')])
    switch(context.status){
        case null: // no result set yet means success
        case "SUCCESS":
          this.post_message("info", "BUILD SUCCESS", ${WECHAT_TOKEN})
          break;
        case "FAILURE":
          this.post_message("warning", "Build Failure", ${WECHAT_TOKEN})
          break;
        default:
          echo "WeChat Notifier doing nothing: ${context.status}"
    }
}

void post_message(color, message, key_token){
  sh '''
  USER_NAME=$(git show -s --pretty=%an)
  curl 'http://qyapi.weixin.qq.com/cgi-bin/webhook/send?key=${key_token}' \
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
  '''
}