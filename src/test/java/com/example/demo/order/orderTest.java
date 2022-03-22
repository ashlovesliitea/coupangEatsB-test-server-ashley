package com.example.demo.order;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.List;

public class orderTest {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){this.jdbcTemplate=jdbcTemplate;}

    public List<String> getOptionList(int orderIdx, int menuIdx){
        String getOrderDetailOptionListQuery="SELECT mo.option_name\n" +
                "FROM order_detail od\n" +
                "INNER JOIN order_detail_option odo\n" +
                "ON od.order_detail_idx=odo.order_detail_idx \n" +
                "INNER JOIN menu_option mo \n" +
                "ON odo.option_idx=mo.option_idx\n" +
                "WHERE od.order_id=? and od.menu_idx=?";

        Object[] optionListParam={orderIdx,menuIdx};

        return this.jdbcTemplate.query(getOrderDetailOptionListQuery,
                (rs,rowNum)->{
                String option_name=rs.getString("option_name");
                return option_name;},
                optionListParam

        );
    }

    @Test
    public void optionListTest(){
        List<String>getOptionList=getOptionList(1,1);
        for (String option:getOptionList){
            System.out.println("option = " + option);
        }
    }
}
