(ns pong.draw
  (:use [quil.core]
        [pong.util]
        [pong.state]))

(def colour  {:green-plyr [ 64 255  64]
              :red-plyr   [255 128 128]
              :ball       [  0 128 256]
              :background [  0   0   0]
              :grid       [  0  64   0]})

(defn tronify [colour-key]
  (stroke-weight 3)
  (stroke-join :round)
  (apply fill   (conj (colour-key colour) 128))
  (apply stroke       (colour-key colour)))

(defn to-px 
  "Converts world space to pixel window space when run within a quil
   draw, explodes otherwise. Arguments in the form :x 1.0 :y 1.0 :w 0.5
   can take any number of :x :y :w :h dimensions and will return them
   in the same order but untagged"
  [& args]
  (map (fn [[type v]]
         (let [scale   (if (type #{:x :w}) (width) (height))
               padding (if (type #{:y :x})   0.05     0.00)]
           (* scale (+ padding (* v 0.9))))) 
       (partition 2 args)))

(defn draw-paddle
  "Draws a player's paddle"
  [player]
  (let [{[x y]       :position
         [w h]       :size
         plyr-colour :colour   } player
               [xt  yt   wt   ht] 
        (to-px :x x :y y :w w :h h)]
    (tronify plyr-colour)
    (ellipse-mode :center)
    (ellipse xt yt wt ht)))

(defn draw-ball  
  [ball]
  (let [{[x y] :position
         rad   :radius    } ball
        [xt yt st] (to-px :x x :y y :w rad)]
    (tronify :ball)
    (ellipse-mode :center)
    (ellipse xt yt st st)))

(defn draw-background-grid []
  (let [[xl yt] (to-px :x 0.0, :y 0.0)  ;; Top left co-ords
        [xr yb] (to-px :x 1.0, :y 1.0)  ;; Bottom right co-ords
        [xc yc] (to-px :x 0.5, :y 0.5)  ;; Centre co-ords
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
  (background 0)
  (draw-background-grid)
  (draw-paddle @player1)
  (draw-paddle @player2)
  (draw-ball   @ball))


(defn define-playing-field
  [mouse-handler]
  (def handle-mouse-transformed mouse-handler)

  (defsketch playing-field            ;;Define a new sketch named example
	  :title "pong"                     ;;Set the title of the sketch
	  :setup setup                      ;;Specify the setup fn
	  :draw draw                        ;;Specify the draw fn
	  :mouse-moved handle-mouse
	  :size [323 200]))                 ;;You struggle to beat the golden ratio
  