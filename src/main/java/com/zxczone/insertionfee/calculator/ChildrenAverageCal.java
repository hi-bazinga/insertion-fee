package com.zxczone.insertionfee.calculator;

import java.util.List;

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
public class ChildrenAverageCal implements IPriceCalculator {
    
    @Autowired
    CategoryDao categoryDao;

    @Override
    public int calculate(int catId) {
        Category cat = categoryDao.getCategoryById(catId);
        if (cat == null) {
            throw new IllegalArgumentException("Category doesn't exist!");
        } else if (cat.getPrice() != null) {
            return cat.getPrice();
        } else {
            List<Category> catList = categoryDao.getChildrenOfCategory(catId);
            int avg = 0;
            if (catList.size() == 0) {
                return 0;
            } else {
                for (Category c : catList) {
                    avg += (c.getPrice() != null ? c.getPrice() : 0);
                }
                return avg / catList.size();
            }
        }
    }
    
}
