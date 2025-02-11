(ns ft-turing.validation-args
  (:require [ft-turing.json-parser :as json-parser]))


(defn show-help []
  (println "usage: ft_turing [-h | --help] jsonfile input")
  (println "positional arguments:")
  (println "  jsonfile    json description of the machine")
  (println "  input       input of the machine")
  (println "optional arguments:")
  (println "  -h, --help  show this help message and exit"))


(defn validate-help [args]
    [(contains? #{"--help" "-h"} (first args)) (fn [] (show-help) (System/exit 0))])

(defn validate-arg-numbers [args]
    [(not= 2 (count args)) (fn [] (println "Error: Expected exactly 2 arguments, the jsonfile and input.")
             (show-help)
             (System/exit 1))])

(defn validate-file-exists [json-file]
    [(not (.exists (clojure.java.io/file json-file))) (fn [] (println "Error: JSON file does not exist.") (System/exit 1))])



(defn validate-input [args machine]
  true)


(defn validate-config-file-and-input [args]
  (let [json    (json-parser/load-json (first args))
        machine (json-parser/parse-turing-machine json)]
  [(validate-input args machine) (fn [] machine)]))

(defn validate-args [args]
  )