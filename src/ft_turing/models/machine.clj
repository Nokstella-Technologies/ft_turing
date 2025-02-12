(ns ft-turing.models.machine
  (:require [schema.core :as s]))

(s/defn NonEmptySet [s]
  (s/constrained s not-empty))

(defn single-char-string? [s]
  (and (string? s) (= (count s) 1)))

(defn single-char? [chars]
  (every? #(and (string? %) (= (count %) 1)) chars))

(s/defn ValidAlphabet [s]
  (and (s/constrained s not-empty) (s/constrained s single-char? )))

(s/def TransitionSchema
  {:read s/Str
   :to_state  s/Str
   :write  s/Str
   :action  (s/enum "LEFT" "RIGHT")})

(s/def StateTransitionsSchema
  {s/Keyword  (s/constrained [TransitionSchema] not-empty)})

(s/defschema Machine
  {:name s/Str
   :alphabet  (ValidAlphabet [s/Str])
   :blank   (s/constrained  s/Str single-char-string?)
   :states  (NonEmptySet [s/Str])
   :initial  s/Str
   :finals  (NonEmptySet [s/Str])
   :transitions  (NonEmptySet StateTransitionsSchema)})
