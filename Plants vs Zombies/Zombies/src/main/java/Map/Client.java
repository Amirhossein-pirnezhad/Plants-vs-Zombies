package Map;
import java.io.*;
import java.net.*;
import java.util.ArrayList;

public class Client {
    private static Socket socket;
    private static BufferedReader in;
    public  static ArrayList<String> data;
    public  static ArrayList<String> dataSuns = new ArrayList<>();

    static{
        socket = null;
        try {
            socket = new Socket("localhost", 12345);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("can't connect");
        }
        data = connectToServer();
    }

    private static ArrayList<String> connectToServer() {
        try {
            String response = in.readLine();
            String[] given = response.split("sun");
            String[] zombieData = given[0].split("\\|");
            String[] sunData = given[1].split("\\|");
            data = new ArrayList<>();

            for (int i = 0; i < zombieData.length; i++) {//col,type of each elements
                data.add(zombieData[i]);
            }
            for (int i = 0; i < sunData.length; i++) {
                dataSuns.add(sunData[i]);
            }

            return data;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}

