(ns clj-pacto-solver.pacto-parser
  (:require [clojure.string :as s]
            [clojure.data.json :as json]
            [faker.generate :as gen]))

(defn get-verb [contract]
  (keyword (get-in contract
                   [:request :http_method])))

(defn get-path [contract]
  (s/replace (get-in contract [:request :path])
             #"\{(.*?)}" ":$1"))

(defn- modify-keys [f m]
  (zipmap (map f (keys m))
          (vals m)))

(defn- modify-vals [f m]
  (zipmap (keys m)
          (map f (vals m))))

(defn get-response-headers [contract]
  (modify-keys name
               (get-in contract [:response :headers])))

(defn get-response-status [contract]
  (get-in contract [:response :status]))


(defmulti create-json-property
  (fn [property req]
    (println (str "dispatch: "property))
    (keyword (:type property))))

(defmethod create-json-property :string [property req]
  (if (:pattern property)
    (rand-nth
      (s/split
        (second (re-find #"\((.*?)\)"
                         (:pattern property)))
        #"\|"))
    (gen/word)))

(defmethod create-json-property :object [property req]
  (modify-vals #(create-json-property % req) (:properties property)))

(defmethod create-json-property :array [property req]
  (let [minItems (or (:minItems property) 0)]
    (let [maxItems (or (:maxItems property) (+ 1 (* 2 minItems)))]
      (repeatedly (+ minItems (rand-int (- maxItems minItems)))
                  #(create-json-property (:items property) req)))))

(defmethod create-json-property :number [property req]
  (rand 10))

(defmethod create-json-property :integer [property req]
  (rand-int 10))

(defmethod create-json-property :int [property req]
  (rand-int 10))

(defn create-handler [contract]
  (println {:status (get-response-status contract)
            :headers (get-response-headers contract)
            :schema (get-in contract [:response :schema])
            :body (create-json-property
                    (get-in contract [:response :schema])
                    nil)})
  (fn [req]
    {:status (get-response-status contract)
     :headers (get-response-headers contract)
     :body (json/write-str (create-json-property
                             (get-in contract [:response :schema])
                             req))}))
