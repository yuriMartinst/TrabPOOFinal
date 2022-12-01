package br.yuri.univille.factory;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Objects;

public class ConnectionFactory extends javax.swing.JFrame {
    private static ConnectionFactory instance;
    private Connection connection;

    private ConnectionFactory() {
        try {
            Class.forName("org.sqlite.JDBC");
            this.connection = DriverManager.getConnection("jdbc:sqlite::resource:db.sqlite");
            JOptionPane.showMessageDialog(null, "Programa iniciado!");
        } catch (ClassNotFoundException | SQLException | HeadlessException e) {
            JOptionPane.showMessageDialog(null, "Falha ao conectar ao banco de dados: " + e.getMessage());
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public static ConnectionFactory getInstance() throws SQLException {
        if (Objects.isNull(instance) || instance.getConnection().isClosed()) {
            instance = new ConnectionFactory();
        }

        return instance;
    }
}
