@BeforeStep
void call(context){
    node {
        step.when {
            allOf {
            expression { return currentBuild.result == null || currentBuild.result == 'SUCCESS' }
            }
        }
        sh 'echo ${context}'
        if (context.step.equals("Deploy to Qa")){

            timeout(time:5, unit:'DAYS') {
                input message:'Approve deployment to QA?' , ok: 'Yes'
            }
        }
    }
}