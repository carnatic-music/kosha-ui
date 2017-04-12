(ns app.views.ragam
  "Contains views for the ragam page."
  (:require [cljsjs.reactable]
            [re-frame.core :as re-frame]))

(def Table js/Reactable.Table)

(defn- ragam-info
  [ragam]
  [:div.card
   [:div.card-content
    [:h1.title (:name ragam)]
    [:h2.subtitle (if (:melakartha ragam) "Mela ragam" "Janya Ragam")]
    [:div.content
     [:div (str "Arohanam: " (:arohanam ragam))]
     [:div (str "Avarohanam: " (:avarohanam ragam))]]
    [:span.tag.content (:data_source ragam)]]
   (when (not-empty (:wiki_page ragam))
     [:div.card-footer
      [:small.card-footer-item
       [:a {:href (str "http://en.wikipedia.org" (:wiki_page ragam))}
        "View on Wikipedia"]]])])

(defn- parent-ragam
  [ragam]
  (when (not-empty ragam)
    [:div.card
     [:div.card-content
      [:h6.subtitle.is-6 "Parent Ragam"]
      [:div.notification
       [:a {:on-click #(re-frame/dispatch [:navigate! (str "/ragam/" (:ragam_id ragam))])}
        (:name ragam)]
       [:p [:small (:arohanam ragam)]]
       [:p [:small (:avarohanam ragam)]]]]]))

(defn- kritis-of-ragam
  [kritis]
  (when (not-empty kritis)
    [:div.column.container.is-12
     [:div.card
      [:div.card-content
       [:h3.title.is-3 "Kritis"]
       [:> Table {:data kritis
                  :className "table is-striped"
                  :columns [{:key :name        :label "Kriti"}
                            {:key :composer    :label "Composer"}
                            {:key :taala       :label "Taala"}
                            {:key :language    :label "Language"}
                            {:key :data_source :label "Source"}]
                  :itemsPerPage 10}]]]]))


(defn main
  "Ragam panel parent container."
  []
  (let [ragam-data (re-frame/subscribe [:ragam/data])]
    [:div
     [:div.columns.section
      [:div.column.is-10.is-offset-1.columns
       [:div.column.is-8.container [ragam-info (:ragam @ragam-data)]]
       [:div.column.is-4.container [parent-ragam (:parent-ragam @ragam-data)]]]]
     [:div.columns.section
      [:div.column.is-10.is-offset-1.columns [kritis-of-ragam (:kritis @ragam-data)]]]]))
