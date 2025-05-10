public class  RealEstateValueCalculation implements ValueCalculationStrategy {
    @Override
    public double calculateValue(Asset asset) {
        return asset.getQuantity() * asset.getPurchasePrice();
    }
}