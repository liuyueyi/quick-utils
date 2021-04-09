package com.github.liuyueyi.tools.bean.test.model;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author yihui
 * @date 2021/4/7
 */
public class CamelTarget {
    private Integer id;
    private String userName;
    private Double price;
    private List<Long> ids;
    private BigDecimal market_price;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public List<Long> getIds() {
        return ids;
    }

    public void setIds(List<Long> ids) {
        this.ids = ids;
    }

    public BigDecimal getMarket_price() {
        return market_price;
    }

    public void setMarket_price(BigDecimal market_price) {
        this.market_price = market_price;
    }

    @Override
    public String toString() {
        return "CamelTarget{" +
                "id=" + id +
                ", userName='" + userName + '\'' +
                ", price=" + price +
                ", ids=" + ids +
                ", market_price=" + market_price +
                '}';
    }
}
