(ns ft-turing.json-parser
  (:require [cheshire.core :as json]
            [clojure.java.io :as io]))

(defn load-json
  [filename]
  (try
    (with-open [rdr (io/reader filename)]
      (json/parse-stream rdr true))
    (catch Exception e
      (println "Error: Could not read the JSON file.")
      (System/exit 1))))

(defn parse-turing-machine [json]
  {:name        (get json :name)
   :alphabet    (get json :alphabet)
   :blank       (get json :blank)
   :states      (get json :states)
   :initial     (get json :initial)
   :finals      (get json :finals)
   :transitions (get json :transitions)})

(defn validate-machine [machine]
  (println "parseado validacao  " machine)
  (and (string? (:name machine))
       (vector? (:alphabet machine))
       (string? (:blank machine))
       (vector? (:states machine))
       (string? (:initial machine))
       (vector? (:finals machine))
       (map? (:transitions machine))))
