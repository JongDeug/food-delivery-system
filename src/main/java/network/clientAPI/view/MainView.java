package network.clientAPI.view;

import network.clientAPI.Admin;
import network.clientAPI.Crud;
import network.clientAPI.Member;
import network.clientAPI.StoreManager;
import network.connect.ClientIO;
import network.dataPacket.Message;
import network.dataPacket.Packet;

import java.util.Scanner;

public class MainView {
    int inputNum;
    Scanner sc = new Scanner(System.in);
    ClientIO clientIO = ClientIO.getInstance();
    Admin admin = new Admin(clientIO);
    Member member = new Member(clientIO);
    StoreManager storeManager = new StoreManager(clientIO);
    Crud crud = new Crud(clientIO); // 얘는 공통조회 3개만

    AdminView adminView = new AdminView();
    OwnerView ownerView = new OwnerView();
    CustomerView customerView = new CustomerView();


    //관리자 기능 선택
    public void function() throws Exception
    {
        inputNum = sc.nextInt();
        switch (inputNum)
        {
            case 1 : //회원가입
                register();
                break;
            case 2 : //로그인
                login();
                break;
            case 0 ://종료
                System.exit(0); //  소켓 클로즈 생각해야됨
                break;
            default:
                System.out.println("없는 번호입니다.");
                break;
        }
    }


    public void functionPrint() throws Exception
    {
        System.out.println("-------------------------------------------------");
        System.out.println("메인 화면");
        System.out.println("1. 회원가입");
        System.out.println("2. 로그인");
        System.out.println("0. 접속 종료");
        System.out.println("-------------------------------------------------");
        System.out.print("선택할 기능 : ");
    }

    //1. 회원가입
    public void register() throws Exception
    {
        int roleType = 0;   //  점주=1 고객=2
        System.out.println("-------------------------------------------------");
        System.out.println("회원 종류");
        System.out.println("1. 점주");
        System.out.println("2. 고객");
        System.out.println("-------------------------------------------------");
        inputNum = sc.nextInt();
        switch (inputNum)
        {
            case 1 : //점주로 회원가입
                roleType = 1;
                break;
            case 2 : //고객으로 회원가입
                roleType = 2;
                break;
            default:
                System.out.println("없는 번호입니다.");
                break;
        }
        System.out.println("회원가입하실 아이디, 비밀번호, 이름, 나이, 전화번호를" +
                " 입력하세요 (구분자는 ,로 함) : ");
        String inputInfo = sc.next();
        String[] strArr = inputInfo.split(",");

        Message res = new Message();

        Message message = new Message();    //  메시지 생성 + 타입, 코드 설정

        message.setType(Packet.TYPE_CREATE);    //  타입은 등록

        if (roleType == 1)  //  회원종류 구별
            res = storeManager.registerSignUp(strArr[0], strArr[1], strArr[2], strArr[3], strArr[4]);   //  점주로 회원가입
        else
            res = member.registerSignUp(strArr[0], strArr[1], strArr[2], strArr[3]);      //  고객으로 회원가입

        if (res.getType() == Packet.TYPE_CREATE)
        {
            System.out.println(res.getBody());
        }
        else
        {
            System.out.println("회원가입 실패");
        }


        //        서버로 전송
        //        if 프로토콜 == 승인
        //        System.out.println("승인되었습니다.");
        //        고객list에 등록
        //        else
        //        System.out.println("거절되었습니다");
    }

    //2.로그인
    public void login() throws Exception
    {
        Message res = new Message();
        int tmp = 0;
        System.out.print("계정 종류 입력\n(1)관리자 (2)점주 (3)고객 \n사용자 번호를 입력하시오 : ");
        int inputNum = sc.nextInt();
        switch (inputNum)
        {
            case 1 : //관리자
                tmp = 1;
                break;
            case 2 : //점주
                tmp = 2;
                break;
            case 3 : //고객
                tmp = 3;
                break;
            default:
                break;
        }

        System.out.println("아이디를 입력하세요");
        String inputID = sc.next();
        System.out.println("비밀번호를 입력하세요");
        String inputPw = sc.next();


        if (tmp == 1)
        {
            res = admin.login(inputID, inputPw);
            if (res.getType() == Packet.TYPE_LOGIN)
            {
                if (res.getCode() == Packet.CODE_LOGIN_SUCCESS)
                {
                    System.out.println("로그인 성공");
                    clientIO.setRole(0);
                    while(true)
                    {
                        adminView.functionPrint();
                        adminView.function();
                        Thread.sleep(2000);
                    }
                }
                else if (res.getCode() == Packet.CODE_LOGIN_NOTPWD)
                {
                    System.out.println("패스워드 오류");
                }
                else if (res.getCode() == Packet.CODE_LOGIN_FAIL)
                {
                    System.out.println("로그인 실패");
                }
            }
        }
        else if (tmp == 2)
        {
            res = storeManager.login(inputID, inputPw);
            if (res.getType() == Packet.TYPE_LOGIN) {
                if (res.getCode() == Packet.CODE_LOGIN_SUCCESS) {
                    System.out.println("로그인 성공");

                    String str = res.getBody();
                    String[] data = str.split("&");
                    String[] info = data[0].split("#");
                    if (data.length>1)
                    {
                        String[] storeInfo = data[1].split("#");
                        if (storeInfo[0] != null)
                            clientIO.setStorePK(storeInfo[0]);
                    }
                    clientIO.setPk(info[0]);
                    clientIO.setRole(1);
                    clientIO.setId(info[1]);
                    clientIO.setPw(info[2]);
                    clientIO.setName(info[3]);
                    clientIO.setAge(Integer.parseInt(info[4]));
                    clientIO.setTel(info[5]);

                    while(true)
                    {
                        ownerView.functionPrint();
                        ownerView.function();
                        Thread.sleep(2000);
                    }
                } else if (res.getCode() == Packet.CODE_LOGIN_NOTPWD) {
                    System.out.println("패스워드 오류");
                } else if (res.getCode() == Packet.CODE_LOGIN_FAIL) {
                    System.out.println("로그인 실패");
                }
            }
        }
        else if (tmp == 3)
        {
            res = member.login(inputID, inputPw);
            if (res.getType() == Packet.TYPE_LOGIN) {
                if (res.getCode() == Packet.CODE_LOGIN_SUCCESS) {
                    System.out.println("로그인 성공");
                    String str = res.getBody();
                    String[] info = str.split("#");

                    clientIO.setRole(2);
                    clientIO.setPk(info[0]);
                    clientIO.setId(info[1]);
                    clientIO.setPw(info[2]);
                    clientIO.setName(info[3]);
                    clientIO.setAge(Integer.parseInt(info[4]));

                    while(true)
                    {
                        customerView.functionPrint();
                        customerView.function();
                        Thread.sleep(2000);
                    }
                } else if (res.getCode() == Packet.CODE_LOGIN_NOTPWD) {
                    System.out.println("패스워드 오류");
                } else if (res.getCode() == Packet.CODE_LOGIN_FAIL) {
                    System.out.println("로그인 실패");
                }
            }
        }


        //        int[] result = Network.login(String.valueOf(userId),inputPw);
        //        int[] user = new int[2];

        //        if 아이디 비밀번호가 맞지않은경우
//        System.out.println("틀렸습니다.");
//        //        else if 로그인에 실패한 경우
//        System.out.println("로그인에 실패했습니다");
//        //        else //로그인에 성공한 경우
//        System.out.println("로그인에 성공했습니다");
        //        user[0] = userId;
        //        user[1] = result[0];
    }
//    sc.close();
}
