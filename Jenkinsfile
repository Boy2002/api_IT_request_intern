@Library(["x-connext-docker-registry-jenkins-plugin@main", "x-connext-ops-bot-jenkins-plugin@main"]) _

def compress(tarName) {
  def parameters = [
    "Dockerfile",
    "build/libs/app.jar"
  ];

  return "tar -cvf ${tarName} ${parameters.join(' ')}";
}

def sendBuildNotify(buildStatus) {
    def dockerImageName = "${DOCKER_NAME}:${DOCKER_TAG}";
    def commit = sh(
        returnStdout: true,
        script: "git log -1 --pretty='%h'"
    ).trim();
    def commitBy = sh(
        returnStdout: true,
        script: "git log -1 --pretty='%an'"
    ).trim();
    def commitMessage = sh(
        returnStdout: true,
        script: "git log -1 --pretty='%s'"
    ).trim();
    def logUrl = "${env.BUILD_URL}console";

    sendBuildNotify(dockerImageName, commit, commitBy, commitMessage, buildStatus, logUrl);
}

pipeline {

    agent any

    triggers {
        bitbucketPush()
    }

    options {
        buildDiscarder(logRotator(numToKeepStr: '2'))
        disableConcurrentBuilds()
        disableResume()
    }

    environment {
        DOCKER_NAME = "it-request-service"
        DOCKER_TAG = env.BRANCH_NAME.replace("/", "-")
        JAVA_HOME = tool "JDK17"
//         X_CONNEXT_NEXUS = credentials('x-connext-nexus-ci')
//         ORG_GRADLE_PROJECT_xConnextNexusUsername = "${X_CONNEXT_NEXUS_USR}"
//         ORG_GRADLE_PROJECT_xConnextNexusPassword = "${X_CONNEXT_NEXUS_PSW}"
    }

    stages {
      stage('Checkout') {
        steps {
            checkout scm
        }
      }

      stage("Build") {
        steps {
          script {
            withEnv(["JAVA_HOME=${JAVA_HOME}"]) {
              def commands = [
                "rm -rf src/test/java/com/rider/it_request_service/ci",
                // "rm src/test/resources/application-gen-db-for-diff.yml",
                // "make generate-version",
                "chmod +x ./gradlew",
                "./gradlew clean build -x test"
              ];

              sh(commands.join(" && "));
            }
          }
        }
      }

      stage("Linter") {
          steps {
            script {
              withEnv(["JAVA_HOME=${JAVA_HOME}"]) {
                def commands = [
                  "./gradlew spotlessCheck"
                ];

                sh(commands.join(" && "));
              }
            }
          }
      }

      stage("Unit test") {
        steps {
          script {
            withEnv(["JAVA_HOME=${JAVA_HOME}"]) {
              def commands = [
                "./gradlew test"
              ];

              sh(commands.join(" && "));
            }
          }
        }
      }

//       stage("Quality analytics") {
//         steps {
//           script {
//             withSonarQubeEnv('SonarQube') {
//               withEnv(["JAVA_HOME=${JAVA_HOME}"]) {
//                 def commands = [
//                   "./gradlew sonar -D \"sonar.branch.name=${DOCKER_TAG}\""
//                 ];
//
//                 sh(commands.join(" && "));
//               }
//             }
//           }
//         }
//       }
//
//       stage("Quality gate") {
//         steps {
//           waitForQualityGate abortPipeline: true
//         }
//       }

      stage("Push image") {
        steps {
          script {
            withEnv(["JAVA_HOME=${JAVA_HOME}"]) {
              def TAR_NAME = "${DOCKER_NAME}-${DOCKER_TAG}.tar.gz";
              def commands = [
                compress(TAR_NAME),
                buildAndPush(
                  TAR_NAME,
                  DOCKER_NAME,
                  DOCKER_TAG
                )
              ];

              sh(commands.join(" && "));
            }
          }
        }
      }
    }

    post { 
        always { 
          script {
            def buildStatus = currentBuild.result
            sendBuildNotify(buildStatus == 'SUCCESS' || buildStatus == 'NOT_BUILT')
          }
        }
    }

}