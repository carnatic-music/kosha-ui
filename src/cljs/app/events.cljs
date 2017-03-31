(ns app.events
  "Handlers for global events across the app."
    (:require [re-frame.core :as re-frame]
              [app.subs :as subs]))

(re-frame/reg-event-db
 :initialize-db
 (fn  [_ _]
   subs/default-db))

(re-frame/reg-event-db
 :set-active-panel
 (fn [db [_ active-panel]]
   (assoc db :active-panel active-panel)))

(re-frame/reg-event-db
 :search/change-query
 (fn [db [_ new-value]]
   (assoc-in db [:search :query] new-value)))

(re-frame/reg-event-db
 :search/receive-results
 (fn [db [_ search-results]]
   (assoc-in db [:search :results] search-results)))

(re-frame/reg-event-fx
 :search/ragams!
 (fn [{:keys [db]} [_ query]]
   {:db db
    :http-get {:endpoint "search"
               :query query
               :type "ragam"}}))
