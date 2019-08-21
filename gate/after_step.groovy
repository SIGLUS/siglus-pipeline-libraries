def call(context){
    script {
        if (currentBuild.result == null) {
            currentBuild.result = 'SUCCESS'
        }
    }
}