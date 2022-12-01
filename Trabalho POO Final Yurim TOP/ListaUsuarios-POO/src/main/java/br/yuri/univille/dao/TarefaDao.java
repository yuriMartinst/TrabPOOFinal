package br.yuri.univille.dao;

import br.yuri.univille.factory.ConnectionFactory;
import br.yuri.univille.model.Tarefa;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Objects;

public class TarefaDao {

    private Connection connection;

    public TarefaDao() {
        try {
            this.connection = ConnectionFactory.getInstance().getConnection();
            this.criarTabela();
        } catch (SQLException e) {
            System.out.println("Falha ao conectar ao banco de dados: " + e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }


    public void criarTarefa(Tarefa tarefa) {
        if (!validarTarefa(tarefa))
            throw new RuntimeException("Título inválido");

        String querySqlCriarTarefa = "INSERT INTO tarefa(titulo, texto, dataCriacao, dataConclusao, concluida, idLista) VALUES (?, ?, ?, ?, ?, ?);";

        try {
            PreparedStatement stmt = connection.prepareStatement(querySqlCriarTarefa);
            stmt.setString(1, tarefa.getTituloDaTarefa());
            stmt.setString(2, tarefa.getDetalheDaTarefa());
            stmt.setString(3, tarefa.getDataCriacao().toString());
            stmt.setString(4, Objects.isNull(tarefa.getDataConclusao()) ? null : tarefa.getDataConclusao().toString());
            stmt.setInt(5, tarefa.isConcluida() ? 1 : 0);
            stmt.setInt(6, tarefa.getLista().getId());
            stmt.execute();
            stmt.close();
        } catch (Exception e) {
            System.err.println("Falha ao inserir tarefa: " + e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    public Tarefa buscarTarefaPorId(int idTarefa) {
        String querySqlBuscarTarefaPorId = "SELECT * FROM tarefa WHERE id = ?;";
        try {
            PreparedStatement stmt = connection.prepareStatement(querySqlBuscarTarefaPorId);
            stmt.setInt(1, idTarefa);
            ResultSet rs = stmt.executeQuery();

            Tarefa tarefa = new Tarefa();
            tarefa.setId(rs.getInt("id"));
            tarefa.setTituloDaTarefa(rs.getString("titulo"));
            tarefa.setDetalheDaTarefa(rs.getString("texto"));
            tarefa.setDataCriacao(LocalDate.parse(rs.getString("dataCriacao")));
            tarefa.setDataConclusao(Objects.isNull(rs.getString("dataConclusao")) ? null : LocalDate.parse(rs.getString("dataConclusao")));
            tarefa.setConcluida(rs.getInt("concluida") == 1);
            tarefa.setLista(new ListaDao().buscaListaPorId(rs.getInt("idLista")));
            stmt.close();
            return tarefa;
        } catch (Exception e) {
            System.err.println("Falha ao buscar tarefa: " + e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    public ArrayList<Tarefa> buscarTodasTarefasDaLista(int idLista) {
        String querySqlBuscarTarefasLista = "SELECT * FROM tarefa WHERE idLista = ?;";
        try {
            PreparedStatement stmt = connection.prepareStatement(querySqlBuscarTarefasLista);
            stmt.setInt(1, idLista);
            ResultSet listaDeTarefas = stmt.executeQuery();

            ArrayList<Tarefa> tarefas = new ArrayList<>();
            while (listaDeTarefas.next()) {
                Tarefa tarefa = new Tarefa();
                tarefa.setId(listaDeTarefas.getInt("id"));
                tarefa.setTituloDaTarefa(listaDeTarefas.getString("titulo"));
                tarefa.setDetalheDaTarefa(listaDeTarefas.getString("texto"));
                tarefa.setDataCriacao(LocalDate.parse(listaDeTarefas.getString("dataCriacao")));
                tarefa.setDataConclusao(Objects.isNull(listaDeTarefas.getString("dataConclusao")) ? null : LocalDate.parse(listaDeTarefas.getString("dataConclusao")));
                tarefa.setConcluida(listaDeTarefas.getInt("concluida") == 1);
                tarefas.add(tarefa);
            }

            stmt.close();
            return tarefas;

        } catch (SQLException e) {
            System.err.println("Falha ao ler tarefas: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public void atualizarTarefa(Tarefa tarefa) {
        if (!validarTarefa(tarefa))
            throw new RuntimeException("Título inválido");

        String querySqlAtualizarTarefa = "UPDATE tarefa SET titulo = ?, texto = ?, dataConclusao = ?, concluida = ?, idLista = ? WHERE id = ?;";

        try {
            PreparedStatement stmt = connection.prepareStatement(querySqlAtualizarTarefa);
            stmt.setString(1, tarefa.getTituloDaTarefa());
            stmt.setString(2, tarefa.getDetalheDaTarefa());
            stmt.setString(3, Objects.isNull(tarefa.getDataConclusao()) ? null : tarefa.getDataConclusao().toString());
            stmt.setInt(4, tarefa.isConcluida() ? 1 : 0);
            stmt.setInt(5, tarefa.getLista().getId());
            stmt.setInt(6, tarefa.getId());
            stmt.execute();
            stmt.close();
        } catch (Exception e) {
            System.err.println("Falha ao atualizar tarefa: " + e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    public void deletarTarefa(int idTarefa) {
        String querySqlDeletarTarefa = "DELETE FROM tarefa WHERE id = ?;";
        try {
            PreparedStatement stmt = connection.prepareStatement(querySqlDeletarTarefa);
            stmt.setInt(1, idTarefa);
            stmt.execute();
            stmt.close();
        } catch (Exception e) {
            System.err.println("Falha ao deletar tarefa: " + e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    public int getProximoId() {
        String querySqlProximoId = "SELECT MAX(id) FROM tarefa;";
        try {
            PreparedStatement stmt = connection.prepareStatement(querySqlProximoId);
            ResultSet rs = stmt.executeQuery();

            int id = 0;
            if (rs.next()) {
                id = rs.getInt("MAX(id)");
            }

            stmt.close();
            return id + 1;
        } catch (SQLException e) {
            System.err.println("Falha ao buscar id da tarefa: " + e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    private void criarTabela() {
        String querySqlCriarTabela = "CREATE TABLE IF NOT EXISTS tarefa(     " +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "titulo TEXT NOT NULL,                 " +
                "texto TEXT,            " +
                "dataCriacao TEXT NOT NULL,                    " +
                "dataConclusao TEXT,                       " +
                "concluida INTEGER NOT NULL DEFAULT 0,                       " +
                "idLista INTEGER NOT NULL,                       " +
                "FOREIGN KEY (idLista) REFERENCES lista(id) " +
                ");                                    ";
        try {
            this.connection.createStatement().execute(querySqlCriarTabela);
        } catch (Exception e) {
            System.err.println("Falha ao criar tabela: " + e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    private boolean validarTarefa(Tarefa tarefa) {
        for (Tarefa t : buscarTodasTarefasDaLista(tarefa.getLista().getId())) {
            if (t.getTituloDaTarefa().equals(tarefa.getTituloDaTarefa()) && t.getId() != tarefa.getId()) {
                return false;
            }
        }

        if (tarefa.getTituloDaTarefa().isEmpty()) {
            System.err.println("Título da tarefa não pode ser vazio");
            return false;
        }
        return true;
    }
}
