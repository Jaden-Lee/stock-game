public class Stock {
    public static final double DELTA = 0.0001;
    String name;
    double price;

    public Stock(String name) {
        this.name = name;
        price = 0;
    }

    public Stock(String name, double price) {
        this.name = name;
        this.price = price;
    }
    
    public String toString() {
        if (price == 0) {
            return name;
        }
        return name + ": $" + price;
    }

    public boolean valEquals(String str) {
        return str.equals(name);
    }

    public void fluctuatePrice(double rand) {
        double random = Math.random();
        if (price < 15.0) {
            if (rand*rand > random) {
                int change = (int) (Math.abs(rand-random)*100);
                price -= change/100.0;
            }
            else {
                int change = (int) (Math.abs(rand*random-random)*100);
                price += change/100.0;
            }
        }
        else {
            if (price > 2000) {
                if (rand*rand > random) {
                    int change = (int) (Math.abs(rand-random)*100);
                    change *= (int) (Math.log(price));
                    price -= change/100.0;
                }
                else {
                    int change = (int) (Math.abs(rand*random-random)*100);
                    change *= (int) (Math.log(price))*1.1;
                    price += change/100.0;
                }
            }
            else {
                if (rand < random) {
                    int change = (int) (Math.abs(random-rand)*100);
                    change *= (int) (Math.log(price))*1.025;
                    price -= change/100.0;
                }
                else {
                    int change = (int) (Math.abs(random-rand)*100);
                    change *= (int) (Math.log(price))*1.05;
                    price += change/100.0;
                }
            }
        }
        // if (rand < random) {
        //     int change = (int) (Math.abs(random-rand)*100);
        //     if (price < 15.0) {
        //         change *= 0.8;
        //     }
        //     else {
        //         change *= (int) (Math.log(price));
        //     }
        //     price -= change/100.0;
        // }
        // else {
        //     int change = (int) (Math.abs(random-rand)*100);
        //     if (price < 15.0) {
        //         change *= 1.25;
        //     }
        //     else {
        //         change *= (int) (Math.log(price));
        //     }
        //     price += change/100.0;
        // }
        if (checkDELTA(price)) {
            if (price < 0) {
                price = (int) (price-DELTA*10);
            }
            else {
                price = (int) (price+DELTA*10);
            }
        }
        int temp = (int) (price*100);
        price = temp/100.0;
    }
    private static boolean checkDELTA(double num) {
        return (num - DELTA < (int) num || num + DELTA > (int) num+1);
    }
}
