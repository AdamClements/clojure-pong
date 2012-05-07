(ns pong.core
  (:require [clojure.contrib.math :as math])
  (:use [pong.util]
        [pong.draw :only (define-playing-field)]
        [pong.state :only (world)]))


(defn start-thinking
  [fn delay]
  (future (loop [] (fn) (Thread/sleep delay) (recur))))

(def ai-player-velocity (atom 0.0))

(defn ai-player
  "An AI player that moves at random but maintaining velocity and with a 
   slight bias towards the centre"
  [player]
  (swap! ai-player-velocity random-move)
  (let [cur-pos (player @world)
        new-pos (restrict-range (+ @ai-player-velocity cur-pos (* 0.15 (- 0.5 cur-pos))))]
    (swap! world assoc player new-pos)))

(defn speed-transform
  "Moves the ball according to its current speed"
  [ball]
  (let [{[xvel yvel] :velocity
         [xpos ypos] :position} ball]
    (assoc ball :position (list (+ xpos xvel) (+ ypos yvel)))))

(def centre '(0.5 0.5))

(defn bounds-transform
  "Checks for the ball going out of bounds and adjusts accordingly"
  [ball]
  (let [{[xvel yvel] :velocity
         [xpos ypos] :position} ball]
    (cond (or (> ypos 1.0) (< ypos 0.0))
            (assoc ball :position centre :velocity (random-vector 0.05))
          (> xpos 1.0)
            (assoc ball :velocity (list (- (math/abs xvel)) yvel))
          (< xpos 0.0)
            (assoc ball :velocity (list (math/abs xvel) yvel))
          :else
            ball)))

(defn get-new-positions
  "Takes a ball and returns a ball with new positions"
  [ball]
  (speed-transform (bounds-transform ball)))

(defn ball-physics  []
  (swap! world assoc :ball (get-new-positions (:ball @world))))

(defn handle-mouse [pos]
    (swap! world assoc :player1 pos))

(defn start-game! []
  (do 
    (define-playing-field handle-mouse)
    (def computer-player (start-thinking #(ai-player :player2) 50))
    (def ball-actor      (start-thinking ball-physics 50))))
