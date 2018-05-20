import java.util.Scanner;

public class Main {

    public static void main(String args[]) throws Exception {
        Scanner sc = new Scanner(System.in);

        int inputCount = sc.nextInt();

        for (int i = 0; i < inputCount; i++) {
            String[] inputArray = sc.next().split(",");

            int sum = 0;
            for (String input : inputArray) {
                sum += Integer.parseInt(input);
            }

            System.out.println(sum);
        }
    }
}
