
import java.awt.List;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class banco {

    static final String URL = "jdbc:mysql://localhost:3306/desafio";
    static final String USER = "root";
    static final String PASS = "";

    public static void alterarAdm(Pessoa usuario) throws NoSuchAlgorithmException, UnsupportedEncodingException {

        String senha = usuario.getSenha();
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte messageDigest[] = md.digest(senha.getBytes("UTF-8"));

            StringBuilder sb = new StringBuilder();
            for (byte b : messageDigest) {

                sb.append(String.format("%02X", 0xFF & b));

            }
            senha = sb.toString();

            Connection conn = DriverManager.getConnection(URL, USER, PASS);
            //    Statement stmt = conn.createStatement();

            String QUERY = "UPDATE usuario SET nome = ?, login =  ?, senha = ?, email = ?,telefone = ?, dataNasc = ?, status = ? where id = ?";

            PreparedStatement stmt = conn.prepareStatement(QUERY);

            stmt.setString(1, usuario.getNome());
            stmt.setString(2, usuario.getLogin());
            stmt.setString(3, senha);
            stmt.setString(4, usuario.getEmail());
            stmt.setString(5, usuario.getTelefone());
            stmt.setString(6, usuario.getDataNasc());
            stmt.setInt(7, usuario.getStatus());
            stmt.setInt(8, usuario.getId());

            int executeUpdate = stmt.executeUpdate();
            System.out.println("Alterado");
            conn.close();

        } catch (SQLException e) {

        }
    }

    public static void insereUsuario(String nome, String login, String senha, String email, String telefone, String dataNasc, int status) throws NoSuchAlgorithmException, UnsupportedEncodingException {

        try {

            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte messageDigest[] = md.digest(senha.getBytes("UTF-8"));

            StringBuilder sb = new StringBuilder();
            for (byte b : messageDigest) {

                sb.append(String.format("%02X", 0xFF & b));

            }
            senha = sb.toString();

            String QUERY = "INSERT INTO usuario ( nome, login, senha, email, telefone, dataNasc, status)"
                    + " VALUES('" + nome + "', '" + login + "', '" + senha + "','" + email + "','" + telefone + "','" + dataNasc + "'," + status + ")";

            System.out.println(QUERY);
            Connection connection = DriverManager.getConnection(URL, USER, PASS);
            Statement statement = connection.createStatement();
            statement.executeUpdate(QUERY);

            System.out.println("Inserido com sucesso");

        } catch (SQLException ex) {
            Logger.getLogger(banco.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void deletar(int id) throws SQLException {

        try {
            Connection conn = DriverManager.getConnection(URL, USER, PASS);
            Statement stmt = conn.createStatement();

            String QUERY = "DELETE FROM usuario WHERE id = " + id + "";

            stmt.executeUpdate(QUERY);
            System.out.println("Deletado");

        } catch (SQLException e) {

        }

    }

    public ResultSet entrar(Pessoa usuario) throws SQLException, NoSuchAlgorithmException, UnsupportedEncodingException {

        Connection conn = DriverManager.getConnection(URL, USER, PASS);
        Statement stmt = conn.createStatement();
        String senha = usuario.getSenha();
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte messageDigest[] = md.digest(senha.getBytes("UTF-8"));

            StringBuilder sb = new StringBuilder();
            for (byte b : messageDigest) {

                sb.append(String.format("%02X", 0xFF & b));

            }
            senha = sb.toString();

            String sql = "SELECT * FROM usuario where login = ? and senha = ?";
            PreparedStatement pstm = conn.prepareStatement(sql);
            pstm.setString(1, usuario.getLogin());
            pstm.setString(2, senha);

            return pstm.executeQuery();

        } catch (SQLException e) {
            return null;
        }

    }

    public ResultSet usuarios() throws SQLException {

        Connection conn = DriverManager.getConnection(URL, USER, PASS);
        Statement stmt = conn.createStatement();
        try {
            String sql = "SELECT * FROM usuario ";
            PreparedStatement pstm = conn.prepareStatement(sql);
            return pstm.executeQuery();

        } catch (SQLException e) {
            return null;
        }

    }

    public ResultSet entrara(int id) throws SQLException, NoSuchAlgorithmException, UnsupportedEncodingException {

        Connection conn = DriverManager.getConnection(URL, USER, PASS);
        Statement stmt = conn.createStatement();

        try {

            String sql = "SELECT * FROM usuario where id = ?";
            PreparedStatement pstm = conn.prepareStatement(sql);
            pstm.setInt(1, id);

            return pstm.executeQuery();

        } catch (SQLException e) {
            return null;
        }

    }

    public int trocarSenha(String email) throws SQLException, NoSuchAlgorithmException, UnsupportedEncodingException {

        Connection conn = DriverManager.getConnection(URL, USER, PASS);
        Statement stmt = conn.createStatement();

        Random rand = new Random();
       
        int nova =  rand.nextInt(1000)+1000;
        
        
         String senha = Integer.toString(nova);
                
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte messageDigest[] = md.digest(senha.getBytes("UTF-8"));

        StringBuilder sb = new StringBuilder();
        for (byte b : messageDigest) {

            sb.append(String.format("%02X", 0xFF & b));

        }
        senha = sb.toString();

        try {

            String QUERY = "UPDATE usuario SET  senha = ? where email = ?";
            PreparedStatement pstm = conn.prepareStatement(QUERY);
            pstm.setString(1, senha);
            pstm.setString(2, email);
             int executeUpdate = pstm.executeUpdate();
            return nova;

        } catch (SQLException e) {
            return 1;
        }

    }

}
