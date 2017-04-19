(ns app.views.ragam
  "Contains views for the ragam page."
  (:require [app.views.util :as util]
            [cljsjs.reactable]
            [re-frame.core :as re-frame]))

(def Table js/Reactable.Table)
(def Tr js/Reactable.Tr)

(defn- ragam-info
  [ragam]
  [:div.card
   [:div.card-content
    [:h1.title (:name ragam)]
    [:h2.subtitle (if (:melakartha ragam) "Mela ragam" "Janya Ragam")]
    [:div.content
     [:div (str "Arohanam: " (:arohanam ragam))]
     [:div (str "Avarohanam: " (:avarohanam ragam))]]
    [:span.tag.content (:data-source ragam)]]
   (when (not-empty (:wiki-page ragam))
     [:div.card-footer
      [:small.card-footer-item
       [:a {:href (str "http://en.wikipedia.org" (:wiki-page ragam))}
        "View on Wikipedia"]]])])

(defn- parent-ragam
  [ragam]
  (when (not-empty ragam)
    [:div.card
     [:div.card-content
      [:h6.subtitle.is-6 "Parent Ragam"]
      [:div.notification
       [:a {:on-click #(re-frame/dispatch [:navigate! (str "/ragam/" (:ragam-id ragam))])}
        (:name ragam)]
       [:p [:small (:arohanam ragam)]]
       [:p [:small (:avarohanam ragam)]]]]]))

(defn- kritis-of-ragam-row
  [row]
  [:> Tr {:className "clickable"
          :data row
          :key (:kriti-id row)
          :on-click #(re-frame/dispatch [:navigate! (str "/kriti/" (:kriti-id row))])}])

(defn- kritis-of-ragam
  [kritis]
  (when (not-empty kritis)
    [:div.card
     [:div.card-content
      [:h3.title.is-3 "Kritis"]
      [:> Table {:className "table is-striped"
                 :itemsPerPage 10
                 :sortable true
                 :filterable [:name :composer :taala :language]
                 :columns [{:key :name        :label "Kriti"}
                           {:key :composer    :label "Composer"}
                           {:key :taala       :label "Taala"}
                           {:key :language    :label "Language"}
                           {:key :data-source :label "Source"}]}
       (for [row kritis]
         (kritis-of-ragam-row row))]]]))


(defn main
  "Ragam panel parent container."
  []
  (let [ragam-data (re-frame/subscribe [:ragam/data])
        search-query (re-frame/subscribe [:search/query])
        loading? (re-frame/subscribe [:loading])]
    [:div.columns.is-multiline
     [:div.column.is-12
      [:div.column.is-4.is-offset-4
       [util/search-bar @search-query @loading? :is-small]]]
     [:div.column.is-7.is-offset-1 [ragam-info (:ragam @ragam-data)]]
     [:div.column.is-3 [parent-ragam (:parent-ragam @ragam-data)]]
     [:div.column.is-10.is-offset-1 [kritis-of-ragam (:kritis @ragam-data)]]]))
