(ns clj-pacto-solver.github-fetch
  (:require [org.httpkit.client :as http]
            [clojure.data.json :as json]))

(defn- get-url-as-json [url]
  (-> url
      http/get
      deref
      :body
      (json/read-str :key-fn keyword)))

(defn fetch-contracts [url]
  (map #(get-url-as-json (:download_url %))
       (get-url-as-json url)))
