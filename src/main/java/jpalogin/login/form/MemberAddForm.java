package jpalogin.login.form;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Pattern;

@Getter @Setter
public class MemberAddForm {

    @Pattern(regexp = "^[a-zA-Z]{1}[a-zA-Z0-9]{8,12}$", message = "대소문자를 포함한 8~12길이로 작성하세요.")
    private String loginId;

    @Pattern(regexp = "^(?=.*\\d)(?=.*[~`!@#$%\\^&*()-])(?=.*[a-z]).{8,16}$",
            message = "특수문자를 포함한 8~16길이로 작성하세요")
    private String password;
    private String checkPassword;

}
