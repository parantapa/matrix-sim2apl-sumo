#!/bin/python3

import os, re, argparse, subprocess

SUMO_HOME= os.environ.get("SUMO_HOME")
tripGenerator = SUMO_HOME + "/tools/randomTrips.py"

findRegex = r'^\s+<vehicle id="(\d+)".*\n(\s)+<route'
replaceRegex = r'\2<route id="\1"'

def run(args):
	routeFile = args.routesfile
	if not (routeFile and os.path.isfile(routeFile)):
		generateTrips(args)

	if args.collapseStart:
		collapseStart(args)
	elif args.routesOnly:
		updateTripFile(args)

	print("Finished!")
	print("Add '%s' to your SUMO configuration file as additional file to use with 2APL" % routeFile)

def openRoutes(args):
	with open(args.routesfile, 'r') as routes:
		return routes.read() 

def collapseStart(args):
	routes = openRoutes(args)
	replaced, nsubs = re.subn(r'depart="([\d\.]+)"', 'depart="0.00"', routes)

	if nsubs == 0:
		print("Routes file did not contain vehicles. Not changed")
		exit(0)
	
	writeOutput(replaced, args)

	print("Success! All of the %d vehicles now start their route at time step 0.00" % nsubs)

def updateTripFile(args):
	routes = openRoutes(args)

	replaced, nsubs = re.subn(findRegex, replaceRegex, routes, flags=re.MULTILINE)
	replaced, vehiclesRemoved = re.subn(r'^\s*</vehicle>\s*\n', r'', replaced, flags=re.MULTILINE)

	if nsubs == 0 and vehiclesRemoved == 0:
		print("Routes file did not contain vehicles. Not changed")
		exit(0)
	elif nsubs != vehiclesRemoved:
		print("Something went wrong. Please remove all generated files and try again")
		exit(1)

	writeOutput(replaced, args)

	print("Sucessfully removed %d vehicles from the same number of routes. %s now only contains routes, without vehicles associated with them"% (nsubs, args.routesfile))

def writeOutput(content, args):
	output = args.tripfile if args.tripfile else args.routesfile

	with open(output, 'w') as out:
		out.write(content)

def generateTrips(args):
	if args.sumo:
		SUMO_HOME = args.sumo

	md = [tripGenerator, '-l', '-r', args.routesfile]

	if not args.netfile:
		exitError("Netfile is required when no existing routes file is given")
	else:
		md += ["-n", str(args.netfile)]
	if args.end:
		md += ["-e", str(args.end)]
	if args.seed:
		md += ["-s", str(args.seed)]

	subprocess.call(md)

def exitError(message):
	print(message)
	exit(1)

def parseArgs():
    parser = argparse.ArgumentParser(prog="python3 createRoutes.py", description="This program uses SUMO tools to generate routes or trips for use with or without 2APL\n" + 
	"To take leverage of full functionalit, use the tripGenerator.py file provided by SUMO and pass the generated file using '-r'\n\n" +
	"""In the 2APL SUMO connection, agents can randomly select a route from a set of generated routes. This
	is to avoid startup time of the simulation. SUMO provides a tool, randomTrips.py (in sumo-home/tools/),
	but this tool generates trips, not routes. This means when added to the SUMO configuration, SUMO
	will automatically spawn cars for those trips. To avoid this when using in conjunction with 2APL,
	this script automatically removes cars from generated routes files.""")
    parser.add_argument("-d", "--directory-sumo-home", type=str, dest="sumo", help="The SUMO directory on your file system")
    parser.add_argument("-n", "--net-file", dest="netfile", help="define the net file (mandatory)")
    parser.add_argument("-r", "--routes-file", default="routes.route.xml", type=str, dest="routesfile", help="Use an existing routes file, instead of generating a new one from the net file")
    parser.add_argument("-o", "--output-routes", dest="tripfile", help="define the output routes filename. If unspecified, input file or generated routes file will be overwritten")
    parser.add_argument("-e", "--end", type=float, default=3600, help="end time (default 3600)")
    parser.add_argument("-s", "--seed", type=int, help="random seed")
    parser.add_argument("-c", "--collapse-start", type=bool, dest="collapseStart", help="If this flag is added, all trips will start at time 0, instead of one by one, as is the default in randomTrips.py")
    parser.add_argument("-ro", "--routes-only", type=bool, dest="routesOnly", help="If this flag is added, trips will be removed. The file will only generate routes")

    return parser.parse_args()

if __name__=="__main__":
	run(parseArgs())
