package com.explore.weeboos.leetcode;

import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        System.out.print(removeDuplicates(new int[]{1,1,2}));
    }

    public int removeDuplicates(int[] nums) {
        int num = 0;
        for(int i = 0; i < nums.length; i++) {
            if(nums[i] != nums[num]) {
                num ++;
                nums[num] = nums[i];
            }
        }
        num += 1;
        return num;
    }
}