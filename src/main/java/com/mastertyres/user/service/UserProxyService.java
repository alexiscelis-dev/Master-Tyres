package com.mastertyres.user.service;

import com.mastertyres.user.entity.User;
import com.mastertyres.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserProxyService implements IUserService{

    private UserRepository userRepository;

    @Autowired
    public UserProxyService(UserRepository userRepository){
        this.userRepository = userRepository;
    }


    @Override
    public void guardarUsuario(User usuario) {

    }

    @Override
    public User findByEmail(String email) {
        return null;
    }

    @Override
    public User findUser(String username, String correo) {
        return null;
    }

    @Override
    public User findUserById(Integer id) {
        return null;
    }

    @Override
    public void updatePassword(String password, Integer id) {

    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public void updateNextCheck(Integer usuarioId, String fecha) {
        userRepository.updateNextCheck(usuarioId,fecha);
    }

    @Override
    public void actualizarUpdateAt(Integer userId, String updateAt) {

    }


    @Override
    public void actualizarNextCheckAutomatico() {

    }

    @Override
    public List<User> listarUsuarios() {
        return List.of();
    }
}//class
