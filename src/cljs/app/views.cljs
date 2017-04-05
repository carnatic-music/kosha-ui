(ns app.views
  "Contains the views, except the root view."
  (:require [cljsjs.reactable]
            [re-frame.core :as re-frame]
            [reagent.core :as reagent]
            [secretary.core :as secretary]))

(def Table js/Reactable.Table)
(def Tr js/Reactable.Tr)

;;;;;;;;;;;;;;;;;;
;; Search Panel ;;
;;;;;;;;;;;;;;;;;;

(defn- search-results
  "Displays search results in a Reactable Table."
  [results]
  [:div.column
   (when (not-empty results)
     [:> Table {:className "table is-striped"
                :columns [{:key :name        :label "Name"}
                          {:key :arohanam    :label "Arohanam"}
                          {:key :avarohanam  :label "Avarohanam"}
                          {:key :data-source :label "Source"}]}
      (for [row results]
        [:> Tr {:data row
                :key (:ragam-id row)
                :on-click #(re-frame/dispatch [:navigate! (str "/ragam/" (:ragam-id row))])}])])])

(defn- search-bar
  "Displays the search bar and the search button."
  [query loading?]
  [:div.field.has-addons.level-item.column.is-6.is-offset-3
   [:input.input {:type :text
                  :value query
                  :placeholder "Enter ragam name"
                  :on-change #(re-frame/dispatch [:search/change-query (-> % .-target .-value)])}]
   [:button.button
    {:class (if loading? :is-loading :is-primary)
     :on-click #(re-frame/dispatch [:search/ragams! query])}
    "Search"]])

(defn search
  "Search panel parent container."
  []
  (let [query (re-frame/subscribe [:search/query])
        results (re-frame/subscribe [:search/results])
        loading? (re-frame/subscribe [:loading])]
    [:div.columns
     [:div.column.is-offset-2.is-8
      [search-bar @query @loading?]
      [search-results @results]]]))

;;;;;;;;;;;;;;;;;
;; Ragam Panel ;;
;;;;;;;;;;;;;;;;;

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


(defn ragam
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
