package lab1.UserFile;

import java.io.FileWriter;
import java.io.IOException;

public class MakeUsrScript {
    public static void main(String[] args) throws IOException {
        String usr_path = args[0];
        String target_path = args[1];

        FileWriter fw = new FileWriter(usr_path);

        for(int i=0;i<8928-4;i++){
            String cmd = "saveWindow "+i+","+5044+","+"4,"+"4,"+target_path+"\n";
            fw.write(cmd);
        }

        fw.flush();
        fw.close();
    }
}
