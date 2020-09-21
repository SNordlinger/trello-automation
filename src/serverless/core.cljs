(ns serverless.core
  (:require  [aero.core :refer [read-config]]
             ["axios" :as axios]))

(defn json-stringify
  "Convert a clojure data structure to JSON"
  [data]
  (js/JSON.stringify (clj->js data)))

(def config
  (let [stage (if (= js/process.env.NODE_ENV "production") :prod :dev)]
    (read-config "config/config.edn" {:profile stage})))

(defn http-request [config]
  (.then (.request axios (clj->js config))
         #(js->clj (.-data %) {:keywordize-keys true})))

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

(defn fetch-boards []
  (trello-request "/1/members/me/boards" "get" {}))

(defn archive
  [event _ctx _cb]
  (js/Promise.resolve
   (clj->js {:statusCode 200
             :body (json-stringify {:message "Go serverless! Your function executed successfully!"
                                    :input event})})))

(comment 
  (defonce state (atom nil))
  (-> (fetch-boards)
      (.then #(reset! state %))
      (.catch #(reset! state %))))