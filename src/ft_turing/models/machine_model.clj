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


(defn blank-in-alphabet? [{:keys [blank alphabet]}]
  (and (not (nil? blank))
       (some #(= blank %) alphabet)))

(defn valid-machine? [machine]
  (and (blank-in-alphabet? machine)
       (initial-in-states? machine)
       (finals-subset-of-states? machine)))

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
  valid-machine? "machine is bad write, valid one of this conditions: blank alphabet, initial state in state, finals is a subset of state"))
