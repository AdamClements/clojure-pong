(ns pong.core
  (:use [pong.util]
        [pong.draw :only (define-playing-field)]
        [pong.state]))


(defn start-thinking
  [fn delay]
  (future (loop [] (fn) (Thread/sleep delay) (recur))))

(defn ai-player
  "An AI player that moves at random but maintaining velocity and with a 
   slight bias towards the centre"
  [player]
  (let [{[xp yp] :position
         [xv yv] :velocity} @player
        new-vel (vector (random-move xv) yv)
        new-pos (vector (restrict-range (+ xv xp (* 0.25 (- 0.5 xp)))) yp)]
    (swap! player assoc :position new-pos :velocity new-vel)))

(defn speed-transform
  "Moves the ball according to its current speed"
  [b]
  (let [{[xvel yvel] :velocity
         [xpos ypos] :position} b]
  (assoc b :position [(+ xpos xvel) (+ ypos yvel)])))

(def centre [0.5 0.5])

(defn bounds-transform
  "Checks for the ball going out of bounds and adjusts accordingly"
  [b]
  (let [{[xpos ypos] :position} b]
    (cond (or (> ypos 1.0) (< ypos 0.0))
            (assoc b :position centre :velocity (random-vector 0.05))
          :else
            b)));(assoc ball :position [xpos (+ 0.01 ypos)]))));(pong.physics/collide ball {:origin '( 1.0, 0.5)
                   ;                     :normal '(-1.0, 0.0)
                   ;                     :

(defn get-new-positions
  "Takes a ball and returns a ball with new positions"
  [b]
  (-> b bounds-transform speed-transform))

(defn ball-physics  []
  (swap! ball get-new-positions))

(defn handle-mouse [pos]
  (let [{[xp yp] :position} @player1]
    (swap! player1 assoc :position (vector pos yp))))


(define-playing-field handle-mouse)
(def computer-player (start-thinking #(ai-player player2) 50))
(def ball-actor      (start-thinking ball-physics 50))
