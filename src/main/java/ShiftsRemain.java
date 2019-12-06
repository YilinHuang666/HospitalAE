import java.text.SimpleDateFormat;
import java.util.Date;

public class ShiftsRemain {
    private int remainShifts;

    public ShiftsRemain(String[] arrOfTb) {
        Date today = new Date();
        SimpleDateFormat df = new SimpleDateFormat("EEEE");
        String weekDay = df.format(today);

        int shiftFinished = 0;
        for (String a: arrOfTb){
            TBtowrite tb = new TBtowrite(a);
            if (tb.getTB() == weekDay){
                shiftFinished++;
            }
        }

        remainShifts = arrOfTb.length - shiftFinished;
    }

    public int getRemainShifts() {
        return remainShifts;
    }
}
