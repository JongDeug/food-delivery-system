package persistence.service;

import persistence.dao.*;
import persistence.dto.*;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class StoreManagerService extends CommonService {
    private final OrderDAO orderDAO = OrderDAO.getInstance();
    private final ReviewDAO reviewDAO = ReviewDAO.getInstance();
    private final MemberDAO memberDAO = MemberDAO.getInstance();

    /**
     * 등록
     */
    public String registerSignUp(MemberDTO memberDTO) {
        String str = "";
        if (!memberDAO.checkDuplicate(memberDTO.getMemberId())) {
            memberDTO.setMemberRole("storeManager");
            memberDTO.setResponse("요청");
            memberDAO.registerSignUp(memberDTO);
            str = "완료";
        } else {
            str = "아이디 중복";
        }
        return str;
    }

    public void registerStore(StoreDTO storeDTO) {
        storeDTO.setResponse("요청");
        storeDAO.registerStore(storeDTO);
    }

    public void registerMenuOption(OptionDTO optionDTO) {
        optionDTO.setResponse("요청");
        optionDAO.registerMenuOption(optionDTO);
    }

    public void registerMenu(MenuDTO menuDTO) {
        menuDTO.setResponse("요청");
        menuDAO.registerMenu(menuDTO);
    }

    public String registerReplyToReview(ReviewDTO reviewDTO) {
        String str = "";
//        ReviewDTO receive = reviewDAO.readReviewPk(reviewDTO.getPk());
//        if (receive.getReplyToReviewContent() != null) {
            reviewDAO.registerReplyToReview(reviewDTO);
            str = "완료";
//        } else {
//            str = "이미 답글을 등록했습니다.";
//        }
        return str;
    }

    /**
     * 조회
     */
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
                if (v.getOrderDateTime().isAfter(day) || v.getOrderDateTime().isEqual(day)) {
                    // 수량 더하기
                    int numSum = storeStatisticDTO.getDayOrderNumber() + 1;
                    storeStatisticDTO.setDayOrderNumber(numSum);

                    // 매출 더하기
                    int salesSum = storeStatisticDTO.getDaySales() + v.getOrderPrice();
                    storeStatisticDTO.setDaySales(salesSum);
                }

                if (v.getOrderDateTime().isAfter(week) || v.getOrderDateTime().isEqual(week)) {
                    // 수량 더하기
                    int numSum = storeStatisticDTO.getWeekOrderNumber() + 1;
                    storeStatisticDTO.setWeekOrderNumber(numSum);

                    // 매출 더하기
                    int salesSum = storeStatisticDTO.getWeekSales() + v.getOrderPrice();
                    storeStatisticDTO.setWeekSales(salesSum);
                }

                if (v.getOrderDateTime().isAfter(month) || v.getOrderDateTime().isEqual(month)) {
                    // 수량 더하기
                    int numSum = storeStatisticDTO.getMonthOrderNumber() + 1;
                    storeStatisticDTO.setMonthOrderNumber(numSum);

                    // 매출 더하기
                    int salesSum = storeStatisticDTO.getMonthSales() + v.getOrderPrice();
                    storeStatisticDTO.setMonthSales(salesSum);
                }

                if (v.getOrderDateTime().isAfter(year) || v.getOrderDateTime().isEqual(year)) {
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


    public List<OrderDTO> readOrderInfoAll(String storeFk) {
        int fk = Integer.parseInt(storeFk);
        List<OrderDTO> orderDTOS = null;
        // 조회 쿼리
        orderDTOS = orderDAO.readOrderInfoAll(fk);
        return orderDTOS;
    }

    public List<StoreDTO> readMyStores(String storeFk) {
        int fk = Integer.parseInt(storeFk);
        List<StoreDTO> storeDTOS = null;
        storeDTOS = storeDAO.readMyStores(fk);
        return storeDTOS;
    }


    /**
     * 수정
     */
    public void updateBusinessHours(String storePk, String storeStartHours, String storeEndHours) {
        // type 변경 후 수정
        int pk = Integer.parseInt(storePk);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        LocalTime startTime = LocalTime.parse(storeStartHours, formatter);
        LocalTime endTime = LocalTime.parse(storeEndHours, formatter);
        storeDAO.updateBusinessHours(pk, startTime, endTime);
    }

    public void updateDeliveryFinish(String orderPk, String storeFk) {
        int pkForOrder = Integer.parseInt(orderPk);
        int fk = Integer.parseInt(storeFk);
        orderDAO.updateStatus(pkForOrder, "배달중", "배달완료");
    }

    /**
     * 응답 조회
     */
    public List<OrderDTO> responseCancleOrderRead(int storeFk) {
        return orderDAO.responseCancleOrderRead(storeFk);
    }

    public List<OrderDTO> responseOrderRead(int storeFk) {
        return orderDAO.responseOrderRead(storeFk);
    }

    /**
     * 응답
     */
    public void responseOrder(String index, String response) {
        String str = "";
        int pk = Integer.parseInt(index);
        OrderDTO orderDTO = orderDAO.readOrderInfo(pk);

        if (response.equals("접수")) {
            orderDAO.responseOrder(pk, response);
            orderDAO.updateStatus(pk, "접수대기", "배달중");
            menuDAO.updateAmount(orderDTO.getOrderMenu(), "-");
        } else if (response.equals("거절")) {
            orderDAO.responseOrder(pk, response);
        }
    }

    public void responseCancleOrder(String index, String response) {
        int pk = Integer.parseInt(index);
        OrderDTO orderDTO = orderDAO.readOrderInfo(pk);
        if (response.equals("접수")) {
            // response를 통해서 취소 요청을 => 접수, 거절로 바꿔야함.
            orderDAO.responseCancleOrder(pk, response);
            orderDAO.updateStatus(orderDTO.getPk(), "접수대기", "취소");
            menuDAO.updateAmount(orderDTO.getOrderMenu(), "+");
        } else if (response.equals("거절")) {
            orderDAO.responseCancleOrder(pk, response);
        }
    }

}
