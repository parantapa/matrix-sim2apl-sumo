A demonstration of how to use 2APL reasoning agents to to control agents in the [SUMO](http://sumo.sourceforge.net/) simulation environment.

In this simple demo, agents try to maximize their speed, while keeping their CO2 emission under the average. 

# Requirements
* Java 10+
* Maven
* [Sim2APL](https://bitbucket.org/goldenagents/sim2apl/src/master/)
* [SUMO](http://sumo.sourceforge.net/)
* [TraaS](https://sumo.dlr.de/docs/TraCI/TraaS.html)

# Installation
First download and compile [Sim2APL](https://bitbucket.org/goldenagents/sim2apl/src/master/). The directory does not matter, since the compiled JAR will automatically be placed in your maven repository directory (on Linux: `~/.m2/repository`)

```bash
$ git clone https://bitbucket.org/goldenagents/sim2apl.git
$ cd sim2apl
$ mvn install
```

Next, install SUMO following the [SUMO installation instructions](https://sumo.dlr.de/docs/Downloads.html). For Windows, installers exist. For most Linux distributions, a PPA can be added, after which SUMO can be installed and kept up to date through your default package manager.

After the installation of SUMO, locate the TraaS jar file. According to Sumo, this jar is provided with most SUMO installations (e.g. `<SUMO_HOME>/bin/TraaS.jar`). If the file is not present, the SUMO source can be downloaded from Git: (https://github.com/eclipse/sumo)[https://github.com/eclipse/sumo]. It is then located in `<SUMO_GIT_DIR>/tools/contributed/traas/`.

Compile TraaS using maven, by navigating to the TraaS directory and running maven:

```bash
$ mvn install
```

Now, both Sim2APL and TraaS should be added to your local Maven repository, and you are ready to use the software. Clone this repository, and either open it in your favourite Java IDE, or install using maven and run the jar with dependencies.

```
$ git clone https://bitbucket.org/goldenagents/sim2apl-sumo.git
$ cd sim2apl-sumo
$ mvn install
$ java -jar target/sim2apl-SUMO-simulation-1.0-SNAPSHOT-jar-with-dependencies.jar -s sumo-gui -c src/main/resources/eichstaedt/eichstaedt.sumo.cfg --step-length 1 --number-of-cars 20
```

# Usage
```
usage: Sim2APL SUMO -s <SUMO binar> -c <Configuration File Location> [-i <number of iterations>] [-n <Network file location>] [--number-of-cars <number of cars> ] [--random-seed <random seed> ] [--statistics-file <statistics destination file> ]

 -s,--sumo-binary <SUMO binary>                          The exact location of the SUMO binary to execute the SUMO environment
 -c,--configuration-file <Configuration file location>   Loads the named config on startup
    
    --collision.action <none,warn,teleport,remove>       How to deal with collisions: [none,warn,teleport,remove]

 -i,--number-of-iterations <number of iterations>        The number of iterations / ticks the simulation should perform. If not specified, simulation will run until interrupted manually
 
 -n,--net-file <Network file location>                   Load road network description from FILE
 -p --car-id-prefix                                      Prefix of the car agent ID strings (Agents on different compute nodes should have diffrent prefix)
    
    --number-of-cars <number of cars>                    The number of cars to place in the environment
   
    --random-seed <Seed>                                 The seed to use for Random, so reproducibility can be ensured
 

   
    --statistics-file <Statistics destination file>      If specified, Sim2APL will track various statistics about the agents, and write these statisticsto a .csv file when the simulation has finished successfully
   
    --step-length <Step length in seconds>               Defines the step duration in seconds
    --use-matrix <Use Matrix>                            If true use Matrix
```
