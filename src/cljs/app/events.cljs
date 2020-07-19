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

(defn- search-ragams
  [{:keys [db]} [_ query]]
  {:db (assoc db :loading true)
    :http-get {:endpoint "search"
               :params {:query query}
               :on-success #(re-frame/dispatch [:search/receive-results %])}})

(defn- receive-results
  [db [_ search-results]]
  (-> db
       (assoc-in [:search :results] search-results)
       (assoc :loading false)))

(defn- get-ragam
  [{:keys [db]} [_ ragam-id]]
  {:db       (assoc-in db [:ragam :loading] true)
   :http-get {:endpoint (str "ragam" "/" ragam-id)
              :params nil
              :on-success #(re-frame/dispatch [:ragam/receive-data %])}})

(defn- receive-ragam
  [db [_ ragam-data]]
  (-> db
      (assoc-in [:ragam :data] ragam-data)
      (assoc-in [:ragam :loading] false)))

(defn- get-kriti
  [{:keys [db]} [_ kriti-id]]
  {:db (assoc-in db [:kriti :loading] true)
   :http-get {:endpoint (str "kriti" "/" kriti-id)
              :params nil
              :on-success #(re-frame/dispatch [:kriti/receive-data %])}})

(defn- receive-kriti
  [db [_ kriti-data]]
  (-> db
      (assoc-in [:kriti :data] kriti-data)
      (assoc-in [:kriti :loading] false)))

(defn- set-current-track
  [db [_ track]]
  (-> db
      (assoc-in [:kriti :current-track] track)))

(defn- reset-current-track
  [db _]
  (assoc-in db [:kriti :current-track] {:url nil}))

(defn init
  []
  (re-frame/reg-event-db :initialize-db initialize-db)
  (re-frame/reg-event-db :set-active-panel set-active-panel)
  (re-frame/reg-event-fx :navigate! navigate-to-url)
  (re-frame/reg-event-db :search/change-query search-change-query)
  (re-frame/reg-event-fx :search/ragams! search-ragams)
  (re-frame/reg-event-db :search/receive-results receive-results)
  (re-frame/reg-event-fx :ragam/get! get-ragam)
  (re-frame/reg-event-db :ragam/receive-data receive-ragam)
  (re-frame/reg-event-fx :kriti/get! get-kriti)
  (re-frame/reg-event-db :kriti/receive-data receive-kriti)
  (re-frame/reg-event-db :kriti/play-track set-current-track)
  (re-frame/reg-event-db :kriti/reset-track reset-current-track))
