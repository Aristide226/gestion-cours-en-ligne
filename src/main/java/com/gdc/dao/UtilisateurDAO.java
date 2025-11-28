package com.gdc.dao;

import com.gdc.model.Utilisateur;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UtilisateurDAO {

    private static final String URL = "jdbc:postgresql://localhost:5432/gestion_cours_db";
    private static final String USER = "postgres"; 
    private static final String PASSWORD = "root"; 

    // Requêtes SQL
    private static final String SELECT_ALL_UTILISATEURS = "SELECT id, nom, prenom, email, mot_de_passe, role FROM utilisateur ORDER BY nom;";
    private static final String INSERT_UTILISATEUR_SQL = "INSERT INTO utilisateur (nom, prenom, email, mot_de_passe, role) VALUES (?, ?, ?, ?, ?);";
    private static final String DELETE_UTILISATEUR_SQL = "DELETE FROM utilisateur WHERE id = ?;";
    private static final String SELECT_UTILISATEUR_LOGIN = 
    	    "SELECT id, nom, prenom, email, mot_de_passe, role FROM utilisateur WHERE email = ? AND mot_de_passe = ?;";

    protected Connection getConnection() throws SQLException {
        try {
            Class.forName("org.postgresql.Driver");
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException e) {
            System.err.println("Driver PostgreSQL introuvable.");
            throw new SQLException("Driver PostgreSQL introuvable.", e);
        }
    }

    private Utilisateur mapResultSetToUtilisateur(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String nom = rs.getString("nom");
        String prenom = rs.getString("prenom");
        String email = rs.getString("email");
        String motDePasse = rs.getString("mot_de_passe");
        String role = rs.getString("role");
        return new Utilisateur(id, nom, prenom, email, motDePasse, role);
    }
    
    public List<Utilisateur> findAll() {
        List<Utilisateur> utilisateurs = new ArrayList<>();
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_UTILISATEURS);
             ResultSet rs = preparedStatement.executeQuery()) {
            
            while (rs.next()) {
                utilisateurs.add(mapResultSetToUtilisateur(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return utilisateurs;
    }

   
    public void save(Utilisateur utilisateur) throws SQLException {
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_UTILISATEUR_SQL)) {
            
            preparedStatement.setString(1, utilisateur.getNom());
            preparedStatement.setString(2, utilisateur.getPrenom());
            preparedStatement.setString(3, utilisateur.getEmail());
            preparedStatement.setString(4, utilisateur.getMotDePasse());
            preparedStatement.setString(5, utilisateur.getRole());
            
            preparedStatement.executeUpdate();
        }
    }
    
    public boolean delete(int id) throws SQLException {
        boolean rowDeleted;
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(DELETE_UTILISATEUR_SQL)) {
            
            statement.setInt(1, id);
            rowDeleted = statement.executeUpdate() > 0;
        }
        return rowDeleted;
    }
    
    public Utilisateur findByEmailAndPassword(String email, String motDePasse) {
        Utilisateur utilisateur = null;
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_UTILISATEUR_LOGIN)) {

            preparedStatement.setString(1, email);
            preparedStatement.setString(2, motDePasse);

            try (ResultSet rs = preparedStatement.executeQuery()) {
                if (rs.next()) {
                    utilisateur = mapResultSetToUtilisateur(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return utilisateur;
    }
 // Note : Les méthodes findById et update seraient implémentées de manière similaire
}
