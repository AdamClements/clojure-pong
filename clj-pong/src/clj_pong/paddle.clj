(ns clj-pong.paddle
  (:use [clj-pong.util]
        [clj-pong.draw]
        [quil.core]))

(def default-object {:name "Paddle"
                     :position [0.5, 0.5 ]
                     :size     [0.2, 0.01]
                     :colour   [0   0   0]
                     :velocity (random-vector 0.01)
                     :spin     0.0 })

(defn draw
  "Draws a player's paddle"
  [{[x y]  :position
    [w h]  :size
    colour :colour   }]
  (let [[xt  yt   wt   ht] 
        (to-px :x x :y y :w w :h h)]
    (tronify colour)
    (ellipse-mode :center)
    (ellipse xt yt wt ht)))

(defn update [] nil)
(defn colliders [] nil)
(defn mouse-control [] nil)
(defn random-ai [] nil)