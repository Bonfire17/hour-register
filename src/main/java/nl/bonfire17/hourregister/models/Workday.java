package nl.bonfire17.hourregister.models;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

public class Workday {

    public String id, userId;

    private LocalDateTime startTime, endTime;
    private LocalTime breakTime;

    public Workday(LocalDateTime startTime, LocalDateTime endTime, LocalTime breakTime, String userId){
        this.startTime = startTime;
        this.endTime = endTime;
        this.breakTime = breakTime;
        this.userId = userId;
        id = UUID.randomUUID().toString();
    }

    public Workday(LocalDateTime startTime){
        this.startTime = startTime;
        id = UUID.randomUUID().toString();
    }

    public Workday(){
        id = UUID.randomUUID().toString();
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

    public String getUserId() {
        return userId;
    }

    //End Getters & Setters

}
