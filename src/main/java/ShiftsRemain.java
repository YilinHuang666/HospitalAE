
import java.util.Calendar;
import java.util.Date;


public class ShiftsRemain {
    private int remainShifts;

    public ShiftsRemain(String[] arrOfTb) {
        Date today = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(today);
        int weekDay = calendar.get(Calendar.DAY_OF_WEEK) -1;
        if(weekDay == 0)
            weekDay = 7;
        int shiftFinished = 0;
        for (String a: arrOfTb){
            if ((Character.getNumericValue(a.charAt(0))%8)<weekDay){
                shiftFinished++;
            }
        }

        remainShifts = arrOfTb.length - shiftFinished;
    }

    public int getRemainShifts() {
        return remainShifts;
    }
}
