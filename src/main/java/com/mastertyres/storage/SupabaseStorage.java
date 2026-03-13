package com.mastertyres.storage;

import com.mastertyres.common.exeptions.RespaldoException;
import com.mastertyres.respaldo.model.Respaldo;
import com.mastertyres.respaldo.model.StatusRespaldo;
import com.mastertyres.respaldo.model.TipoRespaldo;
import com.mastertyres.respaldo.service.RespaldoProxyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
public class SupabaseStorage {
    @Value("${supabase.url}")
    private String supabaseUrl;
    @Value("${supabase.api.key}")
    private String supabaseApiKey;
    @Value("${supabase.url.bucket}")
    private String supabaseBucket;

    @Autowired
    private RespaldoProxyService respaldoProxyService;

    public void crearRespaldo(File archivoRespaldo, Respaldo archivoDatos) throws RespaldoException {

        try {

            HttpClient httpClient = HttpClient.newHttpClient();

            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(URI.create(
                            supabaseUrl + "/storage/v1/object/" +
                                    supabaseBucket + "/" +
                                    archivoRespaldo.getName()))
                    .header("apikey", supabaseApiKey)
                    .header("Authorization", "Bearer " + supabaseApiKey)
                    .header("Content-Type", "application/octet-stream")
                    .PUT(HttpRequest.BodyPublishers.ofFile(archivoRespaldo.toPath()))
                    .build();

            HttpResponse<String> httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());


            if (httpResponse.statusCode() < 200 || httpResponse.statusCode() >= 300) {
                respaldoProxyService.actualizarEstado(archivoDatos.getRespaldoId(), StatusRespaldo.FALLIDO.toString());
                throw new RespaldoException("El respaldo se generó localmente, pero no se pudo almacenar en la nube.");
            }

            respaldoProxyService.actualizarTipoRespaldo(archivoDatos.getRespaldoId(), TipoRespaldo.CLOUD.toString());


        } catch (Exception e) {
            respaldoProxyService.actualizarEstado(archivoDatos.getRespaldoId(), StatusRespaldo.FALLIDO.toString());
            throw new RespaldoException("El respaldo no se pudo almacenar en la nube. Verifique su conexión \ne intente nuevamente.");
        }

    }//crearRespaldo

}//class
