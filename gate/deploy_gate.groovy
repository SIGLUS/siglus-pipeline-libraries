@BeforeStep
void call(context){
    expression { return currentBuild.result == null || currentBuild.result == 'SUCCESS' }
}