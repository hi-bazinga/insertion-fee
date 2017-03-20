package com.zxczone.insertionfee;

import static org.junit.Assert.assertTrue;

import java.io.File;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.zxczone.insertionfee.calculator.DefaultValueCal;
import com.zxczone.insertionfee.calculator.PricePolicy;
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
public class PriceCalculatorTest2 {
    
    private static final Logger LOG = LoggerFactory.getLogger(PriceCalculatorTest2.class);

    @Autowired
    CategoryDao catDao;

    @Autowired
    CategoryService catService;

    @Before
    public void initTest() {
        catDao.loadFromEntryText(new File("test_data_2"));
    }

    @Test
    public void testGetPrice01() {
        int catId = 17;
        Category cat = catDao.getCategoryById(catId);
        Category parentCat = catDao.getParentOfCategory(catId);
        int calculatedPrice = catService.getPriceOfCategory(catId);
        LOG.info("Calculated Price: " + calculatedPrice);
        
        assertTrue(cat.getPrice() == null);
        assertTrue(calculatedPrice == parentCat.getPrice());
    }
    
    @Test
    public void testGetPrice02() {
        int catId = 17;
        Category cat = catDao.getCategoryById(catId);
        int calculatedPrice = catService.getPriceOfCategory(catId, PricePolicy.DEFAULT_VALUE);
        LOG.info("Calculated Price: " + calculatedPrice);
        
        assertTrue(cat.getPrice() == null);
        assertTrue(calculatedPrice == DefaultValueCal.DEFAULT_PRICE);
    }

}
