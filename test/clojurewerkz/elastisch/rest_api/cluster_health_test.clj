;; Copyright (c) 2011-2014 Michael S. Klishin, Alex Petrov, and the ClojureWerkz Team
;;
;; The use and distribution terms for this software are covered by the
;; Eclipse Public License 1.0 (http://opensource.org/licenses/eclipse-1.0.php)
;; which can be found in the file epl-v10.html at the root of this distribution.
;; By using this software in any fashion, you are agreeing to be bound by
;; the terms of this license.
;; You must not remove this notice, or any other, from this software.

(ns clojurewerkz.elastisch.rest-api.cluster-health-test
  (:refer-clojure :exclude [replace])
  (:require [clojurewerkz.elastisch.rest.admin :as admin]
            [clojurewerkz.elastisch.fixtures :as fx]
            [clojure.test :refer :all]))

(use-fixtures :each fx/reset-indexes fx/prepopulate-people-index fx/prepopulate-tweets-index)

(deftest ^{:rest true} cluster-health
  (let [r (admin/cluster-health)]
    (is (contains? r :number_of_nodes)))
  (let [r (admin/cluster-health :index "tweets")]
    (is (contains? r :number_of_nodes)))
  (let [r (admin/cluster-health :index ["tweets" "people"])]
    (is (contains? r :number_of_nodes)))
  (let [r (admin/cluster-health {:index ["tweets"] :level "shards"})]
    (is (contains? r :number_of_nodes))))
