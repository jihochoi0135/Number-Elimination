/**
 * Jiho Choi
 * 251090958
 */

public class Main_assignment1 {
    public static int part7_a(int i){
        if(i == 0 || i == 1){
            return 2;
        }
        else{
            return part7_a(i-1) + part7_a(i-2);
        }
    }



    public static void main(String[] args) {

        System.out.println("part a");

        for(int i = 0; i <= 10; i++){
            System.out.println(part7_a(i*5));
        }


    }
}