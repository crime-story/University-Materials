# EstateApp
Team Members:
- Linte Robert 331
- Popescu Robertto Paullo Karloss 331
- Paduraru Cristian-Daniel 351

# Intermediate deliverable Estate Team

## Problem statement
_Young people that move to Bucharest (for college, work or other reasons) are having a hard time choosing rents in the city because relevant information is hard to find. To help them have a smoother experience we will build an Estate Ad Portal that will allow the comparison of ads and also gather some common information about the neighborhoods to help in the decision making process._

## Project charter
#### **_Opportunity_**
- _When choosing a place to rent it's difficult to asses whether the offer you are given is a good deal or not._

#### **_Goal_**
- _Build a website that will generate high online traffic by facilitating the access to information for users of our website._

#### **_Objectives_**
- _Have at least 100.000 visits during Q2 and Q3 of the first year since release_
- _Have at least 10.000 unique visitors within the first year since release_
- _Have a page/visit rate of at least 5 within the first year since release_
 
#### **_Business case_**
- _The project is profitable and can be scaled to further increase business_

#### **_Project Scope_**
  - _In scope_
    - _Build a website_
  - _Deliverables_
    - _Estate ads can be published on the website_
    - _Articles and reviews can be published on the website_
  - _Out of Scope_
    - _Mediating transactions between tenants and owners_
    - _Messaging feature between users_
 
#### **_Constraints_**
- _Limited time_
- _Project's budget_
 
#### **_Assumptions_**
- _The website will be free to use and enough revenue can be generated through commercial ads._
- _The city of Bucharest has a dynamic population due to the universities and jobs available. Thus, if the project is not successful for just this area in particular, it would not have had any success if we were to build for a much larger one._
- _Estate owners using our website have a low number of estates to rent so limiting the allowed number of ads to 3 is sufficient and there won't be complaints in that regard in the initial phase of the project._
- _Registered users of our website only have deeper knowledge of a few particular areas of the city so limiting the allowed number of articles to 5 is sufficient and there won't be complaints in that regard in the initial phase of the project._

#### **_Risks_**
- _People will be reluctant to move from the website they are already using_
- _An online community that actively uses and improves the content is is not guaranteed to be formed_
- _People will use the website in undesired ways (posting unrelated ads and articles)_

#### **_Team_**
_**Name**_ | **_Role_**
---- | ----
Paduraru Cristian-Daniel | Project Manager
Popescu Paullo Robertto Karloss | Developer & Designer
Linte Robert Ovidiu | Developer & Designer

#### **_Stakeholders_**

_**StakeHolder**_ | _**Implication**_
---- | ---
Rares Cristea and other people from Tremend | The ones who will evaluate the development process
The development team | The ones who will implement and document the project
Students in Bucharest | The category of people we heavily expect to use our solution
Estate owners in Bucharest | The category of people we expect to use our free services
People moving in Bucharest for a job | People that should find the content of our website useful
People visiting Bucharest for medium to long periods of time | People that could find the content of our website useful

#### **_Budget_**
- _20 hours of total work across the team per week_

#### **_Milestones_**
 
 _**Milestone**_    |    _**Due**_    | **_Done_**
 -----------   | :------: | :---:
 The sign up and login functionalities are completed |  23.12.2022 | 
 Estate owners can add Estate ads |  01.01.2023 | 
 Users can post reviews and articles |  21.01.2023 |  
 Ads Server contracted and ads are displayed in the website |  30.02.2023 | 
 Website is ready for launch |  31.02.2023 | 

(Yes, the last two are intentional 😅 )

Visual representation for the time span of our epics and some of the milestones.

<img src="https://user-images.githubusercontent.com/80215860/204372365-107bee47-5aa7-438e-b787-bbc347595fe7.png" alt="timeline" height="450"/>

## Non-functional requirements list

- _Users will have to accept the GDPR terms & conditions at account creation_

#### **_Performance_**
- _The solution should be able to store the data of up to 25.000 users, 20.000 estate ads and 1.000 articles_
- _The solution should support up to 3.000 requests made in an interval of 2 minutes_
- _All requests should be completed within 6 seconds_
- _Emails should be sent within 10 minutes of being triggered by an user action_
- _Newly added ads and articles should be visible to all users take make a request 5 or more minutes after publication_
- _The list of ads should be loaded in less than 5 seconds and there will be at most 20 ads per page._ 

#### **_Security_**
- _Passwords must have at least 10 characters in length and contain at least one digit, one lowercase letter and one uppercase letter_
- _The solution will never store a user’s password. Passwords will be hashed before being sent to the server_

#### **_Integrity_**
- _The content of Reviews and Estate Ads will only be modified by their owner_
- _The database will not be modified by unauthorized people_

#### **_Availability_**
- _The portal should always be available to visitors between 6AM - 11:30PM_



## Functional decomposition
**_Functionality_** | **_MoSCoW Priority_** | **_Sprint Planning_**
--- | :---: | :---:
The website will have a home page | M | Sprint 1
A site visitor can see all the published estate ads | M |  Sprint 2
A site visitor can filter the list of ads based on price and neighborhood | S | Sprint 2 
A site visitor can sort the list of ads by publication date and price | S | Sprint 2
A site visitor can see all the published articles | M | Sprint 3
A site visitor can filter the list of articles by neighborhood | S | Sprint 3
A site visitor can sort the list of articles by publication date | S | Sprint 3
The solution will use well known techniques in order to be SQL injection proof. (This strongly relates to a non-functional integrity requirement, that the content of  the database will not be altered by unauthorized people. We consider this to be a functional requirement since it describes an implementation detail that is mandatory in order to attain the non-functional and broader requirement) | M | Sprint 1-100  (needs continuous care)
A site visitor can sign up for a free User or Estate Owner type of account by completing the mandatory fields of the sign up form (email address and password) | M | Sprint 1
An Admin can create other Admin accounts, by providing the minimal mandatory fields (email address and password) | S | Sprint 4
An authenticated user will be able to access his Profile page | S | Sprint 1
A user’s Profile page will show the data he has provided (name, email, gender, date of birth, occupation or others) | C | Sprint 1
A user’s Profile page will have the option to edit or hide the data | C | Sprint 1 
For every type of account created, a validation email will be sent at the given address | S | Sprint 1
A user will be authenticated when he tries to log in to the website | M | Sprint 1
An authenticated Estate Owner can create up to 3 Estate Ads. An Estate Ad will have mandatory fields (surface, address, price), optional images and an optional description (up to 300 characters) | M | Sprint 2 
The Estate Owner will be able to access the ad creation page from any other page in the website, as long as he hasn't used all his available slots | M | Sprint 2
The Estate Owner can save the Estate Ad as a draft that only he can see or make it public by publishing it. Drafts can later be published and they take up a slot from the total | S | Sprint 2
An authenticated Estate Owner will have the list of ads that he has created on his profile page. Visitors will only see the ads that are published and not the drafts the owner has created | M | Sprint 2
The Estate Owner can edit his own ads and drafts | S | Sprint 2
The Estate Owner can delete his own ads and drafts | M | Sprint 2
The Estate Owner can convert his ads to drafts in order to hide them | M | Sprint 2
An authenticated user can publish up to 5 articles about the city of Bucharest as a whole or just a particular region of it | M | Sprint 3
An authenticated user can edit or delete his own articles | M | Sprint 3
An authenticated user can mark the ads and articles he is interested in | S | Sprint 3
An authenticated user will have 2 links in his profile page so that he can view his lists of marked ads and articles respectively | S | Sprint 3
An authenticated user will have the list of articles he has created in his profile page | M | Sprint 3
An authenticated user can view the details of 2 marked ads side by side by selecting them from his list of marked ads | C | Sprint 4
Every Estate Ad will have the email of the Estate Owner that has created the ad, as a link to his Profile page | M | Sprint 2
Every published article will contain the email address of the user that wrote the article, as a link to his Profile page | M | Sprint 3
A logged in Admin will be able to disable or enable ads, articles and accounts from their respective viewing page. When an ad, article or account is disabled, the Admin will provide a mandatory brief description to motivate his action | M | Sprint 4
An authenticated user will be able to report an ad or article | C | Sprint 3
An authenticated user will receive a notification when the author of a marked article has published a new one | W | Sprint 4
An authenticated user will receive a notification when the owner of a marked ad has edited the ad | W | Sprint 4

## Activity Diagram

Activity diagram that exposes the flow of Estate Ad creation.

![image](https://user-images.githubusercontent.com/73616883/204161240-3efb5230-081f-47cf-ae12-e287f5b3f13a.png)

## State Diagram

State diagram that highlights how the actions of an Estate Owner change the state of an Estate Ad.

![image](https://user-images.githubusercontent.com/73616883/204161254-ddcaae00-cc86-4fbf-8dd6-ef759c6d6f90.png)

## Definition of Done and Definition of ready
### Story Level:

#### _Definition of Ready_
  - _User story is defined_
  - _User Story is feasible_
  - _User Story is testable_
  - _User Story Acceptance Criteria is defined_
  - _User Story dependencies identified_
  - _User Story sized by Development Team_
  - _Person who will accept the User Story is identified_
  - _Scrum Team accepts User Experience artifacts, where appropriate_
  - _Security criteria identified, where appropriate_
  - _Performance criteria identified, where appropriate_
  - _Team has a good idea what it will mean to Demo the User Story_

#### _Definition of Done_
  - _Code produced (all ‘to do’ items in code completed)_
  - _Passed User Acceptance Testing_
  - _Writing and passing of unit tests, where appropriate_
  - _Project builds without errors_
  - _Cross browser testing done, where appropriate_
  - _Peer reviewed (or produced with pair programming) and meeting development standards_
  - _Relevant documentation / diagrams produced and / or updated_
  - _All tasks that mention the User Story are closed_
  - _Continuous Integration build passing_

## Prioritized product backlog
You can find our product backlog [here](https://github.com/orgs/inginerie-software-22-23/projects/33) and the tasks of our first sprint in [this project](https://github.com/orgs/inginerie-software-22-23/projects/15).

# Final Project
## Sprint deliverables

### First Dev Sprint
Sprint Backlog after planning

<img src="https://user-images.githubusercontent.com/80215860/215193173-9a28d385-1a77-4403-afb1-b133e3a88cbc.png" width=250>
<img src="https://user-images.githubusercontent.com/80215860/215193183-af5c91de-19d5-4c3f-98ff-fd96452fae48.png" width=250>
<img src="https://user-images.githubusercontent.com/80215860/215193075-f78a90ad-d735-43a5-8cdd-adcd717d1dfb.png" width=250>
<img src="https://user-images.githubusercontent.com/80215860/215193197-af7c2172-55fe-430c-a6f5-2e6880d1adc9.png" width=250>


Sprint Report

Sprint Number | 1
:-- | :--
Sprint Period | 28 Nov 2022 - 11 Dec 2022
Sprint Goal | Complete the initial setup of the application. Create the database and the backend server. On the frontend create the pages needed for account creation and authentication. Also create a Profile page for authenticated users.
Stories and Time Committed | 13 Tasks  \| 20 hours
Stories and Time Delivered | <ul> <li>9 Tasks   \| 22 hours </li> <li> 1 task was discarded from sprint and will be solved later on when the team finds an appropriate solution</li> <li> the other 3 tasks were moved to the next sprint  </li>   </ul>
Comments on Sprint Report | During the first dev sprint our focus was to allow users to register and get authenticated on our website. We decided to remove a task from the sprint and come back to it when we had all the tools needed to implement it. Some tasks had to be delayed to the next sprint since we encountered some difficulties with the initial setup that took a consistent part of the planned workload.


Retrospective Outcome

<img src="https://user-images.githubusercontent.com/80215860/215265333-f02af293-4651-4b7d-a520-4741584b97c9.png" width=500>


[Application Demo](https://www.youtube.com/watch?v=vqpasHCtleg)


### Third Dev Sprint (Holiday Sprint)

Sprint Backlog after planning

<img src="https://user-images.githubusercontent.com/80215860/215263366-b73c37d6-ebec-486a-b037-617c5a49ba6d.png" width=380>



Sprint Report

Sprint Number | 3
:-- | :--
Sprint Period | 26 Dec 2022 - 8 Jan 2023
Sprint Goal | Give users complete control over their account and profile data.
Stories and Time Committed | 8 Tasks  \| 14 hours
Stories and Time Delivered | <ul> <li>10 Tasks   \| 15 hours </li> <li> 2 more tasks were added to lay the grounds for tasks in the next sprint</li></ul>
Comments on Sprint Report | In this sprint we planned to allow users to personalize their profile page. All the initial tasks were completed and we also added two more tasks to have a starting point for the next sprint on both the frontend and backend side of our product.


Retrospective Outcome

<img src="https://user-images.githubusercontent.com/80215860/215265718-7391c31b-cb77-4f30-998b-364ac678a50f.png" width = 500>


[Application Demo](https://www.youtube.com/watch?v=it7l9TYzEsE&t=17s)


### Fourth Dev Sprint

Sprint Backlog after planning

<img src="https://user-images.githubusercontent.com/80215860/215197571-d4ebd83d-994b-4206-bc66-4e1c94d0020e.png">


Sprint Report

Sprint Number | 4
:-- | :--
Sprint Period | 9 Jan 2023 - 22 Jan 2023
Sprint Goal | Allow authenticated users to create and publish both estate ads and articles. Also allow any user to view them on our website
Stories and Time Committed | 13 Tasks  \| 20 hours
Stories and Time Delivered | <ul> <li> 7 Tasks   \| 20 hours </li>  <li> 1 task was held on review due to a visual bug that will be fixed next sprint</li> <li> The other 5 tasks were moved to the next sprint. </li>  </ul>
Comments on Sprint Report | During the 4th sprint we intended to give authenticated users the means to publish their estate ads and articles for every other user to see (registered or not). We could not complete all the committed tasks due to a wrong estimation of the time needed for that. Incomplete tasks were moved to the next sprint.


Retrospective Outcome

<img src="https://user-images.githubusercontent.com/80215860/215266009-9eca0669-d359-4d00-b0a3-b88519ed456d.png" width=500>


[Application Demo](https://youtu.be/RSNISqEE0Zw)


### Hardening Sprint

Sprint Backlog after planning

<img src="https://user-images.githubusercontent.com/80215860/215197941-79629f57-8028-471d-b188-20279d8466f9.png">


Sprint Report

Sprint Number | 5
:-- | :--
Sprint Period | 23 Jan 2023 - 1 Feb 2023
Sprint Goal | Complete all tasks related to Estate Ads and make improvements to the visual aspect of the website.
Stories and Time Committed | 12 Tasks  \| 10 hours
Stories and Time Delivered | <ul> <li> 7 Tasks   \|  10 hours </li> <li> The other 5 tasks were the same ones we got from the previous sprint</li>  </ul>
Comments on Sprint Report | During this sprint we have decided that an MVP of our application can do without the articles and thus have focused on some quality of life changes that would allow for an early release, followed by a large update once the tasks related to articles were completed.


Retrospective Outcome

<img src="https://user-images.githubusercontent.com/80215860/216016901-946193b7-5dc7-4f27-844b-9e5902ebe11c.png" width=500>


[Application Demo](https://youtu.be/eY7zyXvrmn4)


## Architecture Report

The main quality attributes of the application are performance and security. 

Performance is required by both users and the product owner. Users need an application that responds to their actions within a reasonable amount of time. The product owner also values this attribute for the same reason, since having a large pool of satisfied clients that continue to use the application is imperative for the success of the product from a business perspective. Security is necessary for people to trust and keep using the application.

In order to have security we have used some common tactics such as input validation and actors authorization.
For performance we have avoided using indirection so that we do not create computational overhead.

In our application we used a client-server architecture. Separating the presentation, application and data access layers allows the development team to work independently on different layers. Having a single data center means there won't be synchronization problems between storage units. The drawback of this architecture is that there is no redundant communication connections. If the application layer were to fail then the data won't be able to reach the presentation layer. It is also vulnerable to DOS type of attacks since congestion at the server side will render all services unavailable.


### Architecture Related diagrams

> Sequence Diagram illustrating the communication between a user and the server
> 
> <img src="https://user-images.githubusercontent.com/80215860/215277962-8c262d75-b432-416f-a02c-6ce804467b16.png" width=400>


> Diagram of the Clean Architecture used on the Backend Server
>
> <img src="https://user-images.githubusercontent.com/80215860/215327202-d5c8ca34-fa0a-43dc-a8f3-c272a9acfb1e.png" width=300>
> 
> - The Presentation layer contains the API of the server while the Infrastructure layer represents the database which offers data persistence
> - The Application layer contains the logic of the servers and facilitates the communication between the Presentation and the Infrastructure layers
> - The Application layer is built upon the Domain layer where all the tools needed by it are defined 

### Quality attributes scenarios

#### Security

<img src="https://user-images.githubusercontent.com/80215860/215279334-504f9ab8-d7f2-4e83-96ed-35d0b634c9c3.png" width=400><br>

#### Integrity

<img src="https://user-images.githubusercontent.com/80215860/215281851-15f20a12-ab6c-4e81-9bb9-a1a1793a5eec.png" width=400>

#### Performance

<img src="https://user-images.githubusercontent.com/80215860/215279496-b4755353-dd90-4157-921a-113ac35729aa.png" width=400> <br>

<img src="https://user-images.githubusercontent.com/80215860/215279696-0e41ba1f-cc3a-4aad-a608-057fd9403d13.png" width=400><br>

<img src="https://user-images.githubusercontent.com/80215860/215281736-e7158b8d-cc16-4c5f-a097-47e292d0d9ec.png" width=400>


## Software Testing Report

#### Unit Tests

We could not perform realistic tests for [performance requirements](https://github.com/inginerie-software-22-23/proiect-inginerie-software-estate-team/wiki/Intermediate-deliverable---Estate-Team#performance) since any test that we would have run would not have taken into account the latency of data transfer. What we managed to test were some security and data integrity related aspects. We created unit tests to check that at sign up credentials are properly validated and that users can't alter content that they are not authorized to.

<img src="https://user-images.githubusercontent.com/80215860/215326920-7e4c464e-23f9-4b38-8df2-115e6093d9fa.png" width=550>

#### Selenium Tests

[Demo](https://www.youtube.com/watch?v=1BhuefQWqYk)

<img src="https://user-images.githubusercontent.com/80642370/216766740-c0b24d9d-8cc7-475c-8b29-bde9abd4453e.png" width=550>

#### Manual tests performed

To validate our [functional requirements](https://github.com/inginerie-software-22-23/proiect-inginerie-software-estate-team/wiki/Intermediate-deliverable---Estate-Team#functional-decomposition) we have ran manual test procedures following the scenarios described in those requirements and also the scenarios described in the acceptance criteria of some user stories. Some of them can be viewed in the application demo made at the end of a sprint. Below we have provided some requirements and screenshots of our website when we were validating their implementation.


> - A user’s Profile page will show the data he has provided
>
> <img src="https://user-images.githubusercontent.com/80215860/215327039-76ec08a3-f378-40c0-b1af-aaa324a4ebbc.png" width = 500>

<br>

> - A site visitor can see all the published estate ads
>
> <img src="https://user-images.githubusercontent.com/80215860/215327060-3cdecb55-d987-4fab-8cc8-9a3fb066db40.png" width = 500>

<br>

> - An authenticated Estate Owner will have the list of ads that he has created on his profile page
> 
> <img src="https://user-images.githubusercontent.com/80215860/215327085-e90cf014-d449-4dcb-a1b4-62eb31c515b8.png" width=500>



#### Security concerns that were addressed

Since the application accepts user inputs we had to assume that there will be some people with malicious intentions and guard against common threats that appear when working with user provided information. The most common attacks on the input forms are the SQL injections and cross-site scripting. In order to test the implementation against these types of attacks we have taken the role of malicious users and tried to change the content of the database and add scripts in our inputs that would run on other people's browser and we failed (successfully? 😅).


#### Relevant metrics for the application

For our application we have found a number of relevant metrics:

- Time needed to process a request on the backend server. While we can't asses the network latency that our users will face when using the website, making sure the backend servers delivers the results as fast as possible is a good start.

<img src="https://user-images.githubusercontent.com/80215860/215329101-2f8a828d-83f0-49fd-a7f6-85bd57087719.png" width=500>

- Number of concurrent request the server can handle simultaneously. During the peak season of hunting for accommodations it is expected that the server will receive a large number of requests in a short amount of time and in order to ensure the availability of our solution we have make some tests. We hypothesized that if the server can handle 100 simultaneous request then it should hold during the initial launch without problems. If it proves not to be enough later on when the user base rises measures can be taken to increase the capacity at that time.

<img src="https://user-images.githubusercontent.com/80215860/215329258-b4a65fc9-7da9-4ce0-9f46-c7a132dda35b.png" width=500>


## CI Pipeline

<img src="https://user-images.githubusercontent.com/80215860/215814391-c868ba39-6a63-44e7-a09b-6cd53382cdfd.png" width=500>






