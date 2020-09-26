(ns trello-automation.http
  (:require ["axios" :as axios]))

(defn http-request [config]
  (.then (.request axios (clj->js config))
         #(js->clj (.-data %) {:keywordize-keys true})))

(defn combined-results [promises]
  (.then (js/Promise.all promises)
         (fn [results] (apply concat results))))