package com.mastertyres.user.service;

import com.mastertyres.common.exeptions.UserException;
import com.mastertyres.user.entity.User;
import com.mastertyres.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

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

    @Scheduled(cron = "0 0 0 */3 * ?", zone = "America/Mexico_City")
    @Transactional
    @Override
    public void actualizarNextCheckAutomatico() {
        String fecha = LocalDateTime.now().plusDays(3).toString();
        userRepository.actualizarNextCheckAutomatico(fecha); //Cada 3 dias
    }

    @Transactional(readOnly = true)
    @Override
    public List<User> listarUsuarios() {
        return userRepository.listarUsuarios();
    }

    @Modifying
   public void eliminarUsuarioLocal(Integer usuarioId){

        try {
            userRepository.deleteById(usuarioId);
        }catch (Exception e) {
            throw new UserException("Ocurrio un error al eliminar el usuario proporcionado. \n" + e);
        }

    }


}//class
