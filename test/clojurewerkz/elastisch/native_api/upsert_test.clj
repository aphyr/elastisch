;; Copyright (c) 2011-2014 Michael S. Klishin, Alex Petrov, and the ClojureWerkz Team
;;
;; The use and distribution terms for this software are covered by the
;; Eclipse Public License 1.0 (http://opensource.org/licenses/eclipse-1.0.php)
;; which can be found in the file epl-v10.html at the root of this distribution.
;; By using this software in any fashion, you are agreeing to be bound by
;; the terms of this license.
;; You must not remove this notice, or any other, from this software.

(ns clojurewerkz.elastisch.native-api.upsert-test
  (:refer-clojure :exclude [replace])
  (:require [clojurewerkz.elastisch.native.document :as doc]
            [clojurewerkz.elastisch.native.index    :as idx]
            [clojurewerkz.elastisch.query           :as q]
            [clojurewerkz.elastisch.fixtures        :as fx]
            [clojurewerkz.elastisch.test.helpers    :as th]
            [clojurewerkz.elastisch.native.response :refer :all]
            [clojure.test :refer :all]))

(th/maybe-connect-native-client)
(use-fixtures :each fx/reset-indexes)


;;
;; upsert
;;

(deftest test-upserting-documents
  (let [index-name "people"
        index-type "person"
        id         "3"
        new-bio    "Such a brilliant person"]
    (idx/create index-name :mappings fx/people-mapping)

    (doc/put index-name index-type "1" fx/person-jack)
    (doc/put index-name index-type "2" fx/person-mary)
    (doc/put index-name index-type "3" fx/person-joe)

    (idx/refresh index-name)
    (is (any-hits? (doc/search index-name index-type :query (q/term :biography "nice"))))
    (is (no-hits? (doc/search index-name index-type :query (q/term :biography "brilliant"))))
    (doc/upsert index-name index-type id (assoc fx/person-joe :biography new-bio))
    (idx/refresh index-name)
    (is (any-hits? (doc/search index-name index-type :query (q/term :biography "brilliant"))))
    (is (no-hits? (doc/search index-name index-type :query (q/term :biography "nice"))))))
