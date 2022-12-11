package jpalogin.login.form;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter @Setter
public class WithdrawalForm {

    @NotEmpty(message = "회원 탈퇴를 입력해주세요")
    private String withdrawal;
}
