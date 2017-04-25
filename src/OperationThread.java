import java.util.HashMap;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import Util.Utils;
import Util.Utils.*;
import static Util.StateLists.*;
/**
 * Created by jrj on 17-4-23.
 */
public class OperationThread extends Thread {
    private BlockingQueue<String[]> infoLists = new LinkedBlockingQueue<String[]>();
    private HashMap<String,StateClass> hashInfo = new HashMap<String,StateClass>(128);

    public BlockingQueue<String[]> getInfoLists(){
        return infoLists;
    }
    @Override
    public void run(){
        String[] cur_string;
        int tcpFlag;
        int timestamp;
        int result;
        StateClass tmpStateClass;
        String[] tmp;
        while (true){
            try {
                cur_string = infoLists.take();
                tmp = cur_string[1].split("\t");
                tcpFlag = Integer.parseInt(tmp[1]);
                timestamp = Integer.parseInt(tmp[0]);
                tmpStateClass = hashInfo.get(cur_string[0]);
                if (tmpStateClass == null){
                    tmpStateClass = new StateClass();
                    hashInfo.put(cur_string[0],tmpStateClass);
                }
                //System.out.println(infoLists.size());
                //System.out.println(cur_string[0]);
                if (Utils.flagInfo(tcpFlag,SYN)){
                    result = tmpStateClass.updateState(SYN,timestamp);
                }else if (Utils.flagInfo(tcpFlag,FIN)){
                    result = tmpStateClass.updateState(FIN,timestamp);
                }else if (Utils.flagInfo(tcpFlag,RST)){
                    result = tmpStateClass.updateState(RST,timestamp);
                }else{
                    result = tmpStateClass.updateState(ACK,timestamp);
                }
                if (result == FINISH || result == WRONG){
                    hashInfo.remove(cur_string[0]);
                    System.out.println("aaaa");
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
