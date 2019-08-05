void call(){
  def (image_repo, image_repo_cred) = get_registry_info()

  withCredentials([usernamePassword(credentialsId: image_repo_cred, passwordVariable: 'pass', usernameVariable: 'user')]) {
    sh "echo ${pass} | docker login -u ${user} --password-stdin ${image_repo}"
  }
}