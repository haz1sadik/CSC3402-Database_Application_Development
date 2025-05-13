package LAB5;

import java.util.*;

public class SortBenchmark {

    static Random rand = new Random();

    public static void main(String[] args) {
        int[] sizes = {1000, 2000, 3000, 4000, 5000, 6000, 7000, 8000, 9000, 10000, 11000, 12000, 13000, 14000, 15000, 16000, 17000, 18000, 19000, 20000};

        System.out.printf("%-6s | %-12s | %-10s | %-10s | %-12s | %-10s | %-10s | %-13s | %-12s | %-12s%n",
                "Size", "Insert Best", "Shell Best", "Quick Best",
                "Insert Avg", "Shell Avg", "Quick Avg",
                "Insert Worst", "Shell Worst", "Quick Worst");
        System.out.println("----------------------------------------------------------------------" +
                "----------------------------------------------------------------");

        for (int size : sizes) {
            int[] best = generateSorted(size);
            int[] average = generateRandom(size);
            int[] worst = generateReversed(size);
            int[] bestQuick = generateQuickSortBestCase(size);


            long insBest = timeSort(best, SortBenchmark::insertionSort);
            long shellBest = timeSort(best, SortBenchmark::shellSort);
            long quickBest = timeSort(bestQuick, arr -> quickSort(arr, 0, arr.length - 1));

            long insAvg = timeSort(average, SortBenchmark::insertionSort);
            long shellAvg = timeSort(average, SortBenchmark::shellSort);
            long quickAvg = timeSort(average, arr -> quickSort(arr, 0, arr.length - 1));

            long insWorst = timeSort(worst, SortBenchmark::insertionSort);
            long shellWorst = timeSort(worst, SortBenchmark::shellSort);
            long quickWorst = timeSort(worst, arr -> quickSort(arr, 0, arr.length - 1));

            System.out.printf("%-6d | %-12d | %-10d | %-10d | %-12d | %-10d | %-10d | %-13d | %-12d | %-12d%n",
                    size, insBest, shellBest, quickBest,
                    insAvg, shellAvg, quickAvg,
                    insWorst, shellWorst, quickWorst);
        }
    }

    // Sort timing (average of 5 runs)
    static long timeSort(int[] arr, Sorter sorter) {
        long total = 0;
        for (int i = 0; i < 50; i++) {
            int[] copy = Arrays.copyOf(arr, arr.length);
            long start = System.nanoTime();
            sorter.sort(copy);
            long end = System.nanoTime();
            total += (end - start);
        }
        System.out.println(total);
        return total / 50 / 1000000; // average in ms
    }

    interface Sorter {
        void sort(int[] arr);
    }

    // Sorting Algorithms

    static void insertionSort(int[] arr) {
        for (int i = 1; i < arr.length; i++) {
            int key = arr[i], j = i - 1;
            while (j >= 0 && arr[j] > key) arr[j + 1] = arr[j--];
            arr[j + 1] = key;
        }
    }

    static void shellSort(int[] arr) {
        for (int gap = arr.length / 2; gap > 0; gap /= 2) {
            for (int i = gap; i < arr.length; i++) {
                int temp = arr[i], j = i;
                while (j >= gap && arr[j - gap] > temp) {
                    arr[j] = arr[j - gap];
                    j -= gap;
                }
                arr[j] = temp;
            }
        }
    }

    static void quickSort(int[] arr, int low, int high) {
        if (low < high) {
            int pivot = arr[low], i = low, j = high;
            while (i < j) {
                while (i < j && arr[j] >= pivot) j--;
                arr[i] = arr[j];
                while (i < j && arr[i] <= pivot) i++;
                arr[j] = arr[i];
            }
            arr[i] = pivot;
            quickSort(arr, low, i - 1);
            quickSort(arr, i + 1, high);
        }
    }

    // Data Generators

    static int[] generateSorted(int n) {
        int[] arr = new int[n];
        for (int i = 0; i < n; i++) arr[i] = i + 1;
        return arr;
    }

    static int[] generateReversed(int n) {
        int[] arr = new int[n];
        for (int i = 0; i < n; i++) arr[i] = n - i;
        return arr;
    }

    static int[] generateRandom(int n) {
        int[] arr = generateSorted(n);
        for (int i = 0; i < n; i++) {
            int j = rand.nextInt(n);
            int temp = arr[i];
            arr[i] = arr[j];
            arr[j] = temp;
        }
        return arr;
    }
    public static int[] generateQuickSortBestCase(int n) {
        int[] arr = new int[n];
        List<Integer> sorted = new ArrayList<>();
        for (int i = 1; i <= n; i++) {
            sorted.add(i);
        }

        List<Integer> best = new ArrayList<>();
        buildBestCaseQuickSortArray(sorted, best);

        // Convert List<Integer> to int[]
        for (int i = 0; i < n; i++) {
            arr[i] = best.get(i);
        }
        return arr;
    }

    private static void buildBestCaseQuickSortArray(List<Integer> sorted, List<Integer> best) {
        if (sorted.isEmpty()) return;

        int mid = sorted.size() / 2;
        best.add(sorted.get(mid));  // Add median to current position
        // Recurse on left and right halves
        buildBestCaseQuickSortArray(sorted.subList(0, mid), best);
        buildBestCaseQuickSortArray(sorted.subList(mid + 1, sorted.size()), best);
    }

}
