package persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import model.Startup;

public class StartupRepository {
    public void salvar(Startup s) {
        String sql = "MERGE INTO startup (nome, caixa, receita_base, reputacao, moral) KEY(nome) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DataSourceProvider.get();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, s.getNome());

            stmt.setDouble(2, s.getCaixa().valor());
            stmt.setDouble(3, s.getReceitaBase().valor());
            stmt.setInt(4, s.getReputacao().valor());
            stmt.setInt(5, s.getMoral().valor());

            stmt.executeUpdate();

            System.out.println("Estado da startup '" + s.getNome() + "' salvo no banco.");

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar startup: " + e.getMessage(), e);
        }
    }

    public void prepararBanco() {
        String sql = """
                    CREATE TABLE IF NOT EXISTS startup (
                        nome VARCHAR(255) PRIMARY KEY,
                        caixa DECIMAL(20, 2) NOT NULL,
                        receita_base DECIMAL(20, 2) NOT NULL,
                        reputacao INT NOT NULL,
                        moral INT NOT NULL
                    )
                """;

        try (java.sql.Connection conn = DataSourceProvider.get();
                java.sql.PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.execute();
        } catch (java.sql.SQLException e) {
            throw new RuntimeException("Erro ao criar tabelas", e);
        }
    }

    public java.util.List<Startup> carregarTodas() {
        java.util.List<Startup> lista = new java.util.ArrayList<>();
        String sql = "SELECT nome, caixa, receita_base, reputacao, moral FROM startup";

        try (Connection conn = DataSourceProvider.get();
                PreparedStatement stmt = conn.prepareStatement(sql);
                java.sql.ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String nome = rs.getString("nome");
                double vCaixa = rs.getDouble("caixa");
                double vReceita = rs.getDouble("receita_base");
                int vReputacao = rs.getInt("reputacao");
                int vMoral = rs.getInt("moral");

                model.vo.Dinheiro caixa = new model.vo.Dinheiro(vCaixa);
                model.vo.Dinheiro receita = new model.vo.Dinheiro(vReceita);
                model.vo.Humor reputacao = new model.vo.Humor(vReputacao);
                model.vo.Humor moral = new model.vo.Humor(vMoral);

                Startup s = new Startup(nome, caixa, receita, reputacao, moral);
                lista.add(s);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao carregar startups", e);
        }
        return lista;
    }
}