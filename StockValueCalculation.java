
public class StockValueCalculation implements ValueCalculationStrategy {
    @Override
    public double calculateValue(Asset asset) {
        return asset.getQuantity() * asset.getPurchasePrice();
    }
}

 