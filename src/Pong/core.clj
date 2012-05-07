(ns Pong.core
  (:use quil.core)
  (:use Pong.util))

;; Define the state of the world in relative co-ordinates.
;; That is these values range from 0 - 1 where that represents their max ranges
(def world (atom {:player1 0.5
                  :player2 0.5
                  :ball    {:position '(0.5, 0.5)
                            :velocity (random-vector 0.15)}}))

(def colour  {:player1    [ 64 255  64]
              :player2    [255 128 128]
              :ball       [  0 128 128]
              :background [240 230 140]})

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


(defn bounds-transform
  "Checks for the ball going out of bounds and adjusts accordingly"
  [ball]
  (let [{[xvel yvel] :velocity
         [xpos ypos] :position} ball]
    (cond (or (> ypos 1.0) (< ypos 0.0))
            (assoc ball :position (list xpos 0.5) :velocity (random-vector 0.05))
          (> xpos 1.0)
            (assoc ball :velocity (list (- (abs xvel)) yvel))
          (< xpos 0.0)
            (assoc ball :velocity (list (abs xvel) yvel))
          :else
            ball)))

(defn get-new-positions
  "Takes a ball and returns a ball with new positions"
  [ball]
  (speed-transform (bounds-transform ball)))


(defn ball-physics  []
  (swap! world assoc :ball (get-new-positions (:ball @world))))

(defn draw-paddle
  "Draws a player's paddle"
  [player vertical-position]
  (apply fill (player colour))
  (let [w (width) h (height)
        x (* w (player @world))
        y vertical-position]
    (rect-mode :center)
    (rect x y 70 10)))

(defn draw-ball  []
  (let [w (width) h (height)
        {ball :ball} @world
        {[x y] :position} ball]
    (apply fill (:ball colour))
    (ellipse-mode :center)
    (ellipse (* w x) (* h y) 20 20)))

(defn setup []
  (smooth)                          ;;Turn on anti-aliasing
  (frame-rate 30)
  (def computer-player (start-thinking #(ai-player :player2) 50))
  (def ball-actor      (start-thinking ball-physics 50)))                

(defn draw []
  (apply background (:background colour))
  (draw-paddle :player1 (- (height) 50))
  (draw-paddle :player2 50)
  (draw-ball))


(defn handle-mouse []
  (let [w    (width)
        x    (mouse-x)]
    (swap! world assoc :player1 (/ x w))))


(defsketch example                  ;;Define a new sketch named example
  :title "Pong"                     ;;Set the title of the sketch
  :setup setup                      ;;Specify the setup fn
  :draw draw                        ;;Specify the draw fn
  :mouse-moved handle-mouse
  :size [323 200])                  ;;You struggle to beat the golden ratio
