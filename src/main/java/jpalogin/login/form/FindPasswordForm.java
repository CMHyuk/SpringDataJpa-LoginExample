package jpalogin.login.form;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter @Setter
public class FindPasswordForm {

    @NotEmpty(message = "아이디를 입력해주세요.")
    private String loginId;
}
