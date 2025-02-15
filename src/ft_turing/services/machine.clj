(ns ft-turing.services.machine
  (:require [ft-turing.models.machine :as models.machine]
            [ft-turing.models.tape :as models.tape]
            [schema.core :as s]))

(s/defn print-tape  [{:keys [tape head state-str state]}]

  (let [tape-str (apply str tape)
        formatted-tape (str
                         (subs tape-str 0 head)    ;; Parte da fita antes do cabeçote
                         "<"                      ;; Indica a posição do cabeçote
                         (nth tape head)      ;; Símbolo sob o cabeçote
                         ">"                      ;; Indica o fim do cabeçote
                         (subs tape-str (inc head))) ;; Parte da fita após o cabeçote
        state-info (str state-str ", " (nth tape head ".") )
        state-action-str (str (:to_state state) ", " (:write state) ", " (:action state))]
    (printf "[%s] (%s) -> (%s)" formatted-tape, state-info, state-action-str)))

(s/defn get-new-state [state-str tape head transitions]
  (get (get transitions (keyword state-str)) (keyword (nth tape head))))


(s/defn take-action
  [{:keys [tape head state state-str halted transitions finals]}]
  (let [new-state (get-new-state state-str tape halted transitions)])
  (merge tape {:halted "final"} ))


(s/defn machine-controller
  [machine :- models.machine/Machine
   input :- s/Str]
    (loop [tape (models.tape/init-Tape machine input)]
      (print-tape tape)
      (if (string? (:halted tape))
        (recur (take-action tape))
        (:halted tape)
    )))