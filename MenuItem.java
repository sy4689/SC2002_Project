public class MenuItem {
    private String name;
    private int priceinCents;
    private int prepUnits;

    public MenuItem(String name, int priceinCents, int prepUnits) {
        this.name  = name;
        this.priceinCents = priceinCents;
        this.prepUnits = prepUnits;
    }

    public String getName() {
        return name;
    }

    public int getPriceinCents() {
        return priceinCents;
    }

    public int getPrepUnits() {
        return prepUnits;
    }
}
