package br.yuri.univille.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;

@Getter
@Setter
@NoArgsConstructor
public class Lista {
    private int id;
    private String tituloDaLista;
    private LocalDate dataCriacao = LocalDate.now();
    private boolean excluida;
    private ArrayList<Tarefa> tarefas;
}
