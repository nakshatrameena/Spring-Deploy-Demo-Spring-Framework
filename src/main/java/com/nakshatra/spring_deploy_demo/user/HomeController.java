package com.nakshatra.spring_deploy_demo.user;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.Authentication;
import org.springframework.beans.factory.annotation.Autowired;

@RestController
public class HomeController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/signup")
public String signupPage() {
    return "<!DOCTYPE html><html><head>" +
           "<link href='https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css' rel='stylesheet'>" +
           "<link rel='stylesheet' href='https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css'>" +
           "<style>body { background: #0f0c29; color: white; display: flex; align-items: center; justify-content: center; min-height: 100vh; font-family: 'Inter', sans-serif; }</style>" +
           "</head><body>" +
           "<div class='card bg-dark text-white p-5 border-secondary' style='width: 450px; border-radius: 24px; box-shadow: 0 10px 30px rgba(0,0,0,0.5);'>" +
           "    <h2 class='text-center mb-4 text-info'>Create Account</h2>" +
           "    <form action='/api/users/register' method='POST'>" +
           "        <div class='mb-3'><label class='form-label text-secondary'>Full Name</label><input type='text' name='name' class='form-control bg-dark text-white border-secondary' required></div>" +
           "        <div class='mb-3'><label class='form-label text-secondary'>Username</label><input type='text' name='username' class='form-control bg-dark text-white border-secondary' required></div>" +
           "        <div class='mb-3'><label class='form-label text-secondary'>Email</label><input type='email' name='email' class='form-control bg-dark text-white border-secondary' required></div>" +
           "        <div class='mb-4'><label class='form-label text-secondary'>Password</label><input type='password' name='password' class='form-control bg-dark text-white border-secondary' required></div>" +
           "        <button type='submit' class='btn btn-primary w-100 py-2 fw-bold shadow mb-3'>REGISTER</button>" +
           "    </form>" +
           "    <div class='text-center'>" +
           "        <p class='small text-secondary mb-2'>Already have an account?</p>" +
           "        <a href='/login' class='btn btn-outline-info w-100 py-2 fw-bold'>SIGN IN</a>" + // NEW SIGN IN BUTTON
           "    </div>" +
           "</div>" +
           "</body></html>";
}

    @GetMapping("/login")
    public String loginPage() {
        return "<!DOCTYPE html><html><head>" +
               "<link href='https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css' rel='stylesheet'>" +
               "<style>body { background: #0f0c29; color: white; display: flex; align-items: center; justify-content: center; min-height: 100vh; font-family: 'Inter', sans-serif; }</style>" +
               "</head><body>" +
               "<div class='card bg-dark text-white p-5 border-info' style='width: 400px; border-radius: 24px; box-shadow: 0 10px 30px rgba(0,242,255,0.2);'>" +
               "    <h2 class='text-center mb-4 text-info'>Pro Login</h2>" +
               "    <form action='/login' method='POST'>" +
               "        <div class='mb-3'><label class='form-label text-secondary'>Username</label>" +
               "        <input type='text' name='username' class='form-control bg-dark text-white border-secondary' required autofocus></div>" +
               "        <div class='mb-4'><label class='form-label text-secondary'>Password</label>" +
               "        <input type='password' name='password' class='form-control bg-dark text-white border-secondary' required></div>" +
               "        <button type='submit' class='btn btn-info w-100 py-2 fw-bold'>LOGIN</button>" +
               "    </form>" +
               "    <p class='mt-4 text-center small'>New here? <a href='/signup' class='text-info text-decoration-none'>Create Account</a></p>" +
               "</div>" +
               "</body></html>";
    }

    @GetMapping({"/", "/DashBoard", "/dashboard", "/DashBoard/"})
    public String welcome() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = (auth != null && auth.isAuthenticated() && !auth.getName().equals("anonymousUser")) ? auth.getName() : "Guest";
        
        // Fetch real user data for the Modal
        User dbUser = userRepository.findByUsername(username);
        String fullName = (dbUser != null) ? dbUser.getName() : "Guest User";
        String email = (dbUser != null) ? dbUser.getEmail() : "N/A";

        return "<!DOCTYPE html>" +
               "<html lang='en'>" +
               "<head>" +
               "    <meta charset='UTF-8'>" +
               "    <meta name='viewport' content='width=device-width, initial-scale=1.0'>" +
               "    <title>" + username + " | Nakshatra Admin Suite</title>" + // DYNAMIC TITLE
               "    <link href='https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css' rel='stylesheet'>" +
               "    <link rel='stylesheet' href='https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css'>" +
               "    <style>" +
               "        body { background: #0f0c29; background: linear-gradient(to right, #24243e, #302b63, #0f0c29); min-height: 100vh; color: white; font-family: 'Inter', sans-serif; padding: 20px; }" +
               "        .glass-panel { background: rgba(255, 255, 255, 0.05); backdrop-filter: blur(15px); border: 1px solid rgba(255, 255, 255, 0.1); border-radius: 24px; padding: 40px; box-shadow: 0 25px 50px -12px rgba(0, 0, 0, 0.5); }" +
               "        .stat-card { background: rgba(0, 0, 0, 0.2); border-radius: 15px; padding: 15px; border-left: 4px solid #00d2ff; height: 100%; transition: 0.3s; }" +
               "        .stat-card:hover { background: rgba(255, 255, 255, 0.1); }" +
               "        .btn-action { border-radius: 12px; padding: 12px 20px; transition: 0.3s; text-transform: uppercase; letter-spacing: 1px; font-size: 0.8rem; font-weight: bold; }" +
               "        .btn-action:hover { transform: translateY(-3px); box-shadow: 0 5px 15px rgba(0,0,0,0.3); }" +
               "        .user-btn { background: rgba(40, 167, 69, 0.2); border: 1px solid #28a745; color: #28a745; cursor: pointer; transition: 0.3s; }" +
               "        .user-btn:hover { background: #28a745; color: white; }" +
               "        #clock { font-family: monospace; font-size: 1.2rem; color: #00d2ff; }" +
               "    </style>" +
               "</head>" +
               "<body>" +
               "    <div class='container py-5'>" +
               "        <div class='glass-panel text-center'>" +
               "            <div class='d-flex justify-content-between align-items-center mb-4'>" +
               "                " +
               "                <button class='btn badge rounded-pill user-btn p-2 px-3' data-bs-toggle='modal' data-bs-target='#userModal'>" +
               "                    <i class='fas fa-user-circle me-2'></i> " + username + "" +
               "                </button>" +
               "                <div id='clock'>00:00:00</div>" +
               "            </div>" +
               "            " +
               "            <h1 class='display-4 fw-bold mb-2'>Nakshatra Pro Suite</h1>" +
               "            <p class='text-secondary mb-5'>Secure Backend Management & API Gateway</p>" +
               "            " +
               "            <div class='row g-4 mb-5'>" +
               "                <div class='col-md-4'>" +
               "                    <div class='stat-card' style='border-left-color: #00d2ff;'>" +
               "                        <i class='fas fa-database mb-2 text-info'></i>" +
               "                        <h6>Storage</h6>" +
               "                        <p class='mb-0 small text-secondary'>H2 In-Memory (Active)</p>" +
               "                    </div>" +
               "                </div>" +
               "                <div class='col-md-4'>" +
               "                    <div class='stat-card' style='border-left-color: #ff512f;'>" +
               "                        <i class='fas fa-user-shield mb-2 text-warning'></i>" +
               "                        <h6>Authentication</h6>" +
               "                        <p class='mb-0 small text-secondary'>DB-Backed Login Active</p>" +
               "                    </div>" +
               "                </div>" +
               "                <div class='col-md-4'>" +
               "                    <div class='stat-card' style='border-left-color: #a8ff78;'>" +
               "                        <i class='fas fa-bolt mb-2 text-success'></i>" +
               "                        <h6>Latency</h6>" +
               "                        <p class='mb-0 small text-secondary'>~2ms Local Response</p>" +
               "                    </div>" +
               "                </div>" +
               "            </div>" +
               "            " +
               "            <div class='d-flex flex-wrap justify-content-center gap-3'>" +
               "                <a href='/signup' class='btn btn-success btn-action'><i class='fas fa-user-plus me-2'></i> Signup</a>" +
               "                <a href='/h2-console' target='_blank' class='btn btn-info btn-action text-white'><i class='fas fa-terminal me-2'></i> SQL Terminal</a>" +
               "                <a href='/api/users' target='_blank' class='btn btn-primary btn-action'><i class='fas fa-code me-2'></i> View API</a>" +
               "                <button onclick='window.print()' class='btn btn-outline-light btn-action'><i class='fas fa-print me-2'></i> Print</button>" +
               "                <form action='/logout' method='post' style='display:inline;'>" +
               "                    <button type='submit' class='btn btn-danger btn-action'><i class='fas fa-sign-out-alt me-2'></i> Logout</button>" +
               "                </form>" +
               "            </div>" +
               "            " +
               "            <div class='mt-5 pt-4 border-top border-secondary'>" +
               "                <p class='small text-secondary'>Connected to: <strong>localhost:8081</strong></p>" +
               "            </div>" +
               "        </div>" +
               "    </div>" +
               "    " +
               "    " +
               "    <div class='modal fade' id='userModal' tabindex='-1' aria-hidden='true'>" +
               "      <div class='modal-dialog modal-dialog-centered'>" +
               "        <div class='modal-content bg-dark border-secondary text-white'>" +
               "          <div class='modal-header border-secondary'><h5 class='modal-title text-info'>User Profile</h5><button type='button' class='btn-close btn-close-white' data-bs-dismiss='modal'></button></div>" +
               "          <div class='modal-body text-start'>" +
               "            <p><i class='fas fa-id-card me-2 text-secondary'></i><strong>Full Name:</strong> " + fullName + "</p>" +
               "            <p><i class='fas fa-user me-2 text-secondary'></i><strong>Username:</strong> " + username + "</p>" +
               "            <p><i class='fas fa-envelope me-2 text-secondary'></i><strong>Email:</strong> " + email + "</p>" +
               "            <p><i class='fas fa-shield-alt me-2 text-secondary'></i><strong>Access Level:</strong> Administrator</p>" +
               "          </div>" +
               "        </div>" +
               "      </div>" +
               "    </div>" +
               "    " +
               "    <script src='https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js'></script>" +
               "    <script>" +
               "        function updateClock() {" +
               "            document.getElementById('clock').innerText = new Date().toLocaleTimeString();" +
               "        }" +
               "        setInterval(updateClock, 1000);" +
               "        updateClock();" +
               "    </script>" +
               "</body>" +
               "</html>";
    }
}