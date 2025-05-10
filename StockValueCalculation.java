// public class StockValueCalculation implements ValueCalculationStrategy {
//     private final Stock stock;
//     public StockValueCalculation(Stock stock) {
//         this.stock = stock;
//     } 
//     @Override
//     public double calculateValue() {
//         return stock.getQuantity() * stock.getPurchasePrice();

//     }
// }
public class StockValueCalculation implements ValueCalculationStrategy {
    @Override
    public double calculateValue(Asset asset) {
        return asset.getQuantity() * asset.getPurchasePrice();
    }
}

 