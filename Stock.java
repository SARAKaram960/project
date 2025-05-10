// public class Stock extends Asset {
//     private static final double CURRENT_PRICE = 150.0;

//     public Stock(String username, String name, int quantity, String purchaseDate, double purchasePrice) {
//         super(username, name, quantity, purchaseDate, purchasePrice); // التأكد من أن هذه القيم تُمرر بشكل صحيح إلى الباني الأب
//     }

//     @Override
//     public double calculateValue() {
//         return quantity * CURRENT_PRICE;
//     }

//     @Override
//     public String getAssetType() {
//         return "Stock";
//     }
// }
public class Stock extends Asset {
    public Stock(String username, String name, int quantity, String purchaseDate, double purchasePrice,ValueCalculationStrategy strategy) {
        super(username, name, quantity, purchaseDate, purchasePrice,strategy ); // التأكد من أن هذه القيم تُمرر بشكل صحيح إلى الباني الأب
    }
      @Override
    public String getAssetType() {
        return "Stock";
    }
}