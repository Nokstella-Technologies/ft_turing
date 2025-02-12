(ns ft-turing.models.machine
  (:require [schema.core :as s]))


(def TransitionSchema
  {:read {:schema s/Str :required true}
   :to_state {:schema s/Str :required true}
   :write {:schema s/Str :required true}
   :action {:schema (s/enum "LEFT" "RIGHT") :required true}})

(def StateTransitionsSchema
  {s/Str [TransitionSchema]})

(s/defschema Machine
  {:name {:schema s/Str :required true}
   :alphabet {:schema [s/Str] :required true}
   :blank {:schema s/Str :required true}
   :states {:schema [s/Str] :required true}
   :initial {:schema s/Str :required true}
   :finals {:schema [s/Str] :required true}
   :transitions {:schema StateTransitionsSchema :required true}})
