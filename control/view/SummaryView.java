package control.view;

public final class SummaryView {
    private final int servedAndPaidCount;
    private final int unhappyDepartures;
    private final int revenueInCents;
    private final Double averageSatisfaction;
    private final int targetPaidCustomers;
    private final int targetRevenueCents;
    private final int targetAverageSatisfaction;

    public SummaryView(int paidCount, int unhappyDepartures, int revenueCents, Double averageSatisfaction, int targetPaidCustomers, int targetRevenueCents, int targetAverageSatisfaction) {
        this.servedAndPaidCount = paidCount;
        this.unhappyDepartures = unhappyDepartures;
        this.revenueInCents = revenueCents;
        this.averageSatisfaction = averageSatisfaction;
        this.targetPaidCustomers = targetPaidCustomers;
        this.targetRevenueCents = targetRevenueCents;
        this.targetAverageSatisfaction = targetAverageSatisfaction;
    }

    public int getPaidCount() {
        return servedAndPaidCount;
    }

    public int getUnhappyDepartures() {
        return unhappyDepartures;
    }

    public int getRevenueCents() {
        return revenueInCents;
    }

    public Double getAverageSatisfaction() {
        return averageSatisfaction;
    }

    public int getTargetPaidCustomers() {
        return targetPaidCustomers;
    }

    public int getTargetRevenueCents() {
        return targetRevenueCents;
    }

    public int getTargetAverageSatisfaction() {
        return targetAverageSatisfaction;
    }
}
