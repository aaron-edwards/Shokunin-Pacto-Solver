(ns clj-pacto-solver.pacto-parser
  (:require [clojure.string :as s]))

(defn get-verb [contract]
  (keyword (get-in contract
                   [:request :http_method])))

(defn get-path [contract]
  (s/replace (get-in contract [:request :path])
             #"\{(.*?)}" ":$1"))

(defn- modify-keys [f m]
  (zipmap (map f
               (keys m))
          (vals m)))

(defn get-response-headers [contract]
  (modify-keys name
        (get-in contract [:response :headers])))

(defn get-response-status [contract]
  (get-in contract [:response :status]))

(defn create-handler [contract]
  (println {:status (get-response-status contract)
     :headers (get-response-headers contract)})
  (fn [req]
    {:status (get-response-status contract)
     :headers (get-response-headers contract)}))
