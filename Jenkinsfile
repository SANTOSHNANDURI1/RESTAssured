
pipeline {

    agent {
        node {
            label 'maven'
        }
    }


 stages {
	stage("Deploy the Latest Build") {
            steps {
                script {
                    scmVars = checkout scm
                    // jenkins environment variables
                    echo "Build Number is: ${env.BUILD_NUMBER}"
                    echo "Branch name is: ${env.GIT_BRANCH}"
                    mvnHome = tool name: 'apache-maven', type: 'maven'
                }
            }
        }
		stage("Execute the Automation Tests") {
            steps {
                script {

                    test_config = readFile("src/test/resources/serenity.properties")
                    writeFile file: "src/test/resources/serenity.properties", text: "${test_config}"

                    configFileProvider([configFile(fileId: 'default_maven_settings', variable: 'MAVEN_SETTINGS')]) {

                            sh "${mvnHome}/bin/mvn -s ${MAVEN_SETTINGS} verify"

                    }
                }
            }
        }

stage ("Generate Jacoco Reports"){
	steps {
            script {
				configFileProvider([configFile(fileId: 'default_maven_settings', variable: 'MAVEN_SETTINGS')]) {
	    sh "${mvnHome}/bin/mvn -s ${MAVEN_SETTINGS} org.jacoco:jacoco-maven-plugin:report"
					}
			}
		}
	}

	stage('SonarQube Test Code analysis') {
	steps {

                 script {
		withSonarQubeEnv('Sonarqube') {
						configFileProvider([configFile(fileId: 'default_maven_settings', variable: 'MAVEN_SETTINGS')]) {

						sh "${mvnHome}/bin/mvn -s ${MAVEN_SETTINGS} sonar:sonar"
                        }


						}
					}
	 }


			}
			stage("Initialise NexusIQ") {
                steps {
                    script {
                        scmVars = checkout scm
                        // jenkins environment variables
                        echo "Build Number is: ${env.BUILD_NUMBER}"
                        echo "Git commit is: ${scmVars.GIT_COMMIT}"
                        // Get latest commit message
                        GIT_COMMIT_MSG = sh(script: "git log -1 --format=%B ${scmVars.GIT_COMMIT}", returnStdout: true).trim()
                        // What is the App Name
                        APP_NAME = readMavenPom().getArtifactId()
                        GROUP_NAME = readMavenPom().getGroupId()
                        NEXUS_IQ_APPLICATION_ID = "${APP_NAME}_${GROUP_NAME}"
                        echo "App being deployed is '${APP_NAME}' within Maven 'groupId' of '${GROUP_NAME}'"
                        mvnHome = tool name: 'apache-maven', type: 'maven'
                    }
                }
            }

			stage("Execute NexusIQ vulnerability Scan") {
                steps {
                    script {
                        echo "Scanning (${APP_NAME}) with NexusIQ AppID of (${NEXUS_IQ_APPLICATION_ID})"
                        nexusPolicyEvaluation advancedProperties: "",
                                enableDebugLogging: false,
                                failBuildOnNetworkError: false,
                                iqScanPatterns: [[scanPattern:  '**/pom.xml' ]],
                                iqApplication: selectedApplication("${NEXUS_IQ_APPLICATION_ID}"),
                                iqStage: "build",
                                jobCredentialsId: "nexus_iq_credentials"
                    }
                }
            }
			stage("publish the Serenity report") {
                steps {
                    script {
						publishHTML(target: [
						reportName : 'Serenity',
						reportDir:   'target/site/serenity',
						reportFiles: 'index.html',
						keepAll:     true,
						alwaysLinkToLastBuild: true,
						allowMissing: false
						])
						}
					}
				}

        }
            post {
                    success {
                        echo "Heritage API's - Automation Test has Passed"
						echo "Check Jenkins Console Output for More Details"
                            mail to: "nanduri.s@heritage.com.au",
                                subject: "Heritage API's - Automation Test has Passed: ${currentBuild.fullDisplayName}",
                                body: " Automation Test has Passed, For detailed Logs Please Check ${env.BUILD_URL}"
                    }

                    failure {
                        echo "Heritage API's - Automation Test has Failed"
                        echo "Check Console Output for More Details"
                            mail to: "nanduri.s@heritage.com.au",
                                subject: "Heritage API's - Automation Test has Failed: ${currentBuild.fullDisplayName}",
                                body: " Automation Test has Failed, For detailed Logs Please Check ${env.BUILD_URL}"

                    }
                 }
   }

