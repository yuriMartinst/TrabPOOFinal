package br.yuri.univille.ui;


import br.yuri.univille.model.Lista;
import br.yuri.univille.model.Tarefa;
import br.yuri.univille.service.ListaService;
import br.yuri.univille.service.TarefaService;

import javax.swing.*;
import java.awt.*;

public class NovaTarefa extends JFrame {

    private final ListaService listaService;
    private final TarefaService tarefaService;

    private JButton btnSalvar;
    private JButton btnCancelar;
    private JTextArea txtTitulo;
    private JTextArea txtTexto;
    private int comboPrioridade;
    private JCheckBox checkConcluida;
    private Lista lista;

    public NovaTarefa(ListaService listaService, TarefaService tarefaService, Lista lista) {

        this.listaService = listaService;
        this.tarefaService = tarefaService;
        this.lista = lista;

        setTitle("Cadastro de Tarefas");
        setSize(500, 500);
        setLayout(null);
        setResizable(false);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        this.txtTitulo = UtilsMethods.newJtextArea(50, 50, 25, 380, "Titulo da tarefa", this);
        this.txtTexto = UtilsMethods.newJtextArea(50, 100, 190, 380, "Detalhe da tarefa", this);
        this.comboPrioridade = this.campoSelectionPrioridade();
        this.checkConcluida = UtilsMethods.newCheckBox(50, 350, 10, 20, "Finalizado", this);
        this.btnCancelar = UtilsMethods.newBtn(200, 425, 20, 100, "Cancelar", this);
        this.btnSalvar = UtilsMethods.newBtn(325, 425, 20, 100, "Salvar", this);

        this.cancelarTarefa();
        this.salvarTarefa();

    }

    private void cancelarTarefa() {
        btnCancelar.addActionListener(e -> {
            this.dispose();
            new TelaTarefas(listaService, tarefaService, lista);
        });
    }

    private void salvarTarefa() {
        btnSalvar.addActionListener(e -> {

            Tarefa tarefa = new Tarefa();
            tarefa.setTituloDaTarefa(txtTitulo.getText());
            tarefa.setConcluida(checkConcluida.isSelected());
            tarefa.setDetalheDaTarefa(txtTexto.getText());
            tarefa.setLista(lista);

            tarefaService.adicionarTarefaEmUmaLista(tarefa);

            this.dispose();
            new TelaTarefas(listaService, tarefaService, lista);
        });

    }

    private int campoSelectionPrioridade() {
        Label labelPrioridade = new Label("Prioridade da Tarefa");
        labelPrioridade.setBounds(50, 280, 70, 10);
        add(labelPrioridade);

        JComboBox comboBoxPrioridade = new JComboBox();
        comboBoxPrioridade.setBounds(50, 300, 375, 30);
        comboBoxPrioridade.addItem("Baixa");
        comboBoxPrioridade.addItem("Média");
        comboBoxPrioridade.addItem("Alta");
        add(comboBoxPrioridade);

        return comboBoxPrioridade.getSelectedIndex();
    }
}
