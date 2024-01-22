package network.clientAPI.view;

import network.clientAPI.Member;
import network.connect.ClientIO;
import network.dataPacket.Message;
import network.dataPacket.Packet;
import persistence.dto.*;

import javax.swing.text.html.Option;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CustomerView {
    int inputNum;
    Scanner sc = new Scanner(System.in);

    ClientIO clientIO = ClientIO.getInstance();
    Member member = new Member(clientIO);

    //관리자 기능 선택
    public void function() throws Exception
    {
        inputNum = sc.nextInt();
        switch (inputNum)
        {
            case 1 ://개인정보 및 비밀번호 수정
                modifyInformation();
                break;
            case 2 ://음식점 및 리뷰와 별점조회
                storeCheck();
                break;
            case 3 ://음식 주문
                orderMenu();
                break;
            case 4 : //주문 취소
                orderCancel();
                break;
            case 5 : //주문내역 조회
                orderCheck();
                break;
            case 6 : //리뷰와 별점 등록
                createReview();
                break;
            case 0 ://뒤로가기
                //뒤로가는거..
            default:
                System.out.println("없는 번호입니다.");
                break;
        }
    }


    public void functionPrint() throws Exception
    {
        System.out.println("-------------------------------------------------");
        System.out.println("고객 기능");
        System.out.println("1. 개인정보 및 비밀번호 수정");
        System.out.println("2. 음식점 및 리뷰와 별점 조회");
        System.out.println("3. 음식 주문");
        System.out.println("4. 주문 취소");
        System.out.println("5. 주문 내역 조회");
        System.out.println("6. 리뷰와 별점 등록");
        System.out.println("0. 뒤로가기");
        System.out.println("-------------------------------------------------");
        System.out.print("선택할 기능 : ");
    }

    //3. 개인정보 및 비밀번호 수정
    public void modifyInformation() throws Exception
    {
//        Message message = member.readMyAccountInfo(clientIO.getPk());   //  clientIO 안에 다있는데?

        System.out.println("ID : " + clientIO.getId());
        System.out.println("PW : " + clientIO.getPw());
        System.out.println("이름 : " + clientIO.getName());
        System.out.println("나이 : " + clientIO.getAge());
//        고객정보 불러오기
        System.out.print("바꿀 내용을 선택하시오 (1)비밀번호 (2)이름 (3)나이 : ");
        //CODE_READ_MY_ORDER_INFO
        inputNum = sc.nextInt();
        System.out.println("바꿀 내용을 입력하세요 : ");
        String inputInfo = sc.next();

        switch (inputNum)
        {
            case 1:
                clientIO.setPw(inputInfo);
                break;
            case 2:
                clientIO.setName(inputInfo);
                break;
            case 3:
                clientIO.setAge(Integer.parseInt(inputInfo));
                break;
        }
        Message message = member.updateProfile(clientIO.getPk(), clientIO.getPw(), clientIO.getName(), String.valueOf(clientIO.getAge()));

        if (message.getType() == Packet.TYPE_UPDATE)
        {
            if (message.getCode() == Packet.CODE_UPDATE_PROFILE)
            {
                System.out.println("개인정보 수정 성공");
            }
            else {
                System.out.println("실패");
            }
        }
        else {
            System.out.println("실패");
        }
//        if 승인
//            customer list 수정
//            else
//        System.out.println("실패하였습니다");

    }
    //4. 음식점 및 리뷰와 별점 조회
    public void storeCheck() throws Exception
    {
        System.out.println("음식점 정보를 출력합니다");
        Message message = member.readStoreInfoAll();
        String str = message.getBody();
        String[] info = str.split("&");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        String inputN;

        List<StoreDTO> storeDTOList = new ArrayList<>();

        for (int i = 0; i<info.length; i++) {
            String[] tmp = info[i].split("#");
            StoreDTO dto = new StoreDTO();
            dto.setPk(Integer.parseInt(tmp[0]));
            dto.setStoreName(tmp[1]);
            dto.setStoreAddress(tmp[2]);
            dto.setStoreTel(tmp[3]);
            dto.setStoreStartHours(LocalTime.parse(tmp[4], formatter));
            dto.setStoreEndHours(LocalTime.parse(tmp[5], formatter));
            dto.setStoreManagerFk(Integer.parseInt(tmp[6]));
            storeDTOList.add(dto);
        }
        for (StoreDTO dtos : storeDTOList)
        {
            System.out.println("가게 번호 : " + dtos.getPk());
            System.out.println("가게 이름 : " + dtos.getStoreName());
            System.out.println("가게 주소 : " + dtos.getStoreAddress());
            System.out.println("가게 전화번호 : " + dtos.getStoreTel());
            System.out.println("가게 운영 시작시간 : " + dtos.getStoreStartHours());
            System.out.println("가게 운영 종료시간 : " + dtos.getStoreEndHours());

            System.out.println("\n\n");   //  기타 가게정보들
        }
        System.out.print("별점을 조회할 음식점 번호를 입력하세요 : ");
        inputN = sc.next();
        System.out.println("음식점 리뷰를 출력합니다.");
        message = member.readStoreInfoForClient(inputN);    //  예외처리는 되는지 보고 만들자 실패만 만들면됨
        str = message.getBody();
        info = str.split("&");

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
    }
    //5. 음식 주문
    public void orderMenu() throws Exception
    {
        System.out.println("음식점 정보를 출력합니다");
        Message message = member.readStoreInfoAll();
        String str = message.getBody();
        String[] info = str.split("&");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        String inputN;
        String storePK;

        List<StoreDTO> storeDTOList = new ArrayList<>();

        for (int i = 0; i<info.length; i++) {
            StoreDTO dto = new StoreDTO();
            String[] tmp = info[i].split("#");
            dto.setPk(Integer.parseInt(tmp[0]));
            dto.setStoreName(tmp[1]);
            dto.setStoreAddress(tmp[2]);
            dto.setStoreTel(tmp[3]);
            dto.setStoreStartHours(LocalTime.parse(tmp[4], formatter));
            dto.setStoreEndHours(LocalTime.parse(tmp[5], formatter));
            dto.setStoreManagerFk(Integer.parseInt(tmp[6]));
            storeDTOList.add(dto);
        }
        for (StoreDTO dtos : storeDTOList)
        {
            System.out.println("가게 번호 : " + dtos.getPk());
            System.out.println("가게 이름 : " + dtos.getStoreName());
            System.out.println("가게 주소 : " + dtos.getStoreAddress());
            System.out.println("가게 전화번호 : " + dtos.getStoreTel());
            System.out.println("가게 운영 시작시간 : " + dtos.getStoreStartHours());
            System.out.println("가게 운영 종료시간 : " + dtos.getStoreEndHours());

            System.out.println("\n\n");   //  기타 가게정보들
        }
        System.out.println("주문할 음식점 번호를 입력하세요");
        storePK = sc.next();
        System.out.println("음식점 메뉴를 출력합니다.");
        message = member.readStoreMenuInfo(storePK);
        str = message.getBody();
        info = str.split("&");

        List<MenuDTO> menuDTOS = new ArrayList<>();

        for (int i = 0; i<info.length; i++) {
            MenuDTO menuDTO = new MenuDTO();
            String[] tmp = info[i].split("#");
            menuDTO.setPk(Integer.parseInt(tmp[0]));
            menuDTO.setStoreFk(Integer.parseInt(tmp[1]));
            menuDTO.setMenuCategory(tmp[2]);
            menuDTO.setMenuName(tmp[3]);
            menuDTO.setMenuPrice(Integer.parseInt(tmp[4]));
            menuDTO.setMenuAvailableOption(tmp[5]);
            menuDTO.setMenuAmount(Integer.parseInt(tmp[6]));
            menuDTOS.add(menuDTO);
        }
        for (MenuDTO dtos : menuDTOS)
        {
            System.out.println("메뉴 번호 : " + dtos.getPk());
            System.out.println("가게 번호 : " + dtos.getStoreFk());
            System.out.println("메뉴 카테고리 명 : " + dtos.getMenuCategory());
            System.out.println("메뉴 이름 : " + dtos.getMenuName());
            System.out.println("메뉴 가격 : " + dtos.getMenuPrice());
            System.out.println("가능 옵션 : " + dtos.getMenuAvailableOption());
            System.out.println("메뉴 재고 : " + dtos.getMenuAmount());
            System.out.println("\n\n");   //  기타 가게정보들
        }
        message = member.readStoreMenuOptionInfo(storePK);
        str = message.getBody();
        info = str.split("&");

        System.out.println("옵션을 출력합니다.");

        List<OptionDTO> optionDTOS = new ArrayList<>();

        for (int i = 0; i<info.length; i++) {
            OptionDTO optionDTO = new OptionDTO();
            String[] tmp = info[i].split("#");
            optionDTO.setPk(Integer.parseInt(tmp[0]));
            optionDTO.setOptionName(tmp[2]);
            optionDTO.setOptionPrice(Integer.parseInt(tmp[3]));
            optionDTOS.add(optionDTO);
        }
        for (OptionDTO dtos : optionDTOS)
        {
            System.out.println("옵션 번호 : " + dtos.getPk());
            System.out.println("옵션 이름 : " + dtos.getOptionName());
            System.out.println("옵션 가격 : " + dtos.getOptionPrice());
            System.out.println("\n\n");   //  기타 가게정보들
        }

        // 주문내역
//        else
        String temp = sc.nextLine();
        System.out.println("주문할 메뉴를 선택하여 주세요 (메뉴명으로 입력해주세요): ");
        String inputNum = sc.nextLine();
        System.out.println("메뉴의 옵션을 선택하세요 (옵션명으로 입력해주세요) : ");
        String optionInput = sc.nextLine();
//
//        for (MenuDTO dtos : menuDTOS)     //  옵션 예외처리 (사용자가 메뉴에 없는 옵션을 입력할 때)
//        {
//            if (dtos.getPk() == inputNum)
//            {
//
//            }
//        }



        message = member.registerOrder(storePK, clientIO.getId(), inputNum, optionInput);

//        if 주문성공
//        System.out.println("정상적으로 주문 신청이 완료 되었습니다");
        System.out.println(message.getBody());
//        else 주문실패 //
//        System.out.println("재고부족으로, 주문에 실패하였습니다.");
    }

    //6. 주문 취소
    public void orderCancel() throws Exception
    {
        System.out.println("주문 내역을 조회합니다");
        Message message = member.readMyOrderResInfo(clientIO.getId());
        String str = message.getBody();
        String[] info = str.split("&");
        List<OrderDTO> orderDTOS = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

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
            System.out.println("\n\n");   //  기타 가게정보들
        }
//        배달완료 제외 주문내역 출력
        System.out.println("취소할 주문의 번호를 입력하세요 : ");
//        inputNum = sc.nextInt();
        String inputN = sc.next();
        message = member.registerCancleOrder(inputN);
        if (message.getType() == Packet.TYPE_CREATE)
        {
            if (message.getCode() == Packet.CODE_CREATE_CANCLE_ORDER)
            {
                System.out.println(message.getBody());
            }
            else {
                System.out.println("실패");
            }
        }
        else {
            System.out.println("실패");
        }


//        if 주문취소 가능
//        else 배달중
//        System.out.println("주문 취소가 거절되었습니다");
    }
    //7. 주문내역 조회
    public void orderCheck() throws Exception
    {
        System.out.println("주문 내역을 조회합니다");
        Message message = member.readMyOrderInfo(clientIO.getId());
        String str = message.getBody();
        String[] info = str.split("&");
        List<OrderDTO> orderDTOS = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

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
            System.out.println("\n\n");   //  기타 가게정보들
        }
//        System.out.println("조회할 주문내역 번호를 입력하세요");

//        String inputN = sc.next();
//        조회할 주문내역 정하기 (기간별, 가게별, 별점별)
//        선택된 본인 주문내역 출력
    }
    //8. 리뷰와 별점 등록
    public void createReview() throws Exception
    {
        System.out.println("주문내역을 조회합니다");
//        배달완료된 || 리뷰등록안된 주문내역 출력
        Message message = member.readMyOrderInfo(clientIO.getId());
        String str = message.getBody();
        String[] info = str.split("&");
        List<OrderDTO> orderDTOS = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

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
            System.out.println("\n\n");   //  기타 가게정보들
        }
        System.out.println("리뷰등록할 주문을 선택하세요");
        String inputReviewNum = sc.next();
        System.out.println("리뷰를 입력하세요 : ");
        String review = sc.next();
        System.out.println("별점을 입력하세요");
        String inputRating = sc.next();
        message = member.registerReviewAndStarRating(inputReviewNum, clientIO.getId(), review, inputRating);

        if (message.getType() == Packet.TYPE_CREATE)
        {
            if (message.getCode() == Packet.CODE_CREATE_REVIEW)
            {
                System.out.println(message.getBody());
            }
            else {
                System.out.println("리뷰 등록에 실패하였습니다");
            }
        }
        else {
            System.out.println("리뷰 등록에 실패하였습니다");
        }
    }
}
