(defn new-person [name]
  {:name (ref name)})

(defn change-name [person new-name]
  (dosync
    (ref-set (person :name ) new-name)))

(defn new-account [owner]
  {:balance (ref 0) :owner (ref owner)})

(defn deposit [account amount]
  (dosync
    (alter (account :balance ) + amount)))


(def johannes (new-person "Johannes"))
(def js-account (new-account johannes))

(println "New account:  " js-account)

(println "Change name and deposit money in one transaction.")
(dosync
  (change-name johannes "Johannes Link")
  (deposit js-account 42))

(println "Changed account:  " js-account)
