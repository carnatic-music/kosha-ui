(ns app.http-client
  "General purpose library of side effects."
  (:require [re-frame.core :as re-frame]
            [ajax.core :as ajax]))

(def api-url "http://localhost:8080/")

(defn- get-request
  [{:keys [endpoint params on-success]}]
   (ajax/GET (str api-url endpoint)
             {:params          params
              :handler         on-success
              :response-format :json
              :keywords?       true
              }))

(defn init
  []
  (re-frame/reg-fx :http-get get-request))
