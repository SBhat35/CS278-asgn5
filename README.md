# CS278 Text Messaging Application

  My project stems from a personal problem that I have continued to face during my time at Vanderbilt. For the majority of time at Vanderbilt, students eat their meals on campus and are restricted to the hours imposed by the different eateries. As a freshman, this was not a huge problem as most meals were eaten in the Commons Center where the hours were standardized and the center was open 7 nights a week. Yet as I moved to main campus, most of my meals were based upon which restaurants I was closest to. While I prioritized convenience over desired menu, I realized that often the places I wanted to eat were not open during the times I wanted to eat. This required me going into the Campus Dining app, looking through all the restaurants, figuring out which were still open, and then going there based on menu. <br/> <br/>
  My proposal involves creating a text-based application that solves the problem of students not knowing either the meal periods or the hours for on campus eateries. Students shall be able to specify the eatery of their choice to learn more about the hours for the day, or find out about hours for the week. This should have a similar interface and feel of the application crated in class with some added features. Example input could be “Grins hours today” with an output of “The hours for Grins today (October 1, 2018) are 7:30AM - 9:00PM.” Further students can check the meal periods at any time to get a breakdown of the different times. My final feature that might be a bit more difficult to implement would be a menu with the items being served on a particular day. This would involve pulling some data from the server or online Vanderbilt Dining webpage, but should be feasible. <br/> <br/>
	As for my interviews, I targeted one sophomore, one junior, and one senior for a diversity of outlooks and thoughts. Based on the responses, it seems as though the campus menus are the most important for students followed by restaurant hours and then meal periods. I think having a simple interface and way of interacting and understanding the campus dining meals and schedule is an important part of one’s meal choice. By allowing for simple commands that can pull specific pieces of data from specified restaurants, the application is limited in scope yet covers some critical problems faced by current students. 


# Questions:
  1. How often do you eat on campus?
  2. Can you give a rough breakdown of the distribution of on campus eateries where you eat?
  3. What are the primary reasons you choose to eat at these eateries?
  4. How well do you know the meal periods off the top of your head? Would your eating habits change if they were more accessible?
  5. Would you utilize a service that allows you to hear from others who have eaten at a certain location?
  6. How important are restaurant hours in choosing where you eat/do they restrict your eating schedule?
  7. Do you check the menus before choosing a restaurant?
  8. How important is it for you to try new meals on campus?
  9. Would it be helpful to get reminders of closing times for certain on campus eateries?
  10. Would a text messaging application to solve some of these problems be of use?



# Answers:

## Question 1: 
Roth: I eat on campus usually 3 times a day, except for Thursday- Sunday where I would say I eat 2 times a day on campus <br/>
Movva: I eat on campus monday through thursday, usually for lunch and dinner. <br/>
Jana: Twice a day during the week, less on weekends <br/>

## Question 2:
Roth: Normally Grins, Rand and commons <br/>
Movva: I eat lunch usually at grins or rand, specifically bowls or at chef james. <br/>
Jana: Rand for almost every meal, with a bit of EBI, Kissam, Aryeh’s and pub mixed in <br/>

## Question 3:
Roth: Grins and Rand are really close, and I eat at commons when I have HOD classes there on Tuesday Thursday <br/>
Movva: I like grins and the bowl line because they have a wide variety of foods that are mostly health and filling. Rand has a lot of options so I have more flexibility when it comes to sides. Also, a lot of my friends do homework in rand and grins, so I also eat at these eateries for social reasons. <br/>
Jana: Type of food they offer and proximity to where I live <br/>

## Question 4:
Roth: Don’t know the meal periods super well at all, but don’t think they would change that much <br/>
Movva: I don't really know the meal periods off the top of my head. I know the dinner meal period is from 4-8, but I always get confused when the breakfast meal time ends. Especially on the weekends when the breakfast options stay open later, I always forget when they end. <br/>
Jana: I know the general time but the specific cut off. I don’t believe my eating habits would change unless the school got rid of the unnecessary meal periods <br/>

## Question 5:
Roth: For certain restaurants yes <br/>
Movva: Yes, I usually base my restaurant preferences on the menus so having an idea of whether others like it would be helpful <br/>
Jana: Probably for a few locations on certain days <br/>

## Question 6:
Roth: Campus dining hours do usually affect my eating schedule, but restaurants i go to are almost always open <br/>
Movva: Not extremely important. Although certain places like Food For Thought cafe closes at varying times. <br/>
Jana: Restaurants closing at 8 do restrict my eating options at times, but for the most part I have enough options that it doesn’t really <br/>

## Question 7:
Roth: Almost always <br/>
Movva: When it comes to rand or kissam, I always check the menus before going because they are different every day. But grins and bowls  are always the same, so i don’t have to check. <br/>
Jana: No <br/>

## Question 8:
Roth: Like to try new things but not necessary i try new meals <br/>
Movva: Not important. <br/>
Jana: Not very. I generally know what I like, however I do like variety so new options would be welcome. <br/>

## Question 9:
For closing times no, but maybe opening times yes <br/>
Movva: Yes <br/>
Jana: Not especially. Would only be relevant at a minute portion of times and an annoyance all other times <br/>

## Question 10:
Roth: I am on my phone quite a bit, I do think this would be of use <br/>
Movva: Yes <br/>
Jana: Yes, app might be easier <br/>


# Requirements
1. Students shall be able to add themselves as experts on a certain eatery after dining there
2. Students shall be able to check the hours of certain on campus eateries 
3. Students can ask other experts from a certain restaurant regarding the meals for the day
4. Application should specify if there are no experts on a certain restaurant
5. Application should provide meal periods when specified by user through URL webpage
6. If there exists expert(s) on a certain restaurant, application will randomly select one for response
7. The application should clear the experts for the restaurants each day after the restaurant closes

# Development Approach
The approach to this application is similar to the one we have developed in class but does have certain nuances. The problem that I am working to solve is the ability to check the hours for certain on campus eateries and get input on the meals for the day. The current MVP focuses on providing the hours of operation for the individual restaurants and the ability to add oneself as an expert and ask experts questions regarding the meals there. The application will work as follows. Each restaurant will be added as a data structure with the hours of operation. When a student eats at a certain restaurant, they can add themselves as an expert on the restaurant they ate at. Then, when other students want to choose a restaurant to eat at, they can ask experts certain questions regarding the meal choice and quality. The application should be able to also give a URL link to the meals served for each restaurant. The input will direct the user to the Vanderbilt Dining website that lists the meals for the day. This application is not meant to solely provide the meals for the day; instead, this is a social application that allows students to hear from other students that have dined at the restaurant. While it is important to know the meals for a certain day, that does not always indicate the taste of the food. <br/> <br/>

There are a number of limitations to this application that are not fully optimized and secure, one being the ability to add oneself as an expert. To do this, there would be some type of authentication to ensure that the student did actually eat at the restaurant specified. Another limitation is that only one expert gets asked for each question. Thus, one expert could be asked multiple questions in a row, and he/she might have scewed thoughts on the meal quality. <br/> <br/>

As for development, if a student requests the hours for a certain restaurant, it will pull all of the information from the 'hours' data structure mentioned earlier (that stores the restaurant hours). To add oneself as an expert, the application will follow a very similar mapping structure to the application built earlier, which will store the phone number and any external data wanted within the restaurant for a certain expert. Each command is mapped to a query that retrieves data for its associateed handler function. The query will look up the list of keys in the stored state. To clear the map after a certain day, I will implement a function similar to action-remove that will recursively run through the experts map and remove the people and their information. This will likely require a timestamp and checking method that will check the time and reset the maps state if passed a certain time (ex 10:00pm). 


# Usage
My application is a text based app that takes commands from users and translates them into responses based on a routing protocol that maps to different functions and actions. The number that is associated with the application is: **+16153984377**. There are a number of key commands that can be inputted and generate a valid response. 

## options 
options gives the users a list of all of the acceptable commands, including menu, hours <restaurant> <day>, describe <restaurant>, restaurants, homepage, diner <restaurant>, ask <restaurant>, answer <restaurant>
	
## ask hours <restaurant> day
Provides the hours of operations for the specified restaurant
	
## menu
Provides the URL of the Vanderbilt Campus Dining menus

## diner <restaurant>
Registers the user who sent text as a expert/diner for a certain restaurant. This allows them to answer questions on the specified restaurant
	
## ask <restaurant> question
This checks the map if there is a diner(s) who has eaten at the specified restaurant. If there are no registered diners for a restaurant it will alert the user of no diners
	
## answer <restaurant> question 
Once registered as a diner from a certain restaurant, they are able to answer the question to a certain 
	
## restaurants
Provides list of all acceptable restaurants

## describe <restaurant> 
Gives a brief description of the specified restaurant
	









