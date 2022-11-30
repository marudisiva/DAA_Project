import matplotlib.pyplot as plt
import numpy as np

f = open("/Users/sivareddy/Desktop/runtime.txt",'r')
print("file opened successfully")
textLength=[]
runTimeRK=[]
runTimeKM=[]
for line in f:
    print(line)
    values = line.split(" ")
    textLength.append(int(values[0]))
    runTimeRK.append(int(values[1]))
    runTimeKM.append(int(values[2]))

print(textLength)
print(runTimeRK)
print(runTimeKM)

fig, ax = plt.subplots()
maxiTime=0
if (max(runTimeRK) > max(runTimeKM)):
    maxiTime = max(runTimeRK)
else:
    maxiTime = max(runTimeKM)

ax.plot(textLength, runTimeRK, linewidth=3.0)
ax.plot(textLength, runTimeKM,linewidth=3.0)
ax.set(xlim=(0, max(textLength)),ylim=(0, maxiTime),ylabel="Run time in Milliseconds",xlabel="Text Length")

plt.show()