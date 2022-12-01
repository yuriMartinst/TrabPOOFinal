package br.yuri.univille.ui;

import br.yuri.univille.model.Lista;
import br.yuri.univille.model.Tarefa;
import br.yuri.univille.service.ListaService;
import br.yuri.univille.service.TarefaService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.util.List;

public class UtilsMethods extends JFrame{

    public static JButton newWindowPanel(int x, int y, int altura, int largura, String nomeBtn, JFrame frameAtual, JFrame frameIr){
        JButton btn = new JButton(nomeBtn);
        btn.setBounds(x, y, largura, altura);
        frameIr.dispose();
        btn.addActionListener(e -> {
            frameIr.setVisible(true);
            frameAtual.dispose();
        });
        frameAtual.add(btn);

        return btn;
    }

    public static JButton newBtn(int x, int y, int altura, int largura, String nomeBtn, JFrame frameAtual){
        JButton btn = new JButton(nomeBtn);
        btn.setBounds(x, y, largura, altura);
        frameAtual.add(btn);
        return btn;
    }

    public static JCheckBox newCheckBox(int x, int y, int altura, int largura, String nomeCheckBox, JFrame frameAtual) {
        JLabel label = new JLabel(nomeCheckBox);
        label.setBounds(x + 20, y, nomeCheckBox.length() * 10, 15);
        frameAtual.add(label);

        JCheckBox checkBox = new JCheckBox();
        checkBox.setBounds(x, y, largura, altura);
        frameAtual.add(checkBox);

        return checkBox;
    }

    public static JTextArea newJtextArea(int x, int y, int altura, int largura, String nomeText, JFrame frameAtual) {
        JLabel label = new JLabel(nomeText);
        label.setBounds(x, y - 20, nomeText.length() * 10, 15);
        frameAtual.add(label);

        JTextArea txtArea = new JTextArea();
        txtArea.setBounds(x, y, largura, altura);
        frameAtual.add(txtArea);

        return txtArea;
    }

    public static List<Object> newJTableByLista(ArrayList<Lista> lista, ListaService listaService, TarefaService tarefaService, JFrame frameAtual) {

        String[] colunas = {"ID", "Titulo", "data de criação", "Excluido", "Tarefas", "Ver tarefas", "Excluir"};
        Object[][] dados = new Object[lista.size()][colunas.length];

        for(int i = 0; i < lista.size(); i++){
            dados[i][0] = lista.get(i).getId();
            dados[i][1] = lista.get(i).getTituloDaLista();
            dados[i][2] = lista.get(i).getDataCriacao();
            dados[i][3] = lista.get(i).isExcluida() ? "Sim" : "Não";
            dados[i][4] = lista.get(i).getTarefas().size();
            dados[i][5] = "Ver tarefas";
            dados[i][6] = "Excluir";
        }

        DefaultTableModel model = new DefaultTableModel(dados, colunas);
        JTable table = new JTable(model);

        table.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int row = table.rowAtPoint(evt.getPoint());
                int col = table.columnAtPoint(evt.getPoint());
                if (row >= 0 && col >= 0) {
                    if(col == 5){
                        Lista lista = listaService.listarUmaListaPeloId((int) table.getValueAt(row, 0));
                        new TelaTarefas(listaService, tarefaService, lista);
                        frameAtual.dispose();
                    }
                    if(col == 6){
                        int id = (int) table.getValueAt(row, 0);
                        listaService.excluir(id);
                        new TelaInicial(listaService, tarefaService);
                        frameAtual.dispose();
                    }
                }
            }
        });


        JScrollPane scroll = new JScrollPane(table);
        frameAtual.add(scroll);
        frameAtual.add(table);

        List<Object> list = new ArrayList<>();
        list.add(table);
        list.add(scroll);

        return list;
    }

    public static List<Object> newJTableByListTarefas(ArrayList<Tarefa> tarefas, ListaService listaService, TarefaService tarefaService, JFrame frameAtual) {

        String[] colunas = {"ID", "Titulo", "Texto", "Data de Criação", "Data de Conclusão", "Concluir", "Excluir"};
        Object[][] dados = new Object[tarefas.size()][colunas.length];

        for(int i = 0; i < tarefas.size(); i++){
            dados[i][0] = tarefas.get(i).getId();
            dados[i][1] = tarefas.get(i).getTituloDaTarefa();
            dados[i][2] = tarefas.get(i).getDetalheDaTarefa();
            dados[i][3] = tarefas.get(i).getDataCriacao();
            dados[i][4] = tarefas.get(i).getDataConclusao();
            if(tarefas.get(i).isConcluida()){
                dados[i][5] = "Desfazer";
            } else {
                dados[i][5] = "Concluir";
            }
            dados[i][6] = "Excluir";
        }

        DefaultTableModel model = new DefaultTableModel(dados, colunas);
        JTable table = new JTable(model);

        table.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int row = table.rowAtPoint(evt.getPoint());
                int col = table.columnAtPoint(evt.getPoint());
                if (row >= 0 && col >= 0) {
                    if(col == 5){
                        Tarefa tarefa = tarefaService.listarUmaTarefa((int) table.getValueAt(row, 0));
                        if(tarefa.isConcluida()){
                            tarefaService.desfazerTarefa(tarefa.getId());
                            JOptionPane.showMessageDialog(null, "Tarefa desmarcada com sucesso!");
                            new TelaTarefas(listaService, tarefaService, tarefa.getLista());
                            frameAtual.dispose();
                        } else {
                            tarefaService.concluirTarefa(tarefa.getId());
                            JOptionPane.showMessageDialog(null, "Tarefa marcada com sucesso!");
                            new TelaTarefas(listaService, tarefaService, tarefa.getLista());
                            frameAtual.dispose();
                        }
                    } else if(col == 6){
                        Tarefa tarefa = tarefaService.listarUmaTarefa((int) table.getValueAt(row, 0));
                        tarefaService.excluirTarefa((int) table.getValueAt(row, 0));
                        JOptionPane.showMessageDialog(null, "Tarefa excluida com sucesso!");
                        new TelaTarefas(listaService, tarefaService, tarefa.getLista());
                        frameAtual.dispose();
                    }
                }
            }
        });

        table.setRowHeight(30);

        JScrollPane scroll = new JScrollPane(table);
        frameAtual.add(scroll);
        frameAtual.add(table);

        List<Object> list = new ArrayList<>();
        list.add(table);
        list.add(scroll);

        return list;

    }
}
