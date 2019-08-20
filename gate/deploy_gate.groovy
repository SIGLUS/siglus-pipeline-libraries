@BeforeStep
void call(context){
    if (context.step.equals("Deploy to Qa")){
        when {
            allOf {
            expression { return currentBuild.result == null || currentBuild.result == 'SUCCESS' }
            }
        }
        timeout(time:5, unit:'DAYS') {
            input message:'Approve deployment to QA?'
        }
    }
}