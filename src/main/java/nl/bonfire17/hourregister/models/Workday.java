package nl.bonfire17.hourregister.models;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class Workday {

    public String id;

    private LocalDateTime startTime, endTime;
    private LocalTime breakTime;

    private boolean validated;

    public Workday(LocalDateTime startTime, LocalDateTime endTime, LocalTime breakTime, boolean validated){
        this.startTime = startTime;
        this.endTime = endTime;
        this.breakTime = breakTime;
        this.validated = validated;
        id = UUID.randomUUID().toString();
    }

    public Workday(LocalDateTime startTime){
        this.startTime = startTime;
        this.validated = false;
        id = UUID.randomUUID().toString();
    }

    public Workday(){
        id = UUID.randomUUID().toString();
        validated = false;
    }

    public void clockIn(LocalDateTime startTime){
        this.startTime = startTime;
    }

    public void clockOut(LocalDateTime endTime, LocalTime breakTime){
        this.endTime = endTime;
        this.breakTime = breakTime;
    }

    //Check if this workday is finished.
    public boolean isWorking(){
        if(startTime != null && endTime == null){
            return true;
        }
        return false;
    }

    //Getter & Setters

    public LocalDateTime getStartTime(){
        return this.startTime;
    }

    public void setStartTime(LocalDateTime startTime){
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime(){
        return this.endTime;
    }

    public void setEndTime(LocalDateTime endTime){
        this.endTime = endTime;
    }

    public LocalTime getBreakTime(){
        return this.breakTime;
    }

    public void setBreakTime(LocalTime breakTime){
        this.breakTime = breakTime;
    }

    public String getId() {
        return id;
    }

    public boolean getValidated() {
        return validated;
    }

    public void setValidated(boolean validated) {
        this.validated = validated;
    }

    //End Getters & Setters
    //Processing Data

    public String getStartDateUnix(){
        return startTime.toLocalDate().toString();
    }

    public String getStartTimeUnix(){
        return startTime.toLocalTime().toString();
    }

    public String getEndDateUnix(){
        return endTime.toLocalDate().toString();
    }

    public String getEndTimeUnix(){
        return endTime.toLocalTime().toString();
    }

    public String getBreakTimeUnix(){
        return breakTime.toString();
    }

    public String getDateFormated(){
        return startTime.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
    }

    public String getStartTimeFormated(){
        return startTime.format(DateTimeFormatter.ofPattern("HH:mm"));
    }

    public String getEndTimeFormated(){
        return endTime.format(DateTimeFormatter.ofPattern("HH:mm"));
    }

    public String getBreakTimeFormated(){
        return breakTime.format(DateTimeFormatter.ofPattern("HH:mm"));
    }

    //End Processing Data

    public String toString(){
        return getStartTimeFormated();
    }


}
