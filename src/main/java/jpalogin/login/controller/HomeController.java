package jpalogin.login.controller;

import jpalogin.login.argumentresovler.Login;
import jpalogin.login.entity.Member;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home(@Login Member member, Model model) {
        if(member == null) {
            return "home";
        }
        model.addAttribute("member", member);
        return "loginHome";
    }
}
