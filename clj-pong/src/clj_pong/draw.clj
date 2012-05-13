(ns clj-pong.draw
  (:use [quil.core]
        [clj-pong.util]))

(defn tronify [colour]
  (stroke-weight 3)
  (stroke-join :round)
  (apply fill   (conj colour 128))
  (apply stroke       colour))

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



(defn draw-background-grid []
  (let [[xl yt] (to-px :x 0.0, :y 0.0)  ;; Top left co-ords
        [xr yb] (to-px :x 1.0, :y 1.0)  ;; Bottom right co-ords
        [xc yc] (to-px :x 0.5, :y 0.5)  ;; Centre co-ords
        centre-diam (/ (- xc xl) 2) ]
    (tronify [0 0 255])
    (rect-mode :corners)
    (rect xl yt xr yb)
    (line xl yc xr yc)
    (fill 0 0 0 0)
    (ellipse xc yc centre-diam centre-diam)))

  