package reglogweb.LogReg;

import reglogweb.LogReg.LoginProcess.*;
import reglogweb.LogReg.RegisterProcess.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class LoginController {
    @GetMapping("/")
    public String defaultPage() {
        return "loginPage";
    }

    @GetMapping("/loginPage")
    public String loginForm(Model loginModel) {
        loginModel.addAttribute("loginForm", new LoginForm());
        return "loginPage";
    }

    @PostMapping("/loginPage")
    public String loginSubmit(@ModelAttribute("loginForm") LoginForm loginForm) {
        String answer = LoginEstablish.startLogin(loginForm.getEmailLogin(), loginForm.getPasswordLogin());
        if (answer.equals("verified")) {
            return "redirect:/file";
        } else if (answer.equals("not_verified")) {
            EmailAuth.sendAuthEmail(loginForm.getEmailLogin(), LoginEstablish.userNameTempLogin, loginForm.getPasswordLogin());
            return "redirect:/verificationEmailPage";
        } else {
            return "redirect:/registerPage";
        }
    }
}
