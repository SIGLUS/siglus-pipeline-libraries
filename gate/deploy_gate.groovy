@BeforeStep
void call(context){
    node {
        expression { return currentBuild.result == null || currentBuild.result == 'SUCCESS' }
        if (context.step.equals("Deploy to QA")){
            timeout(time:5, unit:'DAYS') {
                input message:'Approve deployment to QA?' , ok: 'Yes'
            }
        }
    }
}