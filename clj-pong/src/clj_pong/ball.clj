(ns clj-pong.ball
  (:use [clj-pong.util]
        [clj-pong.draw]
        [quil.core]))

(def default-object {:name "Ball"
                     :position [0.5, 0.5]
                     :velocity (random-vector 0.01)
                     :radius   0.05
                     :colour   [0 64 255]
                     :spin     0.0 })

(defn draw "Draws the ball - a tronified circle" 
  [{[x y] :position
    rad   :radius    
    col   :colour}]
  (let [[xt yt st] (to-px :x x :y y :w rad)]
    (tronify col)
    (ellipse-mode :center)
    (ellipse xt yt st st)))

(defn update [] nil)
(defn colliders [] nil)