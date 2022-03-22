package com.example.demo.src.order;

import com.example.demo.src.order.model.GetOrderRes;
import com.example.demo.src.order.model.OrderDetail;
import org.hibernate.criterion.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Repository
public class OrderDao {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){this.jdbcTemplate=new JdbcTemplate(dataSource);}

    public GetOrderRes getOrder(int orderIdx){
       String getOrderedMenuListQuery="Select od.order_detail_idx,menu.menu_name,od.menu_amount " +
               "FROM order_detail od " +
               "INNER JOIN order_list ol ON od.order_idx=ol.order_idx " +
               "INNER JOIN menu ON od.menu_idx=menu.menu_idx " +
               "WHERE ol.order_idx = ?";

       int orderedMenuParam=orderIdx;

       List<Integer>orderedDetailIdxList=new ArrayList<>();
       List<Integer>orderedMenuAmountList=new ArrayList<>();
       List<String>orderedMenuList=
               this.jdbcTemplate.query(getOrderedMenuListQuery,
                       (rs,rowNum)->{
                        int idx=rs.getInt("order_detail_idx");
                        orderedDetailIdxList.add(idx);
                        String menuName=rs.getString("menu_name");
                        int menuAmount=rs.getInt("menu_amount");
                        orderedMenuAmountList.add(menuAmount);
                        return menuName;
                       },orderedMenuParam);

       List<OrderDetail>orderDetailList=new ArrayList<>();
       int detailListLen=orderedDetailIdxList.size();
       for(int i=0;i<detailListLen;i++){

           String orderedMenu=orderedMenuList.get(i);
           int orderedAmount=orderedMenuAmountList.get(i);
           int orderDetailIdx=orderedDetailIdxList.get(i);
           List<String>orderedOptionList=getOptionList(orderDetailIdx);
           OrderDetail orderDetail=new OrderDetail(orderedMenu,orderedAmount,orderedOptionList);
           orderDetailList.add(orderDetail);
           System.out.println("---------");
       }
        String getOrderListQuery="SELECT User.user_name,CONCAT_WS(' ',UA.siNm,UA.sggNm,UA.emdNm,UA.streetNm,UA.detailNm) AS user_address,\n" +
                "\t\tStore.store_name,ol.order_request_store,ol.order_request_delivery,ol.discount,\n" +
                "        UP.payment_name,ol.order_date\n" +
                "FROM order_list ol\n" +
                "INNER JOIN Store ON Store.store_idx=ol.store_idx\n" +
                "INNER JOIN User ON ol.user_idx=User.user_idx\n" +
                "INNER JOIN User_Address UA ON UA.user_address_idx=ol.user_address_idx\n" +
                "INNER JOIN User_Payment UP ON UP.payment_idx=ol.payment_idx\n" +
                "WHERE ol.order_idx=?";

       int orderParam=orderIdx;
       return this.jdbcTemplate.queryForObject(getOrderListQuery,
               (rs,rowNum)->{
                 String user_name=rs.getString("User.user_name");
                 String user_address=rs.getString("user_address");
                 String store_name=rs.getString("Store.store_name");
                 String order_request_store=rs.getString("ol.order_request_store");
                 String order_request_delivery=rs.getString("ol.order_request_delivery");
                 int discount=rs.getInt("ol.discount");
                 String payment_name=rs.getString("UP.payment_name");
                 Timestamp order_date=rs.getTimestamp("ol.order_date");
                 GetOrderRes getOrderRes=new GetOrderRes(orderIdx,user_name,user_address,store_name,
                         order_request_store,order_request_delivery,discount,payment_name,order_date,orderDetailList);
                 return getOrderRes;

               },orderParam);

    }

    public List<String> getOptionList(int orderDetailIdx){
        String getOrderDetailOptionListQuery="SELECT mo.option_name\n" +
                "FROM order_detail od\n" +
                "INNER JOIN order_detail_option odo\n" +
                "ON od.order_detail_idx=odo.order_detail_idx \n" +
                "INNER JOIN menu_option mo \n" +
                "ON odo.option_idx=mo.option_idx\n" +
                "WHERE od.order_detail_idx=?";

        Object[] optionListParam={orderDetailIdx};

        return this.jdbcTemplate.query(getOrderDetailOptionListQuery,
                (rs,rowNum)-> {
                    String option_name=rs.getString("option_name");
                    return option_name;},
                optionListParam

        );
    }

}
