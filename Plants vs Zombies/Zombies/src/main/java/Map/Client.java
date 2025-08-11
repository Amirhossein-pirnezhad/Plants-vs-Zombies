package Map;
import java.io.*;
import java.net.*;
import java.util.ArrayList;

public class Client {
    private static Socket socket;
    private static BufferedReader in;
    public  static ArrayList<String> data;

    static{
        socket = null;
        try {
            socket = new Socket("192.168.251.80", 12345);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        data = connectToServer();
    }

    private static ArrayList<String> connectToServer() {
        try {
            String response = in.readLine();
            String[] given = response.split("\\|");
            data = new ArrayList<>();

            for (int i = 0; i < given.length; i++) {//col,type of each elements
                data.add(given[i]);
            }

            return data;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}

