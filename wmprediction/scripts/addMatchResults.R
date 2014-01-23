addMatchResults <- function(wmdata) {
	wmdata <- na.omit(wmdata)
	wmdata$result <- as.factor(
		ifelse(wmdata$score.A > wmdata$score.B, "H",
			ifelse(wmdata$score.A < wmdata$score.B, "A", "D")))
	wmdata
}
