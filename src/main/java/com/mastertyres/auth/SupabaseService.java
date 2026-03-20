package com.mastertyres.auth;

import com.mastertyres.common.exeptions.UserException;

import com.mastertyres.user.model.User;
import com.mastertyres.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;


@Service
public class SupabaseService {

    @Value("${supabase.url}")
    private String supabaseUrl;


    @Value("${supabase.apikey}")
    private String apiKey;

    @Autowired
    private SupabaseAuthService supabaseAuthService;
    @Autowired
    private UserService userService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    private final RestTemplate restTemplate = new RestTemplate();


    public User findUsuarioById(String authId) throws Exception {


        String url = supabaseUrl + "/rest/v1/usuario?auth_id=eq." + authId;
        System.out.println(authId);

        HttpHeaders headers = new HttpHeaders();
        headers.set("apikey", apiKey);

        headers.set("Authorization", "Bearer " + apiKey);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Void> request = new HttpEntity<>(headers);

        ResponseEntity<User[]> response =
                restTemplate.exchange(
                        url,
                        HttpMethod.GET,
                        request,
                        User[].class
                );

        User[] users = response.getBody();

        return (users != null && users.length > 0) ? users[0] : null;

    }//findUsuarioById


    //Metodo que se utiliza para recuperar contraseña
    public User findUsuarioByCorreo(String correo) throws Exception {


        String url = supabaseUrl + "/rest/v1/usuario?correo=eq." + correo;


        HttpHeaders headers = new HttpHeaders();
        headers.set("apikey", apiKey);

        headers.set("Authorization", "Bearer " + apiKey);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Void> request = new HttpEntity<>(headers);

        ResponseEntity<User[]> response =
                restTemplate.exchange(
                        url,
                        HttpMethod.GET,
                        request,
                        User[].class
                );

        User[] users = response.getBody();

        return (users != null && users.length > 0) ? users[0] : null;

    }//findUsuarioByCorreo


    public void actualizarPassword(String authId, User user, String password) {
        try {
            // Construye la URL con el filtro por auth_id
            String url = supabaseUrl + "/rest/v1/usuario?auth_id=eq." + authId;

            user.setUpdatedAt(LocalDateTime.now().toString());

            // Construye el JSON body
            Map<String, Object> body = new HashMap<>();
            body.put("contrasena", password);
            body.put("updated_at", user.getUpdatedAt());


            // Crea el WebClient
            WebClient webClient = WebClient.builder()
                    .baseUrl(url)
                    .defaultHeader("apikey", apiKey)
                    .defaultHeader("Authorization", "Bearer " + apiKey)
                    .defaultHeader("Content-Type", "application/json")
                    .defaultHeader("Prefer", "return=representation")
                    .build();

            // Hace el PATCH y obtiene la respuesta
            String response = webClient.patch()
                    .bodyValue(body)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block(); // bloqueamos hasta recibir respuesta



        } catch (WebClientResponseException e) {
            e.printStackTrace();
            throw new UserException("No fue posible actualizar la contraseña. Verifique los datos.");

        } catch (WebClientRequestException e) {
            e.printStackTrace();
            throw new UserException("No se pudo conectar con el servidor. Verifique su conexión a internet.");

        } catch (ResourceAccessException e) {
            e.printStackTrace();
            throw new UserException("No se pudo conectar con el servidor. Verifique su conexión a internet.");

        } catch (Exception e) {
            e.printStackTrace();
            throw new UserException("Error inesperado al cambiar la contraseña.");
        }
    }//actualizarPassword

    public void supabaseLogin(String correo, String password) {
        SupabaseUserResponse supabaseUserAuth = supabaseAuthService.authenticate(correo, password);
        User userSupabaseJson = null;

        try {
            userSupabaseJson = findUsuarioById(supabaseUserAuth.getUsuarioId());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


        if (userSupabaseJson == null) {
            throw new UserException("Usuario no encontrado");
        }


        if (supabaseUserAuth == null) {
            throw new UserException("Usuario o contraseña incorrectos");
        }

        //Se busca el usuario para saber si forzar actualizar o no
        User userInsert = userService.findUser(userSupabaseJson.getUsername(), userSupabaseJson.getCorreo());


        User userActualizar = User.builder()
                .usuarioId(userInsert != null ? userInsert.getUsuarioId() : null)
                .authId(userSupabaseJson.getAuthId())
                .username(userSupabaseJson.getUsername())
                .password(passwordEncoder.encode(userSupabaseJson.getPassword()))
                .role(userSupabaseJson.getRole())
                .nombre(userSupabaseJson.getNombre())
                .apellidos(userSupabaseJson.getApellidos())
                .fotoPerfil(userSupabaseJson.getFotoPerfil())
                .createdAt(userSupabaseJson.getCreatedAt())
                .updatedAt(userSupabaseJson.getUpdatedAt())
                .active(userSupabaseJson.getActive())
                .statusLicencia(userSupabaseJson.getStatusLicencia())
                .correo(userSupabaseJson.getCorreo())
                .nextCheck(LocalDateTime.now().plusDays(3).toString())
                .build();

        userService.guardarUsuario(userActualizar);

    }//supabaseLogin


    public void actualizarUsername(String authId, User user) {
        try {
            // Construye la URL con el filtro por auth_id
            String url = supabaseUrl + "/rest/v1/usuario?auth_id=eq." + authId;

            user.setUpdatedAt(LocalDateTime.now().toString());

            // Construye el JSON body
            Map<String, Object> body = new HashMap<>();
            body.put("username", user.getUsername());
            body.put("updated_at", user.getUpdatedAt());


            // Crea el WebClient
            WebClient webClient = WebClient.builder()
                    .baseUrl(url)
                    .defaultHeader("apikey", apiKey)
                    .defaultHeader("Authorization", "Bearer " + apiKey)
                    .defaultHeader("Content-Type", "application/json")
                    .defaultHeader("Prefer", "return=representation")
                    .build();

            // Hace el PATCH y obtiene la respuesta
            String response = webClient.patch()
                    .bodyValue(body)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block(); // bloqueamos hasta recibir respuesta


        } catch (WebClientResponseException e) {
            e.printStackTrace();
            throw new UserException("No fue posible actualizar el nombre de usuario. Verifique los datos.");

        } catch (WebClientRequestException e) {
            e.printStackTrace();
            throw new UserException("No se pudo conectar con el servidor. Verifique su conexión a internet.");

        } catch (ResourceAccessException e) {
            e.printStackTrace();
            throw new UserException("No se pudo conectar con el servidor. Verifique su conexión a internet.");

        } catch (Exception e) {
            e.printStackTrace();
            throw new UserException("Error inesperado al actualizar usuario.");
        }
    }//actualizarUsername


    public void actualizarFotoPerfil(String authId, User user) {
        try {
            // Construye la URL con el filtro por auth_id
            String url = supabaseUrl + "/rest/v1/usuario?auth_id=eq." + authId;

            user.setUpdatedAt(LocalDateTime.now().toString());

            Map<String, Object> body = new HashMap<>();
            body.put("foto_perfil", user.getFotoPerfil());
            body.put("updated_at", user.getUpdatedAt());


            // Crea el WebClient
            WebClient webClient = WebClient.builder()
                    .baseUrl(url)
                    .defaultHeader("apikey", apiKey)
                    .defaultHeader("Authorization", "Bearer " + apiKey)
                    .defaultHeader("Content-Type", "application/json")
                    .defaultHeader("Prefer", "return=representation")
                    .build();

            // Hace el PATCH y obtiene la respuesta
            String response = webClient.patch()
                    .bodyValue(body)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block(); // bloqueamos hasta recibir respuesta


        } catch (WebClientResponseException e) {
            e.printStackTrace();
            throw new UserException("No fue posible actualizar la foto de perfil. Verifique los datos.");

        } catch (WebClientRequestException e) {
            e.printStackTrace();
            throw new UserException("No se pudo conectar con el servidor. Verifique su conexión a internet.");

        } catch (ResourceAccessException e) {
            e.printStackTrace();
            throw new UserException("No se pudo conectar con el servidor. Verifique su conexión a internet.");

        } catch (Exception e) {
            e.printStackTrace();
            throw new UserException("Error inesperado al actualizar la foto de perfil.");
        }
    }//actualizarFotoPerfil


}//class
