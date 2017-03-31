(ns app.views
  "Contains the views, exceot the parent view."
  (:require [re-frame.core :as re-frame]))

(defn search []
  (let [query (re-frame/subscribe [:search/query])]
    (fn []
      [:div
       [:div [:input {:type :text
                      :value @query
                      :placeholder "Enter ragam name"
                      :on-change #(re-frame/dispatch [:search/change-query (-> % .-target .-value)])}]]
       [:div [:button {:on-click #(re-frame/dispatch [:search/ragams! @query])}
              "Search Ragams"]]])))
