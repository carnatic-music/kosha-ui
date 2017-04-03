(ns app.core
  (:require [app.events :as events]
            [app.routes :as routes]
            [app.subs :as subs]
            [app.http-client :as http-client]
            [cljsjs.reactable]
            [reagent.core :as reagent]
            [re-frame.core :as re-frame]
            [re-frisk.core :refer [enable-re-frisk!]]))

(def debug?
  ^boolean js/goog.DEBUG)

(defn dev-setup []
  (when debug?
    (enable-console-print!)
    (enable-re-frisk!)
    (println "dev mode")))

(defn root-view []
  (let [active-panel (re-frame/subscribe [:active-panel])]
    (fn []
      [routes/show-panel @active-panel])))

(defn mount-root []
  (re-frame/clear-subscription-cache!)
  (reagent/render [root-view]
                  (.getElementById js/document "app")))

(defn ^:export init []
  (routes/app-routes)
  (events/init)
  (subs/init)
  (http-client/init)
  (re-frame/dispatch-sync [:initialize-db])
  (dev-setup)
  (mount-root))
