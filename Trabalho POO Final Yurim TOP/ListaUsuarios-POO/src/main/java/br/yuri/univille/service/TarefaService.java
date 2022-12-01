package br.yuri.univille.service;

import br.yuri.univille.dao.TarefaDao;
import br.yuri.univille.model.Tarefa;

import javax.swing.*;
import java.time.LocalDate;

public class TarefaService {

    private final TarefaDao tarefaDao;

    public TarefaService(TarefaDao tarefaDao) {
        this.tarefaDao = tarefaDao;
    }

    public void adicionarTarefaEmUmaLista(Tarefa tarefa) {

        if (tarefa.getTituloDaTarefa().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Por favor, insira um título para a tarefa");
            throw new IllegalArgumentException("O título da tarefa não pode ser vazio");
        }

        if (tarefa.isConcluida()) {
            tarefa.setDataConclusao(LocalDate.now());
        }

        tarefa.setId(tarefaDao.getProximoId());
        tarefaDao.criarTarefa(tarefa);
    }

    public void concluirTarefa(int idTarefa) {
        Tarefa tarefa = tarefaDao.buscarTarefaPorId(idTarefa);
        tarefa.setConcluida(true);
        tarefa.setDataConclusao(LocalDate.now());
        tarefaDao.atualizarTarefa(tarefa);
    }

    public void desfazerTarefa(int idTarefa) {
        Tarefa tarefa = tarefaDao.buscarTarefaPorId(idTarefa);
        tarefa.setConcluida(false);
        tarefa.setDataConclusao(null);
        tarefaDao.atualizarTarefa(tarefa);
    }

    public void excluirTarefa(int idTarefa) {
        Tarefa tarefa = tarefaDao.buscarTarefaPorId(idTarefa);
        tarefaDao.deletarTarefa(tarefa.getId());
    }

    public Tarefa listarUmaTarefa(int idTarefa) {
        Tarefa tarefa = tarefaDao.buscarTarefaPorId(idTarefa);
        return tarefa;
    }
}
