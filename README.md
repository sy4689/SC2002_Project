# SC2002_Project Stage 3
<h2>FOLDER STRUCTURE (Obeying BCE framework)</h2>

```diff
@@ Folder structure legend: @@
+ (Green label) : Additional code for Stage 3
! (Orange label): Modified code for Stage 3
- (Red label)   : Removed code for Stage 3

** Additional details about changes can be referred below 

SC2002_Project
      ├── boundary
!     │   ├── GameCLI.java
      │   └── GameUI.java
      ├── control
!     │   ├── GameController.java
!     │   ├── GameDisplay.java
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
!     │   ├── customer
      │   │   ├── CriticCustomer.java
      │   │   ├── Customer.java
      │   │   ├── CustomerState.java
      │   │   ├── RegularCustomer.java
      │   │   └── VIPCustomer.java
      │   ├── DiscountPolicy
      │   │   ├── DiscountPolicy.java
+     │   │   ├── HappyHourDiscountPolicy.java
      │   │   └── VIPDiscountPolicy.java
      │   ├── Event
+     │   │   ├── Event.java
+     │   │   └── HappyHourEvent.java
      │   ├── menu
+     │   │   ├── ComboMeal.java
!     │   │   ├── Menu.java
      │   │   ├── MenuItem.java
      │   │   └── MenuItemType.java
      │   ├── order
      │   │   ├── Order.java
      │   │   └── OrderStatus.java
      │   ├── Restaurant.java
      │   ├── staff
      │   │   ├── Cashier.java
      │   │   ├── Chef.java
+     │   │   ├── Host.java
+     │   │   ├── Seater.java
      │   │   ├── Staff.java
!     │   │   └── Waiter.java
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
!     ├── README.md
      └── testNotes
!         ├── defeat_input.txt
!         ├── defeat_output.txt
!         ├── victory_input.txt
!         └── victory_output.txt

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

*4. <code>Event</code> folder:*
> - Classify all event-related classes
> - <code>Event</code>, <code>HappyHourEvent</code>
<br></br>

<h2>ACCESS NOTE:</h2>

> - Run application : Run <code>Main.java</code>
> - Access test run : Open <code>testNotes</code> folder
<br></br>

<h2>ADDITIONAL DETAILS FOR CHANGES IN STAGE 3:</h2>

*1.  Additional classes / interfaces for Stage 3*
> - <code>Host</code> (Seats a queued customer or waits, in addition to the Waiter's task each turn)
> - <code>Seater</code> (Interface: defines <code>seatCustomer(Customer cust, Table t)</code> for both <code>Host</code> and <code>Waiter</code>, so the controller can reuse one seating helper)
> - <code>ComboMeal</code> (Implements <code>MenuItem</code> by combining two existing menu items; discounted price, preparation units are summed)
> - <code>Event</code> (Manages activation availability and remaining duration)
> - <code>HappyHourEvent</code> (Extends <code>Event</code>; provides the Happy Hour discount policy and a satisfaction boost for waiting customers)
> - <code>HappyHourDiscountPolicy</code> (Implements <code>DiscountPolicy</code>; reduces the supplied bill)

*2.  <code>Customer</code>*
> - Add <code>recoverSatisfaction(int pts)</code> to restore satisfaction up to the maximum
> - Existing customer-specific waiting penalties and menu-selection methods are retained

*3.  <code>Menu</code> and customer food choice*
> - Register Salad + Pasta (<code>ComboMeal</code>) as an additional menu item after the single dishes
> - Customer food choice follows the updated menu order

*4.  <code>Waiter</code>*
> - Accept additional discount policies when taking an order, so event discounts apply together with the customer's privileged discount

*5.  <code>GameController</code>*
> Updated game-flow:
> - Offer Happy Hour activation at the start of a turn while it is still available, then apply its boost and track its duration
> - Add a Host task step before the Waiter's task
> - Pass active event discount to the Waiter when taking an order

*6.  <code>GameDisplay</code>, <code>GameCLI</code>*
> - UX integration : Add <code>promptHappyHourActivation()</code>, <code>displayHappyHourStatus(int turnsRemaining)</code> and <code>promptHostTask(RestaurantView view)</code>
