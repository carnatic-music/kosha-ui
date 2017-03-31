(ns app.effects
  "General purpose library of side effects."
  (:require [re-frame.core :as re-frame]
            [ajax.core :as ajax]))

(def api-url "http://localhost:8080/")

(re-frame/reg-fx
 :http-get
 (fn [{:keys [endpoint query type]}]
   (ajax/GET (str api-url endpoint)
             {:params          {:q query
                                :t type}
              :handler         #(re-frame/dispatch [:search/receive-results %])
              :response-format :json
              :keywords?       true
              })))
