package com.github.liuyueyi.tools.bean.test.model;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author yihui
 * @date 2021/4/7
 */
public class Source {
    private Integer id;
    private String user_name;
    private Double price;
    private List<Long> ids;
    private BigDecimal marketPrice;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
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

    public BigDecimal getMarketPrice() {
        return marketPrice;
    }

    public void setMarketPrice(BigDecimal marketPrice) {
        this.marketPrice = marketPrice;
    }

    @Override
    public String toString() {
        return "Source{" +
                "id=" + id +
                ", user_name='" + user_name + '\'' +
                ", price=" + price +
                ", ids=" + ids +
                ", marketPrice=" + marketPrice +
                '}';
    }
}
