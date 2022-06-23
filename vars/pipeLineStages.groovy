def smokeStage(def caller) {

	switch(true) {
		case (!caller.includeSmokeExecution):
			println pipeLineConstants.SMOKE_SKIP_MESSAGE1
			return
		case (!caller.continueExecution):
			println pipeLineConstants.SMOKE_SKIP_MESSAGE2
			return
		case caller.smokeMap.isEmpty():
			println pipeLineConstants.SMOKE_SKIP_MESSAGE3
			return
	}
	searchUtility.updateSearchMap(caller.smokeMap,caller)
	caller.smokeMap.each{
		if(caller.currentBuild.result.toString().trim().equalsIgnoreCase(pipeLineConstants.SUCCESS) || caller.healthCheck.toString().trim().equalsIgnoreCase(pipeLineConstants.SKIP)) {
			def smokeJobName = it.key
			def jobBuildParam=it.value
			List<hudson.model.ParameterValue> paramValueList=pipeLineUtil.parseBuildInputParam(jobBuildParam)
			def build = build job:smokeJobName,propagate: false,parameters:paramValueList
			reportUtil.updateBuildResult(caller,build,smokeJobName)
			reportUtil.setCurrentBuildStatus(caller,build)
			reportDataAllRow=reportUtil.updateReportData(caller,build,smokeJobName)
		}
	}
}
