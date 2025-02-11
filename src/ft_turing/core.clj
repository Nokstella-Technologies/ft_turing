(ns ft-turing.core
  (:require [ft-turing.json-parser :as json-parser])
  (:require [ft-turing.validation-args :as validation-args]))

; precisa ser 2 argumentos
; tem que ser a ordem certa, primeiro o json e depois a maquina
; se o primeiro arguemnto for o help ou h ele vai retornar o help, ele aceita so o help como argumento
; se ele nao encontrar o path do json ele vai dar um erro
; o segundo input tem que estar dentro do alfabeto do json

;(defn show-help []
;  (println "usage: ft_turing [-h | --help] jsonfile input")
;  (println "positional arguments:")
;  (println "  jsonfile    json description of the machine")
;  (println "  input       input of the machine")
;  (println "optional arguments:")
;  (println "  -h, --help  show this help message and exit"))


;(defn validate-args [args]
;  (cond
;    (contains? #{"--help" "-h"} (first args))
;    (do (show-help) (System/exit 0))
;
;
;    (not= 2 (count args))
;    (do (println "Error: Expected exactly 2 arguments, the jsonfile and input.")
;        (show-help)
;        (System/exit 1))
;
;    :else
;    (let [json-file (first args)]
;      (if (not (.exists (clojure.java.io/file json-file)))
;        (do (println "Error: JSON file does not exist.")
;            (System/exit 1))))))


(defn -main [& args]
  (println args)
  (let [machine (validation-args/validate-args args)]
    (if (json-parser/validate-machine machine)
      (println "Máquina de Turing válida!" machine)
      (println "Erro na estrutura do JSON."))))
