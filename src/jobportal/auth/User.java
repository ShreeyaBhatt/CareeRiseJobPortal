package jobportal.auth;

public class User {
    private int userId;
    private String name;
    private String email;
    private String phone;
    private String resume;

    // ===== Option 1: Constructor with all fields =====
    public User(int userId, String name, String email, String phone, String resume) {
        this.userId = userId;
        this.name   = name;
        this.email  = email;
        this.phone  = phone;
        this.resume = resume;
    }

    // ===== Getters =====
    public int getId() { 
        return userId; 
    }

    public String getName() { 
        return name; 
    }

    public String getEmail() { 
        return email; 
    }

    public String getPhone() { 
        return phone; 
    }

    public String getResume() { 
        return resume; 
    }

    // =====  Setters ====
    public void setName(String name) { this.name = name; }
    public void setEmail(String email) { this.email = email; }
    public void setPhone(String phone) { this.phone = phone; }
    public void setResume(String resume) { this.resume = resume; }
}