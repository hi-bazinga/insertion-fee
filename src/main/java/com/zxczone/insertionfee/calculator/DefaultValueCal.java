package com.zxczone.insertionfee.calculator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.zxczone.insertionfee.dao.CategoryDao;
import com.zxczone.insertionfee.pojo.Category;

/**
 * 
 * @author Jason Zhao
 * @date Mar 18, 2017 
 */
@Component
public class DefaultValueCal implements IPriceCalculator {
    
    public static final int DEFAULT_PRICE = 100;
    
    @Autowired
    CategoryDao categoryDao;

    @Override
    public int calculate(int catId) {
        Category cat = categoryDao.getCategoryById(catId);
        if (cat == null) {
            throw new IllegalArgumentException("Category doesn't exist!");
        }
        return cat.getPrice() != null ? cat.getPrice() : DEFAULT_PRICE;
    }
    
}
