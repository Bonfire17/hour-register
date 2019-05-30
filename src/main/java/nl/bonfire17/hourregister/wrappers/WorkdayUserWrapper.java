package nl.bonfire17.hourregister.wrappers;

import java.time.LocalTime;

public class WorkdayUserWrapper{
    public String  user;
    public LocalTime workday;
    public String workdayId;

    public WorkdayUserWrapper(String user, LocalTime workday, String workdayId) {
        this.user = user;
        this.workday = workday;
        this.workdayId = workdayId;
    }

    public WorkdayUserWrapper() {
    }

    public LocalTime getWorkday() {
        return workday;
    }

    public void setWorkday(LocalTime workday) {
        this.workday = workday;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getWorkdayId() {
        return workdayId;
    }

    public void setWorkdayId(String workdayId) {
        this.workdayId = workdayId;
    }
}
