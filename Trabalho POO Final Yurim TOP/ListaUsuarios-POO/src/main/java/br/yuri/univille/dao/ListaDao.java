package br.yuri.univille.dao;

import br.yuri.univille.factory.ConnectionFactory;
import br.yuri.univille.model.Lista;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

public class ListaDao {

    private Connection connection;

    public ListaDao() {
        try {
            this.connection = ConnectionFactory.getInstance().getConnection();
            this.criarTabelaLista();
        } catch (Exception e) {
            System.out.println("Falha ao conectar ao banco de dados: " + e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    public void criarLista(Lista lista) {
        if (!validarLista(lista))
            throw new RuntimeException("Título da lista inválido");

        String querySqlCriarLista = "INSERT INTO lista(titulo, dataCriacao, excluida) VALUES (?, ?, ?);";

        try {
            var stmt = connection.prepareStatement(querySqlCriarLista);
            stmt.setString(1, lista.getTituloDaLista());
            stmt.setString(2, lista.getDataCriacao().toString());
            stmt.setInt(3, lista.isExcluida() ? 1 : 0);
            stmt.execute();
            stmt.close();
        } catch (Exception e) {
            System.err.println("Falha ao inserir lista: " + e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    public Lista buscaListaPorId(int idLista) {
        String querySqlBuscarListaId = "SELECT * FROM lista WHERE id = ?;";

        try {
            var stmt = connection.prepareStatement(querySqlBuscarListaId);
            stmt.setInt(1, idLista);
            ResultSet listas = stmt.executeQuery();

            Lista lista = new Lista();
            if (listas.next()) {
                lista.setId(listas.getInt("id"));
                lista.setTituloDaLista(listas.getString("titulo"));
                lista.setDataCriacao(LocalDate.parse(listas.getString("dataCriacao")));
                lista.setExcluida(listas.getInt("excluida") == 1);
                lista.setTarefas(new TarefaDao().buscarTodasTarefasDaLista(lista.getId()));
                lista.getTarefas().stream().forEach(t -> t.setLista(lista));
            }

            stmt.close();
            return lista;
        } catch (Exception e) {
            System.err.println("Falha ao buscar lista: " + e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    public ArrayList<Lista> buscarTodasListas() {
        String querySqlBuscarTodasListas = "SELECT * FROM lista;";

        try {
            PreparedStatement stmt = connection.prepareStatement(querySqlBuscarTodasListas);
            ResultSet rs = stmt.executeQuery();

            ArrayList<Lista> listas = new ArrayList<>();
            while (rs.next()) {
                Lista lista = new Lista();
                lista.setId(rs.getInt("id"));
                lista.setTituloDaLista(rs.getString("titulo"));
                lista.setDataCriacao(LocalDate.parse(rs.getString("dataCriacao")));
                lista.setExcluida(rs.getInt("excluida") == 1);
                lista.setTarefas(new TarefaDao().buscarTodasTarefasDaLista(lista.getId()));
                lista.getTarefas().stream().forEach(t -> t.setLista(lista));
                listas.add(lista);
            }

            stmt.close();
            return listas;

        } catch (SQLException e) {
            System.err.println("Falha ao ler lista: " + e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    public void atualizarLista(Lista lista) {
        if (!validarLista(lista))
            throw new RuntimeException("Título da lista inválido");

        String querySqlAtualizarLista = "UPDATE lista SET titulo = ?, excluida = ? WHERE id = ?;";

        try {
            PreparedStatement stmt = connection.prepareStatement(querySqlAtualizarLista);
            stmt.setString(1, lista.getTituloDaLista());
            stmt.setInt(2, lista.isExcluida() ? 1 : 0);
            stmt.setInt(3, lista.getId());
            stmt.execute();
            stmt.close();
        } catch (SQLException e) {
            System.err.println("Falha ao atualizar lista: " + e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    public void deletarLista(int idLista) {
        String querySqlDeletarTarefaDaLista = "DELETE FROM tarefa WHERE idLista = ?;";
        String querySqlDeletarLista = "DELETE FROM lista WHERE id = ?;";

        try {
            PreparedStatement stmt = connection.prepareStatement(querySqlDeletarTarefaDaLista);
            stmt.setInt(1, idLista);
            stmt.execute();
            stmt.close();

            stmt = connection.prepareStatement(querySqlDeletarLista);
            stmt.setInt(1, idLista);
            stmt.execute();
            stmt.close();

        } catch (SQLException e) {
            System.err.println("Falha ao deletar lista: " + e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    public void deletarTodasListas() {
        String sqlTarefas = "DELETE FROM tarefa;";
        String sqlListas = "DELETE FROM lista;";
        try {
            PreparedStatement stmt = connection.prepareStatement(sqlTarefas);
            stmt.execute();
            stmt.close();

            stmt = connection.prepareStatement(sqlListas);
            stmt.execute();
            stmt.close();

        } catch (SQLException e) {
            System.err.println("Falha ao deletar lista: " + e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    private void criarTabelaLista() {
        String sql = "CREATE TABLE IF NOT EXISTS lista(      " +
                "id INTEGER PRIMARY KEY AUTOINCREMENT,  " +
                "titulo TEXT NOT NULL,                  " +
                "dataCriacao INTEGER NOT NULL DEFAULT 0," +
                "excluida TEXT                        " +
                ");                                     ";
        try {
            this.connection.createStatement().execute(sql);
        } catch (Exception e) {
            System.err.println("Falha ao criar tabela: " + e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    private boolean validarLista(Lista lista) {
        ArrayList<Lista> listas = buscarTodasListas();
        if (listas.stream().anyMatch(l -> l.getTituloDaLista().equals(lista.getTituloDaLista()) && l.getId() != lista.getId())) {
            System.err.println("Título da lista já existe");
            return false;
        }

        if (lista.getTituloDaLista().isEmpty()) {
            System.err.println("Título da lista não pode ser vazio");
            return false;
        }

        return true;
    }
}
