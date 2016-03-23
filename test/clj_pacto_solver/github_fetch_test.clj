(ns clj-pacto-solver.github-fetch-test
  (:use org.httpkit.fake)
  (:require [clojure.test :refer :all]
            [clj-pacto-solver.github-fetch :refer :all]))

(deftest test-fetch-contracts
  (let [contracts-url "http://contracts"]
    (testing "should return empty list if there are no contracts"
      (testing "should return empty if no contracts"
        (with-fake-http [contracts-url "[]"]
          (is (empty? (fetch-contracts contracts-url))))))
    (testing "should return a list of all json files"
      (let [contract-url-1 "http://contract_1.json"
            contract-url-2 "http://contract_2.json"]
        (with-fake-http [contracts-url (str "[{\"download_url\":\"" contract-url-1 "\"},"
                                            "{\"download_url\":\"" contract-url-2 "\"}]")
                         contract-url-1 "{\"contract\": 1}"
                         contract-url-2 "{\"contract\": 2}"]
          (let [contracts (fetch-contracts contracts-url)]
            (is (= 2 (count contracts)))
            (is (= {:contract 1} (first contracts)))
            (is (= {:contract 2} (second contracts)))))))))


