(ns app.http-client
  "General purpose library of side effects."
  (:require [ajax.core :as ajax]
            [re-frame.core :as re-frame]
            [secretary.core :as secretary]))

(def api-url "http://localhost:8080/")

(defn- get-request
  [{:keys [endpoint params on-success]}]
   (ajax/GET (str api-url endpoint)
             {:params          params
              :handler         on-success
              :response-format :json
              :keywords?       true}))

(defn- change-url
  [url-string]
  (secretary/dispatch! url-string)
  (aset js/location "hash" url-string))

(defn init
  []
  (re-frame/reg-fx :http-get get-request)
  (re-frame/reg-fx :change-url change-url))
