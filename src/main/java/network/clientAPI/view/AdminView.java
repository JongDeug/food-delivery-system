package network.clientAPI.view;

import network.clientAPI.Admin;
import network.clientAPI.StoreManager;
import network.connect.ClientIO;
import network.dataPacket.Message;
import persistence.dto.*;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class AdminView {
    //관리자 관련 기능 수행
    Scanner sc = new Scanner(System.in);
    int inputNum;
    ClientIO clientIO = ClientIO.getInstance();
    Admin admin = new Admin(clientIO);
    public void function() throws Exception
    {
        inputNum = sc.nextInt();
        switch (inputNum)
        {
            case 1 : //점주 가입 승인 거절
                createOwner();
                break;
            case 2 :// 가게등록 승인 거절
                createStore();
                break;
            case 3 :// 메뉴등록 승인 거절
                createMenu();
                break;
            case 4 :// 옵션등록 승인 거절
                createOption();
                break;
            case 5 :// 통계정보 확인
                statisticsCheck();
                break;
            case 6 :// 전체 고객 조회
                viewAllClient();
                break;
            case 7 :// 전체 점주 조회
                viewAllShopkeeper();
                break;
            case 0 : //뒤로가기
                //뒤로가는거..
                break;
            default:
                break;
        }
    }



    public void functionPrint() throws Exception
    {
        System.out.println("-------------------------------------------------");
        System.out.println("관리자 기능");
        System.out.println("1. 점주가입 승인 거절");
        System.out.println("2. 가게등록 승인 거절");
        System.out.println("3. 메뉴등록 승인 거절");
        System.out.println("4. 옵션등록 승인 거절");
        System.out.println("5. 통계정보 조회");
        System.out.println("6. 전체 고객 조회");
        System.out.println("7. 전체 점주 조회");
        System.out.println("0. 뒤로가기");
        System.out.println("-------------------------------------------------");
        System.out.print("선택할 기능 : ");
    }

    //1.점주가입 승인거절
    public void createOwner() throws Exception
    {
        System.out.println("가입요청한 점주의 리스트를 불러옵니다");
        Message message = admin.responseReadStoreManager();
        String str = message.getBody();
        String[] info = str.split("&");

        //점주 리스트 불러오기

        List<MemberDTO> memberDTOS = new ArrayList<>();

        for (int i = 0; i<info.length; i++) {
            String[] tmp = info[i].split("#");

            MemberDTO memberDTO = new MemberDTO();
            memberDTO.setPk(Integer.parseInt(tmp[0]));
            memberDTO.setMemberId(tmp[1]);
            memberDTO.setMemberPassword(tmp[2]);
            memberDTO.setMemberName(tmp[3]);
            memberDTO.setMemberAge(Integer.parseInt(tmp[4]));
            memberDTO.setMemberTel(tmp[5]);
            memberDTO.setMemberRole(tmp[6]);
            memberDTOS.add(memberDTO);
        }
        for (MemberDTO dtos : memberDTOS)
        {
            System.out.println("점주 번호 : " + dtos.getPk());
            System.out.println("점주 이름 : " + dtos.getMemberName());
            System.out.println("점주 아이디 : " + dtos.getMemberId());
            System.out.println("점주 패스워드 : " + dtos.getMemberPassword());
            System.out.println("점주 이름 : " + dtos.getMemberId());
            System.out.println("점주 나이 : " + dtos.getMemberAge());
            System.out.println("점주 전화번호 : " + dtos.getMemberTel());

            System.out.println("\n\n");   //  기타 주문정보들
        }

        System.out.println("어떤 점주를 선택하시겠습니까?");
        String storeManagerPK = sc.next();
        //선택된 점주 불러오기
        System.out.println("위 점의 가입을 승인하시겠습니까? (1)네 (2)아니오 : ");
        inputNum = sc.nextInt();
        if(inputNum == 1)
        {
            message = admin.responseStoreManager(storeManagerPK, "접수");
            System.out.println("승인했습니다.");
            //점주정보 점주 db에 등록
            //점주에게 승인 확인 메세지 전송
        }
        else if(inputNum == 2)
        {
            message = admin.responseStoreManager(storeManagerPK, "거절");
            System.out.println("승인이 거절되었습니다.");
            //승인 거절 메세지 전송
        }
    }
    //2. 고객 가입 승인거절
    public void createCustomer() throws Exception
    {
        System.out.println("가입요청한 고객의 리스트를 불러옵니다");
        //손님 리스트 불러오기
        System.out.println("어떤 점주를 선택하시겠습니까?");
        inputNum = sc.nextInt();
        //선택된 손님 불러오기
        System.out.println("위 고객의 가입을 승인하시겠습니까? (1)네 (2)아니오");
        inputNum = sc.nextInt();
        if(inputNum == 1)
        {
            System.out.println("고객의 가입을 승인했습니다");
            //서버의 고객테이블에 저장
            //가입완료 메세지 손님에게 전송
        }
        else if(inputNum == 2)
        {
            System.out.println("고객 가입을 거절했습니다");
            //거절메세지 전송
        }
    }
    //3. 가게등록 승인 거절
    public void createStore() throws Exception
    {
        System.out.println("가게등록 신청 리스트를 불러옵니다");
        Message message = admin.responseReadStore();
        String str = message.getBody();
        String[] info = str.split("&");
        //가게 리스트 불러오기
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");

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

        System.out.println("어떤 가게를 선택하시겠습니까?");
        String storePK = sc.next();
        //선택된 가게 store list에서 검색
        System.out.println("위 가게의 등록을 승안히시겠습니까? (1)네 (2)아니오");
        inputNum = sc.nextInt();
        if(inputNum == 1)
        {
            message = admin.responseStore(storePK, "접수");
            System.out.println("가게의 등록을 승인했습니다");
            //서버의 store테이블에 저장
            //등록완료 메세지 점주에 전송
        }
        else if(inputNum == 2)
        {
            message = admin.responseStore(storePK, "거절");
            System.out.println("가게의 등록을 거절하였습니다");
            //점주에게 등록거절 메세지 전송
        }
    }
    //4. 메뉴등록 승인 거절
    public void createMenu() throws Exception
    {
        System.out.println("메뉴등록 신청 리스트를 불러옵니다");
        //메뉴등록신청ID 리스트 불러오기
        Message message = admin.responseReadMenu();
        String str = message.getBody();
        String[] info = str.split("&");

        List<MenuDTO> menuDTOS = new ArrayList<>();

        for (int i = 0; i<info.length; i++) {
            String[] tmp = info[i].split("#");
            MenuDTO menuDTO = new MenuDTO();
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

        System.out.println("어떤 메뉴를 선택하시겠습니까?");
        String menuPK = sc.next();
        //선택된 가게 menu list에서 검색
        System.out.println("위 가게의 메뉴를 등록을 승인하시겠습니까? (1)네 (2)아니오");
        inputNum = sc.nextInt();
        if(inputNum == 1)
        {
            message = admin.responseMenu(menuPK, "접수");
            System.out.println("가게의 메뉴 등록을 승인했습니다");
            //서버의 menu테이블에 저장
            //등록완료 메세지 점주에 전송
        }
        else if(inputNum == 2)
        {
            message = admin.responseMenu(menuPK, "거절");
            System.out.println("가게의 메뉴 등록을 거절하였습니다");
            //점주에게 등록거절 메세지 전송
        }
    }
    public void createOption() throws Exception
    {
        System.out.println("옵션등록 신청 리스트를 불러옵니다");
        Message message = admin.responseReadMenuOption();
        String str = message.getBody();
        String[] info = str.split("&");
        //option 리스트 불러오기

        List<OptionDTO> optionDTOS = new ArrayList<>();

        for (int i = 0; i<info.length; i++) {
            String[] tmp = info[i].split("#");
            OptionDTO optionDTO = new OptionDTO();
            optionDTO.setPk(Integer.parseInt(tmp[0]));
            optionDTO.setStoreFk(Integer.parseInt(tmp[1]));
            optionDTO.setOptionName(tmp[2]);
            optionDTO.setOptionPrice(Integer.parseInt(tmp[3]));
            optionDTOS.add(optionDTO);
        }
        for (OptionDTO dtos : optionDTOS)
        {
            System.out.println("가게 번호 : " + dtos.getStoreFk());
            System.out.println("옵션 번호 : " + dtos.getPk());
            System.out.println("옵션 이름 : " + dtos.getOptionName());
            System.out.println("옵션 가격 : " + dtos.getOptionPrice());
            System.out.println("\n\n");   //  기타 가게정보들
        }

        System.out.println("어떤 옵션을 선택하시겠습니까?");
        String optionPK = sc.next();
        //선택된 가게 store list에서 검색
        System.out.println("위 옵션의 등록을 승인히시겠습니까? (1)네 (2)아니오");
        inputNum = sc.nextInt();
        if(inputNum == 1)
        {
            message = admin.responseMenuOption(optionPK, "접수");
            System.out.println("옵션의 등록을 승인했습니다");
            //서버의 store테이블에 저장
            //등록완료 메세지 점주에 전송
        }
        else if(inputNum == 2)
        {
            message = admin.responseMenuOption(optionPK, "거절");
            System.out.println("옵션의 등록을 거절하였습니다");
            //점주에게 등록거절 메세지 전송
        }
    }
    //5. 통계정보(음식점 별 전체 주문건수 및 매출) 열람
    public void statisticsCheck() throws Exception
    {
        System.out.println("가게의 리스트를 불러옵니다");
        //음식점 별 전체 주문건수 및 매출 뽑아와서 출력
        Message message = admin.readStoreInfoAll();
        String str = message.getBody();
        String[] info = str.split("&");
        //가게 리스트 불러오기
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");

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
        System.out.println("어떤 가게를 선택하시겠습니까?");
        String storePK = sc.next();
        System.out.println("선택한 가게의 통계정보를 불러옵니다");
        message = admin.readStatisticInfo(storePK);
        str = message.getBody();
        String[] stInfo = str.split("#");
        System.out.println("일일 주문량 : " + stInfo[0]);
        System.out.println("주간 주문량 : " + stInfo[1]);
        System.out.println("월간 주문량 : " + stInfo[2]);
        System.out.println("연간 주문량 : " + stInfo[3]);
        System.out.println("일일 판매량 : " + stInfo[4]);
        System.out.println("주간 판매량 : " + stInfo[5]);
        System.out.println("월간 판매량 : " + stInfo[6]);
        System.out.println("연간 판매량 : " + stInfo[7]);
    }
    public void viewAllClient() throws Exception
    {
        System.out.println("전체 고객 리스트를 불러옵니다");
        Message message = admin.readClientInfo();
        String str = message.getBody();
        String[] info = str.split("&");
        for (int i = 0; i<info.length; i++) {
            String[] stInfo = info[i].split("#");
            System.out.println("고객 번호 : " + stInfo[0]);
            System.out.println("고객 ID : " + stInfo[1]);
            System.out.println("고객 PW : " + stInfo[2]);
            System.out.println("고객 이름 : " + stInfo[3]);
            System.out.println("고객 나이 : " + stInfo[4]);
            System.out.println("\n\n");
        }
    }
    public void viewAllShopkeeper() throws Exception
    {
        System.out.println("전체 점주 리스트를 불러옵니다");
        Message message = admin.readStoreManagerInfo();
        String str = message.getBody();
        String[] info = str.split("&");
        for (int i = 0; i<info.length; i++) {
            String[] stInfo = info[i].split("#");
            System.out.println("점주 번호 : " + stInfo[0]);
            System.out.println("점주 ID : " + stInfo[1]);
            System.out.println("점주 PW : " + stInfo[2]);
            System.out.println("점주 이름 : " + stInfo[3]);
            System.out.println("점주 나이 : " + stInfo[4]);
            System.out.println("점주 전화번호 : " + stInfo[5]);
            System.out.println("\n\n");
        }
    }
//    sc.close();
}
