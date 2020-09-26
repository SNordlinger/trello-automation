(ns trello-automation.trello
  (:require [aero.core :refer [read-config]]
            [trello-automation.http :refer [http-request]]))

(def config
  (let [stage (if (= js/process.env.NODE_ENV "production") :prod :dev)]
    (read-config "config/config.edn" {:profile stage})))

(defn trello-request [endpoint method params]
  (let [{api-token :api-token
         api-key :api-key
         api-url :api-url} (config :trello)]
    (http-request {:method method
                   :url endpoint
                   :baseURL api-url
                   :params (merge {:token api-token
                                   :key api-key}
                                  params)})))

(defn fetch-board-ids []
  (-> (trello-request "/1/members/me/boards" "get" {})
      (.then (fn [boards] (map #(get % "id") boards)))))

(defn fetch-cards-in-list [list-id]
  (let [url (str "/1/list/" list-id "/cards")]
    (trello-request url "get" {})))

(defn fetch-lists-in-board [board-id]
  (let [url (str "/1/boards/" board-id "/lists")]
    (trello-request url "get" {})))
