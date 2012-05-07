(ns pong.draw
  (:use [quil.core]
        [pong.util]
        [pong.state]))

(def colour  {:player1    [ 64 255  64]
              :player2    [255 128 128]
              :ball       [  0 128 256]
              :background [  0   0   0]
              :grid       [  0  64   0]})

(defn tronify [colour-key]
  (stroke-weight 2)
  (stroke-join :round)
  (apply fill   (conj (colour-key colour) 192))
  (apply stroke       (colour-key colour)))

(defn world-to-win
  "Can only be called within a quil applet"
  [[x y]]
  (let [w (width) h (height)]
    (vector (* w (+ 0.05 (* x 0.9))) (* h (+ 0.05 (* y 0.9))))))

(defn draw-paddle
  "Draws a player's paddle"
  [player vertical-position]
  (tronify player)
  (let [[x y] 
        (world-to-win 
          (vector (player @world), vertical-position))]
    (rect-mode :center)
    (rect x y 70 10)))

(defn draw-ball  [ball]
  (let [{pos :position} ball
        [x y] (world-to-win pos)]
    (tronify :ball)
    (ellipse-mode :center)
    (ellipse x y 20 20)))

(defn draw-background-grid []
  (let [[xl yt] (world-to-win '(0.0, 0.0))  ;; Top left co-ords
        [xr yb] (world-to-win '(1.0, 1.0))  ;; Bottom right co-ords
        [xc yc] (world-to-win '(0.5, 0.5))  ;; Centre co-ords
        centre-diam (/ (- xc xl) 2) ]
    (tronify :grid)
    (rect-mode :corners)
    (rect xl yt xr yb)
    (line xl yc xr yc)
    (fill 0 0 0 0)
    (ellipse xc yc centre-diam centre-diam)))

(def handle-mouse-transformed (fn [pos] nil))
(defn handle-mouse []
  (let [w    (width)
        x    (mouse-x)]
    (handle-mouse-transformed (/ x w))))

(defn setup []
  (smooth)                          ;;Turn on anti-aliasing
  (frame-rate 30))                

(defn draw []
  (apply background (:background colour))
  (draw-background-grid)
  (draw-paddle :player1 1.0)
  (draw-paddle :player2 0.0)
  (draw-ball (:ball @world)))


(defn define-playing-field
  [mouse-handler]
  (def handle-mouse-transformed mouse-handler)

  (defsketch playing-field            ;;Define a new sketch named example
	  :title "pong"                     ;;Set the title of the sketch
	  :setup setup                      ;;Specify the setup fn
	  :draw draw                        ;;Specify the draw fn
	  :mouse-moved handle-mouse
	  :size [323 200]))                 ;;You struggle to beat the golden ratio
  