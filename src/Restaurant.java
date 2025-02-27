// Restaurant.java
public class Restaurant {
    private int id;
    private String name;
    private String cuisine;

    public Restaurant(int id, String name, String cuisine) {
        this.id = id;
        this.name = name;
        this.cuisine = cuisine;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCuisine() {
        return cuisine;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCuisine(String cuisine) {
        this.cuisine = cuisine;
    }
}
