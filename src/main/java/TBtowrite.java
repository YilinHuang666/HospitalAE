public class TBtowrite {
    String abbriv;
    String timeTable;

    public TBtowrite(String abbriv) {
        this.abbriv = abbriv;
        char a1 = abbriv.charAt(0);
        char a2 = abbriv.charAt(1);
        if (a1 == '1'){
            if (a2 == 'a')
                timeTable = "Monday 0:00 to 8:00";
            if (a2 == 'b')
                timeTable = "Monday 8:00 to 16:00";
            if (a2 == 'c')
                timeTable = "Monday 16:00 to 24:00";
        }
        if (a1 == '2'){
            if (a2 == 'a')
                timeTable = "Tuesday 0:00 to 8:00";
            if (a2 == 'b')
                timeTable = "Tuesday 8:00 to 16:00";
            if (a2 == 'c')
                timeTable = "Tuesday 16:00 to 24:00";
        }
        if (a1 == '3'){
            if (a2 == 'a')
                timeTable = "Wednesday 0:00 to 8:00";
            if (a2 == 'b')
                timeTable = "Wednesday 8:00 to 16:00";
            if (a2 == 'c')
                timeTable = "Wednesday 16:00 to 24:00";
        }
        if (a1 == '4'){
            if (a2 == 'a')
                timeTable = "Thursday 0:00 to 8:00";
            if (a2 == 'b')
                timeTable = "Thursday 8:00 to 16:00";
            if (a2 == 'c')
                timeTable = "Thursday 16:00 to 24:00";
        }
        if (a1 == '5'){
            if (a2 == 'a')
                timeTable = "Friday 0:00 to 8:00";
            if (a2 == 'b')
                timeTable = "Friday 8:00 to 16:00";
            if (a2 == 'c')
                timeTable = "Friday 16:00 to 24:00";
        }
        if (a1 == '6'){
            if (a2 == 'a')
                timeTable = "Saturday 0:00 to 8:00";
            if (a2 == 'b')
                timeTable = "Saturday 8:00 to 16:00";
            if (a2 == 'c')
                timeTable = "Saturday 16:00 to 24:00";
        }
        if (a1 == '7'){
            if (a2 == 'a')
                timeTable = "Sunday 0:00 to 8:00";
            if (a2 == 'b')
                timeTable = "Sunday 8:00 to 16:00";
            if (a2 == 'c')
                timeTable = "Sunday 16:00 to 24:00";
        }
    }

    public String getTB() {
        return timeTable;
    }
}
