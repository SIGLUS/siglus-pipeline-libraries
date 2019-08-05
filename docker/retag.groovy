void call(old_tag, new_tag){
    node{
        unstash "workspace"

        login_to_registry()

        get_images_to_build().each{ img ->
          sh "docker pull ${img.registry}/${img.repo}:${old_tag}"
          sh "docker tag ${img.registry}/${img.repo}:${old_tag} ${img.registry}/${img.repo}:${new_tag}"
          sh "docker push ${img.registry}/${img.repo}:${new_tag}"
        }
    }
}