package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexao {
    public static Connection getConexao(){
        Connection conexao = null;

        try {
            // URL Banco - Tem que atualizar o caminho (IF ou BRUNO);
            String url = "jdbc:mysql://localhost:3306/eventos_poo";

            String usuario = "root";
            String senha = "2314";

            // Criando a conexão
            conexao = DriverManager.getConnection(url, usuario, senha);

            // Só para testar a conexão | Caso queira usar
            // System.out.println("Conectado e funcionando");

        } catch (SQLException error) {
            System.out.println("Erro ao conectar: " + error.getMessage());
        }

        return conexao;
    }

    public static void main(String[] args) {
        try (Connection conn = getConexao()) {
            if (conn != null) {
                System.out.println("Conexão estabelecida");
            } else {
                System.out.println("Falha ao conectar");
            }
        } catch (SQLException e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }
}
