(defproject com.stuartsierra/log.dev "0.2.1-SNAPSHOT"
  :description "Quick-start logging for local development using Logback and SLF4J"
  :url "https://github.com/stuartsierra/log.dev"
  :dependencies
  [;; Use Logback as the main logging implementation:
   [ch.qos.logback/logback-classic "1.2.3"]
   [ch.qos.logback/logback-core "1.2.3"]

   ;; Logback implements the SLF4J API:
   [org.slf4j/slf4j-api "1.7.30"]

   ;; Redirect Apache Commons Logging to Logback via the SLF4J API:
   [org.slf4j/jcl-over-slf4j "1.7.30"]

   ;; Redirect Log4j 1.x to Logback via the SLF4J API:
   [org.slf4j/log4j-over-slf4j "1.7.30"]

   ;; Redirect Log4j 2.x to Logback via the SLF4J API:
   [org.apache.logging.log4j/log4j-to-slf4j "2.13.0"]

   ;; Redirect OSGI LogService to Logback via the SLF4J API
   [org.slf4j/osgi-over-slf4j "1.7.30"]

   ;; Redirect java.util.logging to Logback via the SLF4J API.
   ;; Requires installing the bridge handler, see README:
   [org.slf4j/jul-to-slf4j "1.7.30"]
   ]

  :exclusions
  [;; Exclude transitive dependencies on all other logging
   ;; implementations, including other SLF4J bridges.
   commons-logging
   log4j
   org.apache.logging.log4j/log4j
   org.slf4j/simple
   org.slf4j/slf4j-jcl
   org.slf4j/slf4j-nop
   org.slf4j/slf4j-log4j12
   org.slf4j/slf4j-log4j13
   ]
  
  :profiles {:dev {:dependencies [[org.clojure/clojure "1.10.1"]
                                  [org.clojure/tools.logging "0.5.0"]
                                  [org.clojure/test.check "0.10.0"]]}}
  :deploy-repositories [["releases" :clojars]]
  :license {:name "The MIT License"
            :url "http://opensource.org/licenses/MIT"
            :distribution :repo})
