package com.android.hhn.javalib;

/**
 * Author: haonan.he ;<p/>
 * Date: 2020/4/14,5:59 PM ;<p/>
 * Description: 菲波那切数列实现;<p/>
 * Other: ;
 */
public class Fibonacci {
    /**
     * 循环实现
     *
     * @param num
     *
     * @return
     */
    private static long fibLoop(int num) {
        if (num < 1 || num > 92)
            return 0;
        long pre = 1;
        long next = 1;
        long temp;
        for (int i = 3; i <= num; i++) {
            temp = pre;
            pre = next;
            next = next + temp;
        }
        return next;
    }

    /**
     * 递归方法实现
     * f(n) = f(n - 1) + f(n - 2)
     * 最高支持 n = 92 ，否则超出 Long.MAX_VALUE
     *
     * @param num n
     *
     * @return f(n)
     */
    private static long fibRec(int num) {
        if (num < 1 || num > 93)
            return 0;
        if (num < 3)
            return 1;
        return fibRec(num - 1) + fibRec(num - 2);
    }

    private static long[] cache;

    /**
     * @param num
     *
     * @return
     */
    private static long testFibCacheRec(int num) {
        cache = new long[num + 1];
        return fibCacheRec(num);
    }

    private static long fibCacheRec(int num) {
        if (num < 1 || num > 93)
            return 0;
        if (num < 3)
            return 1;
        if (cache[num] == 0) {
            cache[num] = fibCacheRec(num - 1) + fibCacheRec(num - 2);
        }
        return cache[num];
    }

    public static void main(String[] args) {
        int num = 40;
        long start = System.currentTimeMillis();
        System.out.println(fibLoop(num));
        System.out.println(System.currentTimeMillis() - start);

        long start2 = System.currentTimeMillis();
        System.out.println(fibRec(num));
        System.out.println(System.currentTimeMillis() - start2);

        long start3 = System.currentTimeMillis();
        System.out.println(testFibCacheRec(num));
        System.out.println(System.currentTimeMillis() - start3);
    }

}
