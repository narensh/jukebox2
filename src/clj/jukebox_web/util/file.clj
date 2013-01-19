(ns jukebox-web.util.file
  (:import [java.io File])
  (:require [clojure.java.io :as io]
            [clojure.string :as cstr])
  (:use [ring.util.codec :only (url-encode)]))

(defn not-dotfiles [file]
  (not (.startsWith (.getName file) ".")))

(defn directory? [file]
  (.isDirectory file))

(defn has-known-extension? [file]
  (re-find #"mp3|m4a$" (.getName file)))

(defn default-filter [file]
  (and (not-dotfiles file)
       (or (directory? file) (has-known-extension? file))))

(defn relativize [parent child]
  (let [parent-uri (.toURI (io/file parent))
        child-uri (.toURI (io/file child))]
    (io/file (.getPath (.relativize parent-uri child-uri)))))

(defn relative-uri [file]
   (url-encode (.getPath file)))

(defn file-path [& parts]
  (clojure.string/join File/separator parts))

(defn mkdir-p [path]
  (.mkdirs (io/as-file path)))

(defn mv [from to]
  (.renameTo (io/as-file from) (io/as-file to)))

(defn strip-slashes [string]
  (cstr/replace "/" " " string))

(defn ls [dir path & [filters]]
  (let [filterer (or filters default-filter)
        files (filter filterer (.listFiles (io/file dir path)))]
    (map #(relativize dir %) files)))