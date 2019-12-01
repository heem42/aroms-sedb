package se.aroms;

import java.util.Map;

public class chef_list {


    Map<String, dish_list2> dishes;
    //String email;
    String name;
    Long queue;
    String speciality;
    String time;


    public chef_list( Map<String,dish_list2> dishe,String cname, Long queu, String specialit) {
        this.name = cname;
        this.queue = queu;
        this.speciality = specialit;
        this.dishes = dishe;
    }
    public chef_list() {
    }
   /* public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

*/


    public chef_list(Map<String, dish_list2> dishes, String name, Long queue, String speciality, String time) {
        this.dishes = dishes;
        this.name = name;
        this.queue = queue;
        this.speciality = speciality;
        this.time = time;
    }
/*  public chef_list(Map<String, dish_list2> dishes, String name, Long queue, String speciality, String email) {
        this.dishes = dishes;
        this.name = name;
        this.queue = queue;
        this.speciality = speciality;
        this.email = email;
    }*/

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getname() {
        return name;
    }

    public void setname(String cname) {
        this.name = cname;
    }

    public long getQueue() {
        return queue;
    }

    public void setQueue(long queue) {
        this.queue = queue;
    }

    public String getSpeciality() {
        return speciality;
    }

    public void setSpeciality(String speciality) {
        this.speciality = speciality;
    }

    public Map<String, dish_list2> getDishes() {
        return dishes;
    }

    public void setDishes( Map<String, dish_list2> dishes) {
        this.dishes = dishes;
    }

}
