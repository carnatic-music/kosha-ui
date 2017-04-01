(ns app.views
  "Contains the views, exceot the parent view."
  (:require [re-frame.core :as re-frame]))

(defn search-results
  "Displays the search results."
  [results]
  [:div
   (for [item results]
     [:div (:name item)])])

(defn search-bar
  "Displays the search bar and the search button."
  [query]
  [:div [:input {:type :text
                 :value query
                 :placeholder "Enter ragam name"
                 :on-change #(re-frame/dispatch [:search/change-query (-> % .-target .-value)])}]
   [:button {:on-click #(re-frame/dispatch [:search/ragams! query])}
    "Search Ragams"]])

(defn search []
  (let [query (re-frame/subscribe [:search/query])
        results (re-frame/subscribe [:search/results])
        loading? (re-frame/subscribe [:loading])]
    (fn []
      [:div
       (search-bar @query)
       (if @loading?
         [:p "Loading..."]
         (search-results @results))])))
