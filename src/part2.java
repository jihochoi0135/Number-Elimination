/**
 * Jiho Choi
 * 251090958
 */
public class part2 {

    public static long[] Nn(int n) {
        if (n == 0) {
            return new long[] {2, 0};
        } else if (n == 1) {
            return new long[] {2, 2};
        } else {
            long[] prev = Nn(n - 1);
            return new long[] {prev[0] + prev[1], prev[0]};
        }
    }

    public static void main(String[] args) {
        System.out.println("part b");

        for (int i = 0; i <= 25; i++) {
            long[] res = Nn(i * 20);
            System.out.println(res[0]);
        }
    }
}
