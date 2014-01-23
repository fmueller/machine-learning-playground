resultFreq <- ftable(wmdata$result)
print(resultFreq / apply(resultFreq, 1, sum))
