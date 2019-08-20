@AfterStep
def call(context){
  node{
    withCredentials([string(credentialsId: 'wechat_token', variable: 'WECHAT_TOKEN')]){
      switch(context.status){
          case null: // no result set yet means success
          case "SUCCESS":
            sh '''
              GIT_USER_NAME=$(git show -s --pretty=%an)
              cat << EOF > data.json
{"msgtype": "markdown","markdown": {"content": "<font color=\\"info\\">${JOB_NAME} Build Success: commit by ${GIT_USER_NAME}</font>\\n>[Details](${RUN_DISPLAY_URL})"}}
EOF
            '''
            sh '''
                curl -s "https://qyapi.weixin.qq.com/cgi-bin/webhook/send?key=${WECHAT_TOKEN}" \
                  -H 'Content-Type: application/json' \
                  -d @./data.json
              '''
            break;
          case "FAILURE":
            sh '''
              GIT_USER_NAME=$(git show -s --pretty=%an)
              cat << EOF > data.json
{"msgtype": "markdown","markdown": {"content": "<font color=\\"warning\\">${JOB_NAME} Build Failure: commit by ${GIT_USER_NAME}</font>\\n>[Details](${RUN_DISPLAY_URL})","mentioned_list":[\"@all\"]}}
EOF
            '''
            sh '''
              curl -s "https://qyapi.weixin.qq.com/cgi-bin/webhook/send?key=${WECHAT_TOKEN}" \
                  -H 'Content-Type: application/json' \
                  -d @./data.json
            '''
            break;
          default:
            echo "WeChat Notifier doing nothing: ${context.status}"
      }
    }
  }
}