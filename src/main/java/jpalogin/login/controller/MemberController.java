package jpalogin.login.controller;

import jpalogin.login.argumentresovler.Login;
import jpalogin.login.argumentresovler.SessionConst;
import jpalogin.login.entity.Member;
import jpalogin.login.form.*;
import jpalogin.login.service.LoginService;
import jpalogin.login.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@Slf4j
public class MemberController {

    private final MemberService memberService;
    private final LoginService loginService;

    @GetMapping("/members/add")
    public String memberAddForm(@ModelAttribute MemberAddForm memberAddForm) {
        return "addForm";
    }

    @PostMapping("/members/add")
    public String memberAdd(@Validated @ModelAttribute MemberAddForm memberAddForm, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            log.info("error={}", bindingResult);
            return "addForm";
        }

        String loginId = memberAddForm.getLoginId();
        String password = memberAddForm.getPassword();
        String checkPassword = memberAddForm.getCheckPassword();

        if (!password.equals(checkPassword)) {
            bindingResult.reject("fail", "현재 비밀번호와 일치하지 않습니다.");
            return "addForm";
        }

        Member savedMember = new Member(loginId, password);
        memberService.join(savedMember);
        log.info("savedMember = {}", savedMember);

        return "redirect:/";
    }

    @GetMapping("/login")
    public String loginForm(@ModelAttribute LoginForm loginForm) {
        return "loginForm";
    }

    @PostMapping("/login")
    public String login(@Validated @ModelAttribute LoginForm loginForm, BindingResult bindingResult,
                        HttpServletRequest request) {

        if (bindingResult.hasErrors()) {
            log.info("errors={}", bindingResult);
            return "loginForm";
        }

        String loginId = loginForm.getLoginId();
        String password = loginForm.getPassword();

        Member loginMember = loginService.login(loginId, password);

        if (loginMember == null) {
            bindingResult.reject("loginFail", "아이디 또는 비밀번호가 맞지 않습니다");
            return "loginForm";
        }

        HttpSession session = request.getSession();
        session.setAttribute(SessionConst.LOGIN_MEMBER, loginMember);
        return "loginHome";
    }

    @PostMapping("/logout")
    public String logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return "home";
    }

    @GetMapping("/members/editHome")
    public String editHome() {
        return "editHome";
    }

    @GetMapping("/members/edit")
    public String editForm(@ModelAttribute ChangePasswordForm changePassword) {
        return "edit";
    }

    @PostMapping("/members/edit")
    public String edit(@Validated @ModelAttribute ChangePasswordForm changePassword,
                       BindingResult bindingResult, @Login Member member,
                       HttpServletRequest request) {
        log.info("member={}", member);

        String nowPassword = changePassword.getNowPassword();
        String newPassword = changePassword.getNewPassword();
        String checkNewPassword = changePassword.getCheckNewPassword();

        log.info("현재 비밀번호={}", nowPassword);
        log.info("새로운 비밀번호={}", newPassword);
        log.info("새로운 비밀번호 확인={}", checkNewPassword);

        if (!nowPassword.equals(member.getPassword())) {
            bindingResult.reject("checkFail", "현재 비밀번호와 다릅니다.");
            return "edit";
        }

        if (newPassword.equals(member.getPassword())) {
            bindingResult.reject("sameFail", "현재 비밀번호와 같습니다.");
            return "edit";
        }

        if (!newPassword.equals(checkNewPassword)) {
            bindingResult.reject("changeFail", "새 비밀번호와 다릅니다.");
            return "edit";
        }

        memberService.update(member.getId(), newPassword);
        HttpSession session = request.getSession(false);
        session.invalidate();

        return "redirect:/";
    }

    @GetMapping("/members/remove")
    public String remove(@ModelAttribute WithdrawalForm withdrawalForm) {
        return "removeForm";
    }

    @PostMapping("/members/remove")
    public String removeMember(@Validated @ModelAttribute WithdrawalForm withdrawalForm,
                               BindingResult bindingResult, @Login Member member,
                               HttpServletRequest request) {

        String withdrawal = withdrawalForm.getWithdrawal();

        if (bindingResult.hasErrors()) {
            log.info("error={}", bindingResult);
            return "removeForm";
        }

        if (!withdrawal.equals("회원 탈퇴")) {
            bindingResult.reject("withdrawalFail", "탈퇴를 위하시면 회원 탈퇴를 입력해주세요.");
            return "removeForm";
        }

        memberService.delete(member);
        HttpSession session = request.getSession(false);
        session.invalidate();

        return "home";
    }

    @GetMapping("/members/findPassword")
    public String findPasswordForm(@ModelAttribute FindPasswordForm findPasswordForm) {
        return "findPasswordForm";
    }

    @PostMapping("/members/findPassword")
    public String findPassword(@Validated @ModelAttribute FindPasswordForm findPasswordForm,
                               BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            log.info("errors={}", bindingResult);
            return "findPasswordForm";
        }

        String loginId = findPasswordForm.getLoginId();
        Optional<Member> findMember = memberService.findByLoginId(loginId);

        if (findMember.isEmpty()) {
            bindingResult.reject("findFail", "존재하지않는 아이디입니다.");
            return "findPasswordForm";
        }

        Member member = findMember.get();
        model.addAttribute("password", member.getPassword());
        return "findPassword";
    }

    @GetMapping("/members/findAllMember")
    public String findAllMember(Model model) {
        List members = memberService.findAll();
        model.addAttribute("members", members);
        return "findAll";
    }
}
