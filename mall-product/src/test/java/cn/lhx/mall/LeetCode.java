package cn.lhx.mall;

import java.util.Arrays;

/**
 * @author lee549
 * @date 2020/10/28 21:17
 */
public class LeetCode {
    public static void main(String[] args) {
        constructArr(new int[]{1,2,3});

    }
    // 0 1 1 2 3 5 8 13 21
    public static int fib(int n) {
        if(n==0){
            return 0;
        }

        int[] arr = new int[n+1];
        arr[0] = 0;
        arr[1] = 1;
        for(int i = 2;i<arr.length;i++){
            arr[i] = (arr[i-1] + arr[i-2])%(int)(1e9 + 7);
        }
        return arr[n];

    }

    public static int[] printNumbers(int n) {
        // StringBuilder builder = new StringBuilder();
        // for (int j=1;j<=n;j++){
        //     builder.append(9);
        // }
        // String s = builder.toString();
        // System.out.println(builder);
        // int k = Integer.parseInt(s);
         int pow = (int)Math.pow(10, n)-1;
        int[] res =new int[pow];
        // System.out.println(k);
        for(int i=0;i<pow;i++){
            res[i]=i+1;
            System.out.println(res[i]);
        }
        return res;
    }

    public static int[] constructArr(int[] a) {
        int n = a.length;
        int[] B = new int[n];
        for (int i = 0, product = 1; i < n; product *= a[i], i++)       /* 从左往右累乘 */
            B[i] = product;
        for (int i = n - 1, product = 1; i >= 0; product *= a[i], i--)  /* 从右往左累乘 */
            B[i] *= product;
        return B;

    }
}
