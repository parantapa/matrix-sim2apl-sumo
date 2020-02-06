#!/bin/python3

import os, subprocess
import time

java = "java"
jar = "../../../target/sim2apl-SUMO-simulation-1.0-SNAPSHOT-jar-with-dependencies.jar"
# net = "bilt/debilt.sumo.cfg"
net = "eichstaedt/eichstaedt.sumo.cfg"

agentSeed = 42

timestamp = time.strftime("%Y-%m-%d-%H.%M.%S")

iterations = 5000
ncars = [200, 2000]
rich = [0, 25, 50, 75, 100]
speed_reduction_factor = [1.3]
min_gap = [1]

# iterations = 10
# ncars = [50, 100, 200, 300, 400, 500, 600, 700, 800, 900]
# rich = [25]
# speed_reduction_factor = [1.3]
# min_gap = [0.5, 1, 2]

startTime = time.time()

timeFile = open(timestamp + '.times', 'w', 1)

for n in ncars:
	for r in rich:
		for srf in speed_reduction_factor:
			for mg in min_gap:

				logdir = "../../../output/experiment/stats_" + timestamp + "/rich"+ str(r) + "-speed" + str(srf) + "-gap" + str(mg) + "-agents" + str(n) + "/"

				medium = str(0)
				poor = str(100-r)
				args = [java, "-jar", jar, "-s", "sumo", "-c", net, "-i", str(iterations), "--step-length", str(1), 
				"--statistics-directory", logdir,
					"--number-of-cars", str(n), "--rich", str(r), "--medium", medium, "--poor", poor,
					"--speed-reduction", str(srf), "--min-gap", str(mg), "--agent-seed", str(agentSeed), "--full-statistics"]

				expStartTime = time.time()
				subprocess.call(args)
				took = time.time() - expStartTime
				timeFile.write(f'{took} seconds for {n} agents in {net} with {r}% rich and a minimum gap of {mg}\n')

				wd = os.getcwd()
				os.chdir(logdir)
				files = {'agent' : '', 'routes' : '', 'emission' : ''}
				for f in os.listdir("."):
					for k in files.keys():
						if k in f:
							files[k] = f

				subprocess.call([wd + "/extractValues.py", files['routes'], files['agent'], files['emission']])
				os.chdir(wd)
tookTotal = time.time() - startTime
timeFile.write(f'total execution time was {tookTotal} seconds\n')
timeFile.close()