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

(defn- play-icon
  "A play icon to pass "
  []
  (util/rawHTML "<span class=\"icon is-small\"><i class=\"fa fa-play\"></i></span>"))


(defn- renditions-table-row
  [track current-track-id]
  (let [is-playing? (= current-track-id (:rendition-id track))
        track-with-icon (if-not is-playing?
                          (assoc track :playing (play-icon))
                          track)]
    [:> util/Tr {:data      track-with-icon
                 :className (str "clickable"
                                 (when is-playing? " now-playing"))
                 :on-click  #(re-frame/dispatch [:kriti/play-track track])
                 :key       (str "rendition-" (:rendition-id track))}]))

(defn- renditions-table
  [renditions current-track-id]
  [:> util/Table {:className  "table"
                  :sortable   [:kritiName :mainArtist :trackNumber :concertId]
                  :filterable [:kritiName :mainArtist :trackNumber :concertId]
                  :columns    [{:key :playing :label ""}
                               {:key :kritiName :label "Kriti"}
                               {:key :mainArtist :label "Main Artist"}
                               {:key :trackNumber :label "Track No."}
                               {:key :concertId :label "Concert"}]}
   (for [track renditions]
     ^{:key (:rendition-id track)}
     (renditions-table-row track current-track-id))])

(defn- renditions-panel
  "Contains the audio player and renditions table."
  [renditions current-track]
  [:div.panel
   [:p.panel-heading
    "Renditions"]
   [:div.panel-block.has-text-centered.audio-container
    [audio-tag (:url current-track)]
    [:div (str (:kriti-name current-track) " - " (:main-artist current-track))]]
   [:div.panel-block
    (renditions-table renditions (:rendition-id current-track))]])

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
     [:div.column.is-3.is-offset-1
      [kriti-info-panel (:kriti @kriti-data)]
      [kriti-lyrics     (:kriti @kriti-data)]]
     [:div.column.is-7
      [renditions-panel (:renditions @kriti-data) @current-track]]
]))
