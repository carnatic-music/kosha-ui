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
                          {:key :ragam-link  :label "Wiki Page"}
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
  [:div
   [:div (str "Name: ") (:name ragam)]
   [:div (str "Arohanam: " (:arohanam ragam))]
   [:div (str "Avarohanam: " (:avarohanam ragam))]
   [:div (str "Wikipedia: " (:wiki_page ragam))]
   [:div (if (:melakartha ragam) "Mela ragam" "Janya Ragam")]
   [:div (:data_source ragam)]])

(defn- parent-ragam
  [ragam]
  (when (not-empty ragam)
    [:div (str "Parent Ragam: " (:name ragam))]))

(defn- kritis-of-ragam
  [kritis]
  (when (not-empty kritis)
    [:> Table {:data kritis
               :columns [{:key :name        :label "Kriti"}
                         {:key :composer    :label "Composer"}
                         {:key :taala       :label "Taala"}
                         {:key :language    :label "Language"}
                         {:key :data_source :label "Source"}]
               :itemsPerPage 10}]))


(defn ragam
  "Ragam panel parent container."
  []
  (let [ragam-data (re-frame/subscribe [:ragam/data])]
    [:div
     [:div [ragam-info (:ragam @ragam-data)]]
     [:div [parent-ragam (:parent-ragam @ragam-data)]]
     [:div [kritis-of-ragam (:kritis @ragam-data)]]]))
