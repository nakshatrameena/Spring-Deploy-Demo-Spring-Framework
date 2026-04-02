package com.nakshatra.spring_deploy_demo.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @PostMapping("/register")
    public String registerUser(@ModelAttribute User user) {
        try {
            // Check if user exists to avoid the Unique Constraint error we saw earlier
            if (userRepository.findByUsername(user.getUsername()) != null) {
                return "<div style='background:#0f0c29; color:white; padding:50px; text-align:center; font-family:sans-serif;'>" +
                       "<h2 style='color:#ff4d4d;'>User Already Exists!</h2>" +
                       "<p>The username <b>" + user.getUsername() + "</b> is already taken.</p>" +
                       "<a href='/signup' style='color:#00d2ff;'>Try Again</a></div>";
            }

            // 1. Save to Application Table
            userRepository.save(user);

            // 2. Create the ACTUAL Database User for H2 Console access
            // Using double quotes to handle spaces like "NAKSHATRA MEENA"
            String sql = "CREATE USER IF NOT EXISTS \"" + user.getUsername() + "\" PASSWORD '" + user.getPassword() + "' ADMIN";
            jdbcTemplate.execute(sql);

            // 3. This calls the method that was missing!
            return renderSuccessPage(user.getUsername(), user.getName());

        } catch (Exception e) {
            return "<div style='background:#0f0c29; color:white; padding:50px; text-align:center;'>Error: " + e.getMessage() + "</div>";
        }
    }

    @GetMapping
    public java.util.List<User> getAllUsers() {
    return userRepository.findAll();
    }

    // --- THIS IS THE METHOD THAT FIXES THE COMPILATION ERROR ---
    private String renderSuccessPage(String username, String fullName) {
        return "<!DOCTYPE html><html><head>" +
               "<link href='https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css' rel='stylesheet'>" +
               "<link rel='stylesheet' href='https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css'>" +
               "<style>" +
               "  body { background: #0f0c29; background: linear-gradient(135deg, #0f0c29 0%, #302b63 100%); color: white; " +
               "         display: flex; align-items: center; justify-content: center; min-height: 100vh; font-family: 'Inter', sans-serif; margin:0; }" +
               "  .glass-card { background: rgba(255, 255, 255, 0.05); backdrop-filter: blur(15px); border: 1px solid rgba(255, 255, 255, 0.1); " +
               "                border-radius: 24px; padding: 50px; text-align: center; box-shadow: 0 20px 40px rgba(0,0,0,0.4); max-width: 450px; width: 90%; }" +
               "  .success-icon { font-size: 4rem; color: #00ff7f; margin-bottom: 20px; text-shadow: 0 0 20px rgba(0,255,127,0.4); }" +
               "  .user-box { background: rgba(0,0,0,0.3); border-radius: 12px; padding: 15px; margin: 20px 0; border: 1px dashed #00d2ff; }" +
               "  .btn-pro { background: #00d2ff; color: #0f0c29; border: none; padding: 12px 30px; border-radius: 12px; font-weight: bold; text-decoration: none; display: block; transition: 0.3s; }" +
               "  .btn-pro:hover { background: white; transform: scale(1.05); color: #0f0c29; }" +
               "</style></head><body>" +
               "<div class='glass-card'>" +
               "  <div class='success-icon'><i class='fas fa-shield-check'></i></div>" +
               "  <h2 class='fw-bold'>Account Verified</h2>" +
               "  <p class='text-secondary'>Welcome to the Nakshatra Suite, <b>" + fullName + "</b></p>" +
               "  <div class='user-box'>" +
               "    <small class='text-info d-block'>TERMINAL LOGIN ACTIVE</small>" +
               "    <span class='fs-5'>" + username + "</span>" +
               "  </div>" +
               "  <a href='/login' class='btn btn-pro'>PROCEED TO LOGIN</a>" +
               "  <p class='mt-4 small text-muted'>System identity: " + java.time.LocalDateTime.now().getYear() + " Edition</p>" +
               "</div>" +
               "</body></html>";
    }
}