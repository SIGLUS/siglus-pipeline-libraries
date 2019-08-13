@Library('pipeline-build-step')
import org.jenkinsci.plugins.workflow.support.steps.build
void call(app_env){
  stage("Integrated with ReferenceUI: ${app_env.long_name}") {
    build job: 'openlmis-reference-ui/master', wait: false
  }
}