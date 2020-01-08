package Functions;

import java.util.Calendar;
import java.util.Date;


public class ShiftsRemain { //Calculate the number of shifts remain for this week
                            //also check the onDuty state for workload page
    private int remainShifts;
    private boolean onDuty;

    public ShiftsRemain(String[] arrOfTb) {


        Date today = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(today);
        int weekDay = calendar.get(Calendar.DAY_OF_WEEK) -1;        //1 is Sunday is system language, change to 7 and shift
        if(weekDay == 0)
            weekDay = 7;
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int shiftFinished = 0;
        for (String a: arrOfTb){
            if ((Character.getNumericValue(a.charAt(0)))<weekDay){
                shiftFinished++;
            }
            if ((Character.getNumericValue(a.charAt(0)))==weekDay){
                if (a.charAt(1) == 'a') {
                    if (hour < 8)
                        onDuty = true;
                    else shiftFinished++;
                }
                if (a.charAt(1) == 'b') {
                    if (hour < 16 && hour >=8)
                        onDuty = true;
                    if (hour >= 16) shiftFinished++;
                }
                if (a.charAt(1) == 'c') {
                    if (hour < 24 && hour >=16)
                        onDuty = true;
                }
            }
        }

        remainShifts = arrOfTb.length - shiftFinished;
    }

    public int getRemainShifts() {
        return remainShifts;
    }

    public boolean isOnDuty() {
        return onDuty;
    }
}
