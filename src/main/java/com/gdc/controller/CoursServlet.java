package com.gdc.controller;

import com.gdc.dao.CoursDAO;
import com.gdc.model.Cours;
import com.gdc.model.Utilisateur;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/cours")
public class CoursServlet extends HttpServlet{
	
	private static final long serialVersionUID = 1L;
    
    private CoursDAO coursDao;

    public void init() {
        this.coursDao = new CoursDAO();
    }
    
    private boolean estProfesseur(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            Utilisateur utilisateur = (Utilisateur) session.getAttribute("utilisateurConnecte");
            return utilisateur != null && "PROF".equals(utilisateur.getRole());
        }
        return false;
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
    	
    	String action = request.getParameter("action");
    	if (action == null) {
            action = "list";
        }
    	
    	HttpSession session = request.getSession(false);
        Utilisateur utilisateur = (session != null) ? (Utilisateur) session.getAttribute("utilisateurConnecte") : null;
        
        // Récupération des données via le DAO
        try {
        	switch (action) {
            case "new":
                showNewForm(request, response); // Afficher le formulaire d'ajout
                break;
            case "edit":
            	showEditForm(request, response);
            	break;
            case "delete":
            	deleteCours(request,response);
            	break;
            case "enroll":
            	enrollEtudiant(request,response,utilisateur);
            	break;
            case "list":
            default:
                listCours(request, response, utilisateur); // Afficher la liste des cours 
                break;
        }
        } catch (Exception e) {
        	throw new ServletException(e);
        }
    }

    private void showNewForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    	
        request.getRequestDispatcher("/WEB-INF/views/nouveauCours.jsp").forward(request, response);
    }
    
    private void listCours(HttpServletRequest request, HttpServletResponse response, Utilisateur utilisateur)
            throws SQLException, IOException, ServletException {
        
        List<Cours> listeCours = coursDao.findAll();
        
        if (utilisateur != null && "ETUDIANT".equals(utilisateur.getRole())) {
            int userId = utilisateur.getId();
            
            // stocker id du cours et un booléen (inscrit ou non)
            java.util.Map<Integer, Boolean> inscriptionStatus = new java.util.HashMap<>();
            
            for (Cours cours : listeCours) {
                boolean inscrit = coursDao.isEtudiantInscrit(userId, cours.getId());
                inscriptionStatus.put(cours.getId(), inscrit);
            }
            
            // Rendre la map disponible pour la JSP
            request.setAttribute("inscriptionStatus", inscriptionStatus);
        }
        
        request.setAttribute("listeCours", listeCours);
        request.getRequestDispatcher("/WEB-INF/views/listeCours.jsp").forward(request, response);
    }
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
    		request.setCharacterEncoding("UTF-8");
    		
    		if (!estProfesseur(request)) {
    			response.sendError(HttpServletResponse.SC_FORBIDDEN, "Seuls les professeurs peuvent supprimer des cours.");
            	return; 
    		}
    		String action = request.getParameter("action");
    		
    		if (action == null) {
    	        action = "insert"; 
    	    }
    		
    		try {
    	        switch (action) {
    	            case "insert":
    	                insertCours(request, response);
    	                break;
    	            case "update": 
    	                updateCours(request, response); 
    	                break;
    	        }
    	    } catch (Exception e) {
    	        throw new ServletException(e);
    	    }
    }
    
    private void showEditForm(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {
        
    	if (!estProfesseur(request)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Seuls les professeurs peuvent supprimer des cours.");
            return; 
        }
    	
        int id = Integer.parseInt(request.getParameter("id"));
        Cours existingCours = coursDao.findById(id);
        request.setAttribute("cours", existingCours);
    
        request.getRequestDispatcher("/WEB-INF/views/modifierCours.jsp").forward(request, response);
    }
    
    private void insertCours(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ServletException {
        
    	if (!estProfesseur(request)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Seuls les professeurs peuvent supprimer des cours.");
            return; 
        }
    	
        String titre = request.getParameter("titre");
        String description = request.getParameter("description");
        String niveau = request.getParameter("niveau");
        int dureeHeures = Integer.parseInt(request.getParameter("dureeHeures"));
        
        Cours nouveauCours = new Cours(0, titre, description, niveau, dureeHeures); 
        coursDao.save(nouveauCours);
        
        // Redirection
        response.sendRedirect("cours?success=added"); 
    }

    private void updateCours(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ServletException {
        
    	if (!estProfesseur(request)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Seuls les professeurs peuvent supprimer des cours.");
            return; 
        }
    	
        int id = Integer.parseInt(request.getParameter("id"));
        String titre = request.getParameter("titre");
        String description = request.getParameter("description");
        String niveau = request.getParameter("niveau");
        int dureeHeures = Integer.parseInt(request.getParameter("dureeHeures"));

        Cours coursAUpdater = new Cours(id, titre, description, niveau, dureeHeures);
        coursDao.update(coursAUpdater);
        
        response.sendRedirect("cours?success=updated");
    }
    
    private void deleteCours(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {
    	
    	if (!estProfesseur(request)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Seuls les professeurs peuvent supprimer des cours.");
            return; 
        }
    	
        int id = Integer.parseInt(request.getParameter("id"));
        
        coursDao.delete(id);
        
        response.sendRedirect("cours?success=deleted");
    }
    
    private void enrollEtudiant(HttpServletRequest request, HttpServletResponse response, Utilisateur utilisateur)
            throws SQLException, IOException {

        if (utilisateur == null || !"ETUDIANT".equals(utilisateur.getRole())) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Seuls les étudiants peuvent s'inscrire à un cours.");
            return; 
        }
        
        try {
            int coursId = Integer.parseInt(request.getParameter("id"));
            int userId = utilisateur.getId();
            
            coursDao.inscrireEtudiant(userId, coursId);
            
            response.sendRedirect("cours?success=enrolled&titre=" + request.getParameter("titre")); 
            
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID de cours invalide.");
        } catch (SQLException e) {
            String errorMessage = e.getMessage().toLowerCase();
            if (errorMessage.contains("duplicate key") || errorMessage.contains("clé dupliquée")) {
                 response.sendRedirect("cours?error=already_enrolled");
            } else {
                 response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Erreur lors de l'inscription.");
            }
        }
    }
}
