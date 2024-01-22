package persistence.service;

import persistence.dao.*;
import persistence.dto.*;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class MemberService extends CommonService {
    private final OrderDAO orderDAO = OrderDAO.getInstance();
    private final ReviewDAO reviewDAO = ReviewDAO.getInstance();
    private final MemberDAO memberDAO = MemberDAO.getInstance();

    /**
     * 등록
     */
    public String registerSignUp(MemberDTO memberDTO) {
        String str = "";
        // 등록 쿼리
        // 등록 전 아이디 중복인지 확인
//        System.out.println("memberDTO = " + !memberDAO.checkDuplicate(memberDTO.getMemberId()));
        boolean check = memberDAO.checkDuplicate(memberDTO.getMemberId());
        if (!check) {
            memberDTO.setMemberRole("client");
            memberDAO.registerSignUp(memberDTO);
            str = "완료";
        } else {
            str = "아이디 중복";
        }
        return str;
    }

    public String registerOrder(OrderDTO orderDTO) {
        String str = "";
        int amount = menuDAO.menuAmount(orderDTO.getStoreFk(), orderDTO.getOrderMenu());
        if (amount > 0) {
            // 영업 시간 비교
            LocalTime start = storeDAO.readStore(orderDTO.getStoreFk()).getStoreStartHours();
            LocalTime end = storeDAO.readStore(orderDTO.getStoreFk()).getStoreEndHours();
            LocalTime now = LocalTime.now();

            if (start.isBefore(now) && end.isAfter(now)) {
                // 최종
                // 오더 메뉴 가격 처리 후
                int orderMenuPrice = menuDAO.menuPrice(orderDTO.getStoreFk(), orderDTO.getOrderMenu());

                String[] orderOptions = orderDTO.getOrderOptions().split(",");
                int orderOptionsPrice = 0;
                for (int i = 0; i < orderOptions.length; i++) {
                    orderOptionsPrice += optionDAO.optionPrice(orderDTO.getStoreFk(), orderOptions[i]);
                }
                int totalPrice = orderMenuPrice + orderOptionsPrice;

                orderDTO.setOrderPrice(totalPrice);
                orderDTO.setOrderStatus("접수대기");
                orderDTO.setOrderDateTime(LocalDateTime.now());
                orderDTO.setResponse("요청");

                orderDAO.registerOrder(orderDTO);
                str = "완료";
            } else {
                str = "영업 시간이 아님";
            }
        } else {
            str += "재료가 소진됨";
        }
        return str;
    }

    public String registerCancleOrder(int pk) {
        String str = "";
        OrderDTO orderDTO = orderDAO.readOrderInfo(pk);
        if (orderDTO.getOrderStatus().equals("배달중")) {
            str = "이미 배달중인 주문은 취소가 불가능함";
        } else if (orderDTO.getOrderStatus().equals("취소")) {
            str = "이미 주문을 취소함";
        } else if(orderDTO.getOrderStatus().equals("배달완료")){
            str = "이미 배달완료 했습니다만?";
        }else {
            orderDAO.registerCancleOrder(pk);
            str = "완료";
        }
        return str;
    }

    public String registerReviewAndStarRating(ReviewDTO reviewDTO) {
//        ReviewDTO receive = reviewDAO.readReviewFk(reviewDTO.getOrderFk());
        //
        String str = "";
//        if (receive != null) {
//            str = "이미 리뷰를 등록했습니다.";
//        } else {
            reviewDAO.registerReviewAndStarRating(reviewDTO);
            str = "리뷰 등록 완료";
//        }
        return str;
    }


    /**
     * 수정
     */
    // 먼저 값을 조회 후 무엇을 바꿀지 선택하고 그것만 바꾸고 나머지는 다시 집어넣으면됨.
    public void updateProfile(MemberDTO memberDTO) {
        memberDAO.updateProfile(memberDTO);
    }

    /**
     * 조회
     */
    public MemberDTO readMyAccountInfo(String memberPk) {
        int pk = Integer.parseInt(memberPk);
        MemberDTO memberDTO = null;
        // 조회 쿼리
        memberDTO = memberDAO.readMyAccountInfo(pk);
        return memberDTO;
    }

    public MemberDTO readMemberAccount(String memberId) {
        MemberDTO memberDTO = null;
        memberDTO = memberDAO.readAccount(memberId);
        return memberDTO;
    }

    public MemberDTO readStoreManagerAccount(String memberId) {
        MemberDTO memberDTO = null;
        memberDTO = memberDAO.readStoreManagerAccount(memberId);
        return memberDTO;
    }

    public MemberDTO readAdminAccount(String memberId) {
        MemberDTO memberDTO = null;
        memberDTO = memberDAO.readAdminAccount(memberId);
        return memberDTO;
    }

    public List<OrderDTO> readMyOrderInfo(String memberId) {
        List<OrderDTO> orderDTOS = null;
        // 조회 쿼리
        orderDTOS = memberDAO.readMyOrderInfo(memberId);
        return orderDTOS;
    }

    public List<OrderDTO> readMyOrderResInfo(String memberId) {
        List<OrderDTO> orderDTOS = null;
        // 조회 쿼리
        orderDTOS = memberDAO.readMyOrderResInfo(memberId);
        return orderDTOS;
    }

    // readStoreInfo와 같이 조회 해야함 한쌍임!
    public List<ReviewDTO> readStoreReviewInfo(String storePk) {
        int pk = Integer.parseInt(storePk);
        List<ReviewDTO> reviewDTOS = null;
        // 조회 쿼리
        reviewDTOS = reviewDAO.readStoreReview(pk);
        return reviewDTOS;
    }
}
