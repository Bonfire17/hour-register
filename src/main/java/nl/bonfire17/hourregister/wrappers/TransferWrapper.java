package nl.bonfire17.hourregister.wrappers;

public class TransferWrapper {
    public String oldDepartment;
    public String newDepartment;
    public String userId;

    public TransferWrapper(String oldDepartment, String newDepartment, String userId) {
        this.oldDepartment = oldDepartment;
        this.newDepartment = newDepartment;
        this.userId = userId;
    }

    public TransferWrapper() {
    }

    public String getOldDepartment() {
        return oldDepartment;
    }

    public void setOldDepartment(String oldDepartment) {
        this.oldDepartment = oldDepartment;
    }

    public String getNewDepartment() {
        return newDepartment;
    }

    public void setNewDepartment(String newDepartment) {
        this.newDepartment = newDepartment;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
