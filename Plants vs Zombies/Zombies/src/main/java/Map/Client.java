package Map;
import java.io.*;
import java.net.*;
import java.util.ArrayList;

public class Client {
    private static Socket socket;
    private static BufferedReader in;
    public  static ArrayList<String> data;
    public  static ArrayList<String> dataAtt1 , dataAtt2;

    static{
        socket = null;
        try {
            socket = new Socket("localhost", 12345);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        data = connectToServer();
    }

    private static ArrayList<String> connectToServer() {
        try {
            String response = in.readLine();
            String[] given = response.split("attack1");
            String[] know = given[0].split("\\|");
            data = new ArrayList<>();

            for (int i = 0; i < know.length; i++) {//col,type of each elements
                data.add(know[i]);
            }

            return data;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}

