# SC2002_Project Stage 2
**FOLDER STRUCTURE (Obeying BCE framework)**

```diff
@@ Folder structure legend: @@
+ (Green label) : Additional code for Stage 2
! (Orange label): Modified code for Stage 2
- (Red label)   : Removed code for Stage 2

** Additional details about changes can be referred below 

SC2002_Project
    ├── boundary
!   │   ├── GameCLI.java (!! UPDATE OUTPUT FORMATTING !!)
    │   └── GameUI.java
    ├── control
!   │   ├── GameController.java (!! UPDATE GAME-FLOW !!)
    │   ├── GameDisplay.java
    │   ├── GameState.java
    │   ├── GameUI.java
    │   ├── InvalidChoiceException.java
    │   ├── view
!   │   │   ├── CustomerView.java (!! UPDATE ADDITIONAL ATTRIBUTE FOR NEW OUTPUT FORMATTING !!)
    │   │   ├── OrderView.java
    │   │   ├── RestaurantView.java
    │   │   ├── SummaryView.java
    │   │   └── TableView.java
!   │   ├── ViewBuilder.java (!! UPDATE CONSTRUCTOR ARGUMENT !!)
    │   ├── WaiterAction.java
    │   └── WaiterActionType.java
    ├── entity
    │   ├── customer
+   │   │   ├── CriticCustomer.java 
!   │   │   ├── Customer.java (!! ADDITIONAL METHOD for DISCOUNT !!)
    │   │   ├── CustomerState.java
    │   │   ├── RegularCustomer.java
+   │   │   └── VIPCustomer.java
    |   |
+   │   ├── DiscountPolicy (!! NEW FOLDER !!)
+   │   │   ├── DiscountPolicy.java (!! NEW INTERFACE !!)
+   │   |   └── VIPDiscountPolicy.java
    |   |
    │   ├── menu
    │   │   ├── Menu.java
    │   │   ├── MenuItem.java
    │   │   └── MenuItemType.java
    │   ├── order
!   │   │   ├── Order.java (!! UPDATE PRICE COUNTING METHOD'S PARAMETER !!)
    │   │   └── OrderStatus.java
    │   ├── Restaurant.java
    │   ├── staff
    │   │   ├── Cashier.java
    │   │   ├── Chef.java
    │   │   ├── Staff.java
!   │   │   └── Waiter.java (!! UPDATE ORDER-TAKING METHOD TO CONSIDER DISCOUNT !!)
    │   ├── Table.java
    │   └── WaitingQueue.java
    ├── Main.java
    ├── out
    │   └── production
    │       └── sc2002_project
    │           ├── boundary
!   │           │   ├── GameCLI.class
    │           │   └── GameUI.class
    │           ├── control
!   │           │   ├── GameController.class
    │           │   ├── GameController$1.class
    │           │   ├── GameDisplay.class
    │           │   ├── GameState.class
    │           │   ├── GameUI.class
    │           │   ├── InvalidChoiceException.class
    │           │   ├── view
!   │           │   │   ├── CustomerView.class
    │           │   │   ├── OrderView.class
    │           │   │   ├── RestaurantView.class
    │           │   │   ├── SummaryView.class
    │           │   │   └── TableView.class
!   │           │   ├── ViewBuilder.class
    │           │   ├── WaiterAction.class
    │           │   └── WaiterActionType.class
    │           ├── entity
    │           │   ├── customer
+   │           │   │   ├── CriticCustomer.class
!   │           │   │   ├── Customer.class
    │           │   │   ├── CustomerState.class
    │           │   │   ├── RegularCustomer.class
+   │           │   │   └── VIPCustomer.class 
    |           |   |
+   │           |   ├── DiscountPolicy (!! NEW FOLDER !!)
+   │           |   │   ├── DiscountPolicy.java
+   │           |   |   └── VIPDiscountPolicy.java
    |           |   |
    │           │   ├── menu
    │           │   │   ├── Menu.class
    │           │   │   ├── MenuItem.class
    │           │   │   └── MenuItemType.class
    │           │   ├── order
!   │           │   │   ├── Order.class
    │           │   │   └── OrderStatus.class
    │           │   ├── Restaurant.class
    │           │   ├── staff
    │           │   │   ├── Cashier.class
    │           │   │   ├── Chef.class
    │           │   │   ├── Staff.class
!   │           │   │   └── Waiter.class
    │           │   ├── Table.class
    │           │   └── WaitingQueue.class
    │           ├── Main.class
    │           ├── README.md
    │           └── testNotes
    │               ├── defeat_input.txt
    │               ├── defeat_output.txt
    │               ├── victory_input.txt
    │               └── victory_output.txt
!   ├── README.md 
    |
!   └── testNotes (!! EXCULSIVE TEST CASE FOR STAGE 2 !!)
!       ├── defeat_input.txt
!       ├── defeat_output.txt
!       ├── victory_input.txt
!       └── victory_output.txt
    
24 directories, 81 files
```

**Access note**
- Run application : Run Main.java
- Access test run : Open testNotes folder
<br></br>

**Additional details for changes for Stage 2:**<br></br>
*1.  Additional classes / interfaces for Stage 2*
> - VIPCustomer (Dependency on VIPDiscountPolicy)
> - CriticCustomer (Unique Behaviour: Choose priciest food item from the menu)
> - DiscountPolicy 
> - VIPDiscountPolicy

*2.  GameController*
> Updated game-flow:
> - VIPCustomer #2 at turn 4, CriticCustomer #4 at turn 10, rest are RegularCustomer
> - Send request to customer-related entities to obtain customer type for arrival message 

*3.  Waiter*
> - Send request to customer-related entities to obtain customer's privileged discount (if not, null is received)
> - Send request to order entity to lock price for customer order 

*4.  Order*
> - Adjust method to pricing system to accommodate the newly added discount system
> - Apply discount on original menu prices to get final price 

*5.  CustomerView, ViewBuilder, GameCLI*
> - UX integration : Update new attribute to display customer type in game


