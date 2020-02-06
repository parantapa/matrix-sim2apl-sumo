#!/bin/python3

import xml.etree.ElementTree as ET 
import numpy as np
import json, sys

class Vehicle():
	
	def __init__(self, file):
		self.tree = ET.parse(file)
		fcdExport = self.getRoot()
		values = self.extractValues(fcdExport)
		s = Statistics()
		timestepStatistics = s.calculateStatistics(values['timestepValues'])
		carStatistics = s.calculateStatistics(values['carValues'])
		overallStatistics = s.calculateStatistics({'speed' : values['speedValues']})

		s.writeToFile(timestepStatistics, "speedByTimestep")
		s.writeToFile(carStatistics, "speedByCar")
		s.writeToFile(overallStatistics, "overallSpeed")

		print("Speed values:")
		print(overallStatistics)
		print("\n")

	def getRoot(self):
		fcdExport = self.tree.getroot()

		if fcdExport.tag != "fcd-export":
			print(f"Warning, ROOT XML node was '{fcdExport.tag}'. 'fcd-export' expected.")
			exit(1)

		return fcdExport

	def extractValues(self, fcdExport):
		timestepValues = dict()
		carValues = dict()
		overall = []

		for timestep in fcdExport:
			t = float(timestep.attrib['time'])
			timestepValues[t] = []
			for vehicle in timestep:
				speed = float(vehicle.attrib['speed'])
				overall.append(t)
				timestepValues[t].append(speed)
				try:
					carValues[vehicle.attrib['id']].append(speed)
				except:
					carValues[vehicle.attrib['id']] = [speed]

		return {'timestepValues' : timestepValues, 'carValues' : carValues, 'speedValues' : overall}


class Routes():

	def __init__(self, file):
		self.tree = ET.parse(file)
		tripinfos = self.getRoot()
		values = self.extractValues(tripinfos)
		s = Statistics()
		statistics = s.calculateStatistics(values)
		s.writeToFile(statistics, "routeInfo")

		self.printHighlights(statistics)

	def getRoot(self):
		tripinfos = self.tree.getroot()
		if tripinfos.tag != "tripinfos":
			print(f"Warning, ROOT XML node was '{tripinfos.tag}'. 'tripinfos' expected.")
			exit(1)

		return tripinfos

	def extractValues(self, tripinfos):
		fields = ['depart', 'departDelay', 'arrival', 'arrivalSpeed', 'duration', 'routeLength', 'waitingTime', 'waitingCount', 'stopTime', 'timeLoss', 'speedFactor']
		values = dict(zip(fields, ([] for _ in range(len(fields)))))
		values['averageSpeed'] = []
		
		for tripinfo in tripinfos:
			for f in fields:
				values[f].append(float(tripinfo.attrib[f]))
				values['averageSpeed'].append(float(tripinfo.attrib['routeLength']) / float(tripinfo.attrib['duration']))

		return values

	def printHighlights(self, statistics):
		print("Trip time:")
		print(statistics['duration'])
		print('\n')
		print("Average speed:")
		print(statistics['averageSpeed'])
		print('\n')
		print("Time loss:")
		print(statistics['timeLoss'])
		print('\n')
		print('\n')

class Emission():

	def __init__(self, file):
		self.tree = ET.parse(file)
		emissionExport = self.getRoot()
		values = self.extractValues(emissionExport)
		s = Statistics()
		statistics = s.calculateStatistics(values)
		s.writeToFile(statistics, "emission")
		self.printHighlights(statistics)

	def getRoot(self):
		emissionExport = self.tree.getroot()
		if emissionExport.tag != "emission-export":
			print(f"Warning, ROOT XML node was '{emissionExport.tag}'. 'emission-export' expected.")
			exit(1)

		return emissionExport

	def extractValues(self, emissions):
		fields = "CO2", "CO", "HC", "NOx", "PMx", "fuel", "electricity", "noise", "waiting", "speed"
		values = dict(zip(fields, ([] for _ in range(len(fields)))))
		
		for timestep in emissions:
			for vehicle in timestep:
				for f in fields:
					values[f].append(float(vehicle.attrib[f]))

		return values

	def printHighlights(self, statistics):
		print("CO2 emission:")
		print(statistics['CO2'])
		print("\n")
		print("Noise:")
		print(statistics["noise"])
		print("\n")

class Statistics():

	def calculateStatistics(self, values):
		functions = {'average': np.average, 'median' : np.median, 'mean' : np.mean, 'std' : np.std, 'variance' : np.var}

		statistics = dict()

		for k in values.keys():
			statistics[k] = dict()
			for f in functions:
				statistics[k][f] = functions[f](values[k])

		return statistics

	def writeToFile(self, statistics, name):
		with open(name + ".json", 'w') as out:
			out.write(json.dumps(statistics))

		# TODO add csv?

if __name__ == '__main__':
	if len(sys.argv) > 0:
		routesFile = sys.argv[1]
		vehicleFile = sys.argv[2]
		emissionFile = sys.argv[3]

	r = Routes(routesFile)
	v = Vehicle(vehicleFile)
	e = Emission(emissionFile)