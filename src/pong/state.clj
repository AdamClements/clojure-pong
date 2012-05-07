(ns pong.state
  (:require [pong.util]))

(def ball (atom {:position [0.5, 0.5]
                 :velocity (pong.util/random-vector 0.15)
                 :radius   0.03
                 :spin     0.0                            }))

(def player1 (atom {:position '(0.5, 1.0)
                    :velocity '(0.0, 0.0)
                    :size     '(0.15, 0.02)
                    :colour   :green-plyr
                    :boost    10.0
                    :points   0   }))

(def player2 (atom {:position '(0.5, 0.0)
                    :velocity '(0.0, 0.1)
                    :size     '(0.15, 0.02)
                    :colour   :red-plyr
                    :boost    10.0
                    :points   0   }))
