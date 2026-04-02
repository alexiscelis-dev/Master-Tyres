package com.mastertyres.modelo.entity;

import com.mastertyres.marca.entity.Marca;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "modelo")
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
@Builder
public class Modelo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer modeloId;

    @Column(name = "nombre")
    private String nombreModelo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "marca_id")
    private Marca marca_id;


    @Override
    public String toString(){
        return nombreModelo;
    }

}

