(ns app.routes
    (:require-macros [secretary.core :refer [defroute]])
    (:import goog.History)
    (:require [secretary.core :as secretary]
              [goog.events :as events]
              [goog.history.EventType :as EventType]
              [re-frame.core :as re-frame]
              [app.views.kriti :as kriti-views]
              [app.views.ragam :as ragam-views]
              [app.views.search :as search-views]))

(defn hook-browser-navigation! []
  (doto (History.)
    (events/listen
     EventType/NAVIGATE
     (fn [event]
       (secretary/dispatch! (.-token event))))
    (.setEnabled true)))

(defn app-routes []
  (secretary/set-config! :prefix "#")
  (defroute "/" []
    (re-frame/dispatch [:set-active-panel :search-panel]))
  (defroute #"/ragam/(\d+)" [id]
    (re-frame/dispatch [:set-active-panel :ragam-panel])
    (re-frame/dispatch [:ragam/get! id]))
  (defroute #"/kriti/(\d+)" [id]
    (re-frame/dispatch [:set-active-panel :kriti-panel])
    (re-frame/dispatch [:kriti/get! id]))

(defn- panels [panel-name]
  (case panel-name
    :search-panel [search-views/main]
    :ragam-panel [ragam-views/main]
    :kriti-panel [kriti-views/main]
    [:div]))

(defn show-panel [panel-name]
  [panels panel-name])

  (hook-browser-navigation!))
