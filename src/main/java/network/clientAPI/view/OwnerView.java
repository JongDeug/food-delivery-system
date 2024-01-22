package network.clientAPI.view;

import network.clientAPI.Admin;
import network.clientAPI.Member;
import network.clientAPI.StoreManager;
import network.connect.ClientIO;
import network.dataPacket.Message;
import network.dataPacket.Packet;
import persistence.dto.OrderDTO;
import persistence.dto.ReviewDTO;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class OwnerView
{
    //관리자 관련 기능
    int inputNum;
    ClientIO clientIO = ClientIO.getInstance();
    StoreManager storeManager = new StoreManager(clientIO);
    Scanner sc = new Scanner(System.in);

    //관리자 기능 선택
    public void function() throws Exception
    {
        inputNum = sc.nextInt();
        switch (inputNum)
        {
//            case 1 : //회원가입
//                register();
//                break;
//            case 2 : //가게등록 신청
//                login();
//                break;
            case 1://가게등록 신청
                createStore();
                break;
            case 2 ://메뉴 등록신청
                createMenu();
                break;
            case 3 ://옵션 등록신청
                createOption();
                break;
            case 4 ://음식점 영업시간 설정
                modifyTime();
                break;
            case 5 : //고객의 주문 접수 및 승인 / 거절
                orderReceive();
                break;
            case 6 : //배달 상태 설정
                updateDeliveryState();
                break;
            case 7 : //리뷰와 별점 조회 및 답글
                replyReview();
                break;
            case 8 : //통계정보 조회
                statisticsCheck();
                break;
            case 9 ://주문 취소 승인/ 거절
                cancelOrder();
                break;
            case 0 : //뒤로가기
                //뒤로감..
            default:
                System.out.println("없는 번호입니다.");
                break;
        }
    }

    public void functionPrint() throws Exception
    {
        System.out.println("-------------------------------------------------");
        System.out.println("점주 기능");
//        System.out.println("1. 회원가입");
//        System.out.println("2. 로그인");
        System.out.println("1. 가게 등록 신청");
        System.out.println("2. 메뉴 등록 신청");
        System.out.println("3. 옵션 등록 신청");
        System.out.println("4. 음식점 영업시간 설정");
        System.out.println("5. 고객의 주문 접수 승인 거절");
        System.out.println("6. 배달 상태 설정");
        System.out.println("7. 리뷰와 별점 조회 및 답글");
        System.out.println("8. 통계정보 조회");
        System.out.println("9. 주문취소 승인/ 거절");
        System.out.println("0. 나가기");
        System.out.println("-------------------------------------------------");
        System.out.print("선택할 기능 : ");
    }

    //1.회원가입
//    public void register() throws Exception
//    {
//        System.out.println("회원가입하실 아이디와 비밀번호를 입력하세요 (구분자는 ,로 함) : ");
//        String inputInfo = sc.nextLine();
////        서버로 전송
////        if 프로토콜 == 승인
////        System.out.println("승인되었습니다.");
////        점주list에 등록
////        else
////        System.out.println("거절되었습니다");
//    }

    //2.로그인
//    public void login() throws Exception
//    {
//        System.out.println("아이디를 입력하세요");
//        String inputID = sc.nextLine();
//        System.out.println("비밀번호를 입력하세요");
//        String inputPw = sc.nextLine();
//
////        int[] result = Network.login(String.valueOf(userId),inputPw);
////        int[] user = new int[2];
//
////        if 아이디 비밀번호가 맞지않은경우
//        System.out.println("틀렸습니다.");
////        else if 로그인에 실패한 경우
//        System.out.println("로그인에 실패했습니다");
////        else //로그인에 성공한 경우
//        System.out.println("로그인에 성공했습니다");
////        user[0] = userId;
////        user[1] = result[0];
//    }
    //3.가게등록신청
    public void createStore() throws Exception
    {
        String temp = sc.nextLine();
        System.out.println("가게 정보를 입력하세요 각 구분자는 ,(가게이름, 설명, 주소, 가게전화, 이름, 개인전화, 영업시작, 영업끝)");
        String str = sc.nextLine();
        String[] storeInfo = str.split(",");
        Message message = storeManager.registerStore(storeInfo[0], storeInfo[1], storeInfo[2], storeInfo[3], storeInfo[6], storeInfo[7], clientIO.getPk());
        if (message.getType() == Packet.TYPE_CREATE)
        {
            if (message.getCode() == Packet.CODE_CREATE_STORE)
            {
                System.out.println("가게 등록 요청 성공");
            }
            else {
                System.out.println("실패");
            }
        }
        else {
            System.out.println("실패");
        }

//        storelist로 넘겨주기
//        if 가게등록 될경우
//        System.out.println("가게가 등록되었습니다");
//        아이디와 비밀번호를 받고 Storelist로 넘겨주기
//        else 등록실패
//        System.out.println("등록이 실패했습니다");
    }
    //4. 메뉴(메뉴옵션) 등록 신청
    public void createMenu() throws Exception
    {
        if (clientIO.getStorePK() == null)
        {
            System.out.println("가게가 없습니다.");
        }
        else {
            String tmp = sc.nextLine();
            System.out.println("메뉴 정보를 입력하세요 각 구분자는 '#' (ex 메뉴 카테고리# 메뉴 명# 가격# 선택 가능 옵션# 재고)");
            String str = sc.nextLine();
            String[] storeInfo = str.split("#");
            Message message = storeManager.registerMenu(clientIO.getStorePK(), storeInfo[0], storeInfo[1], storeInfo[2], storeInfo[3], storeInfo[4]);
            if (message.getType() == Packet.TYPE_CREATE)
            {
                if (message.getCode() == Packet.CODE_CREATE_MENU)
                {
                    System.out.println("메뉴 등록 요청 성공");
                }
                else {
                    System.out.println("실패");
                }
            }
            else {
                System.out.println("실패");
            }
        }


//        가게이름 추출
//                if 가게 없음
//        System.out.println("가게가 존재하지 않습니다");
//        else 가게 있음
//            if 메뉴 정보 가능
//        System.out.println("메뉴 등록에 성공하였습니다");
//        else 메뉴 등록 미흡
//        System.out.println("메뉴 등록에 실패하였습니다");
    }
    public void createOption() throws Exception
    {
        if (clientIO.getStorePK() == null)
        {
            System.out.println("가게가 없습니다.");
        }
        else {
            String tmp = sc.nextLine();
            System.out.println("옵션 정보를 입력하세요 각 구분자는 ,(옵션 명, 가격)");
            String str = sc.nextLine();
            String[] storeInfo = str.split(",");
            Message message = storeManager.registerMenuOption(clientIO.getStorePK(), storeInfo[0], storeInfo[1]);
            if (message.getType() == Packet.TYPE_CREATE)
            {
                if (message.getCode() == Packet.CODE_CREATE_MENU_OPTION)
                {
                    System.out.println("옵션 등록 요청 성공");
                }
                else {
                    System.out.println("실패");
                }
            }
            else {
                System.out.println("실패");
            }
        }
    }
    //5. 운영시간 변경
    public void modifyTime() throws Exception
    {
        if (clientIO.getStorePK() == null)
        {
            System.out.println("가게가 없습니다.");
        }
        else {
            System.out.println("운영 시간을 입력하세요 각 구분자는 ,(운영 시작시간, 종료시간) (HH:mm:ss)");
            String str = sc.next();
            String[] storeInfo = str.split(",");
            Message message = storeManager.updateBusinessHours(clientIO.getStorePK(), storeInfo[0], storeInfo[1]);
            if (message.getType() == Packet.TYPE_UPDATE)
            {
                if (message.getCode() == Packet.CODE_UPDATE_BUSINESS_HOURS)
                {
                    System.out.println("영업시간이 변경되었습니다.");
                }
                else {
                    System.out.println("실패");
                }
            }
            else {
                System.out.println("실패");
            }
        }
//        System.out.println("가게가 없습니다.");
//        System.out.print("수정할 시간을 입력하세요\n시작시간 :");
//        int inputStartTime = sc.nextInt();
//        System.out.print("종료시간 :");
//        int inputEndTime = sc.nextInt();
//        if 영업시간 변경 가능
//        System.out.println("영업시간이 변경되었습니다.");
//        변경된 영업시간 정보 전송
//        else 변경 실패
//        System.out.println("영업시간 변경에 실패하였습니다.");
    }
    //6. 주문 접수 승인 / 거절
    public void orderReceive() throws Exception
    {
        System.out.println("현재 접수 대기중인 주문을 출력합니다.");
        Message message = storeManager.responseReadOrder(clientIO.getStorePK());
        String str = message.getBody();
        String[] info = str.split("&");
        List<OrderDTO> orderDTOS = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String inputN;
        String permit = "거절";

        for (int i = 0; i<info.length; i++) {
            String[] tmp = info[i].split("#");
            OrderDTO orderDTO = new OrderDTO();
            orderDTO.setPk(Integer.parseInt(tmp[0]));
            orderDTO.setStoreFk(Integer.parseInt(tmp[1]));
            orderDTO.setMemberId(tmp[2]);
            orderDTO.setOrderMenu(tmp[3]);
            orderDTO.setOrderOptions(tmp[4]);
            orderDTO.setOrderPrice(Integer.parseInt(tmp[5]));
            orderDTO.setOrderStatus(tmp[6]);
            orderDTO.setOrderDateTime(LocalDateTime.parse(tmp[7], formatter));
            orderDTOS.add(orderDTO);
        }
        for (OrderDTO dtos : orderDTOS)
        {
            System.out.println("주문 번호 : " + dtos.getPk());
            System.out.println("가게 번호 : " + dtos.getStoreFk());
            System.out.println("고객 ID : " + dtos.getMemberId());
            System.out.println("메뉴 이름 : " + dtos.getOrderMenu());
            System.out.println("옵션 정보 : " + dtos.getOrderOptions());
            System.out.println("주문 가격 : " + dtos.getOrderPrice());
            System.out.println("주문 상태 : " + dtos.getOrderStatus());
            System.out.println("주문 시간 : " + dtos.getOrderDateTime());
            System.out.println("\n\n");   //  기타 주문정보들
        }
        //order list에서 뽑아옴
        System.out.print("어떤 주문을 선택하시겠습니까? : ");
//        inputNum = sc.nextInt();
        inputN = sc.next();
        System.out.println("선택된 주문을 승인하시겠습니까? (1)승인 (2)거절");
        inputNum = sc.nextInt();
        if (inputNum == 1)
            permit = "접수";

        message = storeManager.responseOrder(inputN, permit);
//        String res = message.getBody();
        System.out.println("처리됨");
    }
    //  배달 상태 설정
    public void updateDeliveryState() throws Exception
    {
        System.out.println("현재 배달중인 주문을 출력합니다.");
        Message message = storeManager.readOrderInfo(clientIO.getStorePK());
        String str = message.getBody();
        String[] info = str.split("&");
        List<OrderDTO> orderDTOS = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String inputN;

        for (int i = 0; i<info.length; i++) {
            String[] tmp = info[i].split("#");
            OrderDTO orderDTO = new OrderDTO();
            orderDTO.setPk(Integer.parseInt(tmp[0]));
            orderDTO.setStoreFk(Integer.parseInt(tmp[1]));
            orderDTO.setMemberId(tmp[2]);
            orderDTO.setOrderMenu(tmp[3]);
            orderDTO.setOrderOptions(tmp[4]);
            orderDTO.setOrderPrice(Integer.parseInt(tmp[5]));
            orderDTO.setOrderStatus(tmp[6]);
            orderDTO.setOrderDateTime(LocalDateTime.parse(tmp[7], formatter));
            orderDTOS.add(orderDTO);
        }
        for (OrderDTO dtos : orderDTOS)
        {
            System.out.println("주문 번호 : " + dtos.getPk());
            System.out.println("가게 번호 : " + dtos.getStoreFk());
            System.out.println("고객 ID : " + dtos.getMemberId());
            System.out.println("메뉴 이름 : " + dtos.getOrderMenu());
            System.out.println("옵션 정보 : " + dtos.getOrderOptions());
            System.out.println("주문 가격 : " + dtos.getOrderPrice());
            System.out.println("주문 상태 : " + dtos.getOrderStatus());
            System.out.println("주문 시간 : " + dtos.getOrderDateTime());
            System.out.println("\n\n");   //  기타 주문정보들
        }
        System.out.print("어떤 주문을 선택하시겠습니까? : ");
        inputN = sc.next();
        System.out.println("선택된 주문을 배달완료 처리합니다. (1)예 (2)아니오");
        inputNum = sc.nextInt();
        if (inputNum == 1)
        {
            message = storeManager.updateDeliveryFinish(inputN, clientIO.getStorePK());
            System.out.println("선택한 주문을 배달완료로 설정하였습니다.");
        }
    }
    //7. 리뷰와 별점 조회 및 답글
    public void replyReview() throws Exception
    {
        System.out.println("가게의 리뷰 리스트를 조회합니다");
        Message message = storeManager.readMyStoreReview(clientIO.getStorePK());
        String str = message.getBody();
        String[] info = str.split("&");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");


        List<ReviewDTO> reviewDTOS = new ArrayList<>();

        for (int i = 1;i<info.length; i++) {
            String[] tmp = info[i].split("#");
            ReviewDTO reviewDTO = new ReviewDTO();

            reviewDTO.setPk(Integer.parseInt(tmp[0]));
            reviewDTO.setOrderFk(Integer.parseInt(tmp[0]));
            reviewDTO.setMemberId(tmp[2]);
            reviewDTO.setReviewContent(tmp[3]);
            reviewDTO.setStarRating(tmp[4]);
            reviewDTO.setStoreManagerId(tmp[5]);
            reviewDTO.setReplyToReviewContent(tmp[6]);
            reviewDTOS.add(reviewDTO);
        }
        for (ReviewDTO dtos : reviewDTOS)
        {
            System.out.println("리뷰 번호 : " + dtos.getPk());
            System.out.println("주문 번호 : " + dtos.getOrderFk());
            System.out.println("고객 ID : " + dtos.getMemberId());
            System.out.println("리뷰 내용 : " + dtos.getReviewContent());
            System.out.println("고객 평점 : " + dtos.getStarRating());
            System.out.println("점주 답글 : " + dtos.getReplyToReviewContent());
            System.out.println("\n\n");   //  기타 리뷰정보들
        }
        System.out.print("리뷰에 답글을 다시겠습니까? (1)네 (2)아니요 : ");
        inputNum = sc.nextInt();
        if(inputNum == 1)
        {

            System.out.print("어떤 리뷰를 선택하시겠습니까? : ");
            String inputReviewNum = sc.next();
            System.out.print("작성하실 답글을 입력하세요 :");
            String review = sc.next();
            message = storeManager.registerReplyToReview(inputReviewNum, clientIO.getId(), review);
//            if 답글등록 성공
            System.out.println(message.getBody());
//            else 답글등록 실패
//            System.out.println("답글 등록에 실패했습니다.");
        }
        else if (inputNum == 2)
        {
            System.out.println("종료합니다.");
            //종료
        }
    }
    //8. 통계정보 (메뉴별 전체 주문건수 및 매출) 조회
    public void statisticsCheck() throws Exception
    {
        Message message = storeManager.readStatisticInfo(clientIO.getStorePK());
        String str = message.getBody();
        String[] info = str.split("&");
        for (int i = 0;i<info.length; i++) {
            String[] tmp = info[i].split("#");

            System.out.println("메뉴이름 : " + tmp[0]);
            System.out.println("총 매출액 : " + tmp[1] + " 원");
            System.out.println("총 판매량 : " + tmp[2] + " 건");
        }
//        if !가게ID
//        System.out.println("가게가 없습니다.");
////        else 가게ID 있음
//        System.out.println("가계의 메뉴별 주문건수 및 매출을 조회합니다");
//        orderlist 출력
    }
    //9. 주문취소 승인 / 거절   //  이거는 정상적으로 주문 취소 요청이 된 리스트들만 있다고 가정함
    public void cancelOrder() throws Exception
    {
        System.out.println("<취소신청내역>");
//        canel list 출력
        Message message = storeManager.responseReadCancleOrder(clientIO.getStorePK());  //  수정되었다고 가정
        String str = message.getBody();
        String[] info = str.split("&");
        List<OrderDTO> orderDTOS = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String inputN;
        String permit = "거절";

        for (int i = 0; i<info.length; i++) {
            String[] tmp = info[i].split("#");
            OrderDTO orderDTO = new OrderDTO();
            orderDTO.setPk(Integer.parseInt(tmp[0]));
            orderDTO.setStoreFk(Integer.parseInt(tmp[1]));
            orderDTO.setMemberId(tmp[2]);
            orderDTO.setOrderMenu(tmp[3]);
            orderDTO.setOrderOptions(tmp[4]);
            orderDTO.setOrderPrice(Integer.parseInt(tmp[5]));
            orderDTO.setOrderStatus(tmp[6]);
            orderDTO.setOrderDateTime(LocalDateTime.parse(tmp[7], formatter));
            orderDTOS.add(orderDTO);
        }
        for (OrderDTO dtos : orderDTOS)
        {
            System.out.println("주문 번호 : " + dtos.getPk());
            System.out.println("가게 번호 : " + dtos.getStoreFk());
            System.out.println("고객 ID : " + dtos.getMemberId());
            System.out.println("메뉴 이름 : " + dtos.getOrderMenu());
            System.out.println("옵션 정보 : " + dtos.getOrderOptions());
            System.out.println("주문 가격 : " + dtos.getOrderPrice());
            System.out.println("주문 상태 : " + dtos.getOrderStatus());
            System.out.println("주문 시간 : " + dtos.getOrderDateTime());
            System.out.println("\n\n");   //  기타 주문정보들
        }
        System.out.print("어떤 주문을 선택하시겠습니까? : ");
        inputN = sc.next();
        System.out.println("선택된 취소 요청을 승인하시겠습니까? (1)승인 (2)거절");
        inputNum = sc.nextInt();
        if (inputNum == 1)
            permit = "접수";

        message = storeManager.responseCancleOrder(inputN, permit);
//        String res = message.getBody();
        System.out.println("처리됨");
    }
//    sc.close();
}
