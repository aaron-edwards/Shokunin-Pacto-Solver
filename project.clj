(defproject clj-pacto-solver "1.0.0"
  :description "Clojure web app that will solve pacto files"
  :dependencies [[org.clojure/clojure "1.7.0"]
                 [compojure "1.4.0"]
                 [ring/ring-jetty-adapter "1.4.0"]]
  :min-lein-version "2.0.0"
  :uberjar-name "clj-pacto-solver.jar"
  :main "clj-pacto-solver.web"
  :profiles {:production {:env {:production true}}})
