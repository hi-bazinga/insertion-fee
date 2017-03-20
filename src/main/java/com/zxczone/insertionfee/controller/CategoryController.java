package com.zxczone.insertionfee.controller;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zxczone.insertionfee.calculator.PricePolicy;
import com.zxczone.insertionfee.pojo.Category;
import com.zxczone.insertionfee.service.CategoryService;

/**
 * Category Controller
 * 
 * @author Jason Zhao
 * @date Mar 18, 2017
 */
@Controller
@RequestMapping("/categories")
public class CategoryController {

    @Autowired
    CategoryService categoryService;

    @RequestMapping(value = "", method = RequestMethod.GET, headers = "accept=application/json")
    @ResponseBody
    public Category getCategories() {
        return categoryService.getCategories();
    }

    @RequestMapping(value = "/tree", method = RequestMethod.GET)
    @ResponseBody
    public String getCategoryTree(HttpServletResponse response) {
        response.setContentType("text/html");
        return categoryService.getCategories().toTreeHtml();
    }

    @RequestMapping(value = "/{cat_id}/price", method = RequestMethod.GET, headers = "accept=application/json")
    @ResponseBody
    public Integer getPriceOfCategory(@PathVariable("cat_id") int catId,
            @RequestParam(value = "policy", required = false) PricePolicy policy) {
        policy = policy != null ? policy : PricePolicy.CLOSEST_PARENT;
        return categoryService.getPriceOfCategory(catId, policy);
    }
}
