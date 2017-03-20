package com.zxczone.insertionfee;

import static org.junit.Assert.assertTrue;

import java.io.File;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.zxczone.insertionfee.dao.CategoryDao;
import com.zxczone.insertionfee.pojo.Category;
import com.zxczone.insertionfee.service.CategoryService;

/**
 * 
 * @author Jason Zhao
 * @date Mar 19, 2017
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class PriceCalculatorTest {

    @Autowired
    CategoryDao catDao;

    @Autowired
    CategoryService catService;

    @Before
    public void initTest() {
        catDao.loadFromEntryText(new File("test_data_1"));
    }

    @Test
    public void testGetPrice01() {
        int catId = 1;
        Category cat = catDao.getCategoryById(catId);
        int calculatedPrice = catService.getPriceOfCategory(catId);
        assertTrue(calculatedPrice == cat.getPrice());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetPrice02() {
        int catId = 2;
        catService.getPriceOfCategory(catId);
    }

}
