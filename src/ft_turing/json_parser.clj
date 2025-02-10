(ns ft-turing.json-parser
  (:require [cheshire.core :as json]
            [clojure.java.io :as io]))

(defn load-json
  [filename]
  (with-open [rdr (io/reader filename)]
    (json/parse-stream rdr true)))

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

(defn -main []
  (let [json (load-json "resources/json.json")
        machine (parse-turing-machine json)]
    (if (validate-machine machine)
      (println "Máquina de Turing válida!" machine)
      (println "Erro na estrutura do JSON."))))
