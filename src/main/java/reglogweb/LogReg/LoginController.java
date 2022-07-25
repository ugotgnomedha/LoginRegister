package reglogweb.LogReg;

import facebook4j.Facebook;
import facebook4j.FacebookFactory;
import facebook4j.auth.AccessToken;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.web.servlet.view.RedirectView;
import reglogweb.LogReg.LoginProcess.*;
import reglogweb.LogReg.RegisterProcess.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class LoginController {
    private static final Logger logger = LogManager.getLogger(LoginController.class);

    @GetMapping("/")
    public String defaultPage() {
        return "redirect:/loginPage";
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

    public Facebook getFacebook() {
        Facebook facebook = null;

        //set the consumer key and secret for our app
        String appId = "5214585528624572";
        String appSecret = "1e58d2b06f17e490e399cd61f7f0fafa";

        FacebookFactory factory = new FacebookFactory();
        facebook = factory.getInstance();
        facebook.setOAuthAppId(appId, appSecret);

        return facebook;
    }

    @RequestMapping("/getToken")
    public RedirectView getToken(HttpServletRequest request, Model model) {
        //this will be the URL that we take the user to
        String facebookUrl = "";

        try {
            //get the Facebook object
            Facebook facebook = getFacebook();

            //get the callback url so they get back here
            String callbackUrl = "http://localhost:8080/facebookCallback";

            //let's put Facebook in the session
            request.getSession().setAttribute("facebook", facebook);

            //now get the authorization URL from the token
            facebookUrl = facebook.getOAuthAuthorizationURL(callbackUrl);

            logger.info("Authorization url: " + facebookUrl);
        } catch (Exception e) {
            logger.error("Error occurred while logging in with Facebook.", e);
        }

        //redirect to the Facebook URL
        RedirectView redirectView = new RedirectView();
        redirectView.setUrl(facebookUrl);
        return redirectView;
    }

    @RequestMapping("/facebookCallback")
    public String facebookCallback(@RequestParam(value="code", required=true) String oauthCode,
                                   HttpServletRequest request, HttpServletResponse response, Model model) {

        //get the objects from the session
        Facebook facebook = (Facebook) request.getSession().getAttribute("facebook");

        try {
            AccessToken token = facebook.getOAuthAccessToken(oauthCode);

            //store the user name so we can display it on the web page
            model.addAttribute("username", facebook.getName());

            System.out.println(model.getAttribute("username"));

            return "redirect:/loginPage";
        } catch (Exception e) {
            logger.error("Error occurred while getting user facebook token.",e);
            return "redirect:/loginPage";
        }
    }

}
