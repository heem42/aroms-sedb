package se.aroms;

public class chefs {

    String name;
    String keyoforder;
    String key;
    String speciality;
    Long queue;

    public String getKeyoforder() {
        return keyoforder;
    }

    public void setKeyoforder(String keyoforder) {
        this.keyoforder = keyoforder;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }


    public String getSpeciality() {
        return speciality;
    }

    public void setSpeciality(String speciality) {
        this.speciality = speciality;
    }

    public long getQueue() {
        return queue;
    }

    public void setQueue(long queue) {
        this.queue = queue;
    }


    public String getname() {
        return name;
    }

    public void setname(String dish) {
        this.name = dish;
    }

    public chefs( String dish) {
        this.name = dish;
    }

    chefs()
    {

    }

}
