public class Restaurant
{
    private int id;
    private String name;
    private String cuisineType;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCuisineType() {
        return cuisineType;
    }

    public void setCuisineType(String cuisineType) {
        this.cuisineType = cuisineType;
    }

    public Restaurant(String name, String cuisineType) {
        this.name = name;
        this.cuisineType = cuisineType;


    }
}
