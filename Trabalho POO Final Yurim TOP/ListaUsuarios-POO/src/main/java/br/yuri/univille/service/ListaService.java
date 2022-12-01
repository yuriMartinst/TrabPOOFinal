package br.yuri.univille.service;

import br.yuri.univille.dao.ListaDao;
import br.yuri.univille.model.Lista;

import javax.swing.*;
import java.util.ArrayList;

public class ListaService {

    private final ListaDao listaDao;

    public ListaService(ListaDao listaDao) {
        this.listaDao = listaDao;
    }

    public void criar(Lista lista) {
        try {
            listaDao.criarLista(lista);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Erro ao criar lista...");
        }
    }

    public void excluir(int idLista) {
        Lista lista = listaDao.buscaListaPorId(idLista);
        listaDao.deletarLista(lista.getId());
    }

    public void excluirTodas() {
        listaDao.deletarTodasListas();
    }

    public ArrayList<Lista> listarTodas() {
        return listaDao.buscarTodasListas();
    }

    public Lista listarUmaListaPeloId(int idLista) {
        return listaDao.buscaListaPorId(idLista);
    }
}
