
import java.io.IOException;
import java.util.*;

public class Main {
    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        AuthService authService = AuthService.getInstance();

        System.out.println("Welcome to Invest Wise");
        System.out.println("1 - Sign Up");
        System.out.println("2 - Login");
        System.out.print("Choose an option: ");
        int choice = Integer.parseInt(scanner.nextLine());

        boolean loggedIn = false;
        String username = "";

        // تسجيل حساب أو تسجيل الدخول
        if (choice == 1) {
            System.out.println("=== User Sign-Up ===");
            System.out.print("Enter username: ");
            username = scanner.nextLine();
            System.out.print("Enter password: ");
            String password = scanner.nextLine();
            System.out.print("Enter email: ");
            String email = scanner.nextLine();

            User newUser = new User(username, password, email);
            boolean success = authService.signUp(newUser);

            if (success) {
                System.out.println(" Sign-up successful! You can now log in.");
            } else {
                System.out.println(" Username already exists. Try again.");
                scanner.close();
                return;
            }
        }

        if (choice == 2 || choice == 1) {
            System.out.println("\n=== User Login ===");
            System.out.print("Enter username: ");
            username = scanner.nextLine();
            System.out.print("Enter password: ");
            String password = scanner.nextLine();

            loggedIn = authService.login(username, password);

            if (loggedIn) {
                System.out.println(" Login successful! Welcome, " + username);
            } else {
                System.out.println(" Login failed. Exiting...");
                scanner.close();
                return;
            }
        } else {
            System.out.println("❗ Invalid choice.");
            scanner.close();
            return;
        }

        // إنشاء خدمات الأصول والمستخدم
        AssetService assetService = new AssetService();
        AssetManager assetManager = new AssetManager();

        // تسجيل Observer الزكاة
        Observer zakatService = new ZakatService();
        assetManager.addObserver(zakatService);

        // تحميل الأصول الخاصة بالمستخدم
        List<Asset> assets = assetManager.loadUserAssets(username);

        // تعيين الإستراتيجية المناسبة لكل أصل
        for (Asset a : assets) {
            switch (a.getAssetType().toLowerCase()) {
                case "stock" -> a.setStrategy(new StockValueCalculation());
                case "realestate" -> a.setStrategy(new RealEstateValueCalculation());
                case "crypto" -> a.setStrategy(new CryptoValueCalculation());
                // case "gold" -> a.setStrategy(new GoldValueCalculation());
            }
        }
        // طباعة الأصول مع القيم المحسوبة
        System.out.println("\n📊 Your Assets:");
        for (Asset a : assets) {
            double value = a.getStrategy().calculateValue(a);
            System.out.println("- " + a.getAssetType() + ": " + a.name +
                    " | Qty: " + a.quantity +
                    " | Price: " + a.purchasePrice +
                    " | Total Value: " + value);
        }

        // حساب الزكاة الإجمالية
        double totalZakat = ZakatCompliance.calculateTotalZakat(assets);
        System.out.println("\nTotal Zakat: " + totalZakat);

        // إدارة الأصول
        while (true) {
            System.out.println("\n=== Asset Management ===");
            System.out.println("1 - Add Asset");
            System.out.println("2 - Edit Asset");
            System.out.println("3 - Remove Asset");
            System.out.println("4- Connect Bank Account");
            System.out.println("5 - Exit");
            System.out.print("Choose an option: ");
            int assetChoice = Integer.parseInt(scanner.nextLine());

            boolean notifyObserver = false;  // Flag to track if notification is needed

            if (assetChoice == 1) {
                System.out.print("Enter Asset Type (Stock, RealEstate, Crypto, Gold): ");
                String type = scanner.nextLine();

                System.out.print("Enter Asset Name: ");
                String name = scanner.nextLine();

                System.out.print("Enter Quantity: ");
                int qty = Integer.parseInt(scanner.nextLine());

                System.out.print("Enter Purchase Date: ");
                String date = scanner.nextLine();

                System.out.print("Enter Purchase Price: ");
                double price = Double.parseDouble(scanner.nextLine());

                Asset asset = switch (type.toLowerCase()) {
                    case "stock" -> new Stock(username, name, qty, date, price, new StockValueCalculation());
                    case "realestate" -> new RealEstate(username, name, qty, date, price, new RealEstateValueCalculation());
                    case "crypto" -> new Crypto(username, name, qty, date, price, new CryptoValueCalculation());
                    // case "gold" -> new Gold(username, name, qty, date, price, new GoldValueCalculation());
                    default -> null;
                };

                if (asset != null) {
                    assetService.saveAsset(asset);
                    assets.add(asset);
                    notifyObserver = true;  // Set flag to true after asset addition
                    System.out.println(" Asset saved successfully.");
                } else {
                    System.out.println(" Invalid asset type.");
                }

            } else if (assetChoice == 2) {
                if (assets.isEmpty()) {
                    System.out.println("No assets to edit.");
                    continue;
                }

                for (int i = 0; i < assets.size(); i++) {
                    Asset a = assets.get(i);
                    System.out.println((i + 1) + ". " + a.getAssetType() + " - " + a.name + " (Qty: " + a.quantity + ")");
                }

                System.out.print("Enter asset number to edit: ");
                int index = Integer.parseInt(scanner.nextLine()) - 1;

                if (index >= 0 && index < assets.size()) {
                    Asset a = assets.get(index);

                    System.out.print("Enter new quantity: ");
                    int newQty = Integer.parseInt(scanner.nextLine());

                    System.out.print("Enter new purchase price: ");
                    double newPrice = Double.parseDouble(scanner.nextLine());

                    a.quantity = newQty;
                    a.purchasePrice = newPrice;

                    assetManager.saveAllAssets(username, assets);
                    notifyObserver = true;  // Set flag to true after asset update
                    System.out.println(" Asset updated successfully.");
                } else {
                    System.out.println(" Invalid index.");
                }

            } else if (assetChoice == 3) {
                if (assets.isEmpty()) {
                    System.out.println("No assets to remove.");
                    continue;
                }

                for (int i = 0; i < assets.size(); i++) {
                    Asset a = assets.get(i);
                    System.out.println((i + 1) + ". " + a.getAssetType() + " - " + a.name + " (Qty: " + a.quantity + ")");
                }

                System.out.print("Enter asset number to remove: ");
                int index = Integer.parseInt(scanner.nextLine()) - 1;

                if (index >= 0 && index < assets.size()) {
                    // حذف الأصل
                    assets.remove(index);

                    // حفظ الأصول بعد الحذف
                    assetManager.saveAllAssets(username, assets);
                    notifyObserver = true;  // Set flag to true after asset removal
                    System.out.println(" Asset removed successfully.");
                } else {
                    System.out.println(" Invalid index.");
                }

           

            }  
            else if(assetChoice==4){
                  BankAccountConnector.connectBankAccount();
                          break;
            }
            
            
            else if (assetChoice == 5) {
                System.out.println(" Exiting... Goodbye!");
                break;
            } else {
                System.out.println("Invalid option.");
            }

            // Notify observers only once after any change (add, edit, remove)
            if (notifyObserver) {
                assetManager.notifyObservers(assets);
            }  
        }
          

        scanner.close();
    }
}
