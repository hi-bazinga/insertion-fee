package com.zxczone.insertionfee.calculator;

import com.zxczone.insertionfee.StaticContext;

/**
 * 
 * @author Jason Zhao
 * @date Mar 18, 2017 
 */
public class PriceCalFactory {
    
    public static IPriceCalculator getCalculator(PricePolicy policy) {
        switch (policy) {
            case CLOSEST_PARENT:
                return StaticContext.getContext().getBean(ClosestParentCal.class);
            case DEFAULT_VALUE:
                return StaticContext.getContext().getBean(DefaultValueCal.class);
            case CHILDREN_AVERAGE:
                return StaticContext.getContext().getBean(ChildrenAverageCal.class);
            default:
                throw new IllegalArgumentException("Invalid price policy: " + policy);
        }
    }
}
