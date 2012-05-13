(ns clj-pong.core
  (:require [quil.core       :as quil]
            [clj-pong.ball   :as ball]
            [clj-pong.paddle :as paddle])) 

(def classic-configuration 
  {:main-ball 
   {:object    ball/default-object
    :draw      ball/draw
    :update    ball/update
    :colliders ball/colliders}
   
   :human-player
   {:object    (assoc paddle/default-object
                      :position [0.5, 0.0]
                      :colour   [255 0 63])
    :draw      paddle/draw
    :update    paddle/mouse-control
    :colliders paddle/colliders}
   
   :ai-player
   {:object    (assoc paddle/default-object
                      :position [0.5, 1.0]
                      :colour   [0 255 99])
    :draw      paddle/draw
    :update    paddle/random-ai
    :colliders paddle/colliders}})

(defn setup "Build a pong game at 30 fps with one ball and two players" []
  (quil/frame-rate 30)
  (quil/smooth)
  (quil/set-state! :world (atom {})))

(defn draw "Takes an object and calls its draw funcion" [object-def]
  ((:draw object-def) (:object object-def)))
  
(defn main-loop "Runs the position update and draw functions for each of the world objects" [] 
  (quil/background 255)
  (quil/fill 0)
  (quil/text "Testing" 50 50)
  (dorun (map draw (vals @(quil/state :world)))))

(quil/defsketch my-world
	  :title "Functional Pong!"
	  :setup setup
	  :draw main-loop
	  :size [200 323])

(reset! (quil/sketch-state my-world :world) classic-configuration)
        