@AfterStep
def call(context){
  node{
    withCredentials([string(credentialsId: 'wechat_token', variable: 'WECHAT_TOKEN')]){
      switch(context.status){
          case null: // no result set yet means success
          case "SUCCESS":
            sh '''
              USER_NAME=$(git show -s --pretty=%an)
              curl -s 'http://qyapi.weixin.qq.com/cgi-bin/webhook/send?key=${WECHAT_TOKEN}' \
                  -H 'Content-Type: application/json' \
                  -d '
                  {
                    "msgtype": "markdown",
                    "markdown": {
                        "content": "<font color=\"info\">Build Success: by ${USER_NAME}</font>\n>JOB_NAME: ${env.JOB_NAME}"
                    }
                  }'
            '''
            break;
          case "FAILURE":
            sh '''
              USER_NAME=$(git show -s --pretty=%an)
              curl -s 'http://qyapi.weixin.qq.com/cgi-bin/webhook/send?key=${WECHAT_TOKEN}' \
                  -H 'Content-Type: application/json' \
                  -d '
                  {
                    "msgtype": "markdown",
                    "markdown": {
                        "content": "<font color=\"warning\">Build Failure: by ${USER_NAME}</font>\n>JOB_NAME: ${env.JOB_NAME}"
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