(ns storehouse)

(defn new-shelf[capacity] {:products '() :capacity capacity})

(defn put-in [shelf product]
  (if (= (count (shelf :products)) (shelf :capacity))
    (throw (Exception. "Shelf is full."))
    (assoc shelf :products (conj (shelf :products) product))))

(def s1 (new-shelf 1))
(def s2 (put-in s1 :book))

(println s2)

(put-in s2 :shoebox)