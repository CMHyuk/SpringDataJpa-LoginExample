package jpalogin.login.form;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ChangePasswordForm {

    private String nowPassword;
    private String newPassword;
    private String checkNewPassword;
}
