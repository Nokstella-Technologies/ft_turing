(ns ft-turing.services.machine
  (:require [ft-turing.models.machine-model :as models.machine]
            [ft-turing.models.tape :as models.tape]
            [schema.core :as s]))
; TODO validar LEFT or RIGTH in machine
(defn make-action
  [action head]
  (if (= action "LEFT")
    (dec head)
    (inc head)))

(s/defn halted? :- (s/maybe s/Str)
  [finals new_state]
  (cond
    (nil? new_state) (str "error: failed to take next action")
    (some #(= % (:to_state new_state)) finals) (str "ok: the program came to an good ending")
    :else nil))

(s/defn print-tape
  [{:keys [tape head state-str state] :as tape-real}]
  (let [tape-str (apply str tape)
        formatted-tape (str
                         (subs tape-str 0 head)    ;; Parte da fita antes do cabeçote
                         "<"                      ;; Indica a posição do cabeçote
                         (nth tape head)      ;; Símbolo sob o cabeçote
                         ">"                      ;; Indica o fim do cabeçote
                         (subs tape-str (inc head))) ;; Parte da fita após o cabeçote
        state-info (str state-str ", " (nth tape head) )
        state-action-str (str (:to_state state) ", " (:write state) ", " (:action state))]
    (printf "[%s] (%s) -> (%s)\n" formatted-tape, state-info, state-action-str))
  tape-real)


; TODO validar tamanho do  head para nao dar underflow e overflow
(s/defn get-new-state
  [state-str tape head transitions]
  (get (get transitions (keyword state-str))  (keyword (str (nth tape head)))))

(s/defn take-action
  [{:keys [tape head state-str transitions finals] :as tape-real}]

  (let [new-state (get-new-state state-str tape head transitions)
        new-halted (halted? finals new-state)]
    (print-tape (merge tape-real {:state new-state}))
    (if (not (nil? new-halted))
      (merge tape-real {:halted new-halted})
        (merge tape-real {:state new-state
                      :state-str (:to_state new-state)
                      :tape (assoc tape head  (:write new-state))
                      :head (make-action (:action new-state) head)
                      :halted new-halted
                      }))))

(s/defn machine-controller
  [machine :- models.machine/Machine
   input :- s/Str]
    (loop [tape (models.tape/init-Tape machine input)]
      (if (string? (:halted tape))
        (:halted tape)
        (recur (take-action tape)))))