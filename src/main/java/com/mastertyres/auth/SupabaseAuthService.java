package com.mastertyres.auth;

import com.mastertyres.common.exeptions.UserException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class SupabaseAuthService {
    @Value("${supabase.url}")
    private String supabaseUrl;
    @Value("${supabase.apikey}")
    private String apiKey;




    private final RestTemplate restTemplate = new RestTemplate();

    public SupabaseUserResponse authenticate(String email, String password) {
        System.out.println("Autenticando con supabase");

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.set("apikey", apiKey);
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, String> body = new HashMap<>();
        body.put("email", email);
        body.put("password", password);

        HttpEntity<Map<String, String>> request = new HttpEntity<>(body, headers);


        try {

            ResponseEntity<Map> response = restTemplate.postForEntity(
                    supabaseUrl + "/auth/v1/token?grant_type=password",
                    request,
                    Map.class
            );

            if (response.getStatusCode().is2xxSuccessful()) {
                Map<String, Object> responseBody = response.getBody();

                String accessToken = (String) responseBody.get("access_token");
                String refreshToken = (String) responseBody.get("refresh_token");

                Map<String, Object> userMap = (Map<String, Object>) responseBody.get("user");

                SupabaseUserResponse supabaseUser = new SupabaseUserResponse();
                supabaseUser.setAccessToken(accessToken);
                supabaseUser.setRefreshToken(refreshToken);
                supabaseUser.setUsuarioId((String) userMap.get("id"));
                supabaseUser.setUsername((String) userMap.get("username"));


                return supabaseUser;

            }

        } catch (HttpClientErrorException cliEx) {
            if (cliEx.getStatusCode() == HttpStatus.UNAUTHORIZED) {
                throw new UserException("Credenciales invalidas");
            }
            if (cliEx.getStatusCode() == HttpStatus.BAD_REQUEST) {
                throw new UserException("Credenciales inválidas");
            }

            throw new UserException("Credenciales invalidas");
        } catch (ResourceAccessException reEx) {
            throw new UserException("No se pudo conectar con el servidor, intenta más tarde.");
        } catch (HttpServerErrorException serverEx) {
            throw new UserException("Error interno del servidor");
        } catch (Exception e) {
            throw new UserException("Error inesperado");
        }
        return null;

    }

    public void actualizarPassword(String authId, String password) {

        try {

            String url = supabaseUrl + "/auth/v1/admin/users/" + authId;

            HttpHeaders headers = new HttpHeaders();
            headers.set("apikey", apiKey);
            headers.setBearerAuth(apiKey);
            headers.setContentType(MediaType.APPLICATION_JSON);

            Map<String, Object> body = new HashMap<>();
            body.put("password", password);
/*
            String body = """
                    {
                        "password": "%s"
                    }
                    """.formatted(password);

 */

            HttpEntity<Map<String,Object>> request = new HttpEntity<Map<String, Object>>(body, headers);

            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.PUT, request, String.class);


        } catch (HttpClientErrorException e) {
            e.printStackTrace();


            throw new UserException("No fue posible actualizar la contraseña. Verifique los datos.");

        } catch (ResourceAccessException e) {
            e.printStackTrace();


            throw new UserException("No se pudo conectar con el servidor. Verifique su conexión a internet.");

        } catch (HttpServerErrorException e) {
            e.printStackTrace();


            throw new UserException(
                    "El servidor presentó un problema. Intente nuevamente más tarde."
            );
        } catch (Exception e) {
            e.printStackTrace();

            throw new UserException("Error inesperado al cambiar la contraseña.");
        }


    }//cambiarPassword

}//class
