(ns kosha-ui.app
  (:require [bidi.ring :as br]
            [cider.nrepl :as cider]
            [clojure.tools.nrepl.server :as nrepl]
            [ring.adapter.jetty :as jetty]
            [ring.util.response :as res]))

(defn serve-index
  [& args]
  (res/resource-response "public/index.html"))

(def routes ["" [["/" serve-index]
                 ["/css/" (br/->ResourcesMaybe {:prefix "public/css/"})]
                 ["/js/" (br/->ResourcesMaybe {:prefix "public/js/"})]]])

(def main-handler
  (-> (br/make-handler routes)))

(defn start! [port nrepl-port]
  (nrepl/start-server :port nrepl-port :handler cider/cider-nrepl-handler)
  (jetty/run-jetty main-handler {:port port}))
