(ns app.views.search
  "Contains views for the search panel."
  (:require [cljsjs.reactable]
            [re-frame.core :as re-frame]))

(def Table js/Reactable.Table)
(def Tr js/Reactable.Tr)

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

(defn main
  "Search panel parent container."
  []
  (let [query (re-frame/subscribe [:search/query])
        results (re-frame/subscribe [:search/results])
        loading? (re-frame/subscribe [:loading])]
    [:div.columns
     [:div.column.is-offset-2.is-8
      [search-bar @query @loading?]
      [search-results @results]]]))
