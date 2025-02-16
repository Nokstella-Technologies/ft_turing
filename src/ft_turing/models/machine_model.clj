(ns ft-turing.models.machine-model
  (:require [schema.core :as s]))

(s/defn NonEmptySet [s]
  (s/constrained s not-empty))

(defn single-char-string? [s]
  (and (string? s) (= (count s) 1)))

(defn single-char? [chars]
  (every? #(and (string? %) (= (count %) 1)) chars))

(s/defn ValidAlphabet [s]
  (and (s/constrained s not-empty) (s/constrained s single-char? )))

(defn initial-in-states? [{:keys [initial states]}]
  (some #(= initial %) states))

(defn finals-subset-of-states? [{:keys [finals states]}]
  (every? #(some #{%} states) finals))


(defn in-alphabet?
  [alphabet chr]
  (and (not (nil? chr))
       (some #(= chr %) alphabet)))

(defn print_error [bool str]
  (when (not bool)
    (println str))
  bool)

(defn validate-transitions-field-by-vec? [{:keys [transitions]} vector-to-verify field]
  (every? (fn [[_ state-transitions]]
            (every? (fn [transition]
                      (print_error (in-alphabet? vector-to-verify (field transition)) transition))
                    state-transitions))
          transitions))

(defn valid-machine? [machine]
  (and (print_error (in-alphabet? (:alphabet machine) (:blank machine)) "Error: blank is not in alphabet")
       (print_error (initial-in-states? machine) "Error: initial state is not in state" )
       (print_error (validate-transitions-field-by-vec? machine (:alphabet machine) :write) "Error: transition was some :write field with not valid alphabet")
       (print_error (validate-transitions-field-by-vec? machine (:alphabet machine) :read) "Error: transition was some :read field with not valid alphabet")
       (print_error (validate-transitions-field-by-vec? machine (:states machine) :to_state) "Error: transition was some :to_state field with not valid alphabet")
       (print_error (finals-subset-of-states? machine) "Error: finals not a subset of state")))

(s/def TransitionSchema
  {:read s/Str
   :to_state  s/Str
   :write  s/Str
   :action  (s/enum "LEFT" "RIGHT")})

(s/def StateTransitionsSchema
  {s/Keyword  (s/constrained [TransitionSchema] not-empty)})

(s/defschema Machine
  (s/constrained
  {:name s/Str
   :alphabet  (ValidAlphabet [s/Str])
   :blank   (s/constrained  s/Str single-char-string?)
   :states  (NonEmptySet [s/Str])
   :initial  s/Str
   :finals  (NonEmptySet [s/Str])
   :transitions  (NonEmptySet StateTransitionsSchema)}
  valid-machine? "machine is bad write, valid one of this conditions"))
