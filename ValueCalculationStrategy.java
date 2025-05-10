// // واجهة لحساب القيمة
// public interface ValueCalculationStrategy {
//     double calculateValue();  // دالة لحساب قيمة الأصل
// }
public interface ValueCalculationStrategy {
    double calculateValue(Asset asset);
}