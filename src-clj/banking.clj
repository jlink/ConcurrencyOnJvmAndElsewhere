(def account-a (ref 0))
(def account-b (ref 0))

(defn deposit [account amount]
  (dosync
    (alter account + amount)))

(defn withdraw [account amount]
  (dosync
    (if (> amount @account) (throw (new java.lang.RuntimeException "Amount not covered")))
    (alter account - amount)))

(defn transfer [from-account to-account amount]
  (dosync
    (deposit to-account amount)
    (withdraw from-account amount)))

(println "Concurrent banking example")
(println "account-a:" @account-a)
(println "account-b:" @account-b)

(println "Deposit 100 to account-a")
(deposit account-a 100)
(println "account-a:" @account-a)

(println "Withdraw 60 from account-a")
(withdraw account-a 60)
(println "account-a:" @account-a)

(println "Withdraw 41 from account-a")
(try
  ((withdraw account-a 41))
  (catch Exception e (println "Withdraw failed: ", (.getMessage e))))
(println "account-a:" @account-a)

(println "Transfer 11 from account-a to account-b")
(transfer account-a account-b 11)
(println "account-a:" @account-a)
(println "account-b:" @account-b)

(println "Transfer 30 from account-a to account-b")
(try
  ((transfer account-a account-b 30))
  (catch Exception e (println "Transfer failed: ", (.getMessage e))))
(println "account-a:" @account-a)
(println "account-b:" @account-b)
