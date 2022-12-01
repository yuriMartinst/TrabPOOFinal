package br.yuri.univille;

import br.yuri.univille.dao.ListaDao;
import br.yuri.univille.dao.TarefaDao;
import br.yuri.univille.service.ListaService;
import br.yuri.univille.service.TarefaService;
import br.yuri.univille.ui.TelaInicial;

import java.awt.*;

public class Main {
    public static void main(String[] args){
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    TelaInicial frame = new TelaInicial(new ListaService(new ListaDao()), new TarefaService(new TarefaDao()));
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
