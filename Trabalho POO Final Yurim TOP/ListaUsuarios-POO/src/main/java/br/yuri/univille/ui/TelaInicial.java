package br.yuri.univille.ui;

import br.yuri.univille.model.Lista;
import br.yuri.univille.service.ListaService;
import br.yuri.univille.service.TarefaService;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class TelaInicial extends JFrame {

    private final ListaService listaService;
    private final TarefaService tarefaService;

    public TelaInicial(ListaService listaService, TarefaService tarefaService) {

        this.listaService = listaService;
        this.tarefaService = tarefaService;

        setTitle("Lista de tarefas");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 700);
        setResizable(false);
        setLayout(null);
        setLocationRelativeTo(null);
        setVisible(true);

        ArrayList<Lista> listas = listaService.listarTodas();
        List<Object> listObjects = UtilsMethods.newJTableByLista(listas, listaService, tarefaService, this);

        JTable table = (JTable) listObjects.get(0);
        JScrollPane scrollPane = (JScrollPane) listObjects.get(1);
        table.setBounds(50, 50, 600, 450);
        scrollPane.setBounds(650, 50, 15, 450);

        this.btnAdicionarUmaLista();
        this.btnDeletarTodasListas();
        this.labelJtable();
    }

    public void labelJtable() {
        JLabel label1 = new JLabel("ID");
        JLabel label2 = new JLabel("Titulo");
        JLabel label3 = new JLabel("Criação");
        JLabel label4 = new JLabel("Deletado");
        JLabel label5 = new JLabel("Tarefas");

        label1.setBounds(50, 25, 50, 25);
        label2.setBounds(135, 25, 50, 25);
        label3.setBounds(225, 25, 150, 25);
        label4.setBounds(310, 25, 150, 25);
        label5.setBounds(395, 25, 150, 25);

        this.add(label1);
        this.add(label2);
        this.add(label3);
        this.add(label4);
        this.add(label5);
    }

    public void btnAdicionarUmaLista() {
        JButton btnNewLista = UtilsMethods.newWindowPanel(50, 550, 50, 250, "Adicionar", this, new NovaLista(listaService, tarefaService));
        btnNewLista.setFont(new java.awt.Font("Tahoma", 1, 15));
    }

    public void btnDeletarTodasListas() {
        JButton btnDeletarTodasListas = UtilsMethods.newBtn(400, 550, 50, 250, "Limpar listas", this);
        btnDeletarTodasListas.setFont(new java.awt.Font("Tahoma", 1, 15));

        btnDeletarTodasListas.addActionListener(e -> {
            listaService.excluirTodas();
            JOptionPane.showMessageDialog(null, "Listas excluídas com sucesso!");
            this.dispose();
            new TelaInicial(listaService, tarefaService);
        });
    }
}