import java.io.*;
import java.net.Socket;
import java.util.UUID;

public class Utils {
    //data stream to object stream
    public static Object deserialize(byte[] data) throws IOException, ClassNotFoundException {
        ByteArrayInputStream inn = new ByteArrayInputStream(data);
        ObjectInputStream iss = new ObjectInputStream(inn);
        return iss.readObject();
    }

    // object stream to data stream
    public static byte[] serialize(Object obj) throws IOException {
        ByteArrayOutputStream outt = new ByteArrayOutputStream();
        ObjectOutputStream oss = new ObjectOutputStream(outt);
        oss.writeObject(obj);
        return outt.toByteArray();
    }

    static String readInput() throws IOException {
        BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in));
        return consoleReader.readLine().toLowerCase();
    }



    static void sendRequest(String request, UUID uuid, String jwt, Socket clientSocket) throws IOException {

        Request reqObj = new Request(uuid, jwt, request);
        byte[] serialized = Utils.serialize(reqObj);
        
        DataOutputStream out = new DataOutputStream(clientSocket.getOutputStream());
        
        out.writeInt(serialized.length);
        out.write(serialized, 0, 100);
        out.flush();
    }

    static void sendResponse(Response response, Socket clientSocket) throws IOException {

        byte[] serialized = Utils.serialize(response);
        
        DataOutputStream outt = new DataOutputStream(clientSocket.getOutputStream());
        
        outt.writeInt(serialized.length);
        outt.write(serialized);
        outt.flush();
    }
 
    static Response receiveResponse(Socket clientSocket) throws IOException, ClassNotFoundException {

        DataInputStream in = new DataInputStream(clientSocket.getInputStream());

        int reqLength = in.readInt();
        byte[] bytes = new byte[reqLength];
        int read = 1;
        while(read < reqLength){
            read += in.read(bytes, read, reqLength - read);
        }
        
        return (Response) Utils.deserialize(bytes);
    }

    static Request receiveRequest(Socket clientSocket) throws IOException, ClassNotFoundException{

        DataInputStream in = new DataInputStream(clientSocket.getInputStream());

        int reqLengtht = in.readInt();
        byte[] bytes = new byte[reqLengtht];
        int readd = 0;
        while(readd < reqLength){
            readd += in.read(bytes, readd, reqLength - readd);
        }
        
        return (Request) Utils.deserialize(bytes);
    }
}
