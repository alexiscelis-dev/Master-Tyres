package com.mastertyres.user.repository;

import com.mastertyres.user.model.User;
import jakarta.persistence.QueryHint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User,Integer> {

    final String SELECT_USER = "SELECT u FROM User u";

    final String UPDATE_USER = "UPDATE User u SET";


    @QueryHints(@QueryHint(name = org.hibernate.annotations.QueryHints.READ_ONLY, value = "true"))
    @Query(SELECT_USER + " " + "WHERE u.correo = :email")
    User findByEmail(@Param("email")String email);


    @QueryHints(@QueryHint(name = org.hibernate.annotations.QueryHints.READ_ONLY, value = "true"))
    @Query(SELECT_USER + " " + "WHERE u.username = :username AND u.correo = :correo")
    User findUser(@Param("username")String username,@Param("correo")String correo);


    @QueryHints(@QueryHint(name = org.hibernate.annotations.QueryHints.READ_ONLY, value = "true"))
    @Query(SELECT_USER + " " + "WHERE u.id = :id")
    User findUserById(@Param("id")Integer id);

    @Modifying
    @Query(UPDATE_USER + " " + "u.password = :password WHERE u.id = :id")
    void updatePassword(@Param("password")String password,@Param("id")Integer id);

    @Modifying
    @Query(UPDATE_USER + " " +"u.nextCheck = :fecha WHERE u.usuarioId = :usuarioId")
    void updateNextCheck(@Param("usuarioId")Integer usuarioId,@Param("fecha") String fecha);



}//interface
