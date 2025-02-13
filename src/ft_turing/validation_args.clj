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
        machine (json-parser/parse-turing-machine json)
        fails   (json-parser/validate-machine machine)]
    (when fails
      (println "Parser Json Error: JSON parser fails.", fails) (System/exit 1))
    machine))

(s/defn validate-input  :- models.machine/Machine
  [machine :- models.machine/Machine
   fita :- s/Str]
  (let [alphabet (set (:alphabet machine))
        invalid-chars (filter #(not (contains? alphabet (str %))) fita)]
    (when (not (empty? invalid-chars))
      (println "Parser Json Error: Chars invalids in the input") (println invalid-chars) (System/exit 1))
      machine))

(s/defn validate-transitions-was-all-keys-needed :- models.machine/Machine
  [machine :- models.machine/Machine]
  (let [action-state (remove (set (:finals machine)) (:states machine))]
    (when (not (every?
                 (fn [x] (contains? (:transitions machine) (keyword x)))
                 action-state))
      (println "Parser Json Error: one of the states that is not finals is without any action") (println action-state) (System/exit 1)))
    machine)


(s/defn validate-transitions-was-final-action :- models.machine/Machine
  [machine :- models.machine/Machine]
  (let [{:keys [ finals transitions]} machine
        finals-set (set finals)]
    (if (some #(some (comp finals-set :to_state) (val %)) transitions)
              machine
      ((println "Parser Json Error: one of the transitions need to has a final action") (System/exit 1)))))

(s/defn validate-transitions-was-action-just-states :- models.machine/Machine
  [machine :- models.machine/Machine]
  (let [{:keys [ states transitions]} machine
        states-set (set states)]
    (if (every? #(every? (comp states-set :to_state) (val %)) transitions)
      machine
      ((println "Parser Json Error: all transitions action need to be an state") (System/exit 1)))))

(s/defn validate-args :- models.machine/Machine
  [args]
  (-> args
      (validate-help)
      (validate-arg-numbers)
      (validate-file-exists)
      (validate-config-file-and-input)
      (validate-transitions-was-all-keys-needed)
      (validate-transitions-was-final-action)
      (validate-transitions-was-action-just-states)
      (validate-input (second args))))