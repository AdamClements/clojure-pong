(ns clj-pong.util)

(defn restrict-range
  "Takes a min and a max (default 0.0 and 1.0) in any order, 
   returns the inputted number restricted into the given range"
  ([input r1 r2]
    (let [r-max (max r1 r2)
          r-min (min r1 r2)]
      (-> input (max r-min) (min r-max))))
  ([input] 
    (restrict-range input 0.0 1.0))
  ([input r1] 
    (restrict-range input 0.0 r1)))

(defn my-random
  [min max]
  (+ min (rand (- max min))))

(defn random-vector 
  "Returns a randomised vector where each component
   can be between - max-val and + max-val"
  [max-vel]
  (list (my-random (- max-vel) max-vel) 
        (my-random (- max-vel) max-vel)))


(defn random-move
  "Given a current position, offsets it by a random amount, making sure
   it stays between 0.0 and 1.0"
  ([current] 
    (random-move current 0.01))
  ([current max-variation]
    (restrict-range 
      (+ current (- (rand (* 2 max-variation)) max-variation))
      -0.1, 0.1)))