package se.aroms.Modelclass;

public class Tablets {

    private String tablet_Name,Assign_tablet;


    public Tablets(String tablet_Name, String assign_tablet) {
        this.tablet_Name = tablet_Name;
        Assign_tablet = assign_tablet;
    }


    public Tablets() {
    }


    public String getTablet_Name() {
        return tablet_Name;
    }

    public void setTablet_Name(String tablet_Name) {
        this.tablet_Name = tablet_Name;
    }

    public String getAssign_tablet() {
        return Assign_tablet;
    }

    public void setAssign_tablet(String assign_tablet) {
        Assign_tablet = assign_tablet;
    }
}
