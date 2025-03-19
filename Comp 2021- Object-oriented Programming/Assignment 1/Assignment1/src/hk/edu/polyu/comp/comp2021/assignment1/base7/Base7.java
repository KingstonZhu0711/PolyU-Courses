package hk.edu.polyu.comp.comp2021.assignment1.base7;

public class Base7 {
    public static String convertToBase7(int num) {
            if (num == 0) {
                return "0";
            } else {
                String result = new String();

                if (num < 0) {
                    num = -num;
                    while (num != 0) {
                        int remainderofnum = num % 7;
                        num =num/ 7;
                        result += remainderofnum;

                    }
                    result+="-";
                }
                else {
                    while (num != 0) {
                        int remainderofnum = num % 7;
                        num =num/ 7;
                        result += remainderofnum;
                    }
                }
                //reversing the string
                char[] finalresult = result.toCharArray();
                int start = 0;
                int end = finalresult.length - 1;
                while (start < end) {
                    char temp = finalresult[start];
                    finalresult[start] = finalresult[end];
                    finalresult[end] = temp;
                    start++;
                    end--;
                }

                return new String(finalresult);
            }
    }
}
