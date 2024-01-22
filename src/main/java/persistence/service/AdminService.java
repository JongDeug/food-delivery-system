package persistence.service;

import persistence.dao.*;
import persistence.dto.*;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class AdminService extends CommonService {


    private final MemberDAO memberDAO = MemberDAO.getInstance();
    private final OrderDAO orderDAO = OrderDAO.getInstance();

    /**
     * 조회
     */
/*    // 선택한 가게 manager 가져오기
    public MemberDTO readStoreManagerInfo(String storePk) {
        int pk = Integer.parseInt(storePk);
        int storeManagerFk;
        MemberDTO memberDTO = null;
        // 점주 정보만 따로 빼서 보내줘야할 듯.
        // 매장의 점주 정보를 받아오자!
        // 조회 쿼리
        storeManagerFk = storeDAO.readStoreManagerInfo(pk);
        memberDTO = memberDAO.readMyAccountInfo(storeManagerFk);
        return memberDTO;
    }*/

    public List<MemberDTO> readStoreManagerInfo() {
        List<MemberDTO> memberDTOS = null;
        // 조회 쿼리
        memberDTOS = memberDAO.readStoreManagerInfo();
        return memberDTOS;
    }

    public List<MemberDTO> readClientInfo() {
        List<MemberDTO> memberDTOS = null;
        // 조회 쿼리
        memberDTOS = memberDAO.readClientInfo();
        return memberDTOS;
    }

    public StoreStatisticDTO readStatisticInfo(String storeFk) {
        StoreStatisticDTO storeStatisticDTO = new StoreStatisticDTO();
        int fk = Integer.parseInt(storeFk);
        // 조회 쿼리
        List<OrderDTO> orderDTOS = orderDAO.readOrderInfoAll(fk);
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime day = now.minus(1, ChronoUnit.DAYS);
        LocalDateTime week = now.minus(7, ChronoUnit.DAYS);
        LocalDateTime month = now.minus(1, ChronoUnit.MONTHS);
        LocalDateTime year = now.minus(1, ChronoUnit.YEARS);
        // 하루 부터
        orderDTOS.stream().forEach(v -> {
            // 배달 완료인 경우에만
            if (v.getOrderStatus().equals("배달완료")) {
                // 시간이 같거나 이후면 저장
                if (v.getOrderDateTime().isAfter(day) && v.getOrderDateTime().isEqual(day)) {
                    // 수량 더하기
                    int numSum = storeStatisticDTO.getDayOrderNumber() + 1;
                    storeStatisticDTO.setDayOrderNumber(numSum);

                    // 매출 더하기
                    int salesSum = storeStatisticDTO.getDaySales() + v.getOrderPrice();
                    storeStatisticDTO.setDaySales(salesSum);
                }

                if (v.getOrderDateTime().isAfter(week) && v.getOrderDateTime().isEqual(week)) {
                    // 수량 더하기
                    int numSum = storeStatisticDTO.getWeekOrderNumber() + 1;
                    storeStatisticDTO.setWeekOrderNumber(numSum);

                    // 매출 더하기
                    int salesSum = storeStatisticDTO.getWeekSales() + v.getOrderPrice();
                    storeStatisticDTO.setWeekSales(salesSum);
                }

                if (v.getOrderDateTime().isAfter(month) && v.getOrderDateTime().isEqual(month)) {
                    // 수량 더하기
                    int numSum = storeStatisticDTO.getMonthOrderNumber() + 1;
                    storeStatisticDTO.setMonthOrderNumber(numSum);

                    // 매출 더하기
                    int salesSum = storeStatisticDTO.getMonthSales() + v.getOrderPrice();
                    storeStatisticDTO.setMonthSales(salesSum);
                }

                if (v.getOrderDateTime().isAfter(year) && v.getOrderDateTime().isEqual(year)) {
                    // 수량 더하기
                    int numSum = storeStatisticDTO.getYearOrderNumber() + 1;
                    storeStatisticDTO.setYearOrderNumber(numSum);

                    // 매출 더하기
                    int salesSum = storeStatisticDTO.getYearSales() + v.getOrderPrice();
                    storeStatisticDTO.setYearSales(salesSum);
                }
            }
        });
        return storeStatisticDTO;
    }

    /**
     * 응답 조회
     */
    public List<StoreDTO> responseStoreRead(){
        return storeDAO.responseStoreRead();
    }

    public List<MemberDTO> responseStoreManagerRead(){
        return memberDAO.responseStoreManagerRead();
    }

    public List<MenuDTO> responseMenuRead(){
        return menuDAO.responseMenuRead();
    }

    public List<OptionDTO> responseMenuOptionRead(){
        return optionDAO.responseMenuOptionRead();
    }

    /**
     * 응답
     */
    public void responseStore(String index, String response) {
        int pk = Integer.parseInt(index);
        if (response.equals("접수")) {
            storeDAO.responseStore(pk, response);
        } else if (response.equals("거절")) {
            storeDAO.responseStore(pk, response);
        }
    }

    public void responseStoreManager(String index, String response) {
        int pk = Integer.parseInt(index);
        if (response.equals("접수")) {
            memberDAO.responseStoreManager(pk, response);
        } else if (response.equals("거절")) {
            memberDAO.responseStoreManager(pk, response);
        }
    }

    public void responseMenuOption(String index, String response) {
        int pk = Integer.parseInt(index);
        if (response.equals("접수")) {
            optionDAO.responseMenuOption(pk, response);
        } else if (response.equals("거절")) {
            optionDAO.responseMenuOption(pk, response);
        }
    }

    public void responseMenu(String index, String response) {
        int pk = Integer.parseInt(index);
        if (response.equals("접수")) {
            menuDAO.responseMenu(pk, response);
        } else if (response.equals("거절")) {
            menuDAO.responseMenu(pk, response);
        }
    }

//    public void permitRequestStore(int id) {
//        adminDAO.permitRequestStore(storeDTOs.get(id));
//    }
//
//    public void addRequestStoreList(StoreDTO store){
//        storeDTOs.add(store);
//    }
//
//    public void printRequestStoreList(){
//        AtomicInteger i = new AtomicInteger();
//        storeDTOs.stream().forEach(v -> {
//            System.out.println(i + " : " + v.toString());
//            i.getAndIncrement();
//        });
//    }
//
//    public List<StoreDTO> selectAll(){
//        List<StoreDTO> dtos = adminDAO.selectAll();
//        return dtos;
//    }
}
