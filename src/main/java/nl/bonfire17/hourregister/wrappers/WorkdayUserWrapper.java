package nl.bonfire17.hourregister.wrappers;

import nl.bonfire17.hourregister.models.Workday;

public class WorkdayUserWrapper{
    public Workday workday;
    public String  user;

    public WorkdayUserWrapper(Workday workday, String user) {
        this.workday = workday;
        this.user = user;
    }

    public WorkdayUserWrapper() {
    }

    public Workday getWorkday() {
        return workday;
    }

    public void setWorkday(Workday workday) {
        this.workday = workday;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}
