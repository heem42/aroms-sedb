package se.aroms.Modelclass;

public class Table {


    String Capacity,Table_Number,status;


    public Table() {
    }

    public Table(String capacity, String table_Number) {
        Capacity = capacity;
        Table_Number = table_Number;
    }

    public Table(String capacity, String table_Number, String status) {
        Capacity = capacity;
        Table_Number = table_Number;
        this.status = status;
    }


    public String getCapacity() {
        return Capacity;
    }

    public void setCapacity(String capacity) {
        Capacity = capacity;
    }

    public String getTable_Number() {
        return Table_Number;
    }

    public void setTable_Number(String table_Number) {
        Table_Number = table_Number;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
