package com.example.administrator.flowlayout;

import org.junit.Test;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.List;
import java.lang.System;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    private List<Integer> gongji = new ArrayList<>();
    private List<Integer> muji = new ArrayList<>();
    private List<Integer> xiaoji = new ArrayList<>();
    private int gongjiNum = 0;
    private int mujiNum = 0;
    private int xiaojiNum = 0;
    private static int total = 100;
    private static int gongjiPrice = 5;
    private static int mujiPrice = 3;
    private static int xiaojiPrice = 1 / 3;

    @Test
    public void caculate() {
        for (int i = 0; i <= total / 5; i++) {
            for (int j = 0; j <= total / 3; j++) {
                if (gongjiNum * gongjiPrice + mujiNum * mujiPrice + xiaojiNum * xiaojiPrice == 100
                        && gongjiNum + mujiNum + xiaojiNum == 100 && xiaojiNum % 3 == 0) {
                    gongji.add(gongjiNum);
                    muji.add(mujiNum);
                    xiaoji.add(xiaojiNum);
                }

            }
        }

        for (Integer integer : gongji) {
            System.out.print(integer);
        }

        for (Integer integer : muji) {
            System.out.print(integer);
        }

        for (Integer integer : xiaoji) {
            System.out.print(integer);
        }
        for (int i = 0; i <= total / 5; i++) {
            for (int j = 0; j <= total / 3; j++) {
                if (gongjiNum * gongjiPrice + mujiNum * mujiPrice + xiaojiNum * xiaojiPrice == 100
                        && gongjiNum + mujiNum + xiaojiNum == 100 && xiaojiNum % 3 == 0) {
                    gongji.add(gongjiNum);
                    muji.add(mujiNum);
                    xiaoji.add(xiaojiNum);
                }

            }
        }

        for (Integer integer : gongji) {
            System.out.print(integer);
        }

        for (Integer integer : muji) {
            System.out.print(integer);
        }

        for (Integer integer : xiaoji) {
            System.out.print(integer);
        }
    }

}