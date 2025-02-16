(ns ft-turing.core
  (:require [ft-turing.validation-args :as validation-args]
            [ft-turing.services.machine :as service-machine]))

(defn print-machine [machine]
  (let [{:keys [name alphabet states initial finals transitions]} machine
        separator (apply str (repeat 80 "*"))
        print-header (fn []
                       (println separator)
                       (println (format "* %-76s *" " "))
                       (println (format "* %-76s *" name))
                       (println (format "* %-76s *" " "))
                       (println separator))
        print-section (fn [label values]
                        (println (str label ": [" (clojure.string/join ", " values) "]")))
        print-transition (fn [state {:keys [read to_state write action]}]
                           (println (format "(%s, %s) -> (%s, %s, %s)"
                                            state read to_state write action)))]
    (print-header)
    (print-section "Alphabet" alphabet)
    (print-section "States" states)
    (println (str "Initial : " initial))
    (print-section "Finals" finals)
    (doseq [[state transitions-list] transitions]
      (doseq [transition transitions-list]
        (print-transition state transition))))
  machine)

(defn -main [& args]
  (-> (validation-args/validate-args args)
      (print-machine)
      (service-machine/machine-controller (second args))
      (println)))

