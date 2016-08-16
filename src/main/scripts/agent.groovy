/**
 * © Copyright IBM Corporation 2014, 2016.
 * This is licensed under the following license.
 * The Eclipse Public 1.0 License (http://www.eclipse.org/legal/epl-v10.html)
 * U.S. Government Users Restricted Rights:  Use, duplication or disclosure restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import com.urbancode.air.AirPluginTool;
import com.urbancode.air.CommandHelper;

def apTool = new AirPluginTool(this.args[0], this.args[1])
def workDir = new File(".");
def ch = new CommandHelper(workDir)

def props = apTool.getStepProperties()
def commandPath = props['commandPath']

def baseArgs = {
    def oneTime = props['oneTime']
    def verbose = props['verbose']
    def daemonize = props['daemonize']

    def cmdArgs = []
    cmdArgs << commandPath
    cmdArgs << "agent"
    
    
    if (oneTime != "null") {
        cmdArgs << oneTime
    }
    
    if (verbose != "null") {
        cmdArgs << verbose
    }
    
    cmdArgs << daemonize
    
    return cmdArgs
}

def agent = { ->
    def args = baseArgs()
    def additionalArgs = props['additionalArgs']

    for (arg in additionalArgs.split("\\n")) {
        args << arg.trim()
    }

    ch.runCommand("Running puppet command:", args)
}

agent()

System.exit(0)