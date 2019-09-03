void call(context){
   stage('wait'){
    node{
           try {
                timeout ( time: 24, unit: "HOURS" ) {
                    input(message: 'Deploy this build to QA?')
                }
            } catch (err) {
              def user = err.getCauses()[0].getUser()
              if('SYSTEM' == user.toString()) { //timeout
                currentBuild.result = "SUCCESS"
              }
            }
    }
   }
}

