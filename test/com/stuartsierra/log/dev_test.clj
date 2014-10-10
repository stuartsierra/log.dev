(ns com.stuartsierra.log.dev-test
  (:require [clojure.java.io :as io]
            [clojure.tools.logging]
            [clojure.test.check :as tc]
            [clojure.test.check.generators :as gen]
            [clojure.test.check.properties :as prop]
            [clojure.test.check.clojure-test :refer [defspec]])
  (:import (org.slf4j.bridge SLF4JBridgeHandler)))

(set! *warn-on-reflection* true)

(SLF4JBridgeHandler/removeHandlersForRootLogger)
(SLF4JBridgeHandler/install)

(def ^String logger-name
  "com.stuartsierra.log.dev-test")

(defmulti emit-log
  (fn [impl & _] impl))

(defmethod emit-log :tools.logging [_ level message]
  (clojure.tools.logging/logp level message))

(defmethod emit-log :slf4j [_ level message]
  (let [logger (org.slf4j.LoggerFactory/getLogger logger-name)]
    (case level
      :trace (.trace logger message)
      :debug (.debug logger message)
      :info (.info logger message)
      :warn (.warn logger message)
      :error (.error logger message)
      ;; SLF4J has no FATAL level
      :fatal (.error logger message))))

(defmethod emit-log :log4j1 [_ level message]
  (let [logger (org.apache.log4j.Logger/getLogger logger-name)]
    (case level
      :trace (.trace logger message)
      :debug (.debug logger message)
      :info (.info logger message)
      :warn (.warn logger message)
      :error (.error logger message)
      :fatal (.fatal logger message))))

(defmethod emit-log :log4j2 [_ level message]
  (let [logger (org.apache.logging.log4j.LogManager/getLogger logger-name)]
    (case level
      :trace (.trace logger message)
      :debug (.debug logger message)
      :info (.info logger message)
      :warn (.warn logger message)
      :error (.error logger message)
      :fatal (.fatal logger message))))

(defmethod emit-log :commons [_ level message]
  (let [logger (org.apache.commons.logging.LogFactory/getLog logger-name)]
    (case level
      :trace (.trace logger message)
      :debug (.debug logger message)
      :info (.info logger message)
      :warn (.warn logger message)
      :error (.error logger message)
      :fatal (.fatal logger message))))

(defmethod emit-log :jul [_ level message]
  (let [logger (java.util.logging.Logger/getLogger logger-name)]
    (case level
      :trace (.finest logger message)
      :debug (.finer logger message)
      ;; java.util.logging also has level FINE, not used here
      :info (.info logger message)
      :warn (.warning logger message)
      :error (.severe logger message)
      ;; java.util.logging has no FATAL level
      :fatal (.severe logger message))))

(def gen-level
  (gen/elements [:fatal :error :warn :info :debug :trace]))

(def gen-impl
  (gen/elements [:tools.logging :slf4j :log4j1 :log4j2 :commons :jul]))

(defn ^java.io.File default-log-file []
  (io/file "log" "dev.log"))

(defn clear-log-file []
  (.mkdirs (.getParentFile (default-log-file)))
  (spit (default-log-file) ""))

(defn log-file-contains? [message]
  (.contains ^String (slurp (default-log-file)) message))

(defspec log-one-message-any-level
  1000  ; number of iterations for test.check
  (prop/for-all [impl gen-impl
                 level gen-level
                 message (gen/not-empty gen/string)]
    (clear-log-file)
    (emit-log impl level message)
    (log-file-contains? message)))

(defn log-fast
  "Creates a future which logs a lot of messages, as quickly as
  possible. Returns a function to stop logging. Use for testing file
  rotation."
  []
  (let [running? (atom true)]
    (future
      (while @running?
        (let [impls (gen/sample gen-impl 100)
              levels (gen/sample gen-level 100)
              messages (gen/sample (gen/resize 100 gen/string-alpha-numeric) 100)]
          (dorun (map emit-log impls levels messages)))))
    (fn [] (reset! running? false))))
