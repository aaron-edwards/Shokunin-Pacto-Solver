(ns clj-pacto-solver.web
  (:require [compojure.core :refer [defroutes ANY routes make-route]]
            [compojure.handler :refer [site]]
            [compojure.route :as route]
            [ring.adapter.jetty :as jetty]
            [clj-pacto-solver.github-fetch :refer :all]
            [clj-pacto-solver.pacto-parser :as pp]))

(defroutes app
  (ANY "*" [] "Much lost such wow"))

(defn get-contracts [pacto-url]
  (if pacto-url
    (fetch-contracts pacto-url)
    []))

(defn pacto-routes [contracts]
  (apply routes
         (map #(make-route (pp/get-verb %)
                           (pp/get-path %)
                           (pp/create-handler %))
              contracts)))


(defn -main [& args]
  (let [port (Integer. (or (System/getenv "PORT") 5000))
        contracts (get-contracts (or (System/getenv "PACTO_LOCATION")
                                     (first args)))]
    (println "Found " (count contracts) " pacto contracts")

    (jetty/run-jetty (site (pacto-routes contracts))
                     {:port port :join? false})))
