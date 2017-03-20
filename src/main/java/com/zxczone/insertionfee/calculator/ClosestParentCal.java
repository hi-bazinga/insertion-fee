package com.zxczone.insertionfee.calculator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.zxczone.insertionfee.dao.CategoryDao;
import com.zxczone.insertionfee.pojo.Category;

/**
 * @author Jason Zhao
 * @date Mar 18, 2017
 */
@Component
public class ClosestParentCal implements IPriceCalculator {

    @Autowired
    CategoryDao categoryDao;

    @Override
    public int calculate(int catId) {
        Category cat = categoryDao.getCategoryById(catId);
        if (cat == null) {
            throw new IllegalArgumentException("Category doesn't exist!");
        }
        if (cat.getPrice() != null) {
            return cat.getPrice();
        } else if (cat.getParentId() == null) {  
            // no price set && is root -> no charge
            return 0;
        } else {  
            // no price set && has parent -> return parent's price
            return calculate(cat.getParentId());
        }
    }

}
