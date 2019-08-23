def fetch_setting_env() {
    withCredentials([file(credentialsId: 'setting_env', variable: 'SETTING_ENV')]) {
        sh '''
            echo "clear env file"
            rm -f .env
            echo "create .env file"
            cp $SETTING_ENV .env
        '''
    }
}