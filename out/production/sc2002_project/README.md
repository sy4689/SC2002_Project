# SC2002_Project Stage 3
<h2>FOLDER STRUCTURE (Obeying BCE framework)</h2>

```diff
@@ Folder structure legend: @@
+ (Green label) : Additional code for Stage 2
! (Orange label): Modified code for Stage 2
- (Red label)   : Removed code for Stage 2

** Additional details about changes can be referred below 

SC2002_Project
   .
├── boundary
│   ├── GameCLI.java
│   └── GameUI.java
├── control
│   ├── GameController.java
│   ├── GameDisplay.java
│   ├── GameState.java
│   ├── GameUI.java
│   ├── InvalidChoiceException.java
│   ├── view
│   │   ├── CustomerView.java
│   │   ├── OrderView.java
│   │   ├── RestaurantView.java
│   │   ├── SummaryView.java
│   │   └── TableView.java
│   ├── ViewBuilder.java
│   ├── WaiterAction.java
│   └── WaiterActionType.java
├── entity
│   ├── customer
│   │   ├── CriticCustomer.java
│   │   ├── Customer.java
│   │   ├── CustomerState.java
│   │   ├── RegularCustomer.java
│   │   └── VIPCustomer.java
│   ├── DiscountPolicy
│   │   ├── DiscountPolicy.java
│   │   ├── HappyHourDiscountPolicy.java
│   │   └── VIPDiscountPolicy.java
│   ├── Event
│   │   ├── Event.java
│   │   └── HappyHourEvent.java
│   ├── menu
│   │   ├── ComboMeal.java
│   │   ├── Menu.java
│   │   ├── MenuItem.java
│   │   └── MenuItemType.java
│   ├── order
│   │   ├── Order.java
│   │   └── OrderStatus.java
│   ├── Restaurant.java
│   ├── staff
│   │   ├── Cashier.java
│   │   ├── Chef.java
│   │   ├── Host.java
│   │   ├── Seater.java
│   │   ├── Staff.java
│   │   └── Waiter.java
│   ├── Table.java
│   └── WaitingQueue.java
├── Main.java
├── out
│   └── production
│       └── sc2002_project
│           ├── boundary
│           │   ├── GameCLI.class
│           │   └── GameUI.class
│           ├── control
│           │   ├── GameController.class
│           │   ├── GameController$1.class
│           │   ├── GameDisplay.class
│           │   ├── GameState.class
│           │   ├── GameUI.class
│           │   ├── InvalidChoiceException.class
│           │   ├── view
│           │   │   ├── CustomerView.class
│           │   │   ├── OrderView.class
│           │   │   ├── RestaurantView.class
│           │   │   ├── SummaryView.class
│           │   │   └── TableView.class
│           │   ├── ViewBuilder.class
│           │   ├── WaiterAction.class
│           │   └── WaiterActionType.class
│           ├── entity
│           │   ├── customer
│           │   │   ├── CriticCustomer.class
│           │   │   ├── Customer.class
│           │   │   ├── CustomerState.class
│           │   │   ├── RegularCustomer.class
│           │   │   └── VIPCustomer.class
│           │   ├── DiscountPolicy
│           │   │   ├── DiscountPolicy.class
│           │   │   ├── HappyHourDiscountPolicy.class
│           │   │   └── VIPDiscountPolicy.class
│           │   ├── Event
│           │   │   ├── Event.class
│           │   │   └── HappyHourEvent.class
│           │   ├── menu
│           │   │   ├── ComboMeal.class
│           │   │   ├── Menu.class
│           │   │   ├── MenuItem.class
│           │   │   └── MenuItemType.class
│           │   ├── order
│           │   │   ├── Order.class
│           │   │   └── OrderStatus.class
│           │   ├── Restaurant.class
│           │   ├── staff
│           │   │   ├── Cashier.class
│           │   │   ├── Chef.class
│           │   │   ├── Host.class
│           │   │   ├── Seater.class
│           │   │   ├── Staff.class
│           │   │   └── Waiter.class
│           │   ├── Table.class
│           │   └── WaitingQueue.class
│           ├── Main.class
│           ├── README.md
│           └── testNotes
│               ├── defeat_input.txt
│               ├── defeat_output.txt
│               ├── victory_input.txt
│               └── victory_output.txt
├── README.md
└── testNotes
    ├── defeat_input.txt
    ├── defeat_output.txt
    ├── victory_input.txt
    └── victory_output.txt

26 directories, 93 files
```

<h2>FILE SUMMARY:</h2>

*1. Enumeration with self-explanatory naming:*
> <code>GameState</code>,<code>WaiterActionType</code> , <code>CustomerState</code>, <code>OrderStatus</code>, <code>MenuItemType</code>

*2. <code>view</code> folder + <code>ViewBuilder.java</code>:*
> - Bridge communication from control class to boundary class
> - Control class get access to entity class's getter or operations, then pass the raw result back to boundary class
> - <code>CustomerView</code>, <code>OrderView</code>, <code>RestaurantView</code>, <code>SummaryView</code>, <code>TableView</code>

*3. <code>DiscountPolicy</code> folder:*
> - Classify all discount-related classes or interfaces
<br></br>

<h2>ACCESS NOTE:</h2>

> - Run application : Run <code>Main.java</code>
> - Access test run : Open <code>testNotes</code> folder
<br></br>

<h2>ADDITIONAL DETAILS FOR CHANGES IN STAGE 2:</h2>

*1.  Additional classes / interfaces for Stage 2*
> - <code>VIPCustomer</code> (Dependency on VIPDiscountPolicy)
> - <code>CriticCustomer</code> (Unique Behaviour: Choose priciest food item from the menu)
> - <code>DiscountPolicy</code> 
> - <code>VIPDiscountPolicy</code>

*2.  <code>GameController</code>*
> Updated game-flow:
> - <code>VIPCustomer</code> #2 at turn 4, <code>CriticCustomer</code> #4 at turn 10, rest are <code>RegularCustomer</code>
> - Send request to customer-related entities to obtain customer type for arrival message 

*3.  <code>Waiter</code>*
> - Send request to customer-related entities to obtain customer's privileged discount (if not, <code>null</code> is received)
> - Send request to order entity to lock price for customer's order 

*4.  <code>Order</code>*
> - Adjust method to pricing system to accommodate the newly added discount system
> - Apply discount on original menu prices to get final price 

*5.  <code>CustomerView</code>, <code>ViewBuilder</code>, <code>GameCLI</code>*
> - UX integration : Update new attribute to display customer type in game

