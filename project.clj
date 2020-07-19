(defproject app "0.1.0-SNAPSHOT"
  :dependencies [[cljs-ajax "0.8.0"]
                 [cljsjs/reactable "0.14.1-0"]
                 [org.clojure/clojure "1.10.1"]
                 [org.clojure/clojurescript "1.10.764"]
                 [reagent "0.6.0"]
                 [re-frame "0.9.1"]
                 [re-frisk "0.3.2"]
                 [secretary "1.2.3"]
                 [bidi "1.19.0"]
                 [ring/ring-core "1.5.0"]
                 [ring/ring-jetty-adapter "1.3.2"]
                 [org.clojure/tools.cli "0.3.3"]]

  :plugins [[lein-cljsbuild "1.1.8"]]

  :min-lein-version "2.5.3"

  :source-paths ["src/clj"]

  :clean-targets ^{:protect false} ["resources/public/js/compiled" "target"
                                    "test/js"]

  :figwheel {:css-dirs ["resources/public/css"]}

  :profiles
  {:dev
   {:dependencies []
    :plugins      [[lein-figwheel "0.5.20"]
                   [lein-doo "0.1.11"]]}}

  :cljsbuild
  {:builds
   [{:id           "dev"
     :source-paths ["src/cljs"]
     :figwheel     {:on-jsload "app.core/mount-root"}
     :compiler     {:main                 app.core
                    :output-to            "resources/public/js/app.js"
                    :output-dir           "resources/public/js/compiled/dev/out"
                    :asset-path           "js/compiled/dev/out"
                    :source-map-timestamp true}}

    {:id           "min"
     :source-paths ["src/cljs"]
     :compiler     {:main            app.core
                    :output-to       "resources/public/js/app.js"
                    :output-dir      "resources/public/js/compiled/min/out"
                    :asset-path      "js/compiled/min/out"
                    :optimizations   :advanced
                    :closure-defines {goog.DEBUG false}
                    :pretty-print    false}}

    {:id           "test"
     :source-paths ["src/cljs" "test/cljs"]
     :compiler     {:main          app.runner
                    :output-to     "resources/public/js/compiled/test.js"
                    :output-dir    "resources/public/js/compiled/test/out"
                    :optimizations :none}}
    ]}

  :main kosha-ui.main
  :aot :all)
