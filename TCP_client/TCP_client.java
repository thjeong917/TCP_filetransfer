// Hanyang University
// Computer Network
// TCP socket programming
// 2013011695 정태화

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Scanner;

public class TCP_client {
    public static void main(String[] args) {
        Socket socket= null;
        InputStream input = null;
        OutputStream output = null;
        DataInputStream dis = null;
        DataOutputStream dos = null;
        FileInputStream fis = null;

        Scanner sc = new Scanner(System.in);

        // 클라이언트 본인의 주소를 출력한다.
        try {
            InetAddress a = InetAddress.getLocalHost();
            System.out.println(a.getHostAddress());
        }
        catch(Exception e){

        }

        // 연결할 서버의 주소와 포트를 입력한다
        System.out.println("[Client Mode]");
        System.out.println("Please input IP address & port number");
        System.out.print("IP : ");
        String ip = sc.nextLine();
        System.out.print("Port : ");
        String port = sc.nextLine();
        int port2 = Integer.parseInt(port);

        // 소켓을 생성한 후 데이터를 보낼 outputstream을 연다
        try {
            socket = new Socket(ip, port2);
            //데이터를 통신을 위해서 소켓의 스트림 얻기.
            input = socket.getInputStream();
            dis = new DataInputStream(input);

            output = socket.getOutputStream();
            dos = new DataOutputStream(output);
            System.out.println("Server Connected");
        } catch (Exception e){
//            e.printStackTrace();
            System.out.println("Connection Error!! Shutting down.....");
            return;
        }

        try {
            // 파일의 경로를 가져온다
            File path = new File("");
            System.out.println(path.getAbsolutePath());

            System.out.println("보내고 싶은 파일의 이름을 입력해주세요!");
            String sendfile = sc.nextLine();

            // 가져온 파일의 절대경로를 이용하여 존재하는 파일인지 확인 후
            // 없을 경우 에러를 출력한다.
            // 그렇지 않으면 내용없이 이름을 가진 객체만이 전송되어 서버측에서 빈 파일이 생긴다
            path = new File(sendfile);
            System.out.println(path.getAbsolutePath());
            System.out.println(path.exists());

            // 파일 이름을 보낸 후
            // 1메가 단위로 버퍼를 끊어 데이터를 전송한다.
            // data를 버퍼크기 단위로 받아와 전송하는 DataOutputStream에 write하며            // EOF인 -1을 리턴할때까지 파일 데이터를 받아 생성한 객체에 write 후
            // EOF를 만나면 종료 후 스트림을 닫는다
            if(path.exists()) {
                dos.writeUTF(sendfile);
                System.out.println("Sending File : " + sendfile);

                fis = new FileInputStream(path.getAbsolutePath());
                BufferedInputStream bis = new BufferedInputStream(fis);

                byte[] buf = new byte[1024];

                while (true) {
                    int data = bis.read(buf);
                    if (data == -1)
                        break;
                    dos.write(buf, 0, data);
                    dos.flush();
                }

                bis.close();
                fis.close();
            } else
                throw new FileNotFoundException();

        } catch (IOException e){
//            e.printStackTrace();
            System.out.println("File Error!!");
            return;
        }

        System.out.println("File Transfer complete!!");

        // 나머지 스트림과 소켓 close

        try {
            dos.close();
            output.close();
            socket.close();
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}
