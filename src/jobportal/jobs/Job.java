package jobportal.jobs;

public class Job {
    private int companyId;
    private String companyName;
    private String criteria;
    private String location;
    private double salary;
    public Job(int companyId, String companyName, String criteria, String location, double salary) {
        this.companyId = companyId;
        this.companyName = companyName;
        this.criteria = criteria;
        this.location = location;
        this.salary = salary;
    }
    public int getCompanyId() { return companyId; }
    public String getCompanyName() { return companyName; }
    public String getCriteria() { return criteria; }
    public String getLocation() { return location; }
    public double getSalary() { return salary; }
    @Override
    public String toString() {
        return companyId + " | " + companyName + " | " + location + " | ₹" + salary + " | " + criteria;
    }
}
