//import Staff.Staff;
//
//public class Waiter extends Staff {
//    public String Waiter (String name) {
//        super.name = name;
//    }
//
//    public boolean seatCustomer (Customer cust, Table t){
//
//        if (!isAvailable() || cust == null || t == null || t.isOccupied() || cust.getState() != CustomerState.WAITING_IN_QUEUE) {
//            throw//import Staff.Staff;
//
//public class Waiter extends Staff {
//    public String Waiter (String name) {
//        super.name = name;
//
//
//    public boolean seatCustomer (Customer cust, Table t){
//
//        if (!isAvailable() || cust == null || t == null || t.isOccupied() || cust.getState() != CustomerState.WAITING_IN_QUEUE) {
//            throw new InvalidChoiceExeption("Cannot seat this customer at this table");
//        }
//
//        t.assignCustomer(cust);
//        cust.setState(CustomerState.SEATED);
//        busyThisTurn = true;
//        return true;
//    }
//
//    public Order takeOrder (Table t, Menu menu) {
//        if (!isAvailable() || t == null || !t.isOccupied() || menu == null) {
//            throw new InvalidChoiceExeption("Cannot take order from this table");
//        }
//
//        Customer cust = t.getCurrentCustomer();
//
//        if (cust.getState() != CustomerState.SEATED || cust.getCurrentOrder() != null) {
//            throw new InvalidChoiceExeption("Customer not ready to order");
//        }
/*////
////        MenuItem item = cust.chooseMenuItem(menu);
////
////        Order order = new Order(cust.getArrivalTurn(), t.getTableID(), item);
////
////        cust.setCurrentOrder(order);
////        cust.setState(CustomerState.ORDERED);
////        busyThisTurn = true;
////        return order;
////    }
////    public void ServiceOrder(Order order) {
////        if (!isAvailable() || order == null || order.getStatus() != OrderStatus.READY) {
////            throw new InvalidChoiceExeption("Waiter can only serve READY orders");
////        }
////        order.markServed();
////        busyThisTurn = true;
////    }
////    public void waitTurn(){
////        if (!isAvailable()){
////            throw new InvalidChoiceExeption("Waiter already used this turn's task");
////        }
////        busyThisTurn = true;
////    }
////} new InvalidChoiceExeption("Cannot seat this customer at this table");
//        }
//*/
//        t.assignCustomer(cust);
//        cust.setState(CustomerState.SEATED);
//        busyThisTurn = true;
//        return true;
//    }
//
//    public Order takeOrder (Table t, Menu menu) {
//        if (!isAvailable() || t == null || !t.isOccupied() || menu == null) {
//            throw new InvalidChoiceExeption("Cannot take order from this table");
//        }
//
//        Customer cust = t.getCurrentCustomer();
//
//        if (cust.getState() != CustomerState.SEATED || cust.getCurrentOrder() != null) {
//            throw new InvalidChoiceExeption("Customer not ready to order");
//        }
//
//        MenuItem item = cust.chooseMenuItem(menu);
//
//        Order order = new Order(cust.getArrivalTurn(), t.getTableID(), item);
//
//        cust.setCurrentOrder(order);
//        cust.setState(CustomerState.ORDERED);
//        busyThisTurn = true;
//        return order;
//    }
//    public void ServiceOrder(Order order) {
//        if (!isAvailable() || order == null || order.getStatus() != OrderStatus.READY) {
//            throw new InvalidChoiceExeption("Waiter can only serve READY orders");
//        }
//        order.markServed();
//        busyThisTurn = true;
//    }
//    public void waitTurn(){
//        if (!isAvailable()){
//            throw new InvalidChoiceExeption("Waiter already used this turn's task");
//        }
//        busyThisTurn = true;
//    }
//}
