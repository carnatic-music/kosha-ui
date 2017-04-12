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
From the project directory, run `bin/deploy` and pass it environment variables `LEINPATH` and `SERVER_PORT`. For example,
```
LEINPATH=lein SERVER_PORT=5000 bin/deploy
```

#### Building
If you want to serve the front-end assets using another mechanism (e.g. nginx), you can build the front end assets by running:
```
lein clean
lein cljsbuild once min
```
The built assets can be found in `resources/public/`.
