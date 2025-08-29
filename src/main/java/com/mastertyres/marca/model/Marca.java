package com.mastertyres.marca.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "marcas")
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
@Builder
public class Marca {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "marca_id")
    private Integer marcaId;
    @Column(name = "nombre")
    private String nombreMarca;

    @Override
    public String toString(){
        return nombreMarca;
    }


}//clase
