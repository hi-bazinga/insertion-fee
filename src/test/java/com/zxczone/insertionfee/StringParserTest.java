package com.zxczone.insertionfee;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * 
 * @author Jason Zhao
 * @date Mar 19, 2017
 */
@RunWith(SpringJUnit4ClassRunner.class)
public class StringParserTest {

    @Test
    public void testParser() {
        Pattern pattern = Pattern.compile("\\-\\s*(.+?)\\s{2,}\\[(\\d+){0,1}\\]");
        Matcher m = pattern.matcher("- Root Category              []");
        m.find();
        String nameStr = m.group(1);
        String priceStr = m.group(2);
        System.out.println(nameStr + " " + priceStr);
    }
    
}
