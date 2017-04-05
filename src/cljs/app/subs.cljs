(ns app.subs
  "Contains the app's global state."
    (:require-macros [reagent.ratom :refer [reaction]])
    (:require [re-frame.core :as re-frame]))

(def default-db
  {:name "re-frame"
   :loading false
   :search {:query ""
            :results []}
   :ragam  {:loading false}})

(defn- loading
  [db]
  (:loading db))

(defn- active-panel
  [db _]
  (:active-panel db))

(defn- search-query
  [{db :search}]
  (:query db))

(defn- search-results
  [{db :search}]
  (:results db))

(defn- ragam-data
  [{db :ragam}]
  (:data db))

(defn init
  []
  (re-frame/reg-sub :loading loading)
  (re-frame/reg-sub :active-panel active-panel)
  (re-frame/reg-sub :search/query search-query)
  (re-frame/reg-sub :search/results search-results)
  (re-frame/reg-sub :ragam/data ragam-data))
