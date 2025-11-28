package com.gdc.dao;

import com.gdc.model.Cours;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CoursDAO {
	// Informations de connexion à PostgreSQL
    private static final String URL = "jdbc:postgresql://localhost:5432/gestion_cours_db";
    private static final String USER = "postgres";
    private static final String PASSWORD = "root"; 
    // Requêtes SQL
    private static final String SELECT_ALL_COURS = "SELECT id, titre, description, niveau, duree_heures FROM cours;";
    private static final String INSERT_COURS_SQL = "INSERT INTO cours (titre, description, niveau, duree_heures) VALUES (?, ?, ?, ?);";
    private static final String SELECT_COURS_BY_ID = "SELECT id, titre, description, niveau, duree_heures FROM cours WHERE id = ?;";
    private static final String UPDATE_COURS_SQL = "UPDATE cours SET titre = ?, description = ?, niveau = ?, duree_heures = ? WHERE id = ?;";
    private static final String DELETE_COURS_SQL = "DELETE FROM cours WHERE id = ?;";
    private static final String SELECT_IS_INSCRIT = "SELECT COUNT(*) FROM inscription WHERE utilisateur_id = ? AND cours_id = ?;";
    private static final String INSERT_INSCRIPTION = "INSERT INTO inscription (utilisateur_id, cours_id) VALUES (?, ?);";
    private static final String SELECT_COURS_BY_ETUDIANT = 
    	    "SELECT c.id, c.titre, c.description, c.niveau, c.duree_heures " +
    	    "FROM cours c " +
    	    "JOIN inscription i ON c.id = i.cours_id " +
    	    "WHERE i.utilisateur_id = ?;";
    
    protected Connection getConnection() throws SQLException {
        try {
            Class.forName("org.postgresql.Driver");
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException e) {
            System.err.println("Driver PostgreSQL introuvable.");
            throw new SQLException("Driver PostgreSQL introuvable.", e);
        }
    }
    
    private Cours mapResultSetToCours(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String titre = rs.getString("titre");
        String description = rs.getString("description");
        String niveau = rs.getString("niveau");
        int dureeHeures = rs.getInt("duree_heures"); 

        return new Cours(id, titre, description, niveau, dureeHeures);
    }

    // -------------------------------------------------------------------------
    // Méthodes CRUD
    // -------------------------------------------------------------------------

    /**
     * Récupère la liste de tous les cours depuis PostgreSQL.
     * @return Une liste d'objets Cours.
     */
    public List<Cours> findAll() {
        List<Cours> coursList = new ArrayList<>();
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_COURS);
             ResultSet rs = preparedStatement.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String titre = rs.getString("titre");
                String description = rs.getString("description");
                String niveau = rs.getString("niveau");
                int dureeHeures = rs.getInt("duree_heures");

                Cours cours = new Cours(id, titre, description, niveau, dureeHeures);
                coursList.add(cours);
            }

        } catch (SQLException e) {
            e.printStackTrace(); 
        }
        return coursList;
    }
    
    public void save(Cours cours) throws SQLException {

        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_COURS_SQL)) {
            
            preparedStatement.setString(1, cours.getTitre());
            preparedStatement.setString(2, cours.getDescription());
            preparedStatement.setString(3, cours.getNiveau());
            preparedStatement.setInt(4, cours.getDureeHeures());
            
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }
    
    public Cours findById(int id) {
        Cours cours = null;
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_COURS_BY_ID)) {

            preparedStatement.setInt(1, id);
            try (ResultSet rs = preparedStatement.executeQuery()) {
                if (rs.next()) {
                    String titre = rs.getString("titre");
                    String description = rs.getString("description");
                    String niveau = rs.getString("niveau");
                    int dureeHeures = rs.getInt("duree_heures");
                    
                    cours = new Cours(id, titre, description, niveau, dureeHeures);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cours;
    }
    
    public boolean update(Cours cours) throws SQLException {
        boolean rowUpdated;
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(UPDATE_COURS_SQL)) {
            
            statement.setString(1, cours.getTitre());
            statement.setString(2, cours.getDescription());
            statement.setString(3, cours.getNiveau());
            statement.setInt(4, cours.getDureeHeures());
            statement.setInt(5, cours.getId());

            rowUpdated = statement.executeUpdate() > 0;
        }
        return rowUpdated;
    }
    
    public boolean delete(int id) throws SQLException {
        boolean rowDeleted;
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(DELETE_COURS_SQL)) {
            
            statement.setInt(1, id);
            rowDeleted = statement.executeUpdate() > 0;
        }
        return rowDeleted;
    }
    
    public boolean isEtudiantInscrit(int userId, int coursId) {
        boolean inscrit = false;
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_IS_INSCRIT)) {
            
            preparedStatement.setInt(1, userId);
            preparedStatement.setInt(2, coursId);

            try (ResultSet rs = preparedStatement.executeQuery()) {
                if (rs.next()) {
                    inscrit = rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return inscrit;
    }
    
    public void inscrireEtudiant(int userId, int coursId) throws SQLException {
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_INSCRIPTION)) {
            
            preparedStatement.setInt(1, userId);
            preparedStatement.setInt(2, coursId);
            preparedStatement.executeUpdate();
            
        } catch (SQLException e) {
            System.err.println("Erreur SQL lors de l'inscription : " + e.getMessage());
            throw e;
        }
    }
    
    public List<Cours> getCoursInscrits(int userId) {
        List<Cours> coursInscrits = new ArrayList<>();
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_COURS_BY_ETUDIANT)) {
            
            preparedStatement.setInt(1, userId);
            
            try (ResultSet rs = preparedStatement.executeQuery()) {
                while (rs.next()) {
                    Cours cours = mapResultSetToCours(rs); 
                    coursInscrits.add(cours);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return coursInscrits;
    }
}
