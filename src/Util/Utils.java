package Util;

/**
 * Created by jrj on 17-4-23.
 */
public class Utils {
    public static void main(String[] args){
        // this main is just for test
        System.out.println(flagInfo(4,3));
    }
    public static boolean flagInfo(int flag,int position){
        flag = flag>>>(position-1);
        if (flag % 2 == 1){
            return true;
        }else{
            return false;
        }
    }
}
