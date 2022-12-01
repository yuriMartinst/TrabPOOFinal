package br.yuri.univille.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class Tarefa {
    private int id;
    private String tituloDaTarefa;
    private String detalheDaTarefa;
    private LocalDate dataCriacao = LocalDate.now();
    private LocalDate dataConclusao;
    private boolean concluida;
    private Lista lista;
}
