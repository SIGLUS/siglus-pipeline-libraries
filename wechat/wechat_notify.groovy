@AfterStep
def call(context){
  node{
    withCredentials([string(credentialsId: 'wechat_token', variable: 'WECHAT_TOKEN')]){
      switch(context.status){
          case null: // no result set yet means success
          case "SUCCESS":
            sh '''
              GIT_USER_NAME=$(git show -s --pretty=%an)
              curl -s "https://qyapi.weixin.qq.com/cgi-bin/webhook/send?key=${WECHAT_TOKEN}" \
                  -H 'Content-Type: application/json' \
                  -d '
                  {
                    "msgtype": "markdown",
                    "markdown": {
                        "content": "<font color=\"info\">Build Success: by ${GIT_USER_NAME}</font>\n>JOB_NAME: ${JOB_NAME}"
                    }
                  }'
            '''
            break;
          case "FAILURE":
            sh '''
              GIT_USER_NAME=$(git show -s --pretty=%an)
              curl -s "https://qyapi.weixin.qq.com/cgi-bin/webhook/send?key=${WECHAT_TOKEN}" \
                  -H 'Content-Type: application/json' \
                  -d '
                  {
                    "msgtype": "markdown",
                    "markdown": {
                        "content": "<font color=\"warning\">Build Failure: by ${GIT_USER_NAME}</font>\n>JOB_NAME: ${env.JOB_NAME}\n"
                    }
                  }'
            '''
            break;
          default:
            echo "WeChat Notifier doing nothing: ${context.status}"
      }
    }
  }
}