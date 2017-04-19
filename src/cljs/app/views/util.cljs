(ns app.views.util
  "Contains views used across multiple panels."
  (:require [re-frame.core :as re-frame]))

(defn- search-input
  [query size-class]
  [:input.input {:type :text
                 :class size-class
                 :value query
                 :placeholder "Name of a ragam or a kriti"
                 :on-change #(re-frame/dispatch [:search/change-query (-> % .-target .-value)])
                 :on-key-press #(when (= (.-charCode %) 13)
                                  (re-frame/dispatch [:navigate! (str "/search?query=" (js/encodeURI query))]))}])

(defn- search-button
  [query loading? size-class]
  [:button.button
   {:class (str (name size-class) " " (if loading? "is-loading" "is-primary"))
    :on-click #(re-frame/dispatch [:navigate! (str "/search?query=" (js/encodeURI query))])}
    "Search"])

(defn search-bar
  "Displays the search bar and the search button."
  [query loading? size-class]
  [:div.field.has-addons.level-item
   [:p.control.has-icon.has-icon-left.is-expanded
    [search-input query size-class]
    [:span.icon.is-left.is-small [:i.fa.fa-search]]]
   [search-button query loading? size-class]])
