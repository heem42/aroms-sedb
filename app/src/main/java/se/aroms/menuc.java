package se.aroms;

public class menuc {
    String uid;
    String name;
    String type;

    String time;

    public void setTime(String time) {
        this.time = time;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTime() {
        return time;
    }

    public menuc(String menuid, String n) {
        this.uid = menuid;
        this.name = n;
    }
    public menuc(String menuid, String n,String t) {
        this.uid = menuid;
        this.name = n;
        this.type=t;
    }


    public menuc() {
    }

    public String getuid() {
        return uid;
    }

    public void setuid(String menuid) {
        this.uid = menuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
