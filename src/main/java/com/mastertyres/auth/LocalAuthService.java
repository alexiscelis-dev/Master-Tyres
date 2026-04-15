package com.mastertyres.auth;

import com.mastertyres.common.exeptions.UserException;
import com.mastertyres.user.entity.TipoLicencia;
import com.mastertyres.user.entity.User;
import com.mastertyres.user.service.UserProxyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class LocalAuthService {

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserProxyService userProxyService;

    public boolean authenticate(User localUser,String password){


        verificarLicencia(localUser);

        if (actualizarNextCheck(localUser.getNextCheck())){
            userProxyService.updateNextCheck(localUser.getUsuarioId(), LocalDateTime.now().plusDays(3).toString());
        }


        if (passwordEncoder.matches(password,localUser.getPassword())){
            return true;
        }else {
            return false;
        }




    }

    private void verificarLicencia(User localUser){
        if ((localUser.getStatusLicencia().equals(TipoLicencia.SUSPENDED.toString()))){
            userProxyService.updateNextCheck(localUser.getUsuarioId(), LocalDateTime.now().toString()); //Obliga authenticarse en supabase para que se pueda volver activar la licencia
            throw new UserException("Cuenta momentaneamente suspendida.");
        }
    }

    private boolean actualizarNextCheck(String fecha){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        LocalDateTime ldFecha = LocalDateTime.parse(fecha,formatter);

        if (ldFecha.isBefore(LocalDateTime.now())){
            return true;
        }else {
            return false;
        }
    }


}//class
