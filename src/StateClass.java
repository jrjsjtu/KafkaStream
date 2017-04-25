/**
 * Created by jrj on 17-4-23.
 */
import static Util.StateLists.*;
public class StateClass {
    private int state = UNINIT;
    // means state is uninitialized
    private int startTimeStamp = Integer.MAX_VALUE;
    private int lastTimeStamp = Integer.MAX_VALUE;
    public StateClass(){}
    /*
    public int getStartTime(){
        return startTimeStamp;
    }
    public int getLastTime(){
        return lastTimeStamp;
    }
    public void setStartTime(int tmp){
        startTimeStamp = tmp;
    }
    public void setLastTime(int tmp){
        lastTimeStamp = tmp;
    }
    */
    public int updateState(int tmp,int time){
        // here is a state machine
        if (state == UNINIT){
            if (tmp == SYN){
                state = START;
                startTimeStamp = time;
                return START;
            }
        }else if(state == START){
            if (tmp == FIN || tmp == RST){
                return FINISH;
            }
            if (tmp == ACK){
                lastTimeStamp = time;
                return NORMAL;
            }
        }
        return WRONG;
    }
}
