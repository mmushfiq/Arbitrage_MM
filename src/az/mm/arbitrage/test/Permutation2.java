package az.mm.arbitrage.test;

//This is a java program to perform all permutation of given list of numbers of a specific length
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;

public class Permutation2 {

    static void permute(int[] a, int k) {
        if (k == a.length) {
            for (int i = 0; i < a.length; i++) {
                System.out.print(" [" + a[i] + "] ");
            }
            System.out.println();
        } else {
            for (int i = k; i < a.length; i++) {
                int temp = a[k];
                a[k] = a[i];
                a[i] = temp;

                permute(a, k + 1);

                temp = a[k];
                a[k] = a[i];
                a[i] = temp;
            }
        }
    }

    public static void main(String args[]) {
        Random random = new Random();
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter the length of list: ");
        int N = sc.nextInt();
        int[] sequence = new int[N];

        for (int i = 0; i < N; i++) {
            sequence[i] = Math.abs(random.nextInt(100));
        }

        System.out.println("The original sequence is: ");
        for (int i = 0; i < N; i++) {
            System.out.print(sequence[i] + " ");
        }

        System.out.println("\nThe permuted sequences are: ");
        permute(sequence, 0);

        sc.close();
    }
}

class Permutation3 {

    /**
     * @param args the command line arguments
     */
    void printArray(int[] a) {
        for (int i = 0; i < a.length; i++) {
            System.out.print(a[i] + " ");

        }
        System.out.println("");
    }

    void permute(int[] a, int k) {
        if (k == a.length) {
            printArray(a);
        } else {
            for (int i = k; i < a.length; i++) {
                int temp = a[k];
                a[k] = a[i];
                a[i] = temp;
                permute(a, k + 1);
                temp = a[k];
                a[k] = a[i];
                a[i] = temp;
            }
        }
    }

    public static void main(String[] args) {
        Permutation3 p = new Permutation3();
        int a[] = {1, 2, 3, 4, 5, 6};
        p.permute(a, 0);
    }
}

class Permutation4 {

    /**
     * @param args the command line arguments
     */
    int count = 1;

    void printArray(String[] a) {
        System.out.print(count++ + " ");
        for (int i = 0; i < a.length; i++) {
            System.out.print(a[i] + " ");
        }
        System.out.println("");
    }

    void permute(String[] a, int k) {
        if (k == a.length) {
            printArray(a);
        } else {
            for (int i = k; i < a.length; i++) {
                String temp = a[k];
                a[k] = a[i];
                a[i] = temp;
                permute(a, k + 1);
                temp = a[k];
                a[k] = a[i];
                a[i] = temp;
            }
        }
    }

    public static void main(String[] args) {
        Permutation4 p = new Permutation4();
        String[] a = {/*"AZN",*/"USD", "EUR", "GBP", "RUB", "TRY",};
        String[] temp;
        for (int i = 1; i <= a.length; i++) {
            temp = new String[i];
            System.arraycopy(a, 0, temp, 0, i);
            p.permute(temp, 0);
            System.out.println("---------------------------------------------");
        }

    }
}


