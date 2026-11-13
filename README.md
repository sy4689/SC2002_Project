# SC2002_Project Stage 2
**FOLDER STRUCTURE (Obeying BCE framework)**
```bash
SC2002_Project
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
│   │   └── VIPDiscountPolicy.java
│   ├── menu
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
│           │   │   └── VIPDiscountPolicy.class
│           │   ├── menu
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

24 directories, 81 files
```
**Access note**
- Run application : Run Main.java
- Access test run : Open testNotes folder

**Checlist for Stage 2 (More customer type and discount policy)**
New Classes Added for Stage 2
- VIPCustomer
- CriticCustomer (We define the behaviour to be choosing the most expensive food item on the menu)
- DiscountPolicy (Interface)
- VIPDiscountPolicy

GameController 
- Creates VIPCustomer #2 at turn 4
- Creates CriticCustomer #4 at turn 10
- The rest are RegularCustomer
- Includes Customer type in arrival message

Waiter
- Uses cust.chooseMenuItem(menu) to obtain each customer food choice
- obtain the customer discount through getDiscountPolicy()
- Calls order.lockPrice() with an empty policy list or the VIP policy
- Applies pricing before attaching the order and adding it to the preparation queue

Order
-A dded lockPrice(List<DiscountPolicy> policies)
- Applies the policy to the menu price
- Stores the calculated amount in finalPriceinCents
- Receives the policy list through method injection without storing the list

Cashier
- Continues reading getFinalPriceInCents() to collect the correct payment
- Requires no additional discount calculation

CustomerView, ViewBuilder, GameCLI
- Added the type field and displayed in queue, tables and prompts


**
