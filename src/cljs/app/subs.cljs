(ns app.subs
  "Contains the app's global state."
    (:require-macros [reagent.ratom :refer [reaction]])
    (:require [re-frame.core :as re-frame]))

(def default-db
  {:name "re-frame"
   :loading false
   :search {:query ""}})

(re-frame/reg-sub
 :loading
 (fn [db]
   (:loading db)))

(re-frame/reg-sub
 :active-panel
 (fn [db _]
   (:active-panel db)))

(re-frame/reg-sub
 :search/query
 (fn [{db :search}]
   (:query db)))
