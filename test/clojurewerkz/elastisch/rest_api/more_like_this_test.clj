;; Copyright (c) 2011-2014 Michael S. Klishin, Alex Petrov, and the ClojureWerkz Team
;;
;; The use and distribution terms for this software are covered by the
;; Eclipse Public License 1.0 (http://opensource.org/licenses/eclipse-1.0.php)
;; which can be found in the file epl-v10.html at the root of this distribution.
;; By using this software in any fashion, you are agreeing to be bound by
;; the terms of this license.
;; You must not remove this notice, or any other, from this software.

(ns clojurewerkz.elastisch.rest-api.more-like-this-test
  (:refer-clojure :exclude [replace])
  (:require [clojurewerkz.elastisch.rest.document      :as doc]
            [clojurewerkz.elastisch.rest          :as rest]
            [clojurewerkz.elastisch.rest.index         :as idx]
            [clojurewerkz.elastisch.query         :as q]
            [clojurewerkz.elastisch.fixtures :as fx]
            [clojurewerkz.elastisch.rest.response :refer :all]
            [clojure.test :refer :all]))


(use-fixtures :each fx/reset-indexes)


;;
;; more-like-this
;;

(deftest ^{:rest true} test-more-like-this
  (let [index "articles"
        type  "article"]
    (idx/create index :mappings fx/articles-mapping)
    (doc/put index type "1" fx/article-on-elasticsearch)
    (doc/put index type "2" fx/article-on-lucene)
    (doc/put index type "3" fx/article-on-nueva-york)
    (doc/put index type "4" fx/article-on-austin)
    (idx/refresh index)
    (let [response (doc/more-like-this index type "2" :mlt_fields ["tags"] :min_term_freq 1 :min_doc_freq 1)]
      (is (= 1 (total-hits response)))
      (is (= fx/article-on-elasticsearch (-> (hits-from response) first :_source))))))
