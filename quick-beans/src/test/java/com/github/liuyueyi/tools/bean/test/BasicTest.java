package com.github.liuyueyi.tools.bean.test;

import com.github.liuyueyi.tools.bean.test.model.Source;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Random;

/**
 * @author yihui
 * @date 2021/4/9
 */
public class BasicTest {

    private Random random = new Random();

    public Source genSource() {
        Source source = new Source();
        source.setId(random.nextInt());
        source.setIds(Arrays.asList(random.nextLong(), random.nextLong(), random.nextLong()));
        source.setMarketPrice(new BigDecimal(random.nextFloat()));
        source.setPrice(random.nextInt(120) / 10.0d);
        source.setUser_name("一灰灰Blog");
        return source;
    }

}
