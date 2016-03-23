(ns clj-pacto-solver.pacto-parser
  (:require [clojure.string :as s]))

(defn get-verb [contract]
  (-> contract
       :request
       :http_method
       keyword))

(defn get-path [contract]
  (-> contract
     :request
     :path
     (s/replace #"\{(.*?)\}" ":$1")))

(defn get-response-headers [contract]
  (-> contract
      :response
      :headers))
