import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Main {
    static final int MONTHS = 12;
    static final int DAYS = 28;
    static final int COMMODITIES = 5;

    static String[] commodities = {"Gold", "Oil", "Silver", "Wheat", "Copper"};
    static String[] months = {"January","February","March","April","May","June",
            "July","August","September","October","November","December"};

    static int[][][] profit = new int[MONTHS][DAYS][COMMODITIES]; // [month][day][commodity]

    // ======== REQUIRED METHOD LOAD DATA ========
    public static void loadData() {
        String dataFolder = "Data_Files/";
        String[] monthFiles = {
            "January.txt", "February.txt", "March.txt", "April.txt",
            "May.txt", "June.txt", "July.txt", "August.txt",
            "September.txt", "October.txt", "November.txt", "December.txt"
        };

        try {
            for (int m = 0; m < MONTHS; m++) {
                BufferedReader br = new BufferedReader(new FileReader(dataFolder + monthFiles[m]));
                String line;
                while ((line = br.readLine()) != null) {
                    String[] parts = line.split(",");
                    if (parts.length != 3) continue;

                    int day = Integer.parseInt(parts[0].trim()) - 1; // zero-based index
                    String commodity = parts[1].trim();
                    int profitValue = Integer.parseInt(parts[2].trim());

                    int commodityIndex = -1;
                    for (int c = 0; c < COMMODITIES; c++) {
                        if (commodities[c].equals(commodity)) {
                            commodityIndex = c;
                            break;
                        }
                    }

                    if (commodityIndex == -1 || day < 0 || day >= DAYS) continue;

                    profit[m][day][commodityIndex] = profitValue;
                }
                br.close();
            }
        } catch (IOException e) {
            // IOException ignored as per project spec (no exceptions thrown)
        }
    }


    // ======== 10 REQUIRED METHODS ========

    public static String mostProfitableCommodityInMonth(int month) {
        if (month < 0 || month >= MONTHS) return "INVALID_MONTH";

        int maxProfit = Integer.MIN_VALUE;
        int maxCommodityIndex = -1;

        for (int c = 0; c < COMMODITIES; c++) {
            int total = 0;
            for (int d = 0; d < DAYS; d++) {
                total += profit[month][d][c];
            }
            if (total > maxProfit) {
                maxProfit = total;
                maxCommodityIndex = c;
            }
        }

        return commodities[maxCommodityIndex] + " " + maxProfit;
    }

    public static int totalProfitOnDay(int month, int day) {
        if (month < 0 || month >= MONTHS || day < 1 || day > DAYS) return -99999;

        int sum = 0;
        for (int c = 0; c < COMMODITIES; c++) {
            sum += profit[month][day - 1][c];
        }
        return sum;
    }

    public static int commodityProfitInRange(String commodity, int fromDay, int toDay) {
        if (fromDay < 1 || toDay > DAYS || fromDay > toDay) return -99999;

        int cIndex = -1;
        for (int i = 0; i < COMMODITIES; i++) {
            if (commodities[i].equals(commodity)) {
                cIndex = i;
                break;
            }
        }
        if (cIndex == -1) return -99999;

        int total = 0;
        for (int m = 0; m < MONTHS; m++) {
            for (int d = fromDay - 1; d <= toDay - 1; d++) {
                total += profit[m][d][cIndex];
            }
        }
        return total;
    }

    public static int bestDayOfMonth(int month) {
        if (month < 0 || month >= MONTHS) return -1;

        int bestDay = -1;
        int maxSum = Integer.MIN_VALUE;

        for (int d = 0; d < DAYS; d++) {
            int sum = 0;
            for (int c = 0; c < COMMODITIES; c++) {
                sum += profit[month][d][c];
            }
            if (sum > maxSum) {
                maxSum = sum;
                bestDay = d;
            }
        }
        return bestDay + 1;
    }

    public static String bestMonthForCommodity(String commodity) {
        int cIndex = -1;
        for (int i = 0; i < COMMODITIES; i++) {
            if (commodities[i].equals(commodity)) {
                cIndex = i;
                break;
            }
        }
        if (cIndex == -1) return "INVALID_COMMODITY";

        int bestMonth = -1;
        int maxProfit = Integer.MIN_VALUE;

        for (int m = 0; m < MONTHS; m++) {
            int sum = 0;
            for (int d = 0; d < DAYS; d++) {
                sum += profit[m][d][cIndex];
            }
            if (sum > maxProfit) {
                maxProfit = sum;
                bestMonth = m;
            }
        }
        return months[bestMonth];
    }

    public static int consecutiveLossDays(String commodity) {
        int cIndex = -1;
        for (int i = 0; i < COMMODITIES; i++) {
            if (commodities[i].equals(commodity)) {
                cIndex = i;
                break;
            }
        }
        if (cIndex == -1) return -1;

        int maxStreak = 0;
        int currentStreak = 0;

        for (int m = 0; m < MONTHS; m++) {
            for (int d = 0; d < DAYS; d++) {
                if (profit[m][d][cIndex] < 0) {
                    currentStreak++;
                    if (currentStreak > maxStreak) maxStreak = currentStreak;
                } else {
                    currentStreak = 0;
                }
            }
        }
        return maxStreak;
    }

    public static int daysAboveThreshold(String commodity, int threshold) {
        int cIndex = -1;
        for (int i = 0; i < COMMODITIES; i++) {
            if (commodities[i].equals(commodity)) {
                cIndex = i;
                break;
            }
        }
        if (cIndex == -1) return -1;

        int count = 0;
        for (int m = 0; m < MONTHS; m++) {
            for (int d = 0; d < DAYS; d++) {
                if (profit[m][d][cIndex] > threshold) {
                    count++;
                }
            }
        }
        return count;
    }

    public static int biggestDailySwing(int month) {
        if (month < 0 || month >= MONTHS) return -99999;

        int maxDiff = 0;
        for (int d = 0; d < DAYS - 1; d++) {
            int dayProfit = 0;
            int nextDayProfit = 0;
            for (int c = 0; c < COMMODITIES; c++) {
                dayProfit += profit[month][d][c];
                nextDayProfit += profit[month][d + 1][c];
            }
            int diff = Math.abs(dayProfit - nextDayProfit);
            if (diff > maxDiff) maxDiff = diff;
        }
        return maxDiff;
    }

    public static String compareTwoCommodities(String c1, String c2) {
        int idx1 = -1, idx2 = -1;
        for (int i = 0; i < COMMODITIES; i++) {
            if (commodities[i].equals(c1)) idx1 = i;
            if (commodities[i].equals(c2)) idx2 = i;
        }
        if (idx1 == -1 || idx2 == -1) return "INVALID_COMMODITY";

        int total1 = 0, total2 = 0;
        for (int m = 0; m < MONTHS; m++) {
            for (int d = 0; d < DAYS; d++) {
                total1 += profit[m][d][idx1];
                total2 += profit[m][d][idx2];
            }
        }

        if (total1 > total2) {
            return c1 + " is better by " + (total1 - total2);
        } else if (total2 > total1) {
            return c2 + " is better by " + (total2 - total1);
        } else {
            return "Equal";
        }
    }

    public static String bestWeekOfMonth(int month) {
        if (month < 0 || month >= MONTHS) return "INVALID_MONTH";

        int[] weekSums = new int[4];

        for (int w = 0; w < 4; w++) {
            int startDay = w * 7;
            int endDay = startDay + 6;
            int sum = 0;
            for (int d = startDay; d <= endDay; d++) {
                for (int c = 0; c < COMMODITIES; c++) {
                    sum += profit[month][d][c];
                }
            }
            weekSums[w] = sum;
        }

        int bestWeek = 0;
        for (int i = 1; i < 4; i++) {
            if (weekSums[i] > weekSums[bestWeek]) bestWeek = i;
        }
        return "Week " + (bestWeek + 1);
    }

    // Main method
    public static void main(String[] args) {
    loadData();
    System.out.println("Data loaded – ready for queries");

    // 1. mostProfitableCommodityInMonth
    System.out.println("Most Profitable Commodity in January: " + mostProfitableCommodityInMonth(0));

    // 2. totalProfitOnDay
    System.out.println("Total profit on January 1: " + totalProfitOnDay(0, 1));

    // 3. commodityProfitInRange
    System.out.println("Profit of Gold from day 1 to 7: " + commodityProfitInRange("Gold", 1, 7));

    // 4. bestDayOfMonth
    System.out.println("Best day of January: " + bestDayOfMonth(0));

    // 5. bestMonthForCommodity
    System.out.println("Best month for Oil: " + bestMonthForCommodity("Oil"));

    // 6. consecutiveLossDays
    System.out.println("Longest consecutive loss days for Silver: " + consecutiveLossDays("Silver"));

    // 7. daysAboveThreshold
    System.out.println("Number of days Wheat profit > 1000: " + daysAboveThreshold("Wheat", 1000));

    // 8. biggestDailySwing
    System.out.println("Biggest daily swing in January: " + biggestDailySwing(0));

    // 9. compareTwoCommodities
    System.out.println("Compare Gold and Copper: " + compareTwoCommodities("Gold", "Copper"));

    // 10. bestWeekOfMonth
    System.out.println("Best week of January: " + bestWeekOfMonth(0));
}

}
