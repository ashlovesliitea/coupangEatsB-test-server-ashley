package com.example.demo.src.order;

import com.example.demo.src.order.model.Details;
import com.example.demo.src.order.model.GetOrderRes;
import com.example.demo.src.order.model.OrderDetail;
import com.example.demo.src.order.model.PostOrderReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Repository
public class OrderDao {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){this.jdbcTemplate=new JdbcTemplate(dataSource);}

    public GetOrderRes getOrder(int orderIdx){
       List<OrderDetail>orderDetailList=getOrderDetailList(orderIdx);
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
                 int totalPrice=0;
                   for(OrderDetail orderDetail:orderDetailList){
                       totalPrice+=orderDetail.getOrder_detail_total() * orderDetail.getMenu_amount();
                   }
                   System.out.println("totalPrice = " + totalPrice);
                 totalPrice-=discount;
                 GetOrderRes getOrderRes=new GetOrderRes(orderIdx,user_name,user_address,store_name,
                         order_request_store,order_request_delivery,discount,totalPrice,payment_name,order_date,orderDetailList);
                 return getOrderRes;

               },orderParam);

    }

    public List<OrderDetail> getOrderDetailList(int orderIdx){
        //주문한 메뉴+옵션 조합 리스트 출력 (상세 주문내역)
        String getOrderedMenuListQuery="Select od.order_detail_idx,menu.menu_name,menu.menu_price,od.menu_amount " +
                "FROM order_detail od " +
                "INNER JOIN order_list ol ON od.order_idx=ol.order_idx " +
                "INNER JOIN menu ON od.menu_idx=menu.menu_idx " +
                "WHERE ol.order_idx = ?";

        int orderedMenuParam=orderIdx;

        List<Integer>orderedDetailIdxList=new ArrayList<>();
        List<Integer>orderedMenuAmountList=new ArrayList<>();
        List<Integer> orderedMenuPriceList=new ArrayList<>();
        List<String> orderedMenuList =this.jdbcTemplate.query(getOrderedMenuListQuery,
                        (rs,rowNum)->{
                            int idx=rs.getInt("order_detail_idx");
                            orderedDetailIdxList.add(idx);
                            String menuName=rs.getString("menu_name");
                            int menuAmount=rs.getInt("menu_amount");
                            int menu_price=rs.getInt("menu_price");
                            System.out.println("menu_price = " + menu_price);
                            orderedMenuPriceList.add(menu_price);
                            orderedMenuAmountList.add(menuAmount);
                            return menuName;
                        },orderedMenuParam);

        List<OrderDetail>orderDetailList=new ArrayList<>();
        int detailListLen=orderedDetailIdxList.size();

        for(int i=0;i<detailListLen;i++){

            String orderedMenu=orderedMenuList.get(i);
            int orderedAmount=orderedMenuAmountList.get(i);
            int orderDetailIdx=orderedDetailIdxList.get(i);
            Map<String,Integer>orderedOptionMap=getOptionList(orderDetailIdx);
            
            int orderDetailTotal=0;
            orderDetailTotal+=orderedMenuPriceList.get(i);
            List<String> orderedOptionList=new ArrayList<>();
            for(String key: orderedOptionMap.keySet()){
               orderDetailTotal+=orderedOptionMap.get(key);
               orderedOptionList.add(key);
            }
            OrderDetail orderDetail=new OrderDetail(orderedMenu,orderedAmount,orderedOptionList,orderDetailTotal);
            orderDetailList.add(orderDetail);

        }

        return orderDetailList;
    }

    public Map<String,Integer> getOptionList(int orderDetailIdx){
        //주문한 메뉴에 포함된 옵션 이름 리스트 출력
        String getOrderDetailOptionListQuery="SELECT mo.option_name,mo.option_additional_price\n" +
                "FROM order_detail od\n" +
                "INNER JOIN order_detail_option odo\n" +
                "ON od.order_detail_idx=odo.order_detail_idx \n" +
                "INNER JOIN menu_option mo \n" +
                "ON odo.option_idx=mo.option_idx\n" +
                "WHERE od.order_detail_idx=?";

        Object[] optionListParam={orderDetailIdx};

        Map<String,Integer> optionMap=new HashMap<>();

        this.jdbcTemplate.query(getOrderDetailOptionListQuery,
                (rs,rowNum)-> {
                    String option_name=rs.getString("option_name");
                    int option_additional_price=rs.getInt("option_additional_price");
                    optionMap.put(option_name,option_additional_price);
                    return 1;
                    },
                optionListParam

        );

        return optionMap;
    }

    public int createOrder(PostOrderReq postOrderReq) {
        int user_idx=postOrderReq.getUser_idx();
        int user_address_idx=postOrderReq.getUser_address_idx();
        int store_idx=postOrderReq.getStore_idx();
        String order_request_store=postOrderReq.getOrder_request_store();
        String order_request_delivery=postOrderReq.getOrder_request_delivery();
        int discount=postOrderReq.getDiscount();
        int payment_idx=postOrderReq.getPayment_idx();
        List<Details> order_detail_list=postOrderReq.getOrder_detail_list();

        String lastInsertedOrderQuery="select max(order_idx) from order_list";
        int lastInsertedOrderId=this.jdbcTemplate.queryForObject(lastInsertedOrderQuery,int.class);

        String createOrderQuery="insert into order_list(order_idx,user_idx,user_address_idx,store_idx,order_request_store,order_request_delivery,discount,payment_idx,order_date)" +
                " values(?,?,?,?,?,?,?,?,?)";
        Object[] createOrderParams={lastInsertedOrderId+1,user_idx,user_address_idx,store_idx,order_request_store,order_request_delivery,
        discount,payment_idx,new Timestamp(System.currentTimeMillis())};

        this.jdbcTemplate.update(createOrderQuery,createOrderParams);

        int order_idx=this.jdbcTemplate.queryForObject(lastInsertedOrderQuery,int.class);

        for(Details details:order_detail_list){
            String lastInsertedOrderDetailIdQuery="select max(order_detail_idx) from order_detail";
            int lastInsertedOrderDetailId=this.jdbcTemplate.queryForObject(lastInsertedOrderDetailIdQuery,int.class);

            int menu_idx=details.getMenu_idx();
            int menu_amount=details.getMenu_amount();
            String createOrderDetailQuery="insert into order_detail(order_detail_idx,order_idx,menu_idx,menu_amount) values(?,?,?,?)";
            Object[] createOrderDetailParams={lastInsertedOrderDetailId+1,order_idx,menu_idx,menu_amount};

            this.jdbcTemplate.update(createOrderDetailQuery,createOrderDetailParams);

            lastInsertedOrderDetailId=this.jdbcTemplate.queryForObject(lastInsertedOrderDetailIdQuery,int.class);
            List<Integer>option_list=details.getOption_list();

            for(int option:option_list){
                String lastInsertedOrderOptionQuery="select max(order_detail_option_idx) from order_detail_option";
                int lastInsertedOrderOptionId=this.jdbcTemplate.queryForObject(lastInsertedOrderOptionQuery,int.class);
                System.out.println("lastInsertedOrderOptionId = " + lastInsertedOrderOptionId);
                String createOrderOptionQuery="insert into order_detail_option(order_detail_option_idx,order_detail_idx,option_idx) values(?,?,?)";
                Object[] createOrderOptionParams={lastInsertedOrderOptionId+1,lastInsertedOrderDetailId,option};
                this.jdbcTemplate.update(createOrderOptionQuery,createOrderOptionParams);
            }



        }
        return order_idx;
    }
}
