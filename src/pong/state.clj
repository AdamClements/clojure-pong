(ns pong.state
  (:require [pong.util]))


;; Define the state of the world in relative co-ordinates.
;; That is these values range from 0 - 1 where that represents their max ranges
(def world (atom {:player1 0.5
                  :player2 0.5
                  :ball    {:position '(0.5, 0.5)
                            :velocity (pong.util/random-vector 0.15)}}))
