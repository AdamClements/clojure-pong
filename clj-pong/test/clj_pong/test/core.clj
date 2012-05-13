(ns clj-pong.test.core
  (:use [clj-pong.core])
  (:use [midje.sweet]))

(fact "Maths is still true" (+ 1 1) => 2)