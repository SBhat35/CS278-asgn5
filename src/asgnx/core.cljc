(ns asgnx.core
  (:require [clojure.string :as string]
            [clojure.core.async :as async :refer [go chan <! >!]]
            [asgnx.kvstore :as kvstore
             :refer [put! get! list! remove!]]))


;; Do not edit!
;; A def for the course home page URL.
(def dining-homepage "https://campusdining.vanderbilt.edu/")

(def foodmenu "https://campusdining.vanderbilt.edu/menus/")

(def restaurant-map {"randcenter" {"monday" {:open 7
                                               :close 15
                                               :extra "and 4:30pm-8pm"}
                                     "tuesday" {:open 7
                                                :close 15
                                                :extra "and 4:30pm-8pm with Tortellini Tuesday"}
                                     "wednesday" {:open 7
                                                  :close 15
                                                  :extra "and 4:30pm-8pm"}
                                     "thursday" {:open 7
                                                 :close 15
                                                 :extra "and 4:30pm-8pm"}
                                     "friday" {:open 7
                                               :close 15
                                               :extra "and 4:30pm-8pm"}
                                     "saturday" {:open 10
                                                 :close 14
                                                 :extra "and 4:30pm-8pm"}
                                     "sunday" {:open 10
                                               :close 14
                                               :extra "and 4:30pm-8pm"}}
                       "randlounge" {"monday" {:open 7
                                               :close 10.3
                                               :extra "11am-3pm, and 4:30pm-8pm"}
                                     "tuesday" {:open 7
                                                :close 10.3
                                                :extra "11am-3pm, and 4:30pm-8pm"}
                                     "wednesday" {:open 7
                                                  :close 10.3
                                                  :extra "11am-3pm, and 4:30pm-8pm"}
                                     "thursday" {:open 7
                                                 :close `10.3
                                                 :extra "11am-3pm, and 4:30pm-8pm"}
                                     "friday" {:open 7
                                               :close 10.3
                                               :extra "and 11am-3pm"}
                                     "sunday" {:open 7
                                               :close 10.3
                                               :extra "11am-3pm, and 4:30pm-8pm"}}
                       "commons" {"monday" {:open 7
                                            :close 20
                                            :extra "and no other open times"}
                                  "tuesday" {:open 7
                                             :close 20
                                             :extra "and no other open times"}
                                  "wednesday" {:open 7
                                               :close 20
                                               :extra "and no other open times"}
                                  "thursday" {:open 7
                                              :close 20
                                              :extra "and no other open times"}
                                  "friday" {:open 7
                                            :close 20
                                            :extra "and no other open times"}
                                  "saturday" {:open 10
                                              :close 14
                                              :extra "4:30pm-8pm"}
                                  "sunday" {:open 10
                                            :close 20
                                            :extra "4:30pm-8pm"}}
                       "e-bronson-ingram" {"monday" {:open 7.3
                                                      :close 10
                                                      :extra "11:00am-7:30pm"}
                                            "tuesday" {:open 7.3
                                                       :close 10
                                                       :extra "11:00am-7:30pm"}
                                            "wednesday" {:open 7.3
                                                         :close 10
                                                         :extra "11:00am-7:30pm"}
                                            "thursday" {:open 7.3
                                                        :close 10
                                                        :extra "11:00am-7:30pm"}
                                            "friday" {:open 7.3
                                                      :close 10
                                                      :extra "and no other open times"}
                                            "sunday" {:open 15.3
                                                      :close 17.3
                                                      :extra "and 5:30pm-7:30pm"}}
                       "rand-munchie" {"monday" {:open 8
                                                 :close 19.3
                                                 :extra "and no other open times"}
                                       "tuesday" {:open 7.3
                                                  :close 10
                                                  :extra "and no other open times"}
                                       "wednesday" {:open 7.3
                                                    :close 10
                                                    :extra "and no other open times"}
                                       "thursday" {:open 7
                                                   :close 22
                                                   :extra "and no other open times"}
                                       "friday" {:open 7
                                                 :close 22
                                                 :extra "and no other open times"}}
                       "towers-munchie" {"monday" {:open 7
                                                   :close 3
                                                   :extra "and no other open times"}
                                         "tuesday" {:open 7
                                                    :close 3
                                                    :extra "and no other open times"}
                                         "wednesday" {:open 7
                                                      :close 3
                                                      :extra "and no other open times"}
                                         "thursday" {:open 7
                                                     :close 3
                                                     :extra "and no other open times"}
                                         "friday" {:open 7
                                                   :close 23
                                                   :extra "and no other open times"}
                                         "saturday" {:open 9
                                                     :close 23
                                                     :extra "and no other open times"}
                                         "sunday" {:open 9
                                                   :close 3
                                                   :extra "and no other open times"}}
                       "kissam-munchie" {"monday" {:open 0
                                                   :close 0
                                                   :extra "and no other open times"}
                                         "tuesday" {:open 0
                                                    :close 0
                                                    :extra "and no other open times"}
                                         "wednesday" {:open 0
                                                      :close 0
                                                      :extra "and no other open times"}
                                         "thursday" {:open 0
                                                     :close 0
                                                     :extra "and no other open times"}
                                         "friday" {:open 0
                                                   :close 23
                                                   :extra "and no other open times"}
                                         "saturday" {:open 9
                                                     :close 23
                                                     :extra "and no other open times"}
                                         "sunday" {:open 9
                                                   :close 0
                                                   :extra "and no other open times"}}
                       "highland-munchie" {"monday" {:open 7
                                                     :close 3
                                                     :extra "and no other open times"}
                                           "tuesday" {:open 7
                                                      :close 3
                                                      :extra "and no other open times"}
                                           "wednesday" {:open 7
                                                        :close 3
                                                        :extra "and nno other open times"}
                                           "thursday" {:open 7
                                                       :close 3
                                                       :extra "and no other open times"}
                                           "friday" {:open 7
                                                     :close 23
                                                     :extra "and no other open times"}
                                           "saturday" {:open 9
                                                       :close 23
                                                       :extra "and no other open times"}
                                           "sunday" {:open 9
                                                     :close 3
                                                     :extra "and no other open times"}}
                       "pub" {"monday" {:open 11
                                        :close 21
                                        :extra "and no other open times"}
                              "tuesday" {:open 11
                                         :close 21
                                         :extra "and no other open times"}
                              "wednesday" {:open 11
                                           :close 21
                                           :extra "and no other open times"}
                              "thursday" {:open 11
                                          :close 21
                                          :extra "and no other open times"}
                              "friday" {:open 11
                                        :close 15
                                        :extra "and no other open times"}
                              "sunday" {:open 15
                                        :close 21
                                        :extra "and no other open times"}}
                       "local-java" {"monday" {:open 7
                                               :close 17
                                               :extra "and no other open times"}
                                     "tuesday" {:open 7
                                                :close 17
                                                :extra "and no other open times"}
                                     "wednesday" {:open 7
                                                  :close 17
                                                  :extra "and no other open times"}
                                     "thursday" {:open 7
                                                 :close 17
                                                 :extra "and no other open times"}
                                     "friday" {:open 7
                                               :close 15
                                               :extra "and no other open times"}}
                       "rocket-subs-towers" {"monday" {:open 11
                                                       :close 22
                                                       :extra "and no other open times"}
                                             "tuesday" {:open 11
                                                        :close 22
                                                        :extra "and no other open times"}
                                             "wednesday" {:open 11
                                                          :close 22
                                                          :extra "and no other open times"}
                                             "thursday" {:open 11
                                                         :close 22
                                                         :extra "and no other open times"}
                                             "sunday" {:open 10
                                                       :close 20
                                                       :extra "and no other open times"}}
                       "kissam-kitchen" {"monday" {:open 7.3
                                                   :close 10
                                                   :extra "11am-2pm, and 5:30pm-7:30pm"}
                                         "tuesday" {:open 7.3
                                                    :close 10
                                                    :extra "11am-2pm, and 5:30pm-7:30pm"}
                                         "wednesday" {:open 7.3
                                                      :close 10
                                                      :extra "11am-2pm, and 5:30pm-7:30pm"}
                                         "thursday" {:open 7.3
                                                     :close 10
                                                     :extra "11am-2pm, and 5:30pm-7:30pm"}
                                         "friday" {:open 7.3
                                                   :close 10
                                                   :extra "11am-2pm"}
                                         "sunday" {:open 17.3
                                                   :close 19.3
                                                   :extra "and no other open times"}}
                       "rocket-subs-highland" {"monday" {:open 11
                                                         :close 22
                                                         :extra "and no other open times"}
                                               "tuesday" {:open 11
                                                          :close 22
                                                          :extra "no no other open times"}
                                               "wednesday" {:open 11
                                                            :close 22
                                                            :extra "and no other open times"}
                                               "thursday" {:open 11
                                                           :close 22
                                                           :extra "and no other open times"}
                                               "sunday" {:open 11
                                                         :close 22
                                                         :extra "and no other open times"}}
                     "food-for-thought" {"monday" {:open 8
                                                   :close 20
                                                   :extra "and no other open times"}
                                         "tuesday" {:open 8
                                                    :close 20
                                                    :extra "and no other open times"}
                                         "wednesday" {:open 8
                                                      :close 20
                                                      :extra "and no other open times"}
                                         "thursday" {:open 8
                                                     :close 20
                                                     :extra "and no other open times"}
                                         "friday" {:open 8
                                                   :close 15
                                                   :extra "and no other open times"}
                                         "sunday" {:open 14
                                                   :close 20
                                                   :extra "and no other open times"}}
                     "grins" {"monday" {:open 7.3
                                        :close 21
                                        :extra "and no other open times"}
                              "tuesday" {:open 7.3
                                         :close 21
                                         :extra "and no other open times"}
                              "wednesday" {:open 7.3
                                           :close 21
                                           :extra "and no other open times"}
                              "thursday" {:open 7.3
                                          :close 21
                                          :extra "and no other open times"}
                              "friday" {:open 7.3
                                        :close 15
                                        :extra "and no other open times"}}
                     "suzies-blair" {"monday" {:open 7.3}
                                              :close 14.3
                                              :extra "and no other open times"}
                                    "tuesday" {:open 7.3
                                               :close 14.3
                                               :extra " and no other open times"}
                                    "wednesday" {:open 7.3
                                                 :close 14.3
                                                 :extra "and no other open times"}
                                    "thursday" {:open 7.3
                                                :close 14.3
                                                :extra "and no other open times"}
                                    "friday" {:open 7.3
                                              :close 14.3
                                              :extra "and no other open times"}
                     "suzies-fgh" {"monday" {:open 7.3
                                             :close 14.3
                                             :extra "and no other open times"}
                                   "tuesday" {:open 7.3
                                              :close 14.3
                                              :extra "and no other open times"}
                                   "wednesday" {:open 7.3
                                                :close 14.3
                                                :extra "and no other open times"}
                                   "thursday" {:open 7.3
                                               :close 14.3
                                               :extra "and no other open times"}
                                   "friday" {:open 7.3
                                             :close 14.3
                                             :extra "and no other open times"}}
                     "suzies-mrb" {"monday" {:open 7
                                             :close 16.3
                                             :extra "and no other open times"}
                                   "tuesday" {:open 7
                                              :close 16.3
                                              :extra "and no other open times"}
                                   "wednesay" {:open 7
                                               :close 16.3
                                               :extra "and no other open times"}
                                   "thursday" {:open 7
                                               :close 16.3
                                               :extra "and no other open times"}
                                   "friday" {:open 7
                                             :close 16.3
                                             :extra "and no other open times"}}})









(def describe-rest {"randcenter" {:description "Rand Dining Center is the focal point of the campus community. Its central location, great food, and comfortable atmosphere provide students,
                                 faculty, and staff an opportunity to gather, socialize, and interact.
                                 Rand retail offers a variety of foods such as baked goods, build-your own salads, Mongolian grill, deli, Fresh Mex, omelets, pasta, potato bar, and more."}
                    "randlounge" {:description  "2301 is an allergen-friendly (free of the top 8 allergens plus sesame seeds) restaurant serving made-to-order
                                 smoothies and salads, Halal protein sautes, and plant-forward, vegan sides."}
                    "commons" {:description "The state-of-the-art dining facility at The Commons Center includes a salad bar, Chef’s Table, specialty pizza oven, deli, wok, grill, Halal beef and chicken,
                              and vegetarian/vegan food. The center is also the first gold-certified (certification for its environmentally friendly construction) building in Nashville."}
                    "e-bronson-ingram" {:description "This all-you-care-to-eat location features breakfast, lunch, and dinner with a salad bar, Asian cuisines, and our award-winning Bamboo Bistro pho concept."}
                    "commons-munchie" {:description "Common Munchie Mart is a 24-hour coffee shop in The Commons Center offers grab-n-go meals, beverages and sundries. Particular emphasis on using biodegradable/compostable, and local products."}
                    "rand-munchie" {:description "In the heart of campus (Sarratt next to Rand Dining Center) is the Rand Munchie Mart which carries meals compatible with the VU Meal Plans, school supplies and gifts, local and organic items as well as various sundries."}
                    "branscomb-munchie" {:description "Open 24 hours! Featuring grab-n-go snacks, microwave meals, salads, fresh fruits, sandwiches, coffee, baked goods, tons of groceries, and more!"}
                    "towers-munchie" {:description "Located in the basement of Carmichael Towers East, Towers Munchie features grocery items, a variety of bottled beverages, dairy products, sandwiches, salads, fresh fruits, and more."}
                    "kissam-munchie" {:description "This store offers grab-n-go meal plan items, groceries, school supplies, Peets Coffee, frozen entrees and more."}
                    "highland-munchie" {:description "Bringing convenience to Area VI, Highland Munchie offers grocery items, beverages, dairy products, and sandwiches, salads, and fresh fruits."}
                    "pub" {:description "Casual dining restaurant serving a-la-carte lunch (burgers, salads, sandwiches, chicken fingers) and weekly specials. The Pub is a perfect place to watch a game on its state-of-the-art TV/sound system."}
                    "local-java" {:description "Local Java, partnered with Frothy Monkey Roasting Co, offers breakfast, lunch, dinner, and coffee."}
                    "rocket-subs-towers" {:description "Located in  Towers Market, Rocket Subs offers made-to-order sandwiches that are out of this world."}
                    "branscomb" {:description "24/7 store."}
                    "kissam-kitche" {:description "Serving hot, prepared meals for breakfast Monday-Friday and dinner Sunday-Thursday. This open-air kitchen is where chef-crafted themed North American meals are served. Located at Moore College and Warren College at Kissam."}
                    "rocket-subs-highland" {:description "Located in the Highland Munchie, Rocket Subs offers made-to-order sandwiches that are out of this world."}
                    "food-for-thought" {:description "Food for Thought Cafe offers an assortment of delicious salads, soups, paninis, sides and desserts located in the Central Library."}
                    "grins" {:description "Grins Vegetarian Cafe in the Schulman Center offers wraps, salads, paninis, and baked goods with a Kosher-certified kitchen."}
                    "suzies-blair" {:description "Suzie's offers an assortment of delicious sandwiches, bagels, muffins, wraps, coffee and many other treats. Suzie’s takes cash, Commodore Card and Meal Plan."}
                    "suzies-mrb" {:description "Grab & Go sandwiches, salad, and coffee. Medical Research Building III."}
                    "suzies-fgh" {:description "Suzie's offers an assortment of delicious sandwiches, bagels, muffins, wraps, coffee and many other treats. Suzie’s takes cash, Commodore Card and Meal Plan."}})








(def rl ["randlounge" "randcenter" "commons" "e-bronson-ingram" "commons-munchie" "rand-munchie" "towers-munchie"
         "branscomb-munchie" "rand-munchie" "kissam-munchie" "highland-munchie" "pub" "local-java"
         "rocket-subs-towers" "branscomb" "kissam-kitchen" "rocket-subs-highland" "food-for-thought"
         "grins" "suzies-blair" "suzies-mrb" "suzies-fgh"])








;; Do not edit!
;; A map specifying the instructor's office hours that is keyed by day of the week.
(def instructor-hours {"tuesday"  {:start    8
                                   :end      14
                                   :location "the chairs outside of the Wondry"}

                       "thursday" {:start    8
                                   :end      10
                                   :location "the chairs outside of the Wondry"}})



(defn restaurants [_]
  (string/join ", " rl))


;; This is a helper function that you might want to use to implement
;; `cmd` and `args`.
(defn words [msg]
  (if msg
      (string/split msg #" ")
      []))

;; Asgn 1.
;;
;; @Todo: Fill in this function to return the first word in a text
;; message.
;;
;; Example: (cmd "foo bar") => "foo"
;;
;; See the cmd-test in test/asgnx/core_test.clj for the
;; complete specification.
;;
(defn cmd [msg]
  (first(words msg)))




;; Asgn 1.
;;
;; @Todo: Fill in this function to return the list of words following
;; the command in a text message.
;;
;; Example: (args "foo bar baz") => ("bar" "baz")
;;
;; See the args-test in test/asgnx/core_test.clj for the
;; complete specification.
;;
(defn args [msg]
  (drop 1 (words msg)))

;; Asgn 1.
;;
;; @Todo: Fill in this function to return a map with keys for the
;; :cmd and :args parsed from the msg.
;;
;; Example:
;;
;; (parsed-msg "foo bar baz") => {:cmd "foo" :args ["bar" "baz"]}
;;
;; See the parsed-msg-test in test/asgnx/core_test.clj for the
;; complete specification.
;;
(defn parsed-msg [msg]
  {:cmd (cmd msg)  :args (args msg)})


;; Asgn 1.
;;
;; @Todo: Fill in this function to prefix the first of the args
;; in a parsed message with "Welcome " and return the result.
;;
;; Example:
;;
;; (welcome {:cmd "welcome" :args ["foo"]}) => "Welcome foo"
;;
;; See the welcome-test in test/asgnx/core_test.clj for the
;; complete specification.
;;
(defn welcome [pmsg]
  (str "Welcome " (first(get pmsg :args))))

;; Asgn 1.
;;
;; @Todo: Fill in this function to return the CS 4278 home page.
;; Use the `cs4278-brightspace` def to produce the output.
;;
;; See the homepage-test in test/asgnx/core_test.clj for the
;; complete specification.
;;
(defn homepage [_]
  (name dining-homepage))


(defn menu [_]
  (name foodmenu))




;; Asgn 1.
;;
;; @Todo: Fill in this function to convert from 0-23hr format
;; to AM/PM format.
;;
;; Example: (format-hour 14) => "2pm"
;;
;; See the format-hour-test in test/asgnx/core_test.clj for the
;; complete specification.
;;
(defn format-hour [h]
  (if (and (> h 0)(< h 12))
    (str h "am")
    (if (= h 12)
      (str h "pm")
      (if (> h 12)
        (str (- h 12) "pm")
        (str 12 "am")))))





;; Asgn 1.
;;
;; @Todo: This function should take a map in the format of
;; the values in the `instructor-hours` map (e.g. {:start ... :end ... :location ...})
;; and convert it to a string format.
;;
;; Example:
;; (formatted-hours {:start 8 :end 10 :location "the chairs outside of the Wondry"}))
;; "from 8am to 10am in the chairs outside of the Wondry"
;;
;; You should use your format-hour function to implement this.
;;
;; See the formatted-hours-test in test/asgnx/core_test.clj for the
;; complete specification.
;;
(defn formatted-hours [hours]
  (str "from " (format-hour (get hours :start)) " to " (format-hour (get hours :end))
       " in " (get hours :location)))

(defn open-close [hours]
  (str "Open from " (format-hour (get hours :open)) " to " (format-hour (get hours :close))
       ", " (get hours :extra)))

(defn describe1 [hours]
  (str (get hours :description)))




;; Asgn 1.
;;
;; @Todo: This function should lookup and see if the instructor
;; has office hours on the day specified by the first of the `args`
;; in the parsed message. If so, the function should return the
;; `formatted-hours` representation of the office hours. If not,
;; the function should return "there are no office hours on that day".
;; The office hours for the instructor should be obtained from the
;; `instructor-hours` map.
;;
;; You should use your formatted-hours function to implement this.
;;
;; See the office-hours-for-day-test in test/asgnx/core_test.clj for the
;; complete specification.
;;
;; (defn office-hours [{:keys [args cmd]}])
;;  (if (contains? instructor-hours (first args))
;;    (formatted-hours [instructor-hours])
;;    (str "there are no office hours on that day")))
(defn office-hours [{:keys [args cmd]}]
  (if (contains? instructor-hours (first args))
    (str (formatted-hours (get instructor-hours (first args))) " and we will be there")
    (str "there are no office hours on that day")))

(defn rest-options [_]
  (str "Welcome to campus dining hours. Your options are: menu, hours <restaurant> <day>,
describe <restaurant>, restaurants, homepage, diner <restaurant>, ask <restaurant>, answer <restaurant>"))

(defn description [{:keys [args cmd]}]
  (if (contains? describe-rest (first args))
    (str (describe1 (get describe-rest (first args))))
    (str (first args) "is not a valid restaurant")))

(defn check-args [{:keys [args cmd]}]
  (if (or (= (first args) "commons-munchie") (= (first args) "branscomb munchie") (= (first args) "branscomb"))
    (str "This location is open 24 hours a day, 7 days a week")
    (if (nil? (get-in restaurant-map [(first args) (second args)]))
      (str (first args) " is not open on that day")
      (if (some (partial = (first args)) rl)
        (str (open-close (get-in restaurant-map [(first args) (second args)])))
        (str "That is not a valid location")))))



; (defn check-args [{:keys [args cmd]}]
;   (if (not (nil? (get-in restaurant-map [(first args) (second args)])))
;     (if (or (= (first args) "commons-munchie") (= (first args) "branscomb-munchie"))
;        (str "This location is open 24 hours a week, 7 days a week")
;        (if (some (partial = (first args)) rl)
;         (str (formatted-hours1 (get-in restaurant-map [(first args) (second args)])))
;         (str "That is not a valid location")))
;     (str "That eatery is not open on that day")))


; (defn restaurant-hours [{:keys [args cmd]}]
;    (if (or (= (first args) "commons-munchie") (= (first args) "branscomb-munchie"))
;     (str "This location is open 24 hours a week, 7 days a week")
;     (if (some (partial = (first args)) rl)
;       (str (open-close (get-in restaurant-map [(first args) (second args)])))
;       (str "That is not a valid location"))))



(defn rest-args [rest-routes]
  (fn [msg]
    (let [args (msg :args)]
      (if (contains? rest-args (first args))
        (get rest-args (first args))
        (get rest-args "default")))))






;; Asgn 2.
;;
;; @Todo: Create a function called action-send-msg that takes
;; a destination for the msg in a parameter called `to`
;; and the message in a parameter called `msg` and returns
;; a map with the keys :to and :msg bound to each parameter.
;; The map should also have the key :action bound to the value
;; :send.
;;
(defn action-send-msg [to msg]
  {:to to :msg msg :action :send})

;; Asgn 2.
;;
;; @Todo: Create a function called action-send-msg that takes
;; takes a list of people to receive a message in a `people`
;; parameter and a message to send them in a `msg` parmaeter
;; and returns a list produced by invoking the above `action-send-msg`
;; function on each person in the people list.
;;
;; java-like pseudo code:
;;
;; output = new list
;; for person in people:
;;   output.add( action-send-msg(person, msg) )
;; return output
;;
(defn action-send-msgs [people msg]
  (map #(action-send-msg % msg) people))


;; Asgn 2.
;;
;; @Todo: Create a function called action-insert that takes
;; a list of keys in a `ks` parameter, a value to bind to that
;; key path to in a `v` parameter, and returns a map with
;; the key :ks bound to the `ks` parameter value and the key :v
;; vound to the `v` parameter value.)
;; The map should also have the key :action bound to the value
;; :assoc-in.
;;
(defn action-insert [ks v]
  {:action :assoc-in, :ks ks, :v v})

;; Asgn 2.
;;
;; @Todo: Create a function called action-inserts that takes:
;; 1. a key prefix (e.g., [:a :b])
;; 2. a list of suffixes for the key (e.g., [:c :d])
;; 3. a value to bind
;;
;; and calls (action-insert combined-key value) for each possible
;; combined-key that can be produced by appending one of the suffixes
;; to the prefix.
;;
;; In other words, this invocation:
;;
;; (action-inserts [:foo :bar] [:a :b :c] 32)
;;
;; would be equivalent to this:
;;
;; [(action-insert [:foo :bar :a] 32)
;;  (action-insert [:foo :bar :b] 32)
;;  (action-insert [:foo :bar :c] 32)]
;;
(defn action-inserts [prefix ks v]
  (map #(action-insert (conj prefix %) v) ks))

;; Asgn 2.
;;
;; @Todo: Create a function called action-remove that takes
;; a list of keys in a `ks` parameter and returns a map with
;; the key :ks bound to the `ks` parameter value.
;; The map should also have the key :action bound to the value
;; :dissoc-in.
;;
(defn action-remove [ks] {:action :dissoc-in :ks ks})

;; Asgn 3.
;;
;; @Todo: Create a function called "experts-register"
;; that takes the current application `state`, a `topic`
;; the expert's `id` (e.g., unique name), and information
;; about the expert (`info`) and registers them as an expert on
;; the specified topic. Look at the associated test to see the
;; expected function signature.
;;
;; Your function should NOT directly change the application state
;; to register them but should instead return a list of the
;; appropriate side-effects (above) to make the registration
;; happen (hint: action-insert).
;;
;; See the integration test in See handle-message-test for the
;; expectations on how your code operates
;;
(defn experts-register [experts topic id info]
  [(action-insert [:expert topic id] info)])


;; Asgn 3.
;;
;; @Todo: Create a function called "experts-unregister"
;; that takes the current application `state`, a `topic`
;; and the expert's `id` (e.g., unique name) and then
;; removes the expert from the list of experts on that topic.
;; Look at the associated test to see the expected function signature.
;;
;; Your function should NOT directly change the application state
;; to unregister them but should instead return a list of the
;; appropriate side-effects (above) to make the registration
;; happen (hint: action-remove).
;;
;; See the integration test in See handle-message-test for the
;; expectations on how your code operates
;;
(defn experts-unregister [experts topic id]
  [(action-remove [:expert topic id])])



(defn experts-question-msg [experts question-words]
  (str "Asking " (count experts) " diner(s) for an answer to: \""
       (string/join " " question-words) "\""))

;; Asgn 3.
;;
;; @Todo: Create a function called "ask-experts"
;; that takes two parameters:
;;
;; 1. the list of experts on the topic
;; 2. a parsed message with the format:
;;    {:cmd "ask"
;;     :user-id "phone number that sent the message"
;;     :args [topic question-word1 question-word2 ... question-wordN]}
;;
;; The sender of the message will be identified by their phone number
;; in the user-id parameter. This is the phone number that you will need
;; to forward answers to the question to.
;;
;; The parsed message is generated by breaking up the words in the ask
;; text message. For example, if someone sent the message:
;;
;; "ask food what is the best pizza in nashville"
;;
;; The parsed message would be:
;;
;; {:cmd "ask"
;;  :user-id "+15555555555"
;;  :args ["food" "what" "is" "the" "best" "pizza" "in" "nashville"]}
;;
;; This function needs to return a list with two elements:
;; [[actions...] "response to asker"]
;;
;; The actions in the list are the *side effects* that need to take place
;; to ask the question (e.g., sending messages to the experts). The string
;; is the response that is going to be sent back to the person that asked
;; the question (e.g. "Asking 2 expert(s) for an answer to ....").
;;
;; The correct string response to a valid question should be produced with
;; the `experts-question-msg` function above.
;;
;; Think about how you are going to figure out where to route messages
;; when an expert answers (see the conversations query) and make sure you
;; handle the needed side effect for storing the conversation state.
;;
;; If there are no registered experts on a topic, you should return an
;; empty list of actions and "There are no experts on that topic."
;;
;; If there isn't a question, you should return "You must ask a valid question."
;;
;; Why this strange architecture? By returning a list of the actions to take,
;; rather than directly taking that action, we can keep this function pure.
;; Pure functions are WAY easier to test / maintain. Also, we can isolate our
;; messy impure action handling at the "edges" of the application, where it is
;; easier to track and reason about.
;;
;; You should look at `handle-message` to get an idea of the way that this
;; function is going to be used, its expected signature, and how the actions
;; and output are going to work.
;;
;; See the integration test in See handle-message-test for the
;; expectations on how your code operates
;;
(defn ask-diner [experts {:keys [args user-id]}]
  (let [question-words (rest args)]
    (cond
      (empty? question-words) [[] "You must ask a valid question."]
      (empty? experts) [[] "There are no experts on that topic."]
      :else [(into [] (concat (action-inserts [:conversations] experts user-id)
                       (action-send-msgs experts (clojure.string/join " " question-words))))
             (experts-question-msg experts question-words)])))


;; Asgn 3.
;;
;; @Todo: Create a function called "answer-question"
;; that takes two parameters:
;;
;; 1. the last conversation describing the last question that was routed
;;    to the expert
;; 2. a parsed message with the format:
;;    {:cmd "ask"
;;     :user-id "+15555555555"
;;     :args [topic answer-word1 answer-word2 ... answer-wordN]}
;;
;; The parsed message is generated by breaking up the words in the ask
;; text message. For example, if someone sent the message:
;;
;; "answer joey's house of pizza"
;;
;; The conversation will be data that you store as a side-effect in
;; ask-experts. You probably want this data to be information about the
;; last question asked to each expert. See the "think about" comment above.
;;
;; The parsed message would be:
;;
;; {:cmd "answer"
;;  :user-id "+15555555555"
;;  :args ["joey's" "house" "of" "pizza"]}
;;
;; This function needs to return a list with two elements:
;; [[actions...] "response to expert answering"]
;;
;; The actions in the list are the *side effects* that need to take place
;; to send the answer to the original question asker. The string
;; is the response that is going to be sent back to the expert answering
;; the question.
;;
;; Think about how you are going to figure out where to route messages
;; when an expert answers (see the conversations query) and make sure you
;; handle the needed side effect for storing the conversation state.
;;
;; Why this strange architecture? By returning a list of the actions to take,
;; rather than directly taking that action, we can keep this function pure.
;; Pure functions are WAY easier to test / maintain. Also, we can isolate our
;; messy impure action handling at the "edges" of the application, where it is
;; easier to track and reason about.
;;
;; You should look at `handle-message` to get an idea of the way that this
;; function is going to be used, its expected signature, and how the actions
;; and output are going to work.
;;
;; See the integration test in See handle-message-test for the
;; expectations on how your code operates
;;
(defn answer-question [conversation {:keys [args]}]
  (let [answer-words (rest args)]
    (cond
      (empty? answer-words) [[] "You did not provide an answer."]
      (= nil conversation) [[] "You haven't been asked a question."]
      :else [[(action-send-msg conversation (clojure.string/join " " args))] (str "Your answer was sent.")])))

;; Asgn 3.
;;
;; @Todo: Create a function called "add-expert"
;; that takes two parameters:
;;
;; 1. the current list of experts on the topic
;; 2. a parsed message with the format:
;;    {:cmd "expert"
;;     :user-id "+15555555555"
;;     :args [topic]
;;
;;
;; The parsed message is generated by breaking up the words in the expert
;; text message. For example, if someone sent the message:
;;
;; "expert food"
;;
;; The parsed message would be:
;;
;; {:cmd "expert"
;;  :user-id "+15555555555"))
;;  :args ["food"]}
;;
;; This function needs to add "sara" to the list of experts on "food" and
;; associate her phone number with her ID.
;;
;; This function needs to return a list with two elements:
;; [[actions...] "response to the person adding themselves as an expert"]
;;
;; The actions in the list are the *side effects* that need to take place
;; to add the person as an expert on the topic (hint: result of calling experts-register). The string
;; is the response that is going to be sent back to the person adding themselves
;; as an expert.
;;
;; You should look at `handle-message` to get an idea of the way that this
;; function is going to be used, its expected signature, and how the actions
;; and output are going to work.
;;
;; See the integration test in See handle-message-test for the
;; expectations on how your code operates
(defn add-diner [experts {:keys [args user-id]}]
  [(experts-register experts (first args) user-id nil)
   (str user-id " is now an expert on " (first args) ".")])

;; Don't edit!
(defn stateless [f]
 (fn [_ & args]
  [[] (apply f args)]))


(def routes {"default"  (stateless (fn [& args] "Unknown command. Text 'options' for help"))
             "welcome"  (stateless welcome)
             "homepage" (stateless homepage)
             "office"   (stateless office-hours)
             "diner"    add-diner
             "ask"      ask-diner
             "answer"   answer-question
             "menu"     (stateless menu)
             "hours"  (stateless check-args)
             "restaurants" (stateless restaurants)
             "describe" (stateless description)
             "options" (stateless rest-options)})



(defn rest-args [rest-routes]
  (fn [msg]
    (let [args (msg :args)]
      (if (contains? rest-args (first args))
        (get rest-args (first args))
        (get rest-args "default")))))

; (def rest-routes {"default"  (stateless (fn [& args] "Unknown command."))}
;     "randcenter"  (stateless randcenter)
;      "homepage" (stateless homepage))

;; Asgn 3.
;;
;; @Todo: Add mappings of the cmds "expert", "ask", and "answer" to
;; to the `routes` map so that the functions that you
;; created will be invoked when the corresponding text message
;; commands are received.
;;})


;; Don't edit!
(defn experts-on-topic-query [state-mgr pmsg]
 (let [[topic]  (:args pmsg)]
  (list! state-mgr [:expert topic])))


;; Don't edit!
(defn conversations-for-user-query [state-mgr pmsg]
 (let [user-id (:user-id pmsg)]
  (get! state-mgr [:conversations user-id])))


;; Don't edit!
(def queries
 {"expert" experts-on-topic-query
  "ask"    experts-on-topic-query
  "answer" conversations-for-user-query})


;; Don't edit!
(defn read-state [state-mgr pmsg]
  (go
   (if-let [qfn (get queries (:cmd pmsg))]
     (<! (qfn state-mgr pmsg))
     {})))


;; Asgn 1.
;;
;; @Todo: This function should return a function (<== pay attention to the
;; return type) that takes a parsed message as input and returns the
;; function in the `routes` map that is associated with a key matching
;; the `:cmd` in the parsed message. The returned function would return
;; `welcome` if invoked with `{:cmd "welcome"}`.
;;
;; Example:
;;
;; (let [msg {:cmd "welcome" :args ["bob"]}]
;;   (((create-router {"welcome" welcome}) msg) msg) => "Welcome bob"
;;
;; If there isn't a function in the routes map that is mapped to a
;; corresponding key for the command, you should return the function
;; mapped to the key "default".
;;
;; See the create-router-test in test/asgnx/core_test.clj for the
;; complete specification.
;;
(defn create-router [routes]
  (fn [msg]
    (let [cmd (msg :cmd)]
      (if (contains? routes cmd)
        (get routes cmd)
        (get routes "default")))))





;; Don't edit!
(defn output [o]
  (second o))


;; Don't edit!
(defn actions [o]
  (first o))


;; Don't edit!
(defn invoke [{:keys [effect-handlers] :as system} e]
  (go
   (println "    Invoke:" e)
   (if-let [action (get effect-handlers (:action e))]
     (do
       (println "    Invoking:" action "with" e)
       (<! (action system e))))))


;; Don't edit!
(defn process-actions [system actions]
  (go
   (println "  Processing actions:" actions)
   (let [results (atom [])]
     (doseq [action actions]
       (let [result (<! (invoke system action))]
         (swap! results conj result)))
     @results)))


;; Don't edit!
(defn handle-message
  "
    This function orchestrates the processing of incoming messages
    and glues all of the pieces of the processing pipeline together.
    The basic flow to handle a message is as follows:
    1. Create the router that will be used later to find the
       function to handle the message
    2. Parse the message
    3. Load any saved state that is going to be needed to process
       the message (e.g., querying the list of experts, etc.)
    4. Find the function that can handle the message
    5. Call the handler function with the state from #3 and
       the message
    6. Run the different actions that the handler returned...these actions
       will be bound to different implementations depending on the environemnt
       (e.g., in test, the actions aren't going to send real text messages)
    7. Return the string response to the message
  "
  [{:keys [state-mgr] :as system} src msg]
  (go
   (println "=========================================")
   (println "  Processing:\"" msg "\" from" src)
   (let [rtr    (create-router routes)
         _      (println "  Router:" rtr)
         pmsg   (assoc (parsed-msg msg) :user-id src)
         _      (println "  Parsed msg:" pmsg)
         state  (<! (read-state state-mgr pmsg))
         _      (println "  Read state:" state)
         hdlr   (rtr pmsg)
         _      (println "  Hdlr:" hdlr)
         [as o] (hdlr state pmsg)
         _      (println "  Hdlr result:" [as o])
         arslt  (<! (process-actions system as))
         _      (println "  Action results:" arslt)]
     (println "=========================================")
     o)))
