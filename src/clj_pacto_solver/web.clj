(ns clj-pacto-solver.web
  (:require [compojure.core :refer [defroutes ANY]]
            [compojure.handler :refer [site]]
            [compojure.route :as route]
            [ring.adapter.jetty :as jetty]))

(defroutes app
  (ANY "*" [] "Much lost such wow"))

(defn -main [& [port]]
  (println port)
  (let [port (Integer. (or (System/getenv "PORT") 5000))]
    (jetty/run-jetty (site #'app) {:port port :join? false})))
