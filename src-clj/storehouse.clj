(ns storehouse)

(defn remove [result list element]
  (if (empty? list)
    result
    list ))

(defn new-shelf[capacity] {:products '() :capacity capacity})

(defn put-in [shelf product]
  (if (= (count (shelf :products)) (shelf :capacity))
    (throw (Exception. "Shelf is full."))
    (assoc shelf :products (conj (shelf :products) product))))

(defn take-out [shelf product]
    (assoc shelf :products (- (shelf :products) (list product))))

(def s1 (new-shelf 2))
(def s2 (put-in s1 :book))
(def s3 (put-in s2 :printer))

(println s3)

(def s4 (take-out s3 :book))

(println s4)
(println "DOES NOT WORK!")
