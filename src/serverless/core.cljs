(ns serverless.core
  (:require [aero.core :refer [read-config]]))

(defn json-stringify
  "Convert a clojure data structure to JSON"
  [data]
  (js/JSON.stringify (clj->js data)))

(def config
  (let [stage (if (= js/process.env.NODE_ENV "production") :prod :dev)]
    (read-config "config/config.edn" {:profile stage})))

(defn archive
  [event _ctx _cb]
  (js/Promise.resolve
   (clj->js {:statusCode 200
             :body (json-stringify {:message "Go serverless! Your function executed successfully!"
                                    :input event})})))