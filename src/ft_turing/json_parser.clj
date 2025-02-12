(ns ft-turing.json-parser
  (:require [cheshire.core :as json]
            [clojure.java.io :as io]
            [ft-turing.models.machine :as models.machine]
            [schema.core :as s]))

(defn load-json
  [filename]
  (try
    (with-open [rdr (io/reader filename)]
      (json/parse-stream rdr true))
    (catch Exception e
      (println "Error: Could not read the JSON file.")
      (System/exit 1))))

(s/defn parse-turing-machine :- models.machine/Machine
  [json]
  {:name        (get json :name)
   :alphabet    (get json :alphabet)
   :blank       (get json :blank)
   :states      (get json :states)
   :initial     (get json :initial)
   :finals      (get json :finals)
   :transitions (get json :transitions)})

(s/defn validate-machine
  [machine]
  (let [fails (s/check models.machine/Machine machine)]
    fails))