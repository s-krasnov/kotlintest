import jetbrains.buildServer.configs.kotlin.v2019_2.*
import jetbrains.buildServer.configs.kotlin.v2019_2.buildSteps.dockerCommand
import jetbrains.buildServer.configs.kotlin.v2019_2.projectFeatures.buildReportTab
import jetbrains.buildServer.configs.kotlin.v2019_2.triggers.vcs
import jetbrains.buildServer.configs.kotlin.v2019_2.vcs.GitVcsRoot

/*
The settings script is an entry point for defining a TeamCity
project hierarchy. The script should contain a single call to the
project() function with a Project instance or an init function as
an argument.

VcsRoots, BuildTypes, Templates, and subprojects can be
registered inside the project using the vcsRoot(), buildType(),
template(), and subProject() methods respectively.

To debug settings scripts in command-line, run the

    mvnDebug org.jetbrains.teamcity:teamcity-configs-maven-plugin:generate

command and attach your debugger to the port 8000.

To debug in IntelliJ Idea, open the 'Maven Projects' tool window (View
-> Tool Windows -> Maven Projects), find the generate task node
(Plugins -> teamcity-configs -> teamcity-configs:generate), the
'Debug' option is available in the context menu for the task.
*/

version = "2021.2"

project {
    description = "Contains all other projects"

    params {
        password("env.TestParam", "credentialsJSON:d1f3356a-dc0a-452c-8f0a-637238c0c65d")
    }

    features {
        buildReportTab {
            id = "PROJECT_EXT_1"
            title = "Code Coverage"
            startPage = "coverage.zip!index.html"
        }
    }

    cleanup {
        baseRule {
            preventDependencyCleanup = false
        }
    }

    subProject(TestProject)
    subProject(TestVars)
}


object TestProject : Project({
    name = "TestProject"

    vcsRoot(TestProject_HttpsGithubComTimurbHttpPongGitRefsHeadsMaster)

    buildType(TestProject_HttpPong)
})

object TestProject_HttpPong : BuildType({
    name = "http-pong"

    vcs {
        root(TestProject_HttpsGithubComTimurbHttpPongGitRefsHeadsMaster)

        cleanCheckout = true
    }

    steps {
        dockerCommand {
            commandType = build {
                source = file {
                    path = "Dockerfile"
                }
            }
            param("dockerImage.platform", "linux")
        }
    }

    triggers {
        vcs {
        }
    }
})

object TestProject_HttpsGithubComTimurbHttpPongGitRefsHeadsMaster : GitVcsRoot({
    name = "https://github.com/timurb/http-pong.git#refs/heads/master"
    url = "https://github.com/timurb/http-pong.git"
    branch = "refs/heads/master"
    branchSpec = "refs/heads/*"
})


object TestVars : Project({
    name = "TestVars"

    params {
        param("env.TestParam", "credentialsJSON:d4fadc46-724e-435a-a79c-758800e85d08")
    }

    subProject(TestVars_TestVarsSub1)
})


object TestVars_TestVarsSub1 : Project({
    name = "TestVarsSub_1"
})
