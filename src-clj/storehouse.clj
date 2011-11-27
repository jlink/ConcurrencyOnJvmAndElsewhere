(defn empty-shelf [capacity] {:products '() :capacity capacity})

(defn put-in [shelf product]
  (if (= (count (shelf :products )) (shelf :capacity ))
    (throw (Exception. "Shelf is full."))
    (assoc shelf :products (conj (shelf :products ) product))))

(defn take-out [shelf product]
  (assoc shelf :products (remove #(= % product) (shelf :products ))))

;(def s1 (empty-shelf 2))
;(def s2 (put-in s1 :book ))
;(def s3 (put-in s2 :printer ))

;(println s3)

;(def s4 (take-out s3 :book ))
;(println s4)

(defn empty-storehouse [] (ref {}))

(defn add-shelf [map name shelf]
  (assoc map name shelf))

(defn replace-shelf [map name shelf]
  (add-shelf map name shelf))

(defn new-shelf [store name capacity]
  (dosync
    (alter store add-shelf name (empty-shelf capacity))))

(defn update-shelf [store shelf-map]
  (dosync
    (let [shelf-name (first (keys shelf-map))
          shelf (shelf-map shelf-name)]
      (alter store replace-shelf shelf-name shelf))))

(defn put-in-shelf [store shelf-name product]
  (dosync
    (let [changed-shelf (put-in (@store shelf-name) product)]
      (update-shelf store {shelf-name changed-shelf}))))

(defn take-from-shelf [store shelf-name product]
  (dosync
    (let [org-shelf (@store shelf-name)
          changed-shelf (take-out org-shelf product)]
      (if (= org-shelf changed-shelf)
        (throw (Exception. (str "No such product in shelf: " product)))
        (update-shelf store {shelf-name changed-shelf})))))

(defn move [store product from-name to-name]
  (dosync
    (put-in-shelf store to-name product)
    (take-from-shelf store from-name product)))

(def store (empty-storehouse))
(new-shelf store :a 2)
(new-shelf store :b 3)
(put-in-shelf store :a "a book")
(put-in-shelf store :b "a lego block")

(println @store)
(println "Move book from :a to :b")
(move store "a book" :a :b )
(println @store)

