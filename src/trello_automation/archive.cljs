(ns trello-automation.archive
  (:require [trello-automation.trello :as trello]
            [trello-automation.http :refer [combined-results]]))

(defn get-done-list-id [lists]
  (->> lists
       (filter #(= (% "name") "Done"))
       first
       (#(get % "id"))))

(defn get-done-cards [board-id]
  (-> board-id
      trello/fetch-lists-in-board
      (.then get-done-list-id)
      (.then trello/fetch-cards-in-list)))

(defn get-all-done-cards [board-ids]
  (->> board-ids
       (map get-done-cards)
       (combined-results)))