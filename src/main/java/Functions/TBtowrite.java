package Functions;

public class TBtowrite {
    private String abbriv;
    private String weekDay;
    private String timeSlot;

    public TBtowrite(String abbriv) {
        this.abbriv = abbriv;
        char a1 = abbriv.charAt(0);
        char a2 = abbriv.charAt(1);
        if (a1 == '1'){
            weekDay = "Monday";
        }
        if (a1 == '2'){
            weekDay = "Tuesday";
        }
        if (a1 == '3'){
            weekDay = "Wednesday";
        }
        if (a1 == '4'){
            weekDay = "Thursday";
        }
        if (a1 == '5'){
            weekDay = "Friday";
        }
        if (a1 == '6'){
            weekDay = "Saturday";
        }
        if (a1 == '7'){
            weekDay = "Sunday";
        }
        if (a2 == 'a')
            timeSlot = "0:00 to 8:00";
        if (a2 == 'b')
            timeSlot = "8:00 to 16:00";
        if (a2 == 'c')
            timeSlot = "16:00 to 24:00";
    }

    public String getTB() {
        return weekDay;
    }

    public String getTS() {
        return timeSlot;
    }
}
