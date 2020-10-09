import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;

public class game {
    public static final double DELTA = 0.0001;
    public static final double INTEREST = 0.005;
    public static void main(String[] args) throws FileNotFoundException {
        System.out.println();
        Scanner days = new Scanner(new File("days.txt"));
        int day = days.nextInt();
        days.close();

        Scanner prices = new Scanner(new File("prices.txt"));
        HashSet<Stock> stocks = new HashSet<>();
        while (prices.hasNextLine()) {
            String cur = prices.nextLine();
            stocks.add(new Stock(cur.substring(0,cur.indexOf(" ")), Double.parseDouble(cur.substring(cur.indexOf(" ")+1))));
        }
        
        Scanner balance = new Scanner(new File("balance.txt"));
        String temp = balance.nextLine();
        // double curmoney = Double.parseDouble(temp);
        BigDecimal curmoney = new BigDecimal(temp);
        curmoney = curmoney.setScale(2, RoundingMode.CEILING);
        if (curmoney.toString().equals("-0.01") || curmoney.toString().equals("0.01")) {
            curmoney = new BigDecimal(0.00);
            curmoney = curmoney.setScale(2, RoundingMode.CEILING);
        }
        HashMap<Stock, Integer> owned = new HashMap<>();
        double bank = 0.0;
        while (balance.hasNextLine()) {
            String cur = balance.nextLine();
            if (cur.indexOf(" ") == -1) {
                bank = Double.parseDouble(cur);
            }
            else {
                String name = cur.substring(0,cur.indexOf(" "));
                int quantity = Integer.parseInt(cur.substring(cur.indexOf(" ")+1));
                for (Stock stock : stocks) {
                    if (stock.valEquals(name)) {
                        owned.put(stock, quantity);
                    }
                }
            }
        }
        
        Scanner decision = new Scanner(System.in);
        System.out.println("It has been " + day + " days since you started this game.");
        displayStats(curmoney, owned, bank);
        System.out.println();
        displayMarket(stocks);
        System.out.println();
        askDecision();
        int choice = decision.nextInt();

        if (choice == 9) {
            for (int i = 0; i < 10; i++) {
                bank *= (1+INTEREST);
                bank = round(bank);
                for (Stock stock : stocks) {
                    double random = Math.random();
                    stock.fluctuatePrice(random);

                    if (goodDay()) {
                        double mult = (1+Math.random());
                        int roundmult = (int) Math.round((mult-1)*10000);
                        stock.price *= mult;
                        int tempprice = (int) (stock.price*100);
                        stock.price = tempprice/100.0;
                        System.out.println("-------------------");
                        System.out.println();
                        System.out.println(stock + " has revealed its new product, and everyone is loving it! The company's share price has increased by " + roundmult/100.0 + "%.");
                        System.out.println();
                        System.out.println("-------------------");
                    }
                    else if (badDay()) {
                        double mult = (1-Math.random()*Math.random());
                        int roundmult = (int) Math.round((mult-1)*10000);
                        stock.price *= mult;
                        int tempprice = (int) (stock.price*100);
                        stock.price = tempprice/100.0;
                        System.out.println("-------------------");
                        System.out.println();
                        System.out.println(stock + " has been accused of embezzlement! The company's share price has decreased by " + roundmult/100.0 + "%.");
                        System.out.println();
                        System.out.println("-------------------");
                    }
                }
            }

            balance.close();
            prices.close();
            day += 10;
        } else if (choice == 99) {
            for (int i = 0; i < 100; i++) {
                bank *= (1+INTEREST);
                bank = round(bank);
                for (Stock stock : stocks) {
                    double random = Math.random();
                    stock.fluctuatePrice(random);

                    if (goodDay()) {
                        double mult = (1+Math.random());
                        int roundmult = (int) Math.round((mult-1)*10000);
                        stock.price *= mult;
                        int tempprice = (int) (stock.price*100);
                        stock.price = tempprice/100.0;
                        System.out.println("-------------------");
                        System.out.println();
                        System.out.println(stock + " has revealed its new product, and everyone is loving it! The company's share price has increased by " + roundmult/100.0 + "%.");
                        System.out.println();
                        System.out.println("-------------------");
                    }
                    else if (badDay()) {
                        double mult = (1-Math.random()*Math.random());
                        int roundmult = (int) Math.round((mult-1)*10000);
                        stock.price *= mult;
                        int tempprice = (int) (stock.price*100);
                        stock.price = tempprice/100.0;
                        System.out.println("-------------------");
                        System.out.println();
                        System.out.println(stock + " has been accused of embezzlement! The company's share price has decreased by " + roundmult/100.0 + "%.");
                        System.out.println();
                        System.out.println("-------------------");
                    }
                }
            }

            balance.close();
            prices.close();
            day += 100;
        } else {
            day++; 
            while (choice != 1 && choice != 0) {
                if (choice == 2) {
                    curmoney = curmoney.setScale(2, RoundingMode.CEILING);
                    System.out.println("You currently have $" + curmoney + ".");

                    displayMarket(stocks);

                    System.out.println("What stock do you want to buy?");
                    String stockChoice = decision.next();
                    for (Stock stock : stocks) {
                        if (stock.valEquals(stockChoice)) {
                            // System.out.println("How many shares do you want to buy? (You can buy "
                            //         + (int) (curmoney / stock.price) + ")");
                            System.out.println("How many shares do you want to buy? (You can buy "
                                    + curmoney.divide(new BigDecimal(stock.price),RoundingMode.CEILING) + ")");
                            int buyQuant = decision.nextInt();
                            owned.put(stock, owned.getOrDefault(stock, 0) + buyQuant);
                            curmoney = curmoney.subtract(new BigDecimal(buyQuant).multiply(new BigDecimal(stock.price)));
                            // curmoney -= buyQuant * stock.price;
                            break;
                        }
                    }
                } else if (choice == 3) {
                    displayStats(curmoney, owned, bank);
                    System.out.println("What stock do you want to sell?");
                    String stockChoice = decision.next();
                    if (stockChoice.equalsIgnoreCase("All")) {
                        for (Stock stock : owned.keySet()) {
                            // curmoney += stock.price*owned.get(stock);
                            curmoney = curmoney.add(new BigDecimal(stock.price).multiply(new BigDecimal(owned.get(stock))));
                            owned.put(stock, 0);
                        }
                    }
                    else {
                        for (Stock stock : stocks) {
                            if (stock.valEquals(stockChoice)) {
                                System.out.println("How many shares do you want to sell? (You can sell " + owned.get(stock)
                                        + " shares)");
                                int sellQuant = decision.nextInt();
                                owned.put(stock, owned.getOrDefault(stock, 0) - sellQuant);
                                // curmoney += sellQuant * stock.price;
                                curmoney = curmoney.add(new BigDecimal(sellQuant).multiply(new BigDecimal(stock.price)));
                                break;
                            }
                        }
                    }
                } else if (choice == 4) {
                    displayStats(curmoney, owned, bank);
                } 
                else if (choice == 5) {
                    displayStats(curmoney, owned, bank);
                    System.out.println("Welcome to the bank! Our interest rate is  " + INTEREST*100 + "% per day. You have $" + bank + " in the bank.");
                    System.out.println("Type '1' to deposit money. Type '2' to withdraw.");
                    Scanner banker = new Scanner(System.in);
                    int bankchoice = banker.nextInt();
                    if (bankchoice == 1) {
                        curmoney = curmoney.setScale(2, RoundingMode.CEILING);
                        System.out.println("How much would you like to deposit? (max: $" + curmoney + ")");
                        double money = banker.nextDouble();
                        bank += money;

                        BigDecimal tmoney = new BigDecimal(money);
                        tmoney = tmoney.setScale(2, RoundingMode.CEILING);
                        curmoney = curmoney.subtract(tmoney);
                    }
                    else if (bankchoice == 2) {
                        System.out.println("How much would you like to withdraw? (max: $" + bank + ")");
                        double money = banker.nextDouble();
                        bank -= money;

                        BigDecimal tmoney = new BigDecimal(money);
                        tmoney = tmoney.setScale(2, RoundingMode.CEILING);
                        curmoney = curmoney.add(tmoney);
                    }
                }
                askDecision();
                choice = decision.nextInt();
            }

            bank *= (1+INTEREST);
            bank = round(bank);

            balance.close();
            prices.close();

            for (int i = 0; i < 3; i++) {
                for (Stock stock : stocks) {
                    double random = Math.random();
                    stock.fluctuatePrice(random);
                    if (goodDay()) {
                        double mult = (1+Math.random());
                        int roundmult = (int) Math.round((mult-1)*10000);
                        stock.price *= mult;
                        int tempprice = (int) (stock.price*100);
                        stock.price = tempprice/100.0;
                        System.out.println("-------------------");
                        System.out.println();
                        System.out.println(stock + " has revealed its new product, and everyone is loving it! The company's share price has increased by " + roundmult/100.0 + "%.");
                        System.out.println();
                        System.out.println("-------------------");
                    }
                    else if (badDay()) {
                        double mult = (1-Math.random()*Math.random());
                        int roundmult = (int) Math.round((mult-1)*10000);
                        stock.price *= mult;
                        int tempprice = (int) (stock.price*100);
                        stock.price = tempprice/100.0;
                        System.out.println("-------------------");
                        System.out.println();
                        System.out.println(stock + " has been accused of embezzlement! The company's share price has decreased by " + roundmult/100.0 + "%.");
                        System.out.println();
                        System.out.println("-------------------");
                    }
                }
            }
        }

        for (Stock stock : owned.keySet()) {
            if (stock.price > 3500.0) {
                stock.price /= 2;
                owned.put(stock, owned.get(stock) * 2);
                System.out.println("-------------------");
                System.out.println();
                System.out.println(stock + " has split in 1:2. The holders of " + stock
                        + " now has double the original amount of shares.");
                System.out.println();
                System.out.println("-------------------");
                System.out.println();
            }
        }

        for (Stock stock : stocks) {
            if (stock.price > 3500) {
                stock.price /= 2;
                System.out.println("-------------------");
                System.out.println();
                System.out.println(stock + " has split in 1:2. The holders of " + stock
                        + " now has double the original amount of shares.");
                System.out.println();
                System.out.println("-------------------");
                System.out.println();
            }
        }

        PrintWriter exportdays = new PrintWriter("days.txt");
        exportdays.println(day);
        exportdays.close();

        // int curmoneytemp = (int) (curmoney * 100);
        // curmoney = curmoneytemp / 100.0;

        PrintWriter balanceExport = new PrintWriter("balance.txt");
        // if (checkDELTA(curmoney)) {
        //     if (curmoney < 0) {
        //         curmoney = (int) (curmoney - DELTA * 10);
        //     } else {
        //         curmoney = (int) (curmoney + DELTA * 10);
        //     }
        // }
        balanceExport.println(curmoney);
        for (Stock stock : owned.keySet()) {
            balanceExport.println(stock.name + " " + owned.get(stock));
        }
        balanceExport.println(bank);
        balanceExport.close();

        PrintWriter pricesExport = new PrintWriter("prices.txt");
        for (Stock stock : stocks) {
            pricesExport.println(stock.name + " " + stock.price);
        }
        pricesExport.close();
        if (choice == 0 || (choice == 9 || choice == 99)) {
            game.main(new String[0]);
        }
    }

    private static void askDecision() {
        System.out.println("What would you like to do?");
        System.out.println("Enter '0' to go on to the next day. Enter '1' to quit. Enter '2' to buy. Enter '3' to sell. Enter '4' to display your shares. \nEnter '5' to enter the bank. Enter '9' to skip 10 days.");
    }

    private static void displayMarket(HashSet<Stock> stocks) {
        System.out.println("-------------------");
        System.out.println("Current Market Standings");
        System.out.println("-------------------");
        for (Stock stock : stocks) {
            System.out.println(stock);
        }
        System.out.println("-------------------");
    }

    private static void displayStats(double curmoney, HashMap<Stock, Integer> owned) {
        System.out.println("You currently have $" + curmoney + ".");

        System.out.println();
        System.out.println("-------------------");
        double tot = 0.0;
        for (Stock stock : owned.keySet()) {
            if (owned.containsKey(stock)) {
                System.out.println("You have " + owned.get(stock) + " shares of " + stock + " - Total: $"
                        + owned.get(stock) * stock.price);
                tot += owned.get(stock) * stock.price;
            }
        }
        System.out.println("Total portfolio: $" + (tot + curmoney) + ".");
        System.out.println("-------------------");
    }

    private static void displayStats(BigDecimal curmoney, HashMap<Stock, Integer> owned, double bank) {
        System.out.println("You currently have $" + curmoney + ".");
        System.out.println("You currently have $" + bank + " in the bank.");

        System.out.println();
        System.out.println("-------------------");
        double tot = 0.0;
        for (Stock stock : owned.keySet()) {
            if (owned.get(stock) != 0) {
                System.out.println("You have " + owned.get(stock) + " shares of " + stock + " - Total: $"
                        + round(owned.get(stock) * stock.price));
                tot += owned.get(stock) * stock.price;
            }
        }
        BigDecimal added = curmoney.add(new BigDecimal(tot));
        added = added.setScale(2, RoundingMode.CEILING);
        BigDecimal tbank = added.add(new BigDecimal(bank));
        tbank = tbank.setScale(2, RoundingMode.CEILING);
        System.out.println("Total portfolio: $" + tbank + ".");
        System.out.println("-------------------");
    }

    private static double round(double d) {
        int temp = (int) (d*100);

        return temp/100.0;
    }

    private static boolean checkDELTA(double num) {
        return (num - DELTA < (int) num || num + DELTA > (int) num + 1);
    }

    private static boolean goodDay() {
        return Math.random() < 0.005;
    }
    private static boolean badDay() {
        return Math.random() < 0.001;
    }
}