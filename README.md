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
New classes and interface added for Stage 3
- Host
  - Seats at most one queued customer per turn, in addition to the Waiter's task, or waits.
- Seater — interface
  - Defines seatCustomer(Customer cust, Table t) for both Host and Waiter.
  - Allows the controller to reuse the same seating helper for either staff member.
- ComboMeal
  - Implements MenuItem and combines two existing menu items.
  - Price is 90% of the combined component prices, rounded to the nearest cent.
- Event
  - Manages activation availability and remaining duration.
  - Allows one activation and starts a two-turn duration.
  - Provides activate(), canActivate(), isActive(), getTurnsRemaining() and decrementDuration().
- HappyHourEvent
  - Extends Event and provides the Happy Hour discount policy.
  - During each active turn, restores 15 satisfaction to queued, seated or ordered-but-unserved customers, capped at 100.
- HappyHourDiscountPolicy
  - Implements DiscountPolicy.
  - Applies a 20% reduction to the supplied bill, rounded to the nearest cent.

Customer
- Adds recoverSatisfaction(int pts) to restore satisfaction up to 100.
- Retains the existing customer-specific waiting penalties and menu-selection methods.
Menu and customer food choice
- Registers Salad + Pasta as the fourth menu item: $24.30 and three preparation units.
- Menu order is Salad, Burger, Pasta, then Salad + Pasta.

GameDisplay and GameCLI
- Add promptHappyHourActivation(), displayHappyHourStatus(int turnsRemaining) and promptHostTask(RestaurantView view).
