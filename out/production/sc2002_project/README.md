# SC2002_Project Stage 1
**FOLDER STRUCTURE**
```bash
SC2002_Project
│   Main.java
│   README.md
│
├───.idea
│       .gitignore
│       misc.xml
│       modules.xml
│       sc2002_project.iml
│       vcs.xml
│
├───boundary
│       GameCLI.java
│       GameUI.java
│
├───control
│   │   GameController.java
│   │   GameDisplay.java
│   │   GameState.java
│   │   InvalidChoiceException.java
│   │   ViewBuilder.java
│   │   WaiterAction.java
│   │   WaiterActionType.java
│   │
│   └───view
│           CustomerView.java
│           OrderView.java
│           RestaurantView.java
│           SummaryView.java
│           TableView.java
│
├───entity
│   │   Restaurant.java
│   │   Table.java
│   │   WaitingQueue.java
│   │
│   ├───customer
│   │       Customer.java
│   │       CustomerState.java
│   │       RegularCustomer.java
│   │
│   ├───menu
│   │       Menu.java
│   │       MenuItem.java
│   │       MenuItemType.java
│   │
│   ├───order
│   │       Order.java
│   │       OrderStatus.java
│   │
│   └───staff
│           Cashier.java
│           Chef.java
│           Staff.java
│           Waiter.java
│
└───out
    └───production
        └───sc2002_project
            │   Main.class
            │   README.md
            │
            ├───.idea
            │       .gitignore
            │       misc.xml
            │       modules.xml
            │       sc2002_project.iml
            │       vcs.xml
            │
            ├───boundary
            │       GameCLI.class
            │       GameUI.class
            │
            ├───control
            │   │   GameController$1.class
            │   │   GameController.class
            │   │   GameDisplay.class
            │   │   GameState.class
            │   │   InvalidChoiceException.class
            │   │   ViewBuilder.class
            │   │   WaiterAction.class
            │   │   WaiterActionType.class
            │   │
            │   └───view
            │           CustomerView.class
            │           OrderView.class
            │           RestaurantView.class
            │           SummaryView.class
            │           TableView.class
            │
            └───entity
                │   Restaurant.class
                │   Table.class
                │   WaitingQueue.class
                │
                ├───customer
                │       Customer.class
                │       CustomerState.class
                │       RegularCustomer.class
                │
                ├───menu
                │       Menu.class
                │       MenuItem.class
                │       MenuItemType.class
                │
                ├───order
                │       Order.class
                │       OrderStatus.class
                │
                └───staff
                        Cashier.class
                        Chef.class
                        Staff.class
                        Waiter.class
```
**How to run**
1. Run Main.java

**Checklist for Stage 1 (Basic restuarant operation)**
- Complete playable game containing:
  > - Regular customers and three menu items
  > - Waiter, Chef, Cashier, Table, Order and the waiting queue
  > - Complete turn sequence: Satisfaction, abandonment, payment, victory and defeat
- Player able to:
  > - Sets seating, ordering, and serving priority through Waiter
  > - Sets kitchen priority through Chef 
