(ns ft-turing.validation-args
  (:require [ft-turing.json-parser :as json-parser]
            [ft-turing.models.machine :as models.machine]
            [schema.core :as s]))


(defn show-help []
  (println "usage: ft_turing [-h | --help] jsonfile input")
  (println "positional arguments:")
  (println "  jsonfile    json description of the machine")
  (println "  input       input of the machine")
  (println "optional arguments:")
  (println "  -h, --help  show this help message and exit"))


(defn validate-help [args]
  (when (contains? #{"--help" "-h"} (first args))
      (show-help) (System/exit 0))
  args)

(defn validate-arg-numbers [args]
  (when (not= 2 (count args))
    (println "Error: Expected exactly 2 arguments, the jsonfile and input.")
     (show-help)
     (System/exit 1))
    args)

(defn validate-file-exists [args]
  (when (not (.exists (clojure.java.io/file (first args))))
    (println "Error: JSON file does not exist.") (show-help) (System/exit 1))
  args)

(s/defn validate-config-file-and-input :- models.machine/Machine
  [args :- [s/Str]]
  (let [json    (json-parser/load-json (first args))
        machine (json-parser/parse-turing-machine json)]
    machine))

(s/defn validate-input  :- models.machine/Machine
  [machine :- models.machine/Machine
   str :- s/Str]
  machine)

(s/defn validate-args :- models.machine/Machine
  [args]
  (-> args
      (validate-help)
      (validate-arg-numbers)
      (validate-file-exists)
      (validate-config-file-and-input)
      (validate-input  (second args))
      ))