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

(defn- navigate-to-url
  [{:keys [db]} [_ url-string]]
  {:db db
   :change-url url-string})

(defn- search-change-query
  [db [_ new-value]]
  (assoc-in db [:search :query] new-value))

(defn- search-ragams-response
  [db [_ search-results]]
  (-> db
       (assoc-in [:search :results] search-results)
       (assoc :loading false)))

(defn- search-ragams-request
  [{:keys [db]} [_ query]]
  {:db (assoc db :loading true)
    :http-get {:endpoint "search"
               :params {:query query
                        :type "ragam"}
               :on-success #(re-frame/dispatch [:search/receive-results %])}})

(defn- get-ragam-request
  [{:keys [db]} [_ ragam-id]]
  {:db       (assoc-in db [:ragam :loading] true)
   :http-get {:endpoint (str "ragam" "/" ragam-id)
              :params nil
              :on-success #(re-frame/dispatch [:ragam/receive-data %])}})

(defn- get-ragam-response
  [db [_ ragam-data]]
  (-> db
      (assoc-in [:ragam :data] ragam-data)
      (assoc-in [:ragam :loading] false)))

(defn init
  []
  (re-frame/reg-event-db :initialize-db initialize-db)
  (re-frame/reg-event-db :set-active-panel set-active-panel)
  (re-frame/reg-event-fx :navigate! navigate-to-url)
  (re-frame/reg-event-db :search/change-query search-change-query)
  (re-frame/reg-event-db :search/receive-results search-ragams-response)
  (re-frame/reg-event-fx :search/ragams! search-ragams-request)
  (re-frame/reg-event-fx :ragam/get! get-ragam-request)
  (re-frame/reg-event-db :ragam/receive-data get-ragam-response))
