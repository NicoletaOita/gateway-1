
properties([
	buildDiscarder(logRotator(artifactDaysToKeepStr: '10', artifactNumToKeepStr: '10', daysToKeepStr: '10', numToKeepStr: '10')),
  pipelineTriggers([])
  ])

  node('java') {
	stage("Tests") {
          sh 'pwd'
	  sh 'ls'
	  sh 'ls ..'
		
          sh 'ls -LRl'
	  sh 'ls -LRl .. '
	  sh 'mvn -q -B -U install -DskipTests -DskipITs -P-docker'
		
	}
  }
