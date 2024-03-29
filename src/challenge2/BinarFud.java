package challenge2;

import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.*;

class MenuItem {
    private String name;
    private double price;

    public MenuItem(String name, double price) {
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }
}

class OrderItem {
    private MenuItem menuItem;
    private int quantity;

    public OrderItem(MenuItem menuItem, int quantity) {
        this.menuItem = menuItem;
        this.quantity = quantity;
    }

    public MenuItem getMenuItem() {
        return menuItem;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getTotalPrice() {
        return menuItem.getPrice() * quantity;
    }
}

class Order {
    private List<OrderItem> items;

    public Order() {
        items = new ArrayList<>();
    }

    public void addItem(OrderItem item) {
        items.add(item);
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public double getTotal() {
        double total = 0;
        for (OrderItem item : items) {
            total += item.getTotalPrice();
        }
        return total;
    }

    public int getTotalQuantity() {
        int totalQuantity = 0;
        for (OrderItem item : items) {
            totalQuantity += item.getQuantity();
        }
        return totalQuantity;
    }
}

public class BinarFud {
    private static Scanner inputScanner = new Scanner(System.in);

    public static void main(String[] args) {
        List<MenuItem> menuItems = new ArrayList<>();
        menuItems.add(new MenuItem("Nasi Goreng", 15000));
        menuItems.add(new MenuItem("Mie Goreng", 13000));
        menuItems.add(new MenuItem("Nasi + Ayam", 18000));
        menuItems.add(new MenuItem("Es Teh manis", 3000));
        menuItems.add(new MenuItem("Es Jeruk", 5000));

        Order order = new Order();

        while (true) {
            displayMainMenu(menuItems);
            int choice = getIntInput("=> ");

            if (choice == 99) {
                confirmOrder(order);
                break;
            } else if (choice == 0) {
                System.out.println("Keluar Aplikasi.");
                break;
            } else if (choice >= 1 && choice <= menuItems.size()) {
                MenuItem menuItem = menuItems.get(choice - 1);
                int quantity = getQuantity(menuItem);
                if (quantity > 0) {
                    order.addItem(new OrderItem(menuItem, quantity));
                }
            } else {
                System.out.println("============================");
                System.out.println("Mohon masukan input pilihan anda");
                System.out.println("============================");
                System.out.print("(Y) untuk lanjut, (n) untuk keluar\n=> ");
                String input = inputScanner.next();
                if (input.equalsIgnoreCase("n")) {
                    break;
                }
            }
        }
    }

    public static void displayMainMenu(List<MenuItem> menuItems) {
        System.out.println("==========================");
        System.out.println("Selamat datang di Binarfud");
        System.out.println("==========================");
        DecimalFormat decimalFormat = new DecimalFormat("#,###.00");
        for (int i = 0; i < menuItems.size(); i++) {
            MenuItem menuItem = menuItems.get(i);
            System.out.println((i + 1) + ". " + menuItem.getName() + " | " + decimalFormat.format(menuItem.getPrice()));
        }
        System.out.println("99. Pesan dan Bayar");
        System.out.println("0. Keluar Aplikasi");
    }

    public static int getQuantity(MenuItem menuItem) {
        System.out.println("========================");
        System.out.println("Berapa pesanan anda");
        System.out.println("========================");
        DecimalFormat decimalFormat = new DecimalFormat("#,###.00");
        System.out.println(menuItem.getName() + " | " + decimalFormat.format(menuItem.getPrice()));
        System.out.println("(input 0 untuk kembali)");
        int quantity = getIntInput("qty => ");
        if (quantity < 0) {
            System.out.println("============================");
            System.out.println("Mohon masukan input pilihan anda");
            System.out.println("============================");
            System.out.print("(Y) untuk lanjut, (n) untuk keluar\n=> ");
            String input = inputScanner.next();
            if (input.equalsIgnoreCase("n")) {
                System.exit(0);
            }
        }
        return quantity;
    }

    public static int getIntInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return inputScanner.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Mohon masukan input yang valid.");
                inputScanner.next(); // Clear the invalid input
            }
        }
    }

    public static void confirmOrder(Order order) {
        System.out.println("=================================");
        System.out.println("Konfirmasi & Pembayaran");
        System.out.println("================================");
        double total = 0;
        for (OrderItem orderItem : order.getItems()) {
            MenuItem menuItem = orderItem.getMenuItem();
            int quantity = orderItem.getQuantity();
            double itemTotal = menuItem.getPrice() * quantity;
            System.out.println(menuItem.getName() + "  " + quantity + "\t\t" + itemTotal);
            total += itemTotal;
        }
        System.out.println("-------------------------------+");
        System.out.println("Total\t\t" + order.getTotalQuantity() + "\t\t" + total);
        System.out.println();
        System.out.println("1. Konfirmasi dan Bayar");
        System.out.println("2. Kembali ke menu utama");
        System.out.println("0. Keluar Aplikasi");
        int choice = getIntInput("=> ");
        if (choice == 1) {
            generateReceipt(order);
        } else if (choice == 0) {
            System.out.println("Keluar Aplikasi.");
        } else {
            System.out.println("Kembali ke menu utama.");
        }
    }

    public static void generateReceipt(Order order) {
        try (FileWriter writer = new FileWriter("receipt.txt")) {
            DecimalFormat decimalFormat = new DecimalFormat("#,###.00");
            writer.write("=================================\n");
            writer.write("            Struk Pembayaran       \n");
            writer.write("=================================\n");
            for (OrderItem orderItem : order.getItems()) {
                MenuItem menuItem = orderItem.getMenuItem();
                int quantity = orderItem.getQuantity();
                double itemTotal = menuItem.getPrice() * quantity;
                writer.write(menuItem.getName() + "\t" + quantity + "\t" + decimalFormat.format(itemTotal) + "\n");
            }
            writer.write("---------------------------------\n");
            writer.write("Total\t\t" + order.getTotalQuantity() + "\t\t" + decimalFormat.format(order.getTotal()) + "\n");
            System.out.println("Struk pembayaran telah disimpan sebagai file 'receipt.txt'.");
        } catch (IOException e) {
            System.out.println("Gagal menyimpan struk pembayaran.");
            e.printStackTrace();
        }
    }
}
