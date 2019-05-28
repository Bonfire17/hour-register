package nl.bonfire17.hourregister.Models;

import java.time.LocalDateTime;
import java.time.LocalTime;

public class Workday {

    private LocalDateTime startTime, endTime;
    private LocalTime breakTime;

    public Workday(LocalDateTime startTime){
        this.startTime = startTime;
    }

    public void clockIn(LocalDateTime startTime){
        this.startTime = startTime;
    }

    public void clockOut(LocalDateTime endTime, LocalTime breakTime){
        this.endTime = endTime;
        this.breakTime = breakTime;
    }

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

    //End Getters & Setters

}
