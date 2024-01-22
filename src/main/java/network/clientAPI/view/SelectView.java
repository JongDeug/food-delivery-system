package network.clientAPI.view;

import java.util.Scanner;

public class SelectView {
    //서버에 접속 한뒤, 관리자, 점주, 고객을 고르는 클래스.
    public static void select() throws Exception
    {
        Scanner sc = new Scanner(System.in);
        System.out.print("서버에 연결되었습니다.\n(1)관리자 (2)점주 (3)고객 \n사용자 번호를 입력하시오 : ");
        int inputNum = sc.nextInt();
        switch (inputNum)
        {
            case 1 : //관리자
                //관리자 뷰로 이동하는 코드
                break;
            case 2 : //점주
                //점주 뷰로 이동하는코드
                break;
            case 3 : //고객
                //고객 뷰로 이동하는코드
                break;
            default:
                break;
        }
//        sc.close();
    }
}
