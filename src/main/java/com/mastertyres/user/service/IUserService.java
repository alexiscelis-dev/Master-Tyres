package com.mastertyres.user.service;

import com.mastertyres.user.entity.User;

import java.util.List;

public interface IUserService {

   void guardarUsuario(User usuario);

   User findByEmail(String email);

   User findUser(String username,String correo);

   User findUserById(Integer id);

   void updatePassword(String password,Integer id);

   void updateNextCheck(Integer usuarioId, String fecha);

   void actualizarUpdateAt(Integer userId, String updateAt);

   void actualizarNextCheckAutomatico();

   List<User> listarUsuarios();



}//interface
