(ns trello-automation.core)

(defn json-stringify
  "Convert a clojure data structure to JSON"
  [data]
  (js/JSON.stringify (clj->js data)))



(defn archive
  [event _ctx _cb]
  (js/Promise.resolve
   (clj->js {:statusCode 200
             :body (json-stringify {:message "Go serverless! Your function executed successfully!"
                                    :input event})})))

(comment 
  (defonce state (atom nil))
  (defn p [promise] (-> promise
              (.then #(reset! state %))
              (.catch #(reset! state %)))))