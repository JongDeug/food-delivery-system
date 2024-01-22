import lombok.Getter;
import network.connect.ServerIO;
import network.dataPacket.Message;
import network.dataPacket.Packet;
import persistence.dto.*;
import persistence.service.AdminService;
import persistence.service.CommonService;
import persistence.service.MemberService;
import persistence.service.StoreManagerService;

import java.io.IOException;
import java.net.Socket;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Getter
public class ServerThread extends Thread {

    private int ID;
    private ServerIO serverIO;
    private Message msg;
    private AdminService adminService = new AdminService();
    private MemberService memberService = new MemberService();
    private StoreManagerService storeManagerService = new StoreManagerService();
    private CommonService commonService = new CommonService();

    public ServerThread(Socket socket) throws IOException {
        this.ID = socket.getPort();
        this.serverIO = new ServerIO(socket);
    }

    @Override
    public void run() {
        while (true) {
            try {
                msg = serverIO.receive();
                handler(msg);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    // 이벤트 발생 시 동작하는 핸들러 함수
    public void handler(Message msg) throws IOException {
        byte type = msg.getType();
        switch (type) {
            case Packet.TYPE_LOGIN:
                serverIO.send(login(msg));
                break;
            case Packet.TYPE_CREATE:
                serverIO.send(create(msg));
                break;
            case Packet.TYPE_READ:
                serverIO.send(read(msg));
                break;
            case Packet.TYPE_UPDATE:
                serverIO.send(update(msg));
                break;
            case Packet.TYPE_RESPONSE:
                serverIO.send(response(msg));
                break;
            case Packet.TYPE_EXIT:
                Server.remove(ID);
                break;
        }
    }

    // 로그인 메시지 처리 함수
    public Message login(Message msg) {
        byte code = msg.getCode();
        Message sendMsg = new Message();
        String body = "";
        String sendBody = "";
        String[] result = {};
        switch (code) {
            case Packet.CODE_LOGIN_ADMIN:
                body = msg.getBody();
                result = filter(body);
                MemberDTO adminDTO = memberService.readAdminAccount(result[0]);
                if (adminDTO != null) {
                    // 아이디가 맞으면?
                    if (adminDTO.getMemberId().equals(result[0])) {
                        // 비밀번호가 맞으면?
                        if (adminDTO.getMemberPassword().equals(result[1])) {
                            sendMsg.setType(msg.getType());
                            sendMsg.setCode(Packet.CODE_LOGIN_SUCCESS);
                            return sendMsg;
                        } else {
                            sendMsg.setType(msg.getType());
                            sendMsg.setCode(Packet.CODE_LOGIN_NOTPWD);
                            return sendMsg;
                        }
                    } else {
                        sendMsg.setType(msg.getType());
                        sendMsg.setCode(Packet.CODE_LOGIN_FAIL);
                        return sendMsg;
                    }
                } else {
                    System.out.println("서버 에러");
                }
            case Packet.CODE_LOGIN_MEMBER:
                body = msg.getBody();
                result = filter(body);
                MemberDTO memberDTO = memberService.readMemberAccount(result[0]);
                if (memberDTO != null) {
                    // 아이디가 맞으면?
                    if (memberDTO.getMemberId().equals(result[0])) {
                        // 비밀번호가 맞으면?
                        if (memberDTO.getMemberPassword().equals(result[1])) {
                            sendBody = memberDTO.getPk() + "#" + memberDTO.getMemberId() + "#" + memberDTO.getMemberPassword() + "#" + memberDTO.getMemberName() + "#" + memberDTO.getMemberAge();
                            sendMsg = Message.makeMessage(msg.getType(), Packet.CODE_LOGIN_SUCCESS, sendBody);
                        } else {
                            sendMsg.setType(msg.getType());
                            sendMsg.setCode(Packet.CODE_LOGIN_NOTPWD);
                        }
                    } else {
                        sendMsg.setType(msg.getType());
                        sendMsg.setCode(Packet.CODE_LOGIN_FAIL);
                    }
                    return sendMsg;
                } else {
                    System.out.println("서버 에러");
                }
            case Packet.CODE_LOGIN_STORE_MANAGER:
                body = msg.getBody();
                result = filter(body);
                MemberDTO storeManagerDTO = memberService.readStoreManagerAccount(result[0]);
                if (storeManagerDTO != null) {
                    // 아이디가 맞으면?
                    System.out.println("storeManagerDTO.getMemberId() = " + storeManagerDTO.getMemberId());
                    if (storeManagerDTO.getMemberId().equals(result[0])) {
                        // 비밀번호가 맞으면?
                        if (storeManagerDTO.getMemberPassword().equals(result[1])) {
                            sendBody = storeManagerDTO.getPk() + "#" + storeManagerDTO.getMemberId() + "#" + storeManagerDTO.getMemberPassword() + "#" + storeManagerDTO.getMemberName() + "#" + storeManagerDTO.getMemberAge() + "#" + storeManagerDTO.getMemberTel();
                            List<StoreDTO> storeDTOS = storeManagerService.readMyStores(String.valueOf(storeManagerDTO.getPk()));
                            for (StoreDTO storeDTO : storeDTOS) {
                                String startTime = storeDTO.getStoreStartHours().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
                                String endTime = storeDTO.getStoreEndHours().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
                                sendBody += "&" + storeDTO.getPk() + "#" + storeDTO.getStoreName() + "#" + storeDTO.getStoreIntro() + "#" + storeDTO.getStoreAddress() + "#" + storeDTO.getStoreTel() + "#" + startTime + "#" + endTime + "#" + storeDTO.getStoreManagerFk();
                            }
                            sendMsg = Message.makeMessage(msg.getType(), Packet.CODE_LOGIN_SUCCESS, sendBody);
                        } else {
                            sendMsg.setType(msg.getType());
                            sendMsg.setCode(Packet.CODE_LOGIN_NOTPWD);
                        }
                    } else {
                        sendMsg.setType(msg.getType());
                        sendMsg.setCode(Packet.CODE_LOGIN_FAIL);
                    }
                    return sendMsg;
                } else {
                    System.out.println("서버 에러");
                }
        }
        return sendMsg;
    }

    // 생성 요청 메시지 처리 함수
    public Message create(Message msg) throws IOException {
        byte code = msg.getCode();
        Message sendMsg = new Message();
        String body = "";
        String[] result = {};
        switch (code) {
            case Packet.CODE_CREATE_STORE:
                body = msg.getBody();
                result = filter(body);
                StoreDTO storeDTO = new StoreDTO();
                storeDTO.setStoreName(result[0]);
                storeDTO.setStoreIntro(result[1]);
                storeDTO.setStoreAddress(result[2]);
                storeDTO.setStoreTel(result[3]);
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
                LocalTime startTime = LocalTime.parse(result[4], formatter);
                storeDTO.setStoreStartHours(startTime);
                LocalTime endTime = LocalTime.parse(result[5], formatter);
                storeDTO.setStoreEndHours(endTime);
                storeDTO.setStoreManagerFk(Integer.parseInt(result[6]));
                storeManagerService.registerStore(storeDTO);
                sendMsg.setType(msg.getType());
                sendMsg.setCode(msg.getCode());
                return sendMsg;
            case Packet.CODE_CREATE_MENU:
                body = msg.getBody();
                result = filter(body);
                MenuDTO menuDTO = new MenuDTO();
                menuDTO.setStoreFk(Integer.parseInt(result[0]));
                menuDTO.setMenuCategory(result[1]);
                menuDTO.setMenuName(result[2]);
                menuDTO.setMenuPrice(Integer.parseInt(result[3]));
                menuDTO.setMenuAvailableOption(result[4]);
                menuDTO.setMenuAmount(Integer.parseInt(result[5]));
                storeManagerService.registerMenu(menuDTO);
                sendMsg.setType(msg.getType());
                sendMsg.setCode(msg.getCode());
                return sendMsg;
            case Packet.CODE_CREATE_MENU_OPTION:
                body = msg.getBody();
                result = filter(body);
                OptionDTO optionDTO = new OptionDTO();
                optionDTO.setStoreFk(Integer.parseInt(result[0]));
                optionDTO.setOptionName(result[1]);
                optionDTO.setOptionPrice(Integer.parseInt(result[2]));
                storeManagerService.registerMenuOption(optionDTO);
                sendMsg.setType(msg.getType());
                sendMsg.setCode(msg.getCode());
                return sendMsg;
            case Packet.CODE_CREATE_REPLY_TO_REVIEW:
                body = msg.getBody();
                result = filter(body);
                ReviewDTO replyReview = new ReviewDTO();
                replyReview.setPk(Integer.parseInt(result[0]));
                replyReview.setStoreManagerId(result[1]);
                replyReview.setReplyToReviewContent(result[2]);
                String replyMessage = storeManagerService.registerReplyToReview(replyReview);
                sendMsg = Message.makeMessage(msg.getType(), msg.getCode(), replyMessage);
                return sendMsg;
            case Packet.CODE_CREATE_SIGNUP_FOR_STORE_MANAGER:
                body = msg.getBody();
                result = filter(body);
                MemberDTO storeManagerDTO = new MemberDTO();
                storeManagerDTO.setMemberId(result[0]);
                storeManagerDTO.setMemberPassword(result[1]);
                storeManagerDTO.setMemberName(result[2]);
                storeManagerDTO.setMemberAge(Integer.parseInt(result[3]));
                storeManagerDTO.setMemberTel(result[4]);
                String returnManagerSignup = storeManagerService.registerSignUp(storeManagerDTO);
                // returnManagerSignup 종류 : 1. 완료 / 2. 아이디 중복
                sendMsg = Message.makeMessage(msg.getType(), msg.getCode(), returnManagerSignup);
                return sendMsg;
            case Packet.CODE_CREATE_SIGNUP_FOR_MEMBER:
                body = msg.getBody();
                result = filter(body);
                MemberDTO memberDTO = new MemberDTO();
                memberDTO.setMemberId(result[0]);
                memberDTO.setMemberPassword(result[1]);
                memberDTO.setMemberName(result[2]);
                memberDTO.setMemberAge(Integer.parseInt(result[3]));
                String returnMemberSignup = memberService.registerSignUp(memberDTO);
                // returnMemberSignup 종류 : 1. 완료 / 2. 아이디 중복
                sendMsg = Message.makeMessage(msg.getType(), msg.getCode(), returnMemberSignup);
                return sendMsg;
            case Packet.CODE_CREATE_ORDER:
                body = msg.getBody();
                result = filter(body);
                OrderDTO orderDTO = new OrderDTO();
                orderDTO.setStoreFk(Integer.parseInt(result[0]));
                orderDTO.setMemberId(result[1]);
                orderDTO.setOrderMenu(result[2]);
                orderDTO.setOrderOptions(result[3]);
                // String
                String res = memberService.registerOrder(orderDTO);
                // res 종류 : 1. 완료 / 2. 영업 시간이 아님 / 3. 재료가 소진됨 / 2,3번이 붙어서 나올 수도 있음.
                sendMsg = Message.makeMessage(msg.getType(), msg.getCode(), res);
                return sendMsg;
            case Packet.CODE_CREATE_REVIEW:
                body = msg.getBody();
                result = filter(body);
                ReviewDTO reviewDTO = new ReviewDTO();
                reviewDTO.setOrderFk(Integer.parseInt(result[0]));
                reviewDTO.setMemberId(result[1]);
                reviewDTO.setReviewContent(result[2]);
                reviewDTO.setStarRating(result[3]);
                String returnReview = memberService.registerReviewAndStarRating(reviewDTO);
                // returnReview 종류 : 1. 이미 리뷰를 등록했습니다. / 2. 리뷰 등록 완료
                sendMsg.setCode(msg.getCode());
                sendMsg = Message.makeMessage(msg.getType(), msg.getCode(), returnReview);
                return sendMsg;
            case Packet.CODE_CREATE_CANCLE_ORDER:
                body = msg.getBody();
                result = filter(body);
                // String
                String res1 = memberService.registerCancleOrder(Integer.parseInt(result[0]));
                // res1 종류 : 1. 이미 배달중인 주문은 취소가 불가능함 / 2. 이미 주문을 취소함 / 3.완료
                sendMsg = Message.makeMessage(msg.getType(), msg.getCode(), res1);
                return sendMsg;
        }
        return sendMsg;
    }

    // 읽기 요청 메시지 처리 함수
    public Message read(Message msg) {
        byte code = msg.getCode();
        Message sendMsg = new Message();
        String sendBody = "";
        String body = "";
        String[] result = {};
        switch (code) {
            case Packet.CODE_READ_STORE_INFO:
                List<StoreDTO> storeDTOS = commonService.readStoreInfoAll();
                if (storeDTOS != null) {
                    for (StoreDTO storeDTO : storeDTOS) {
                        String startTime = storeDTO.getStoreStartHours().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
                        String endTime = storeDTO.getStoreEndHours().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
                        sendBody += storeDTO.getPk() + "#" + storeDTO.getStoreName() + "#" + storeDTO.getStoreAddress() + "#" + storeDTO.getStoreTel() + "#" + startTime + "#" + endTime + "#" + storeDTO.getStoreManagerFk() + "&";
                    }
                    sendMsg = Message.makeMessage(msg.getType(), msg.getCode(), sendBody);
                    return sendMsg;
                } else {
                    System.out.println("서버 에러");
                }
            case Packet.CODE_READ_STORE_MENU_INFO:
                body = msg.getBody();
                result = filter(body);
                List<MenuDTO> menuDTOS = commonService.readStoreMenuInfo(result[0]);
                if (menuDTOS != null) {
                    for (MenuDTO menuDTO : menuDTOS) {
                        sendBody += menuDTO.getPk() + "#" + menuDTO.getStoreFk() + "#" + menuDTO.getMenuCategory() + "#" + menuDTO.getMenuName() + "#" + menuDTO.getMenuPrice() + "#" + menuDTO.getMenuAvailableOption() + "#" + menuDTO.getMenuAmount() + "&";
                    }
                    sendMsg = Message.makeMessage(msg.getType(), msg.getCode(), sendBody);
                    return sendMsg;
                } else {
                    System.out.println("서버 에러");
                }
            case Packet.CODE_READ_STORE_MENU_OPTION_INFO:
                body = msg.getBody();
                result = filter(body);
                List<OptionDTO> optionDTOS = commonService.readStoreMenuOptionInfo(result[0]);
                if (optionDTOS != null) {
                    for (OptionDTO optionDTO : optionDTOS) {
                        sendBody += optionDTO.getPk() + "#" + optionDTO.getStoreFk() + "#" + optionDTO.getOptionName() + "#" + optionDTO.getOptionPrice() + "&";
                    }
                    sendMsg = Message.makeMessage(msg.getType(), msg.getCode(), sendBody);
                    return sendMsg;
                } else {
                    System.out.println("서버 에러");
                }
            case Packet.CODE_READ_ORDER_INFO:
                body = msg.getBody();
                result = filter(body);
                List<OrderDTO> orderDTOS = storeManagerService.readOrderInfoAll(result[0]);
                if (orderDTOS != null) {
                    for (OrderDTO orderDTO : orderDTOS) {
                        String orderTime = orderDTO.getOrderDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                        sendBody += orderDTO.getPk() + "#" + orderDTO.getStoreFk() + "#" + orderDTO.getMemberId() + "#" + orderDTO.getOrderMenu() + "#" + orderDTO.getOrderOptions() + "#" + orderDTO.getOrderPrice() + "#" + orderDTO.getOrderStatus() + "#" + orderTime + "&";
                    }
                    sendMsg = Message.makeMessage(msg.getType(), msg.getCode(), sendBody);
                    return sendMsg;
                } else {
                    System.out.println("서버 에러");
                }
            case Packet.CODE_READ_STORE_STATISTIC_INFO_FOR_ADMIN:
                body = msg.getBody();
                result = filter(body);
                StoreStatisticDTO storeStatisticDTO = storeManagerService.readStatisticInfo(result[0]);
                if (storeStatisticDTO != null) {
                    sendBody = storeStatisticDTO.getDayOrderNumber() + "#" + storeStatisticDTO.getWeekOrderNumber() + "#" + storeStatisticDTO.getMonthOrderNumber() + "#" + storeStatisticDTO.getYearOrderNumber() + "#" + storeStatisticDTO.getDaySales() + "#" + storeStatisticDTO.getWeekSales() + "#" + storeStatisticDTO.getMonthSales() + "#" + storeStatisticDTO.getYearSales();
                    sendMsg = Message.makeMessage(msg.getType(), msg.getCode(), sendBody);
                    return sendMsg;
                } else {
                    System.out.println("서버 에러");
                }
            case Packet.CODE_READ_STORE_STATISTIC_INFO_FOR_STORE_MANAGER:
                body = msg.getBody();
                result = filter(body);
                // 메뉴 이름 땡겨오기, 메뉴 프라이스
                List<MenuDTO> menuList = storeManagerService.readStoreMenuInfo(result[0]);
                if (menuList != null) {
                    for (MenuDTO menuDTO : menuList) {
                        int price = 0;
                        int amount = 0;
                        List<OrderDTO> orderDTOList = storeManagerService.readOrderInfoAll(result[0]);
                        for (OrderDTO orderDTO : orderDTOList) {
                            // 해당 매장 주문에 메뉴 이름 매칭
                            if (orderDTO.getOrderStatus().equals("배달완료")) {
                                if (menuDTO.getMenuName().equals(orderDTO.getOrderMenu())) {
                                    // 해당 메뉴 주문 건수와 팔린 가격
                                    price += menuDTO.getMenuPrice();
                                    amount += 1;
                                }
                            }
                        }
                        sendBody += menuDTO.getMenuName() + "#" + price + "#" + amount + "&";
                    }
                    sendMsg = Message.makeMessage(msg.getType(), msg.getCode(), sendBody);
                    return sendMsg;
                } else {
                    System.out.println("서버 에러");
                }
            case Packet.CODE_READ_MY_ACCOUNT_INFO:
                body = msg.getBody();
                result = filter(body);
                MemberDTO memberDTO = memberService.readMyAccountInfo(result[0]);
                if (memberDTO != null) {
                    sendBody = memberDTO.getPk() + "#" + memberDTO.getMemberId() + "#" + memberDTO.getMemberPassword() + "#" + memberDTO.getMemberName() + "#" + memberDTO.getMemberAge() + "#" + memberDTO.getMemberTel() + "#" + memberDTO.getMemberRole();
                    sendMsg = Message.makeMessage(msg.getType(), msg.getCode(), sendBody);
                    return sendMsg;
                } else {
                    System.out.println("서버 에러");
                }
            case Packet.CODE_READ_MY_ORDER_INFO:
                body = msg.getBody();
                result = filter(body);
                List<OrderDTO> myOrderList = memberService.readMyOrderInfo(result[0]);
                if (myOrderList != null) {
                    for (OrderDTO orderDTO : myOrderList) {
                        String orderTime = orderDTO.getOrderDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                        sendBody += orderDTO.getPk() + "#" + orderDTO.getStoreFk() + "#" + orderDTO.getMemberId() + "#" + orderDTO.getOrderMenu() + "#" + orderDTO.getOrderOptions() + "#" + orderDTO.getOrderPrice() + "#" + orderDTO.getOrderStatus() + "#" + orderTime + "&";
                    }
                    sendMsg = Message.makeMessage(msg.getType(), msg.getCode(), sendBody);
                    return sendMsg;
                } else {
                    System.out.println("서버 에러");
                }
            case Packet.CODE_READ_MY_ORDER_INFO_RES:
                body = msg.getBody();
                result = filter(body);
                List<OrderDTO> myOrderList1 = memberService.readMyOrderResInfo(result[0]);
                if (myOrderList1 != null) {
                    for (OrderDTO orderDTO : myOrderList1) {
                        String orderTime = orderDTO.getOrderDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                        sendBody += orderDTO.getPk() + "#" + orderDTO.getStoreFk() + "#" + orderDTO.getMemberId() + "#" + orderDTO.getOrderMenu() + "#" + orderDTO.getOrderOptions() + "#" + orderDTO.getOrderPrice() + "#" + orderDTO.getOrderStatus() + "#" + orderTime + "&";
                    }
                    sendMsg = Message.makeMessage(msg.getType(), msg.getCode(), sendBody);
                    return sendMsg;
                } else {
                    System.out.println("서버 에러");
                }
            case Packet.CODE_READ_STORE_INFO_FOR_CLIENT:
                body = msg.getBody();
                result = filter(body);
                StoreDTO storeDTO = memberService.readStoreInfo(result[0]);
                List<ReviewDTO> reviewDTOS = memberService.readStoreReviewInfo(result[0]);
                if (storeDTO != null && reviewDTOS != null) {
                    String startTime = storeDTO.getStoreStartHours().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
                    String endTime = storeDTO.getStoreEndHours().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
                    // [0] store 정보
                    sendBody = storeDTO.getPk() + "#" + storeDTO.getStoreName() + "#" + storeDTO.getStoreIntro() + "#" + storeDTO.getStoreAddress() + "#" + storeDTO.getStoreTel() + "#" + startTime + "#" + endTime + "#" + storeDTO.getStoreManagerFk() + "&";
                    // [1]부터 review 정보
                    for (ReviewDTO reviewDTO : reviewDTOS) {
                        sendBody += reviewDTO.getPk() + "#" + reviewDTO.getOrderFk() + "#" + reviewDTO.getMemberId() + "#" + reviewDTO.getReviewContent() + "#" + reviewDTO.getStarRating() + "#" + reviewDTO.getStoreManagerId() + "#" + reviewDTO.getReplyToReviewContent() + "&";
                    }
                    sendMsg = Message.makeMessage(msg.getType(), msg.getCode(), sendBody);
                    return sendMsg;
                } else {
                    System.out.println("서버 에러");
                }
            case Packet.CODE_READ_CLIENT_INFO:
                List<MemberDTO> memberDTOS = adminService.readClientInfo();
                if (memberDTOS != null) {
                    for (MemberDTO client : memberDTOS) {
                        sendBody += client.getPk() + "#" + client.getMemberId() + "#" + client.getMemberPassword() + "#" + client.getMemberName() + "#" + client.getMemberAge() + "&";
                    }
                    sendMsg = Message.makeMessage(msg.getType(), msg.getCode(), sendBody);
                    return sendMsg;
                } else {
                    System.out.println("서버 에러");
                }
            case Packet.CODE_READ_STORE_MANAGER_INFO:
                List<MemberDTO> storeManagerList = adminService.readStoreManagerInfo();
                if (storeManagerList != null) {
                    for (MemberDTO storeManager : storeManagerList) {
                        sendBody += storeManager.getPk() + "#" + storeManager.getMemberId() + "#" + storeManager.getMemberPassword() + "#" + storeManager.getMemberName() + "#" + storeManager.getMemberAge() + "#" + storeManager.getMemberTel() + "&";
                    }
                    sendMsg = Message.makeMessage(msg.getType(), msg.getCode(), sendBody);
                    return sendMsg;
                } else {
                    System.out.println("서버 에러");
                }
            case Packet.CODE_READ_RESPONSE_ORDER:
                body = msg.getBody();
                result = filter(body);
                List<OrderDTO> resOrderList = storeManagerService.responseOrderRead(Integer.parseInt(result[0]));
                if (resOrderList != null) {
                    for (OrderDTO orderDTO : resOrderList) {
                        String orderTime = orderDTO.getOrderDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                        sendBody += orderDTO.getPk() + "#" + orderDTO.getStoreFk() + "#" + orderDTO.getMemberId() + "#" + orderDTO.getOrderMenu() + "#" + orderDTO.getOrderOptions() + "#" + orderDTO.getOrderPrice() + "#" + orderDTO.getOrderStatus() + "#" + orderTime + "&";
                    }
                    sendMsg = Message.makeMessage(msg.getType(), msg.getCode(), sendBody);
                    return sendMsg;
                } else {
                    System.out.println("서버 에러");
                }
            case Packet.CODE_READ_RESPONSE_CANCLE_ORDER:
                body = msg.getBody();
                result = filter(body);
                List<OrderDTO> cancleOrderList = storeManagerService.responseCancleOrderRead(Integer.parseInt(result[0]));
                if (cancleOrderList != null) {
                    for (OrderDTO cancle : cancleOrderList) {
                        String orderTime = cancle.getOrderDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                        sendBody += cancle.getPk() + "#" + cancle.getStoreFk() + "#" + cancle.getMemberId() + "#" + cancle.getOrderMenu() + "#" + cancle.getOrderOptions() + "#" + cancle.getOrderPrice() + "#" + cancle.getOrderStatus() + "#" + orderTime + "&";
                    }
                    sendMsg = Message.makeMessage(msg.getType(), msg.getCode(), sendBody);
                    return sendMsg;
                } else {
                    System.out.println("서버 에러");
                }
            case Packet.CODE_READ_RESPONSE_STORE:
                List<StoreDTO> resStoreList = adminService.responseStoreRead();
                if (resStoreList != null) {
                    for (StoreDTO resStore : resStoreList) {
                        String start = resStore.getStoreStartHours().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
                        String end = resStore.getStoreEndHours().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
                        sendBody += resStore.getPk() + "#" + resStore.getStoreName() + "#" + resStore.getStoreAddress() + "#" + resStore.getStoreTel() + "#" + start + "#" + end + "#" + resStore.getStoreManagerFk() + "&";
                    }
                    sendMsg = Message.makeMessage(msg.getType(), msg.getCode(), sendBody);
                    return sendMsg;
                } else {
                    System.out.println("서버 에러");
                }
            case Packet.CODE_READ_RESPONSE_STORE_MANAGER:
                List<MemberDTO> storeManagers = adminService.responseStoreManagerRead();
                if (storeManagers != null) {
                    for (MemberDTO storeManager : storeManagers) {
                        sendBody += storeManager.getPk() + "#" + storeManager.getMemberId() + "#" + storeManager.getMemberPassword() + "#" + storeManager.getMemberName() + "#" + storeManager.getMemberAge() + "#" + storeManager.getMemberTel() + "#" + storeManager.getMemberRole() + "&";
                    }
                    sendMsg = Message.makeMessage(msg.getType(), msg.getCode(), sendBody);
                    return sendMsg;
                } else {
                    System.out.println("서버 에러");
                }
            case Packet.CODE_READ_RESPONSE_OPTION:
                List<OptionDTO> optionDTOList = adminService.responseMenuOptionRead();
                if (optionDTOList != null) {
                    for (OptionDTO optionDTO : optionDTOList) {
                        sendBody += optionDTO.getPk() + "#" + optionDTO.getStoreFk() + "#" + optionDTO.getOptionName() + "#" + optionDTO.getOptionPrice() + "&";
                    }
                    sendMsg = Message.makeMessage(msg.getType(), msg.getCode(), sendBody);
                    return sendMsg;
                } else {
                    System.out.println("서버 에러");
                }
            case Packet.CODE_READ_RESPONSE_MENU:
                List<MenuDTO> menuDTOList = adminService.responseMenuRead();
                if (menuDTOList != null) {
                    for (MenuDTO menuDTO : menuDTOList) {
                        sendBody += menuDTO.getPk() + "#" + menuDTO.getStoreFk() + "#" + menuDTO.getMenuCategory() + "#" + menuDTO.getMenuName() + "#" + menuDTO.getMenuPrice() + "#" + menuDTO.getMenuAvailableOption() + "#" + menuDTO.getMenuAmount() + "&";
                    }
                    sendMsg = Message.makeMessage(msg.getType(), msg.getCode(), sendBody);
                    return sendMsg;
                } else {
                    System.out.println("서버 에러");
                }
            case Packet.CODE_READ_MY_STORE_REVIEW:
                body = msg.getBody();
                result = filter(body);
                List<ReviewDTO> storeReview = memberService.readStoreReviewInfo(result[0]);
                if (storeReview != null) {
                    for (ReviewDTO reviewDTO : storeReview) {
                        sendBody += reviewDTO.getPk() + "#" + reviewDTO.getOrderFk() + "#" + reviewDTO.getMemberId() + "#" + reviewDTO.getReviewContent() + "#" + reviewDTO.getStarRating() + "#" + reviewDTO.getStoreManagerId() + "#" + reviewDTO.getReplyToReviewContent() + "&";
                    }
                    sendMsg = Message.makeMessage(msg.getType(), msg.getCode(), sendBody);
                    return sendMsg;
                } else {
                    System.out.println("서버 에러");
                }
        }
        return sendMsg;
    }

    // 업데이트 요청 메시지 처리 함수
    public Message update(Message msg) {
        byte code = msg.getCode();
        Message sendMsg = new Message();
        String sendBody = "";
        String body = "";
        String[] result = {};
        switch (code) {
            case Packet.CODE_UPDATE_BUSINESS_HOURS:
                body = msg.getBody();
                result = filter(body);
                storeManagerService.updateBusinessHours(result[0], result[1], result[2]);
                sendMsg.setType(msg.getType());
                sendMsg.setCode(msg.getCode());
                return sendMsg;
            case Packet.CODE_UPDATE_DELIVERY_FINISH:
                body = msg.getBody();
                result = filter(body);
                storeManagerService.updateDeliveryFinish(result[0], result[1]);
                sendMsg.setType(msg.getType());
                sendMsg.setCode(msg.getCode());
                return sendMsg;
            case Packet.CODE_UPDATE_PROFILE:
                body = msg.getBody();
                result = filter(body);
                MemberDTO memberDTO = new MemberDTO();
                memberDTO.setPk(Integer.parseInt(result[0]));
                memberDTO.setMemberPassword(result[1]);
                memberDTO.setMemberName(result[2]);
                memberDTO.setMemberAge(Integer.parseInt(result[3]));
                memberService.updateProfile(memberDTO);
                sendMsg.setType(msg.getType());
                sendMsg.setCode(msg.getCode());
                return sendMsg;
        }
        return msg;
    }

    // 응답(접수/거절) 요청 메시지 처리 함수
    public Message response(Message msg) {
        byte code = msg.getCode();
        Message sendMsg = new Message();
        String sendBody = "";
        String body = "";
        String[] result = {};
        switch (code) {
            case Packet.CODE_RESPONSE_ORDER:
                body = msg.getBody();
                result = filter(body);
                storeManagerService.responseOrder(result[0], result[1]);
                sendMsg.setType(msg.getType());
                sendMsg.setCode(msg.getCode());
                return sendMsg;
            case Packet.CODE_RESPONSE_ORDER_CANCLE:
                body = msg.getBody();
                result = filter(body);
                storeManagerService.responseCancleOrder(result[0], result[1]);
                sendMsg.setType(msg.getType());
                sendMsg.setCode(msg.getCode());
                return sendMsg;
            case Packet.CODE_RESPONSE_STORE:
                body = msg.getBody();
                result = filter(body);
                adminService.responseStore(result[0], result[1]);
                sendMsg.setType(msg.getType());
                sendMsg.setCode(msg.getCode());
                return sendMsg;
            case Packet.CODE_RESPONSE_MENU:
                body = msg.getBody();
                result = filter(body);
                adminService.responseMenu(result[0], result[1]);
                sendMsg.setType(msg.getType());
                sendMsg.setCode(msg.getCode());
                return sendMsg;
            case Packet.CODE_RESPONSE_OPTION:
                body = msg.getBody();
                result = filter(body);
                adminService.responseMenuOption(result[0], result[1]);
                sendMsg.setType(msg.getType());
                sendMsg.setCode(msg.getCode());
                return sendMsg;
            case Packet.CODE_RESPONSE_STORE_MANAGER_SIGNUP:
                body = msg.getBody();
                result = filter(body);
                adminService.responseStoreManager(result[0], result[1]);
                sendMsg.setType(msg.getType());
                sendMsg.setCode(msg.getCode());
                return sendMsg;
        }
        return sendMsg;
    }

    // body 구분자 처리 함수
    public String[] filter(String body) {
        String[] hashResult = {};
        String[] amp = body.split("&");
        for (int i = 0; i < amp.length; i++) {
            String ampResult = amp[i];
            hashResult = ampResult.split("#");
        }
        return hashResult;
    }
}
