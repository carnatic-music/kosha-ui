(ns app.views.kriti
  "Contains views for the kriti page"
  (:require [app.views.util :as util]
            [reagent.core :as reagent]
            [re-frame.core :as re-frame]))


(defn- audio-tag
  [track-url]
  (let [load-audio-source #(.load (.getElementById js/document "audio-player"))
        html5-audio-element (fn [track-url]
                              [:audio {:controls "controls"
                                       :autoPlay "autoplay"
                                       :id "audio-player"}
                               [:span "Sorry, your browser does not support playing audio."]
                               [:source {:src track-url}]])]
    (reagent/create-class
     {:component-will-receive-props load-audio-source
      :display-name  "audio-element"
      :reagent-render html5-audio-element})))

(defn- renditions-panel
  [renditions current-track]
  [:div.panel
   [:p.panel-heading
    "Renditions"]
   [:div.panel-block.has-text-centered.audio-container
    [audio-tag (:url current-track)]]
   (for [track renditions]
     [:a.panel-block {:on-click #(re-frame/dispatch [:kriti/play-track track])
                      :key (:rendition-id track)}
      (str (:kriti-name track) " - " (:main-artist track))])])

(defn- kriti-info-panel
  [kriti]
  [:div.card
   [:div.card-content
    [:h1.title (:name kriti)]
    [:h2.subtitle "Kriti"]
    [:div.content
     [:p "Composer: " (or (:composer kriti) "N/A")]
     [:p (str "Taala: " (or (:taala kriti) "N/A"))]
     [:p "Ragam: " (or [:a {:on-click #(re-frame/dispatch [:navigate! (str "/ragam/" (:ragam-id kriti))])}
                        (:ragam-name kriti)]
                       "N/A")]
     [:p.tag (:data-source kriti)]]]])

(defn- kriti-lyrics
  [kriti]
  (let [lyrics (:lyrics kriti)]
    [:div.card
     [:div.card-content
      [:h2.subtitle "Lyrics"]
      [:div.content
       (or lyrics "N/A")]]]))

(defn main
  "Kriti panel main container"
  []
  (let [kriti-data (re-frame/subscribe [:kriti/data])
        current-track (re-frame/subscribe [:kriti/current-track])
        search-query (re-frame/subscribe [:search/query])
        loading? (re-frame/subscribe [:loading])]
    [:div.columns.is-multiline
     [:div.column.is-12
      [:div.column.is-4.is-offset-4
       [util/search-bar @search-query @loading? :is-small]]]
     [:div.column.is-offset-1.is-4
      [renditions-panel (:renditions @kriti-data) @current-track]]
     [:div.column.is-6
      [kriti-info-panel (:kriti @kriti-data)]
      [kriti-lyrics     (:kriti @kriti-data)]]]))
