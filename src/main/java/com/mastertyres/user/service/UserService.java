package com.mastertyres.user.service;

import com.mastertyres.user.model.User;
import com.mastertyres.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
@Service
public class UserService implements IUserService{

    @Autowired
    private UserRepository userRepository;

    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @Transactional
    @Override
    public void guardarUsuario(User usuario) {
        userRepository.save(usuario);
    }

    @Transactional(readOnly = true)
    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Transactional(readOnly = true)
    @Override
    public User findUser(String username, String correo) {
        return userRepository.findUser(username,correo);
    }

    @Transactional(readOnly = true)
    @Override
    public User findUserById(Integer id) {
        return userRepository.findUserById(id);
    }

    @Transactional
    @Override
    public void updatePassword(String password, Integer id) {
        userRepository.updatePassword(password,id);
    }

    @Transactional
    @Override
    public void updateNextCheck(Integer usuarioId, String fecha) {
        userRepository.updateNextCheck(usuarioId,fecha);
    }

    @Transactional
    @Override
    public void actualizarUpdateAt(Integer userId, String updateAt) {
        userRepository.actualizarUpdateAt(userId,updateAt);
    }


}//class
