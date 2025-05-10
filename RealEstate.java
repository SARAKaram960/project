public class RealEstate extends Asset {
    public RealEstate(String username, String name, int quantity, String purchaseDate, double purchasePrice,ValueCalculationStrategy strategy) {
        super(username, name, quantity, purchaseDate, purchasePrice,strategy);
    }
      @Override
    public String getAssetType() {
        return "RealEstate";
    }
}
