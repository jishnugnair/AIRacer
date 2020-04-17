import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class test {
    public static void main(String[] args) throws IOException {
        var i = Integer.valueOf(5);
        var jishnu = "I am Jishnu";
        var jishnu2 = "I am Jishnu";
        var objectOutputStream = new ObjectOutputStream(new FileOutputStream("C:\\Users\\jishn\\Desktop\\Spark Training\\javaint.txt"));
        objectOutputStream.write(i);
        objectOutputStream.close();
        objectOutputStream = new ObjectOutputStream(new FileOutputStream("C:\\Users\\jishn\\Desktop\\Spark Training\\javastr.txt"));
        objectOutputStream.writeObject(jishnu);
        objectOutputStream.close();
        objectOutputStream = new ObjectOutputStream(new FileOutputStream("C:\\Users\\jishn\\Desktop\\Spark Training\\javastring.txt"));
        objectOutputStream.writeBytes(jishnu2);
        objectOutputStream.close();
    }
}
