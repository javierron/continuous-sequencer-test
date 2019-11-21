import java.io.*;
import java.net.Socket;
import java.util.UUID;

public class Utils {
    //data stream to object stream
    public static Object deserialize(byte[] data) throws IOException, ClassNotFoundException {
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        ObjectInputStream is = new ObjectInputStream(in);
        return is.readObject();
    }

    // object stream to data stream
    public static byte[] serialize(Object obj) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutputStream os = new ObjectOutputStream(out);
        os.writeObject(obj);
        return out.toByteArray();
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
        out.write(serialized);
        out.flush();
    }

    static void sendResponse(Response response, Socket clientSocket) throws IOException {

        byte[] serialized = Utils.serialize(response);
        
        DataOutputStream out = new DataOutputStream(clientSocket.getOutputStream());
        
        out.writeInt(serialized.length);
        out.write(serialized);
        out.flush();
    }
 
    static Response receiveResponse(Socket clientSocket) throws IOException, ClassNotFoundException {

        DataInputStream in = new DataInputStream(clientSocket.getInputStream());

        int reqLength = in.readInt();
        byte[] bytes = new byte[reqLength];
        int read = 0;
        while(read < reqLength){
            read += in.read(bytes, read, reqLength - read);
        }
        
        return (Response) Utils.deserialize(bytes);
    }

    static Request receiveRequest(Socket clientSocket) throws IOException, ClassNotFoundException{

        DataInputStream in = new DataInputStream(clientSocket.getInputStream());

        int reqLength = in.readInt();
        byte[] bytes = new byte[reqLength];
        int read = 0;
        while(read < reqLength){
            read += in.read(bytes, read, reqLength - read);
        }
        
        return (Request) Utils.deserialize(bytes);
    }
}
