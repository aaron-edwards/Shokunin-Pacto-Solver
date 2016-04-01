(ns clj-pacto-solver.pacto-parser-test
  (:require [clojure.test :refer :all]
            [clj-pacto-solver.pacto-parser :as parser]))

(deftest get-verb-should-return-get
  (let [contract {:request {:http_method "get"}}]
    (is (= :get (parser/get-verb contract)))))

(deftest get-verb-should-return-post
  (let [contract {:request {:http_method "post"}}]
    (is (= :post (parser/get-verb contract)))))

(deftest get-path-should-return-path
  (let [contract {:request {:path "something"}}]
    (is (= "something" (parser/get-path contract)))))

(deftest get-path-should-change-id
  (let [contract {:request {:path "something/{id}"}}]
    (is (= "something/:id" (parser/get-path contract)))))

(deftest get-path-should-change-multipl-ids
  (let [contract {:request {:path "something/{id1}/another/{id2}"}}]
    (is (= "something/:id1/another/:id2" (parser/get-path contract)))))

(deftest get-response-headers-should-extract-response-headers
  (let [contract {:response {:headers {:Content-Type "application/json"}}}
        headers (parser/get-response-headers contract)]
    (is (= {"Content-Type" "application/json"} headers))))

(deftest get-response-status-should-return-200
  (let [contract {:response {:status 200}}]
    (is (= 200 (parser/get-response-status contract)))))

(deftest get-response-status-should-return-201
  (let [contract {:response {:status 201}}]
    (is (= 201 (parser/get-response-status contract)))))


(deftest create-json-property-should-return-a-string
  (let [property {:type "string"}]
    (is (string? (parser/create-json-property property)))))

(deftest create-josn-property-should-return-valid-string
  (let [property {:type "string"
                  :pattern "^(READY|MAKING|QUEUED|DELAYED)$"}]
   (is (re-matches (re-pattern (:pattern property))
                  (parser/create-json-property property)))))

(deftest create-json-property-should-return-int
  (let [property {:type "int"}]
    (is (integer? (parser/create-json-property property)))))

(deftest create-json-property-should-return-integer
  (let [property {:type "integer"}]
    (is (integer? (parser/create-json-property property)))))

(deftest create-json-property-should-return-number
  (let [property {:type "number"}]
    (is (number? (parser/create-json-property property)))))

(deftest create-json-should-return-an-array
  (let [property {:type "array"
                  :minItems 2
                  :maxItems 5
                  :items {:type "int"}}
        result (parser/create-json-property property)]
    (is (seq? result))
    (is (>= (count result) (:minItems property)))
    (is (<= (count result) (:maxItems property)))
    (is (every? integer? result))))

(deftest create-json-should-return-object
  (let [property {:type "object"
                  :properties {:key {:type "string"}}}
        result (parser/create-json-property property)]
    (is (map? result))
    (is (string? (:key result)))))
