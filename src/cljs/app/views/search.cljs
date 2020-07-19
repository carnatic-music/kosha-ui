(ns app.views.search
  "Contains views for the search panel."
  (:require [app.views.util :as util]
            [re-frame.core :as re-frame]))

(defn- search-result-row
  [row]
  ^{:key (str (random-uuid))}
  [:> util/Tr {:data      row
               :className "clickable"
               :key       (str (:type row) "-" (:id row))
               :on-click  #(re-frame/dispatch [:navigate! (str "/" (:type row) "/" (:id row))])}])

(defn- search-results
  "Displays search results in a Reactable Table."
  [results]
  [:div.column
   (when (not-empty results)
     [:> util/Table {:className    "table is-striped"
                     :itemsPerPage 10
                     :sortable     true
                     :columns      [{:key :name        :label "Name"}
                                    {:key :data-source :label "Source"}
                                    {:key :type        :label "Type"}]}
      (for [row results]
        (search-result-row row))])])

(defn main
  "Search panel parent container."
  []
  (let [query (re-frame/subscribe [:search/query])
        loading? (re-frame/subscribe [:loading])
        results (re-frame/subscribe [:search/results])]
    [:div.columns
     [:div.column.is-offset-2.is-8
      [:div.column.is-6.is-offset-3
       [util/search-bar @query @loading? :is-normal]]
      [search-results @results]]]))
