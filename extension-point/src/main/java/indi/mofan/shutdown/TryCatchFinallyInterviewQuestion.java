package indi.mofan.shutdown;

/**
 * @author mofan
 * @date 2023/10/15 13:08
 */
public class TryCatchFinallyInterviewQuestion {

    private static int tryCatchFinallyQuestion(int param) {
        try {
            if (param > 10) {
                throw new RuntimeException();
            }
            System.out.println("try param: " + param);
            return param;
        } catch (Exception e) {
            param++;
            System.out.println("catch param: " + param);
            return param;
        } finally {
            param++;
            System.out.println("finally param: " + param);
            if (param > 20 || param < 0) {
                return param;
            }
        }
    }

    private static void neverReturnsNormally() {
        try {
            System.out.println("try block");
            System.exit(0);
        } catch (Exception e) {
            System.out.println("catch block");
        } finally {
            System.out.println("finally block");
        }
    }

    public static void main(String[] args) {
        int result = tryCatchFinallyQuestion(2);
        System.out.println("result: " + result);
        System.out.println("------------------------");
        result = tryCatchFinallyQuestion(-1);
        System.out.println("result: " + result);
        System.out.println("------------------------");
        result = tryCatchFinallyQuestion(11);
        System.out.println("result: " + result);
        System.out.println("------------------------");
        result = tryCatchFinallyQuestion(21);
        System.out.println("result: " + result);

        System.out.println("------------------------");
        neverReturnsNormally();
    }
}
