package com.mastertyres.security;

import com.mastertyres.common.exeptions.UserException;
import com.mastertyres.user.model.User;
import com.mastertyres.user.service.UserService;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;




@Service
public class SecurityPassword {
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserService userService;

    public void cambiarPassword(User userSession,String passwordValidator){
        User user = userService.findUser(userSession.getUsername(),userSession.getCorreo());

        if (user == null){
            throw new UserException("Error interno: Ocurrio un problema al obtener los datos del usuario");
        }

        if (passwordEncoder.matches(passwordValidator,user.getPassword())){

         //   userService.updatePassword(passwordEncoder.encode(passwordValidator),user.getUsuarioId());

        }


    }//cambiarContrasena

    public boolean verificarPasswordActual(PasswordField password, Label lblContrasenaError, User userSession){
        if(!passwordEncoder.matches(password.getText(),userSession.getPassword())){
            lblContrasenaError.setVisible(true);
            password.setStyle("-fx-text-fill: red;");
            return false;
        }else {
            lblContrasenaError.setVisible(false);
            password.setStyle("");
            return true;
        }
    }

    public boolean verificarNewPassword(PasswordField pfpass1, PasswordField pfPass2, TextField tfPassword , Label lblContrasenaError2){

        if(!pfpass1.getText().equals(pfPass2.getText())){
            lblContrasenaError2.setText("Las contraseñas no coinciden");
            lblContrasenaError2.setVisible(true);
            pfPass2.setStyle("-fx-border-color: red;");
            tfPassword.setStyle("-fx-border-color: red;");
            return false;

        }else {
            lblContrasenaError2.setVisible(false);
            pfpass1.setStyle("");
            pfPass2.setStyle("");
            tfPassword.setStyle("");
            return true;
        }

    }//verificarNewPassword

}//class
