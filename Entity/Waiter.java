//import Entity.Staff;
//
//public class Waiter extends Staff {
//    public String Waiter (String name) {
//        super.name = name;
//    }
//
//    public boolean seatCustomer (Entity.Customer cust, Entity.Table t){
//
//        if (!isAvailable() || cust == null || t == null || t.isOccupied() || cust.getState() != CustomerState.WAITING_IN_QUEUE) {
//            throw//import Entity.Staff;
//
//public class Waiter extends Staff {
//    public String Waiter (String name) {
//        super.name = name;
//
//
//    public boolean seatCustomer (Entity.Customer cust, Entity.Table t){
//
//        if (!isAvailable() || cust == null || t == null || t.isOccupied() || cust.getState() != CustomerState.WAITING_IN_QUEUE) {
//            throw new Control.InvalidChoiceExeption("Cannot seat this customer at this table");
//        }
//
//        t.assignCustomer(cust);
//        cust.setState(CustomerState.SEATED);
//        busyThisTurn = true;
//        return true;
//    }
//
//    public Entity.Order takeOrder (Entity.Table t, Entity.Menu menu) {
//        if (!isAvailable() || t == null || !t.isOccupied() || menu == null) {
//            throw new Control.InvalidChoiceExeption("Cannot take order from this table");
//        }
//
//        Entity.Customer cust = t.getCurrentCustomer();
//
//        if (cust.getState() != CustomerState.SEATED || cust.getCurrentOrder() != null) {
//            throw new Control.InvalidChoiceExeption("Entity.Customer not ready to order");
//        }
/*////
////        Entity.MenuItem item = cust.chooseMenuItem(menu);
////
////        Entity.Order order = new Entity.Order(cust.getArrivalTurn(), t.getTableID(), item);
////
////        cust.setCurrentOrder(order);
////        cust.setState(CustomerState.ORDERED);
////        busyThisTurn = true;
////        return order;
////    }
////    public void ServiceOrder(Entity.Order order) {
////        if (!isAvailable() || order == null || order.getStatus() != OrderStatus.READY) {
////            throw new Control.InvalidChoiceExeption("Waiter can only serve READY orders");
////        }
////        order.markServed();
////        busyThisTurn = true;
////    }
////    public void waitTurn(){
////        if (!isAvailable()){
////            throw new Control.InvalidChoiceExeption("Waiter already used this turn's task");
////        }
////        busyThisTurn = true;
////    }
////} new Control.InvalidChoiceExeption("Cannot seat this customer at this table");
//        }
//*/
//        t.assignCustomer(cust);
//        cust.setState(CustomerState.SEATED);
//        busyThisTurn = true;
//        return true;
//    }
//
//    public Entity.Order takeOrder (Entity.Table t, Entity.Menu menu) {
//        if (!isAvailable() || t == null || !t.isOccupied() || menu == null) {
//            throw new Control.InvalidChoiceExeption("Cannot take order from this table");
//        }
//
//        Entity.Customer cust = t.getCurrentCustomer();
//
//        if (cust.getState() != CustomerState.SEATED || cust.getCurrentOrder() != null) {
//            throw new Control.InvalidChoiceExeption("Entity.Customer not ready to order");
//        }
//
//        Entity.MenuItem item = cust.chooseMenuItem(menu);
//
//        Entity.Order order = new Entity.Order(cust.getArrivalTurn(), t.getTableID(), item);
//
//        cust.setCurrentOrder(order);
//        cust.setState(CustomerState.ORDERED);
//        busyThisTurn = true;
//        return order;
//    }
//    public void ServiceOrder(Entity.Order order) {
//        if (!isAvailable() || order == null || order.getStatus() != OrderStatus.READY) {
//            throw new Control.InvalidChoiceExeption("Waiter can only serve READY orders");
//        }
//        order.markServed();
//        busyThisTurn = true;
//    }
//    public void waitTurn(){
//        if (!isAvailable()){
//            throw new Control.InvalidChoiceExeption("Waiter already used this turn's task");
//        }
//        busyThisTurn = true;
//    }
//}
