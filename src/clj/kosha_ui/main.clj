(ns kosha-ui.main
  (:require [clojure.string :as str]
            [clojure.tools.cli :refer [cli]]
            [kosha-ui.app :as app]))

(defn -main [& args]
  (let [[{:keys [port nrepl-port] :as args}
         _args
         _usage]
        (cli args
             ["-p"    "--port" "Port"
              :default 4000 :parse-fn #(Long/parseLong %)]
             ["-np"   "--nrepl-port" "nREPL port"
              :default 4001 :parse-fn #(Long/parseLong %)])
        [task-group task] _args]
    (app/start! port nrepl-port)))

(apply -main *command-line-args*)
