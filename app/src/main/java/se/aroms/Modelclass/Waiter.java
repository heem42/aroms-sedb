package se.aroms.Modelclass;

public class Waiter {


    private String Waiter_Name,Assign_table;


    public Waiter(String waiter_Name, String assign_table) {
        Waiter_Name = waiter_Name;
        Assign_table = assign_table;
    }


    public Waiter() {

    }

    public String getWaiter_Name() {
        return Waiter_Name;
    }

    public void setWaiter_Name(String waiter_Name) {
        Waiter_Name = waiter_Name;
    }

    public String getAssign_table() {
        return Assign_table;
    }

    public void setAssign_table(String assign_table) {
        Assign_table = assign_table;
    }
}
