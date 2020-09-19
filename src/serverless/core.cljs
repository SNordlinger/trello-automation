(ns serverless.core)

(defn json-stringify
  [data]
  (js/JSON.stringify (clj->js data)))

(defn archive
  [event _ctx _cb]
  (js/Promise.resolve
   (clj->js {:statusCode 200
             :body (json-stringify {:message "Go serverless! Your function executed successfully!"
                                    :input event})})))