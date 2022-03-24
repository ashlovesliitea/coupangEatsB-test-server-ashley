package com.example.demo.src.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class TestDao {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public void DBTest(){
        double lng=126.9019532;
        double lat=126.9019532;
        Object[] location={lng,lat};


        String findLocationQuery=
                "SELECT station,\n" +
                "ST_Distance_Sphere(POINT((SELECT user_address_lng FROM User_Address UA WHERE UA.user_idx=1),(SELECT user_address_lat FROM User_Address UA WHERE UA.user_idx=1)), POINT(lng, lat)) AS distance\n" +
                "FROM Station";

        String result=this.jdbcTemplate.queryForObject(findLocationQuery,
                (rs,rowNum)->{
                    String stationName=rs.getString(1);
                    return stationName;
                },location);


    }
}
