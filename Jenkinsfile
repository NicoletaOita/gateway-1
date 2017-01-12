
  node('java') {
	  
	  echo ${env.JOB_NAME}
	stage("Tests") {
          sh 'pwd'
	  sh 'ls'
	  sh 'ls ..'
          sh 'ls -LRl'
	  sh 'ls -LRl .. '
		
	  sh 'mvn -q -B -U install -DskipTests -DskipITs -P-docker'
		
	}
  }
