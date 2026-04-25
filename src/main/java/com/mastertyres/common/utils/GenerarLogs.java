package com.mastertyres.common.utils;

import com.mastertyres.common.exeptions.RespaldoException;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class GenerarLogs {

    public static void generarLogRespaldo(File file, String fechaInicio, String fechaFin, String logContenido) throws RespaldoException {


            try {
                BufferedWriter writer = new BufferedWriter(new FileWriter(file));
                writer.newLine();
                writer.write("Logs del sistema tabla 'Respaldos'." );
                writer.newLine();
                writer.write("Fecha de inicio: " + fechaInicio + ".");
                writer.newLine();
                writer.write("Fecha de fin: " + fechaFin + ".");
                writer.newLine();
                writer.newLine();


                writer.write(logContenido);
                writer.close();


            }catch (IOException e){
                throw new RespaldoException("Error al crear el archivo de logs: " + e.getClass().getName() + " - " + e.getMessage());
            }


    }//generarLogRespaldo


}//class
