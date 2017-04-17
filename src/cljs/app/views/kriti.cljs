(ns app.views.kriti
  "Contains views for the kriti page"
  (:require [reagent.core :as reagent]
            [re-frame.core :as re-frame]))


(defn- audio-tag
  [track-url]
  (let [x nil]
    (reagent/create-class
     {:component-will-receive-props
      #(.load (.getElementById js/document "audio-player"))
      :display-name  "audio-element"
      :reagent-render
        (fn [track-url]
          [:audio {:controls "controls"
                   :autoPlay "autoplay"
                   :id "audio-player"}
           [:span "Sorry, your browser does not support playing audio."]
           [:source {:src track-url}]])})))

(defn- renditions-panel
  [renditions current-track]
  [:div.panel
   [:p.panel-heading
    "Renditions"]
   [:div {:className "panel-block has-text-centered"
          :style {:display :block
                  :padding-top "30px"}}
    [audio-tag (:url current-track)]]
   (for [track renditions]
     [:a.panel-block {:on-click #(re-frame/dispatch [:kriti/play-track track])
                      :key (:rendition-id track)}
      (str (:kriti-name track) " - " (:main-artist track))])])

(defn main
  "Kriti panel main container"
  []
  (let [kriti-data (re-frame/subscribe [:kriti/data])
        current-track (re-frame/subscribe [:kriti/current-track])]
    [:div.columns
     [:div.column.is-one-third
      [renditions-panel (:renditions @kriti-data) @current-track]]]))
