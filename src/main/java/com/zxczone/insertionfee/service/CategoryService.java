package com.zxczone.insertionfee.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zxczone.insertionfee.calculator.IPriceCalculator;
import com.zxczone.insertionfee.calculator.PriceCalFactory;
import com.zxczone.insertionfee.calculator.PricePolicy;
import com.zxczone.insertionfee.dao.CategoryDao;
import com.zxczone.insertionfee.pojo.Category;

/**
 * Category Service
 * 
 * @author Jason Zhao
 * @date Mar 18, 2017
 */
@Service
public class CategoryService {
    
    @Autowired
    CategoryDao categoryDao;

    /**
     * Get categories in tree format
     * @return root category with children
     */
    public Category getCategories() {
        return categoryDao.getCategoryTree();
    }
    
    /**
     * Get category
     * @return category object
     */
    public Category getCategory(int catId) {
        return categoryDao.getCategoryById(catId);
    }
    
    /**
     * Get the price of category based on default policy: CLOSEST_PARENT
     * @param catId category ID
     * @param policy price policy type
     * @return price
     */
    public int getPriceOfCategory(int catId) {
        IPriceCalculator calculator = PriceCalFactory.getCalculator(PricePolicy.CLOSEST_PARENT);
        return calculator.calculate(catId);
    }

    /**
     * Get the price of category based on specific price policy
     * @param catId category ID
     * @param policy price policy type
     * @return price
     */
    public int getPriceOfCategory(int catId, PricePolicy policy) {
        IPriceCalculator calculator = PriceCalFactory.getCalculator(policy);
        return calculator.calculate(catId);
    }
}
