(ns ft-turing.services.machine
  (:require [ft-turing.models.machine-model :as models.machine]
            [ft-turing.models.tape :as models.tape]
            [schema.core :as s]))

(defn validate-infinite-loop [actions]
   (let [last-10 (take-last 10 actions) ;; Pega os últimos 10 elementos
         temp-array (reduce (fn [acc action]
                              (if (or (empty? acc) ;; Adiciona se acc estiver vazio
                                      (not= (first acc) action)) ;; Ou se o primeiro não for igual ao atual
                                (conj acc action)
                                (reduced acc))) ;; Para quando encontrar repetição
                            []
                            last-10)
         pattern-size (count temp-array)] ;; Tamanho do padrão identificado
     (if (and (> pattern-size 1) ;; O padrão deve ter pelo menos 2 elementos
              (= (take-last (* 2 pattern-size) last-10) ;; Pega os últimos 2 padrões
                 (concat temp-array temp-array))) ;; Compara com o padrão duplicado
       true
       false)))

  (defn make-action
  [action head]
  (if (= action "LEFT")
    (dec head)
    (inc head)))

(s/defn halted? :- (s/maybe s/Str)
  [finals new_state head tape]
  (cond
    (nil? new_state) (str "error: failed to take next action")
    (= (count tape) (+ head 1)) (str "error: infinite loop")
    (and (= head 0) (= (:action new_state) "LEFT")) (str "error: the machine try to move the tape behind the start of the tape")
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
    (println (format "[%s] (%s) -> (%s)" formatted-tape, state-info, state-action-str)))
  tape-real)


(s/defn get-new-state
  [state-str tape head transitions]
  (get (get transitions (keyword state-str))  (keyword (str (nth tape head)))))

(s/defn take-action
  [{:keys [tape head state-str transitions finals blank] :as tape-real}]

  (let [new-state (get-new-state state-str tape head transitions)
        new-halted (halted? finals new-state head tape)]
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