import os
import re
import numpy
import matplotlib.pyplot as pyplot

generations = []
average_results = []
max_results = []
min_results = []

working_directory = os.getcwd() + "\\experiment1";
route = os.listdir(working_directory);
route.sort()
for directory in route:
  if not os.path.isdir(working_directory + "\\" + directory):
    continue
  generation_number = re.findall("(\\d+)", directory)
  generations.append(int(generation_number[0]))
  file = open(working_directory + "/" + directory + "/summary.txt")
  lines = str.join("", file.readlines())
  compiled = re.compile("Average: (-?\d*\.\d*)")
  average_results.append(float(compiled.findall(lines)[0]))
  compiled = re.compile("Max: (-?\d+) - player: \d+")
  max_results.append(float(compiled.findall(lines)[0]))
  compiled = re.compile("Min: (-?\d+) - player: \d+")
  min_results.append(float(compiled.findall(lines)[0]))

generations, average_results, max_results, min_results = zip(*sorted(zip(generations, average_results, max_results, min_results)))

pyplot.subplot(211)
pyplot.plot(generations, average_results)
pyplot.plot(generations, min_results)

pyplot.subplot(212);
pyplot.plot(generations, max_results)
pyplot.show()