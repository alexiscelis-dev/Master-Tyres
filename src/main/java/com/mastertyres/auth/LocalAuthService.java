package com.mastertyres.auth;

import com.mastertyres.common.exeptions.UserException;
import com.mastertyres.user.model.TipoLicencia;
import com.mastertyres.user.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class LocalAuthService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    public boolean authenticate(User localUser,String password){

        verificarLicencia(localUser);

        if (passwordEncoder.matches(password,localUser.getPassword())){
            return true;
        }else {
            return false;
        }

    }

    private void verificarLicencia(User localUser){
        if ((localUser.getStatusLicencia().equals(TipoLicencia.SUSPENDED.toString()))){
            throw new UserException("Cuenta momentaneamente suspendida.");
        }
    }


}//class
