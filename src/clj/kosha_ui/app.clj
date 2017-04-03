(ns kosha-ui.app
  (:require [bidi.ring :as br]
            [cider.nrepl :as cider]
            [clojure.tools.nrepl.server :as nrepl]
            [ring.adapter.jetty :as jetty]))

(def routes ["/" [["" (br/->ResourcesMaybe {:prefix "public/"})]]])

(def main-handler
  (-> (br/make-handler routes)))

(defn start! [port nrepl-port]
  (nrepl/start-server :port nrepl-port :handler cider/cider-nrepl-handler)
  (jetty/run-jetty main-handler {:port port}))
