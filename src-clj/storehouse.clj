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

(def store (empty-storehouse))

(new-shelf store :a 2)
(new-shelf store :b 3)

(println @store)

(def shelf-a (put-in (@store :a) "a book"))
(update-shelf store {:a shelf-a} )

(println @store)

