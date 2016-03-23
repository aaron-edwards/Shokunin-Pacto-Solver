(ns clj-pacto-solver.web
  (:require [compojure.core :refer [defroutes ANY]]
            [compojure.handler :refer [site]]
            [compojure.route :as route]
            [ring.adapter.jetty :as jetty]
            [clj-pacto-solver.github-fetch :refer :all]))

(defroutes app
  (ANY "*" [] "Much lost such wow"))

(defn get-contracts [pacto-url]
  (if pacto-url
    (fetch-contracts pacto-url)
    []))


(defn -main [& args]
  (let [port (Integer. (or (System/getenv "PORT") 5000))
        contracts (get-contracts (or (System/getenv "PACTO_LOCATION")
                                     (first args)))]
    (println contracts)
    (jetty/run-jetty (site #'app) {:port port :join? false})))
