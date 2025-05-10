import java.util.List;

public class ZakatService implements Observer {
    @Override
    public void update(List<Asset> assets) {
        double totalZakat = ZakatCompliance.calculateTotalZakat(assets);
        System.out.println(" [ZakatService] Updated Zakat: " + totalZakat);
    }
}
