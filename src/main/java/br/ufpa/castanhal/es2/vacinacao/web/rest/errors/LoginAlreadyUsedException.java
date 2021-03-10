package br.ufpa.castanhal.es2.vacinacao.web.rest.errors;

public class LoginAlreadyUsedException extends BadRequestAlertException {
    private static final long serialVersionUID = 1L;

    public LoginAlreadyUsedException() {
        super(ErrorConstants.LOGIN_ALREADY_USED_TYPE, "Login name already used!", "userManagement", "userexists");
    }
}
