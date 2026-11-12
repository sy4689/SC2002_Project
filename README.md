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

**Access note**
- Run application : Run Main.java
- Access test run : Open testNotes folder

**Checklist for Stage 1 (Basic restuarant operation)**
- Complete playable game containing:
  > - Regular customers and three menu items
  > - Waiter, Chef, Cashier, Table, Order and the waiting queue
  > - Complete turn sequence: Satisfaction, abandonment, payment, victory and defeat
- Player able to:
  > - Sets seating, ordering, and serving priority through Waiter
  > - Sets kitchen priority through Chef
- Deterministic food-choice rule for customer:
  > - Food choice index = Customer ID % Menu size
- Enumeration with self-explanatory naming:
  > - GameState
  > - WaiterActionType
  > - CustomerState
  > - OrderStatus
  > - MenuItemType
