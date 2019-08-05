def call(){
  stage "Building Docker Image", {
    node{
      unstash "workspace"

      login_to_registry()

      def images = get_images_to_build()
      images.each{ img ->
        sh "docker build ${img.context} -t ${img.registry}/${img.repo}:${img.tag}"
        sh "docker push ${img.registry}/${img.repo}:${img.tag}"
      }

    }
  }
}
