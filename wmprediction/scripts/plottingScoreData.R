score_means <- sapply(split(wmdata[,c("score.A", "score.B")], wmdata$year), colMeans, na.rm=TRUE)
years <- dimnames(score_means)[[2]]

plot(score_means[1,], col="blue", type="o", axes = FALSE, ann = FALSE)
lines(score_means[2,], col="red", type="o", pch=22, lty=2)

axis(1, at=1:length(years), lab=years)
axis(2, at = c(0, 0.5, 1.0, 1.5, 2.0, 2.5, 3.0, 3.5, 4.0))

title(xlab = "Year")
title(ylab = "No. of Goals")

box()

legend("topleft", c("Home Team mean.", "Away Team mean."), cex=0.8, col=c("blue","red"), pch=21:22, lty=1:2)
