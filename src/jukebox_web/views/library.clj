(ns jukebox-web.views.library
  (:require [jukebox-web.views.layout :as layout]
            [jukebox-web.models.library :as library])
  (:use [hiccup core page-helpers]
        [hiccup core form-helpers]
        [jukebox-player.tags]
        [ring.util.codec :only (url-encode)]
        [clojure.string :only (join split)]))

(defn- relative-uri [file]
   (url-encode (.getPath file)))

(defn display-file [file]
  (if (library/track? file)
    (link-to (str "/playlist/add/" (relative-uri file)) (.getName file))
    (link-to (str "/library/browse/" (relative-uri file)) (.getName file))))

(defn browse [path files]
  (layout/main "browse library"
     [:h3 "files in " path]
     [:ul (map #(vector :li (display-file %)) files)]))