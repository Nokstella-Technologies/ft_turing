(ns ft-turing.models.tape
  (:require [schema.core :as s]
            [ft-turing.models.machine :as models.machine]))

(s/def StateReadMapTransition
  {s/Keyword (s/constrained models.machine/TransitionSchema not-empty)})

(s/def StateTransitionTape
  {s/Keyword  (s/constrained StateReadMapTransition not-empty)})

(s/def Tape
  {:tape s/Any
   :head  s/Int
   :state-str s/Str
   :state models.machine/TransitionSchema
   :transitions StateTransitionTape
   :finals [s/Str]
   :halted s/Str})

(s/defn init-Tape :- Tape
  [machine :- models.machine/Machine
   input :- s/Str]
  {:tape (vec (str input (apply str (repeat 10 (:blank machine)))))
   :head 0
   :state-str (:initial machine)
   :state (first (filter #(= (:read %) (str (nth input 0))) (get (:transitions machine) (keyword (:initial machine)))))
   :transitions (map (fn [[state transitions]]
                       [state (into {} (map (juxt :read #(dissoc % :read)) transitions))])
                     (:transitions machine))
   :finals (:finals machine)
   :halted nil})
