(ns app.core
    (:require [reagent.core :as reagent]
              [re-frame.core :as re-frame]
              [re-frisk.core :refer [enable-re-frisk!]]
              [app.routes :as routes]
              [app.events] [app.subs] [app.effects]
              ))

(def debug?
  ^boolean js/goog.DEBUG)

(defn dev-setup []
  (when debug?
    (enable-console-print!)
    (enable-re-frisk!)
    (println "dev mode")))

(defn root-view []
  (let [active-panel (re-frame/subscribe [:active-panel])
        loading? (re-frame/subscribe [:loading])]
    (fn []
      (if @loading?
        [:p "Loading..."]
        [routes/show-panel @active-panel]))))

(defn mount-root []
  (re-frame/clear-subscription-cache!)
  (reagent/render [root-view]
                  (.getElementById js/document "app")))

(defn ^:export init []
  (routes/app-routes)
  (re-frame/dispatch-sync [:initialize-db])
  (dev-setup)
  (mount-root))
