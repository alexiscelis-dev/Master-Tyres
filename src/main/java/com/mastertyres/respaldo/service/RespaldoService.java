package com.mastertyres.respaldo.service;

import com.mastertyres.common.exeptions.RespaldoException;
import com.mastertyres.respaldo.entity.Respaldo;
import com.mastertyres.respaldo.entity.StatusRespaldo;
import com.mastertyres.respaldo.entity.TipoRespaldo;
import com.mastertyres.respaldo.repository.RespaldoRepository;
import com.mastertyres.storage.SupabaseStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class RespaldoService implements IRespaldoService {

    private RespaldoRepository respaldoRepository;
    @Autowired
    @Lazy //Le dice a spring que se inyecte solo cuando se necesite y no cuando se crea el bean
    private RespaldoProxyService respaldoProxyService;
    @Autowired
    private SupabaseStorage supabaseStorage;


    @Autowired
    public RespaldoService(RespaldoRepository respaldoRepository) {
        this.respaldoRepository = respaldoRepository;
    }

    @Value("${app.ruta.respaldos}")
    private String rutaRespaldo;
    @Value("${user.home}")
    private String windowsUser;
    @Value("${DB_USER}")
    private String usernameDB;
    @Value("${DB_PASSWORD}")
    private String passwordDB;
    @Value(("${DB_DATABASE}"))
    private String dataBaseName;
    @Value("${mysql.dump.ruta}")
    private String mysqlDumpRuta;
    @Value("${mysql.port}")
    private String mysqlPort;




    @Override
    public boolean generarRespaldo() {

        try {

            File carpeta = new File(rutaRespaldo);

            if (!carpeta.exists()) {
                carpeta.mkdirs();



                //Despues se quitara de comentarios por ahora se necesita asi para ver los respaldos que vamos haciendo
                try {
                    Files.setAttribute(carpeta.toPath(), "dos:hidden", true);

                }catch (Exception e){
                    e.printStackTrace();
                }


            }

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");
            String fecha = LocalDateTime.now().format(formatter);

            String nombreArchivo = "respaldo_" + fecha + ".sql";
            String ruta = rutaRespaldo + File.separator + nombreArchivo;


            ProcessBuilder processBuilder = new ProcessBuilder(

                    mysqlDumpRuta,
                    "-u", usernameDB,
                    "-p" + passwordDB,
                    "--port=" + mysqlPort ,
                    "--protocol=TCP",
                    dataBaseName
            );

            processBuilder.redirectOutput(new File(ruta));

            Process process = processBuilder.start();

            BufferedReader errorReader = new BufferedReader(
                    new InputStreamReader(process.getErrorStream())
            );

            StringBuilder errores = new StringBuilder();
            String linea;
            while ((linea = errorReader.readLine()) != null) {
                errores.append(linea).append("\n");
            }


            int exitCode = process.waitFor();

            if (exitCode != 0) {
                throw new RespaldoException("Error interno: " + errores);

            }


            File archivoRespaldo = new File(ruta);
            long tamanioBytes = archivoRespaldo.length();

            Respaldo respaldo = Respaldo.builder()
                    .nombreArchivo(nombreArchivo)
                    .rutaArchivo(ruta)
                    .tamanioBytes(tamanioBytes)
                    .estado(exitCode == 0 ? StatusRespaldo.COMPLETO.toString() : StatusRespaldo.FALLIDO.toString())
                    .codigoError(exitCode == 0 ? null : errores.toString())
                    .tipoRespaldo(TipoRespaldo.LOCAL.toString())
                    .createdAt(LocalDateTime.now().toString())
                    .build();

            respaldoProxyService.guardarRespaldo(respaldo);
            supabaseStorage.crearRespaldo(archivoRespaldo, respaldo);
            return true;


        } catch (Exception e) {
            e.printStackTrace();

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");
            String fecha = LocalDateTime.now().format(formatter);

            String nombreArchivo = "respaldo_" + fecha + ".sql";
            String ruta = rutaRespaldo + File.separator + nombreArchivo;


            Respaldo archivoRespaldo = Respaldo.builder()
                    .nombreArchivo(nombreArchivo)
                    .rutaArchivo(ruta)
                    .tamanioBytes(null)
                    .estado(StatusRespaldo.FALLIDO.toString())
                    .codigoError(e.toString())
                    .tipoRespaldo(TipoRespaldo.LOCAL.toString())
                    .createdAt(LocalDateTime.now().toString())
                    .build();

            respaldoProxyService.guardarRespaldo(archivoRespaldo);
            if (e instanceof RespaldoException)
                throw new RespaldoException("" + e.getMessage());
            else
                throw new RespaldoException("Error interno. Ocurrio un error inesperado al crear el respaldo.");

        }


    }//generarRespaldo

    @Override
    public void guardarRespaldo(Respaldo archivoRespaldo) {
    }

    @Override
    public void actualizarEstado(Integer id, String estado) {

    }

    @Transactional(readOnly = true)
    @Override
    public Respaldo ObtenerUltimoRespaldo() {
        return respaldoRepository.obtenerUltimoRespaldo();
    }

    @Override
    public void actualizarTipoRespaldo(Integer respaldoId, String tipoRespaldo) {

    }

    @Transactional(readOnly = true)
    @Override
    public List<Respaldo> listarRespaldos(String fechaInicio, String fechaFin) {
        return respaldoRepository.listarRespaldos(fechaInicio, fechaFin);
    }


}//class
