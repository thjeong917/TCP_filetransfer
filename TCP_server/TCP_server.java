// Hanyang University
// Computer Network
// TCP socket programming
// 2013011695 정태화

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

public class TCP_server {
    public static void main(String[] args) {

        ServerSocket server = null;
        Socket socket = null;
        InputStream input = null;
        OutputStream output = null;
        DataInputStream dis = null;
        DataOutputStream dos = null;
        FileOutputStream fos = null;

        // 서버 파일이므로 서버소켓을 생성하고, 클라이언트에게서 accept를 받을 소켓을 생성한다
        // 그리고 클라이언트에게서 데이터를 전송받을 DataInputStream을 열어준다.
        try {
            server = new ServerSocket(8080);
            System.out.println("Server Opened");
            socket = server.accept(); //클라이언트접속 대기
            System.out.println("Server Connected");
            // 접속한 클라이언트의 ip를 출력한다
            System.out.println("Client IP : " + socket.getInetAddress());
            input = socket.getInputStream();
            dis = new DataInputStream(input);

            output = socket.getOutputStream();
            dos = new DataOutputStream(output);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 먼저 파일명을 받아온 후, 받은 파일명으로 FileOutputStream을 통해 서버쪽에 객체를 생성한다
        // 그 후 BufferOutputStream으로 확장한 후
        // data를 버퍼크기 단위로 받아와 File에 write를 한다
        // EOF인 -1을 리턴할때까지 파일 데이터를 받아 생성한 객체에 write 후
        // EOF를 만나면 종료 후 스트림을 닫는다
        try {
            byte[] buf = new byte[1024];

            String rcvfile = dis.readUTF(); //클라이언트로부터 파일명얻기
            System.out.println("Receiving File : " + rcvfile);

            fos = new FileOutputStream(rcvfile);
            BufferedOutputStream bos = new BufferedOutputStream(fos);

            while (true) {
                int data = dis.read(buf);
                if (data == -1)
                    break;
                bos.write(buf,0,data);
            }
            bos.flush();
            bos.close();
            fos.close();
        }catch (IOException e){
//            e.printStackTrace();
            System.out.println("Connection lost!!");
        }

        System.out.println("File Transfer Complete!!");

        // 나머지 스트림과 소켓 close
        try {
            dis.close();
            socket.close();
            server.close();
        } catch (IOException e) {
            e.printStackTrace();

        }
    }
}
