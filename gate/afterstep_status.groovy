@AfterStep
void call(context){
    if (currentBuild.result == null) {
      currentBuild.result = 'SUCCESS'
    }
}