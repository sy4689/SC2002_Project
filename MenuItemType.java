public enum MenuItemType implements MenuItem{
    SALAD(900,1),
    BURGER(1500,1),
    PASTA(1800,2);

    private final int priceInCents;
    private final int prepUnits;

    MenuItemType(int priceInCents, int prepUnits){
        this.priceInCents = priceInCents;
        this.prepUnits = prepUnits;
    }
    public int getPriceInCents(){
        return priceInCents;
    }

    public int getPrepUnits() {
        return prepUnits;
    }
}
