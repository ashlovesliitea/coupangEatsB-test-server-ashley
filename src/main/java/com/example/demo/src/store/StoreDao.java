package com.example.demo.src.store;

import com.example.demo.src.store.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import static com.example.demo.utils.calculateDistanceByKilometer.*;

import javax.sql.DataSource;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class StoreDao {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public List<GetStoreRes> getStoresByCategory(int user_address_idx,String category){
        System.out.println("user_address_idx = " + user_address_idx);
        System.out.println("category = "+category);
        String StoreListByCateQuery="SELECT Store.store_idx,Store.store_lng,Store.store_lat\n" +
                "From Store\n" +
                "Inner join (SELECT sc.store_idx,c.category_name\n" +
                "\t\t\tFrom category c\n" +
                "            INNER JOIN store_cate sc \n" +
                "            ON c.category_id=sc.cate_idx) scc ON Store.store_idx=scc.store_idx\n" +
                "Where category_name= ? ";

        String findCateParam=category;
        Object[] findCateParams={category,user_address_idx,user_address_idx};
        List<StoreLocation> storeIdxList=this.jdbcTemplate.query(StoreListByCateQuery,
                (rs,rowNum)->new StoreLocation(
                   rs.getInt(1),
                   rs.getDouble(2),
                rs.getDouble(3))
                ,findCateParam);

        List<StoreDist> storeIdxFilteredByLocationList=getStoreListFilteredByLocation(storeIdxList,user_address_idx);
        List<GetStoreRes> storeListbyCate=new ArrayList<>();

        for(StoreDist storeDist:storeIdxFilteredByLocationList){
            GetStoreRes getStoreRes=getWholeStoreInfo(storeDist);
            storeListbyCate.add(getStoreRes);

        }
        return storeListbyCate;

    }

    public List<GetStoreRes> getEveryStoreList(int user_address_idx) {
        String findEveryStoreQuery="SELECT Store.store_idx,Store.store_lng,Store.store_lat\n" +
                "From Store";

        List<StoreLocation> storeIdxList=this.jdbcTemplate.query(findEveryStoreQuery,
                (rs,rowNum)->new StoreLocation(
                        rs.getInt(1),
                        rs.getDouble(2),
                        rs.getDouble(3)));

        List <StoreDist> everyStoreListFilteredByLocation=getStoreListFilteredByLocation(storeIdxList,user_address_idx);

        List<GetStoreRes> everyStoreList=new ArrayList<>();
        for(StoreDist storeDist:everyStoreListFilteredByLocation){
            GetStoreRes getStoreRes=getWholeStoreInfo(storeDist);
            everyStoreList.add(getStoreRes);
        }
        return everyStoreList;
    }

    public List<GetStoreRes> getstoresByKeyword(int user_address_idx, String search_query) {
        //keyword= 메뉴 이름, 식당 이름
        String keyword="%"+search_query+"%";
        String getStoresByKeywordQuery="SELECT distinct Store.store_idx, Store.store_lng,Store.store_lat\n" +
                "\t\tFrom Store\n" +
                "\tInner join menu ON Store.store_idx=menu.store_idx\n" +
                "    Where Store.store_name Like ? OR menu.menu_name Like ?";

        Object[] keywordParams={keyword,keyword};
        List<StoreLocation> storeIdxList=this.jdbcTemplate.query(getStoresByKeywordQuery,
                (rs,rowNum)->new StoreLocation(
                        rs.getInt(1),
                        rs.getDouble(2),
                        rs.getDouble(3)),keywordParams);

        List <StoreDist> keywordStoreListFilteredByLocation=getStoreListFilteredByLocation(storeIdxList,user_address_idx);

        List<GetStoreRes> storeListFilteredByKeyword=new ArrayList<>();
        for(StoreDist storeDist:keywordStoreListFilteredByLocation){
            GetStoreRes getStoreRes=getWholeStoreInfo(storeDist);
            storeListFilteredByKeyword.add(getStoreRes);
        }
        return storeListFilteredByKeyword;
    }

    public List<StoreDist> getStoreListFilteredByLocation(List <StoreLocation> storeIdxList, int user_address_idx){
        List<StoreDist>storeIdxListFilteredByLocation=new ArrayList<>();
        for(StoreLocation storeLocation :storeIdxList){

            String findUsrLngQuery="select user_address_lng,user_address_lat from User_Address where user_address_idx=?";
            double[] usrLocation= this.jdbcTemplate.queryForObject(findUsrLngQuery,
                    (rs,rowNum)->{
                        double userLng=rs.getDouble(1);
                        double userLat=rs.getDouble(2);
                        double[] location={userLng,userLat};
                        return location;
                    },user_address_idx);
            double user_lng= usrLocation[0];
            double user_lat= usrLocation[1];

            double dist= distance(user_lat,user_lng, storeLocation.getStore_lat(), storeLocation.getStore_lng());
            System.out.println("dist = " + dist);

            if(dist<10){ //10km 반경에 존재하면 렌더링 리스트에 포함
                storeIdxListFilteredByLocation.add(new StoreDist(storeLocation.getStore_idx(),dist));
            }

        }
        return storeIdxListFilteredByLocation;
    }



    public GetStoreRes getWholeStoreInfo(StoreDist storeDist) {
        int store_idx= storeDist.getStore_idx();
        //식당에 포함된 카테고리 리스트
        String findCateQuery = "SELECT scc.category_name\n" +
                "From Store\n" +
                "Inner join (SELECT sc.store_idx,c.category_name\n" +
                "\t\t\tFrom category c\n" +
                "            INNER JOIN store_cate sc \n" +
                "            ON c.category_id=sc.cate_idx) scc ON Store.store_idx=scc.store_idx\n" +
                "Where Store.store_idx= ?";

        List<String> storeCateList = this.jdbcTemplate.query(
                findCateQuery,
                (rs, rowNum) -> {
                    String cname = rs.getString("category_name");
                    return cname;
                },
               store_idx
        );


        String findMenuCategoryListQuery = "SELECT DISTINCT menu_category_name\n" +
                "From menu\n" +
                "INNER JOIN (SELECT store_idx FROM Store) S ON menu.store_idx=S.store_idx\n" +
                "INNER JOIN menu_category mc ON  menu.menu_category_idx=mc.menu_category_idx\n" +
                "WHERE S.store_idx= ?";

        List<String> menuCategoryList = this.jdbcTemplate.query(findMenuCategoryListQuery,
                (rs, rowNum) -> rs.getString(1), store_idx);

        List<MenuCategory> menuListSortedByCategory = new ArrayList<>();

        for (String menuCateName : menuCategoryList) {
            //식당에서 설정한 메뉴 카테고리 별로 포함된 메뉴 리스트 출력
            String findMenuListQuery = "SELECT menu_idx,menu_name,menu_price,menu_details\n" +
                    "From menu\n" +
                    "INNER JOIN (SELECT store_idx FROM Store) S ON menu.store_idx=S.store_idx\n" +
                    "INNER JOIN menu_category mc ON  menu.menu_category_idx=mc.menu_category_idx\n" +
                    "WHERE S.store_idx= ? and mc.menu_category_name=?";
            Object[] menuParams = {storeDist.getStore_idx(), menuCateName};
            List<Menu> menuList = this.jdbcTemplate.query(findMenuListQuery,
                    (rs, rowNum) -> new Menu(
                            rs.getInt(1),
                            rs.getString(2),
                            rs.getInt(3),
                            rs.getString(4)
                    ), menuParams);

            menuListSortedByCategory.add(new MenuCategory(menuCateName, menuList));

        }
        //식당 대표이미지 URL 리스트 출력
        String findImageURLListQuery = "SELECT store_img_url FROM store_img Where store_idx=?";
        List<String> imageURLList = this.jdbcTemplate.query(findImageURLListQuery,
                (rs, rowNum) -> rs.getString(1), store_idx);

        String findStoreInfoQuery = "SELECT store_name,store_min_order,CONCAT_WS(' ',store_siNm,store_sggNm,store_emdNm,store_streetNm,store_detailNm) AS store_address,store_phone,\n" +
                "store_owner,store_reg_num,store_buisness_hour,store_info,store_owner_note,store_join_date,store_delivery_fee,store_lat,store_lng,store_min_prep_time,store_max_prep_time\n" +
                "From Store\n" +
                "Where Store.store_idx=? ";

        GetStoreRes getStoreRes = this.jdbcTemplate.queryForObject(findStoreInfoQuery,
                (rs, rowNum) -> {
                    String store_name = rs.getString("store_name");
                    int store_min_order = rs.getInt("store_min_order");
                    String store_address = rs.getString("store_address");
                    int store_phone = rs.getInt("store_phone");
                    String store_owner = rs.getString("store_owner");
                    String store_reg_num = rs.getString("store_reg_num");
                    String store_buisness_hour = rs.getString("store_buisness_hour");
                    String store_info = rs.getString("store_info");
                    String store_owner_note = rs.getString("store_owner_note");
                    Timestamp store_join_date = rs.getTimestamp("store_join_date");
                    int store_delivery_fee = rs.getInt("store_delivery_fee");
                    double store_lat=rs.getDouble("store_lat");
                    double store_lng=rs.getDouble("store_lng");
                    int store_min_prep_time=rs.getInt("store_min_prep_time");
                    int store_max_prep_time=rs.getInt("store_max_prep_time");
                    int delivery_time=estimateDeliveryTime(storeDist.getStore_user_dist());
                    int store_min_delivery_time =store_min_prep_time+delivery_time;
                    int store_max_delivery_time=store_max_prep_time+delivery_time;
                    GetStoreRes gSR = new GetStoreRes(store_idx, store_name, store_min_order, store_address, store_phone, store_owner,
                            store_reg_num, store_buisness_hour, store_info, store_owner_note, store_join_date, store_delivery_fee,
                            store_lat,store_lng,storeDist.getStore_user_dist(),store_min_prep_time,store_max_prep_time
                            ,store_min_delivery_time,store_max_delivery_time,storeCateList, imageURLList, menuListSortedByCategory);
                    return gSR;

                }, store_idx);


        return getStoreRes;
    }


    public List<GetStoreRes> getNewestStoreList(int user_address_idx) {

        String findNewestStoreQuery="SELECT Store.store_idx,Store.store_lng,Store.store_lat\n" +
                "From Store\n"
                +"Where Store.store_join_date BETWEEN date_add(now(),interval -1 month) and now();";

        List<StoreLocation> storeIdxList=this.jdbcTemplate.query(findNewestStoreQuery,
                (rs,rowNum)->new StoreLocation(
                        rs.getInt(1),
                        rs.getDouble(2),
                        rs.getDouble(3)));

        List <StoreDist> newestStoreListFilteredByLocation=getStoreListFilteredByLocation(storeIdxList,user_address_idx);

        List<GetStoreRes> newestStoreList=new ArrayList<>();
        for(StoreDist storeDist:newestStoreListFilteredByLocation){
            GetStoreRes getStoreRes=getWholeStoreInfo(storeDist);
            newestStoreList.add(getStoreRes);
        }
        return newestStoreList;
    }

    public GetStoreRes getStoreInfo(int user_address_idx, int store_idx) {
        String findStoreQuery="SELECT Store.store_idx,Store.store_lng,Store.store_lat\n" +
                "From Store Where store_idx=?";

        StoreLocation storeLocation=this.jdbcTemplate.queryForObject(findStoreQuery,
                (rs,rowNum)->new StoreLocation(
                        rs.getInt(1),
                        rs.getDouble(2),
                        rs.getDouble(3)),store_idx);

        String findUsrLngQuery="select user_address_lng,user_address_lat from User_Address where user_address_idx=?";
        double[] usrLocation= this.jdbcTemplate.queryForObject(findUsrLngQuery,
                (rs,rowNum)->{
                    double userLng=rs.getDouble(1);
                    double userLat=rs.getDouble(2);
                    double[] location={userLng,userLat};
                    return location;
                },user_address_idx);
        double user_lng= usrLocation[0];
        double user_lat= usrLocation[1];

        double dist= distance(user_lat,user_lng, storeLocation.getStore_lat(), storeLocation.getStore_lng());
        StoreDist storeDist=new StoreDist(store_idx,dist);
        GetStoreRes getStoreRes=getWholeStoreInfo(storeDist);
        return getStoreRes;
    }


    public GetMenuRes getMenuAndOption(int menu_idx) {
        String getMenuQuery="Select menu_name,menu_price,menu_details from menu where menu_idx=?";
         String getOptionQuery= "Select option_idx,option_name,option_additional_price From menu_option where menu_idx=?";
        int menuParams=menu_idx;

        Menu menu=this.jdbcTemplate.queryForObject(getMenuQuery,
                (rs,rowNum)->{
                     String menu_name=rs.getString(1);
                     int menu_price=rs.getInt(2);
                     String menu_details=rs.getString(3);
                     Menu menu1= new Menu(menu_idx,menu_name,menu_price,menu_details);

                      return menu1;

                },menuParams);

        List<Option> optionList= this.jdbcTemplate.query(getOptionQuery,
                (rs,rowNum)->{
                    int option_idx=rs.getInt(1);
                    String option_name=rs.getString(2);
                    int option_additional_price=rs.getInt(3);
                    Option option1=new Option(option_idx,option_name,option_additional_price);
                    return option1;
                },menuParams);
         GetMenuRes getMenuRes= new GetMenuRes(menu,optionList);
        return getMenuRes;
    }

    public List<Review> getReviewFromStores(int store_idx) {
        String findReviewIdxListQuery="Select review_idx from review Where store_idx=?";
        List <Integer> reviewIdxList= this.jdbcTemplate.query(findReviewIdxListQuery,
                (rs,rowNum)->rs.getInt(1),store_idx);

        List<Review> reviewListFromStores=new ArrayList<>();

        for(int reviewIdx:reviewIdxList){
        String findOrderIdxQuery="Select order_idx From review Where review_idx=?";
        int orderIdx=this.jdbcTemplate.queryForObject(findOrderIdxQuery,
                (rs,rowNum)->rs.getInt(1),reviewIdx);
            System.out.println("orderIdx = " + orderIdx);

        String findMenuListQuery="Select menu.menu_name from order_detail Inner Join menu ON order_detail.menu_idx=menu.menu_idx Where order_detail.order_idx=? ";
        List<String> menuList=this.jdbcTemplate.query(findMenuListQuery,
                (rs,rowNum)->rs.getString(1),orderIdx);


        //리뷰 사진이 존재하는지 여부 체크, 아니면 아래 list찾는 query에서 DataNotFoundException 발생
        String reviewImgExistsQuery="select exists(select review_img_idx from review_img where review_idx=?)";
        int reviewImgExists=this.jdbcTemplate.queryForObject(reviewImgExistsQuery,(rs,rowNum)->rs.getInt(1),reviewIdx);

        List<String> reviewImgList= new ArrayList<>();

        if(reviewImgExists==1){
        String findReviewImgListQuery="select review_img_url from review_img where review_idx=?";
        List<String> reviewImgList1=this.jdbcTemplate.query(findReviewImgListQuery,
                (rs,rowNum)->rs.getString(1),reviewIdx);
        reviewImgList.addAll(reviewImgList1);
        }

        String findReviewQuery="Select r.review_idx,r.user_idx,u.user_name,s.store_name,\n" +
                "\tr.order_idx,r.ratings,r.review_comment\n" +
                "From review r\n" +
                "inner join User u on r.user_idx=u.user_idx\n" +
                "inner join Store s on s.store_idx=r.store_idx\n" +
                "Where r.review_idx=?";

        Review review= this.jdbcTemplate.queryForObject(findReviewQuery,
                (rs,rowNum)->{
                    int review_idx=rs.getInt(1);
                    int user_idx=rs.getInt(2);
                    String user_name=rs.getString(3);
                    int storeIdx=store_idx;
                    String store_name=rs.getString(4);
                    int order_idx=rs.getInt(5);
                    int ratings=rs.getInt(6);
                    String review_comment=rs.getString(7);
                    System.out.println("review_comment = " + review_comment);
                    Review review1=new Review(review_idx,user_idx,user_name,storeIdx,store_name,order_idx,ratings,review_comment,reviewImgExists,reviewImgList,menuList);
                    return review1;
                },reviewIdx);

        reviewListFromStores.add(review);
        }
        return reviewListFromStores;

    }
}

