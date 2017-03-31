# kosha-ui
Rich user interface to explore, and consume data in [kosha](https://github.com/carnatic-music/kosha).

## Usage

Set the URL of the backend API that the frontend will query by redefining the `api-url` var in the `kosha.app.effects` namespace.
#### Development
From the project directory, run:
```
lein clean
lein figwheel dev
```

#### Production
To build the front end, run:
```
lein clean
lein cljsbuild once min
```
The built assets can be found in `resources/public/`.
