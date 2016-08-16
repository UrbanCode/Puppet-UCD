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
def ch = new CommandHelper(workDir);

def props = apTool.getStepProperties();

def commandPath = props['commandPath'];
def includes = props['includes']//.split("\\n");
def excludes = props['excludes'].split("\\n");
def additionalArgs = props['additionalArgs'].split("\\n")


def apply = {file ->
    def args = [commandPath, "apply", file.absolutePath]
    for (arg in additionalArgs) {
        args << arg.trim()
    }

    ch.runCommand("Applying manifest : " + file.absolutePath, args);
}

def ant = new AntBuilder();

scanner = ant.fileScanner {
    fileset(dir:"${workDir.canonicalPath}") {
        for (include in includes) {
            ant.include(name:include)
        }

        if (excludes != null) {
            for (exclude in excludes) {
                ant.exclude(name:exclude)
            }
        }
    }
}

def results = []
for (file in scanner) {
    results << file
}

results.sort().each { file ->
    apply(file);
}

if (results.size() == 0) {
    println "[Warning] No manifests were found!"
}
System.exit(0)