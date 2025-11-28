package com.gdc.controller;

import com.gdc.dao.UtilisateurDAO;
import com.gdc.model.Utilisateur;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/utilisateurs")
public class UtilisateurServlet extends HttpServlet{
	private static final long serialVersionUID = 1L;
    
    private UtilisateurDAO utilisateurDao;
    public void init() {
        this.utilisateurDao = new UtilisateurDAO();
    }
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String action = request.getParameter("action");
        if (action == null) { action = "list"; }

        try {
            switch (action) {
                case "new":
                    showNewForm(request, response);
                    break;
                case "delete":
                    deleteUtilisateur(request, response);
                    break;
                case "list":
                default:
                    listUtilisateurs(request, response);
                    break;
            }
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // Pour cet exemple, nous gérons seulement l'insertion via POST
        try {
			insertUtilisateur(request, response);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ServletException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    private void listUtilisateurs(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ServletException {
        
        List<Utilisateur> listeUtilisateurs = utilisateurDao.findAll();
        request.setAttribute("listeUtilisateurs", listeUtilisateurs);
        request.getRequestDispatcher("/WEB-INF/views/listeUtilisateurs.jsp").forward(request, response);
    }
    
    private void showNewForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/views/nouveauUtilisateur.jsp").forward(request, response);
    }

    private void insertUtilisateur(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ServletException {
        
    	request.setCharacterEncoding("UTF-8");
    	
        String nom = request.getParameter("nom");
        String prenom = request.getParameter("prenom");
        String email = request.getParameter("email");
        String motDePasse = request.getParameter("motDePasse");
        String role = request.getParameter("role");
        
        
        Utilisateur nouvelUtilisateur = new Utilisateur(nom, prenom, email, motDePasse, role);
        utilisateurDao.save(nouvelUtilisateur);
        
        response.sendRedirect("utilisateurs?success=added");
    }
    
    private void deleteUtilisateur(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {
        
        int id = Integer.parseInt(request.getParameter("id"));
        utilisateurDao.delete(id);
        
        response.sendRedirect("utilisateurs?success=deleted");
    }
}
