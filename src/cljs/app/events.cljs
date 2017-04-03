(ns app.events
  "Handlers for global events across the app."
    (:require [re-frame.core :as re-frame]
              [app.subs :as subs]))

(defn- initialize-db
  [_ _]
  subs/default-db)

(defn- set-active-panel
  [db [_ active-panel]]
  (assoc db :active-panel active-panel))

(defn- search-change-query
  [db [_ new-value]]
  (assoc-in db [:search :query] new-value))

(defn- search-receive-results
  [db [_ search-results]]
  (-> db
       (assoc-in [:search :results] search-results)
       (assoc :loading false)))

(defn- search-ragams
  [{:keys [db]} [_ query]]
  {:db (assoc db :loading true)
    :http-get {:endpoint "search"
               :query query
               :type "ragam"}})

(defn init
  []
  (re-frame/reg-event-db :initialize-db initialize-db)
  (re-frame/reg-event-db :set-active-panel set-active-panel)
  (re-frame/reg-event-db :search/change-query search-change-query)
  (re-frame/reg-event-db :search/receive-results search-receive-results)
  (re-frame/reg-event-fx :search/ragams! search-ragams))
