import java.util.List;
public class  ZakatCompliance{
    public static double calculateZakat(Asset asset) {
        double value = asset.calculateValue(); // يستخدم الستراتيجي
        return value * 0.025; // نسبة الزكاة 2.5%
    }
    public static double calculateTotalZakat(List<Asset> assets) {
        double totalZakat = 0;
        for (Asset asset : assets) {
            totalZakat += calculateZakat(asset);
        }
        return totalZakat;
    }
}
