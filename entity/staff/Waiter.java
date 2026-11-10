//import entity.staff.Staff;
//import entity.menu.Menu;
//
//public class Waiter extends Staff {
//    public String Waiter (String name) {
//        super.name = name;
//    }
//
//    public boolean seatCustomer (entity.customer.Customer cust, entity.Table t){
//
//        if (!isAvailable() || cust == null || t == null || t.isOccupied() || cust.getState() != entity.customer.CustomerState.WAITING_IN_QUEUE) {
//            throw//import Staff.Staff;
//
//public class Waiter extends Staff {
//    public String Waiter (String name) {
//        super.name = name;
//
//
//    public boolean seatCustomer (entity.customer.Customer cust, entity.Table t){
//
//        if (!isAvailable() || cust == null || t == null || t.isOccupied() || cust.getState() != entity.customer.CustomerState.WAITING_IN_QUEUE) {
//            throw new control.InvalidChoiceException("Cannot seat this customer at this table");
//        }
//
//        t.assignCustomer(cust);
//        cust.setState(entity.customer.CustomerState.SEATED);
//        busyThisTurn = true;
//        return true;
//    }
//
//    public entity.order.Order takeOrder (entity.Table t, entity.menu.Menu menu) {
//        if (!isAvailable() || t == null || !t.isOccupied() || menu == null) {
//            throw new control.InvalidChoiceException("Cannot take order from this table");
//        }
//
//        entity.customer.Customer cust = t.getCurrentCustomer();
//
//        if (cust.getState() != entity.customer.CustomerState.SEATED || cust.getCurrentOrder() != null) {
//            throw new control.InvalidChoiceException("entity.customer.Customer not ready to order");
//        }
/*////
////        entity.menu.MenuItem item = cust.chooseMenuItem(menu);
////
////        entity.order.Order order = new entity.order.Order(cust.getArrivalTurn(), t.getTableID(), item);
////
////        cust.setCurrentOrder(order);
////        cust.setState(entity.customer.CustomerState.ORDERED);
////        busyThisTurn = true;
////        return order;
////    }
////    public void ServiceOrder(entity.order.Order order) {
////        if (!isAvailable() || order == null || order.getStatus() != entity.order.OrderStatus.READY) {
////            throw new control.InvalidChoiceException("Waiter can only serve READY orders");
////        }
////        order.markServed();
////        busyThisTurn = true;
////    }
////    public void waitTurn(){
////        if (!isAvailable()){
////            throw new control.InvalidChoiceException("Waiter already used this turn's task");
////        }
////        busyThisTurn = true;
////    }
////} new control.InvalidChoiceException("Cannot seat this customer at this table");
//        }
//*/
//        t.assignCustomer(cust);
//        cust.setState(entity.customer.CustomerState.SEATED);
//        busyThisTurn = true;
//        return true;
//    }
//
//    public entity.order.Order takeOrder (entity.Table t, entity.menu.Menu menu) {
//        if (!isAvailable() || t == null || !t.isOccupied() || menu == null) {
//            throw new control.InvalidChoiceException("Cannot take order from this table");
//        }
//
//        entity.customer.Customer cust = t.getCurrentCustomer();
//
//        if (cust.getState() != entity.customer.CustomerState.SEATED || cust.getCurrentOrder() != null) {
//            throw new control.InvalidChoiceException("entity.customer.Customer not ready to order");
//        }
//
//        entity.menu.MenuItem item = cust.chooseMenuItem(menu);
//
//        entity.order.Order order = new entity.order.Order(cust.getArrivalTurn(), t.getTableID(), item);
//
//        cust.setCurrentOrder(order);
//        cust.setState(entity.customer.CustomerState.ORDERED);
//        busyThisTurn = true;
//        return order;
//    }
//    public void ServiceOrder(entity.order.Order order) {
//        if (!isAvailable() || order == null || order.getStatus() != entity.order.OrderStatus.READY) {
//            throw new control.InvalidChoiceException("Waiter can only serve READY orders");
//        }
//        order.markServed();
//        busyThisTurn = true;
//    }
//    public void waitTurn(){
//        if (!isAvailable()){
//            throw new control.InvalidChoiceException("Waiter already used this turn's task");
//        }
//        busyThisTurn = true;
//    }
//}
package entity.staff;

import entity.customer.CustomerState;
import entity.customer.Customer;
import entity.Table;
import entity.menu.Menu;
import entity.menu.MenuItem;
import entity.order.Order;
import entity.Restaurant;

public class Waiter extends Staff {
    public Waiter(String name) {
        super(name);
    }

    public boolean seatCustomer(Customer cust, Table t) {
        if (!t.isOccupied()) {
            markBusy();
            t.assignCustomer(cust);
            cust.setState(CustomerState.SEATED);
        } else{
            throw new IllegalStateException("Table chosen is occupied");
        }
        return t.isOccupied();
    }

    public Order takeOrder(Table t, Menu menu, Restaurant r) {
        Customer cust = t.getCurrentCustomer();
        if (cust == null || cust.getState() != CustomerState.SEATED) {
            throw new IllegalStateException("No seated customer.");
        }

        markBusy();
        MenuItem item = cust.chooseMenuItem(menu);
        Order order = new Order(r.getNextOrderID(), t.getTableID(), item);
        cust.setCurrentOrder(order);
        cust.setState(CustomerState.ORDERED);
        r.addToPrepQueue(order);
        return order;
    }

    public void serveOrder(Table t, int currentTurn) {
        Customer cust = t.getCurrentCustomer();
        Order order = (cust == null) ? null : cust.getCurrentOrder();
        if (order == null || !order.isReady(currentTurn)) {
            throw new IllegalStateException("No order is ready to be served.");
        }
        markBusy();
        order.markServed();
        cust.startEating(currentTurn);
    }

    public void waitTurn() {
        markBusy();
    }
}