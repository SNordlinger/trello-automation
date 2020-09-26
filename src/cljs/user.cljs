(ns cljs.user)

(defonce state (atom nil))

(defn p [promise] (-> promise
                    (.then #(reset! state %))
                    (.catch #(reset! state %))))