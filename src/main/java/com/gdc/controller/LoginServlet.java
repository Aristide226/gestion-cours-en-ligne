package com.gdc.controller;

import com.gdc.dao.UtilisateurDAO;
import com.gdc.model.Utilisateur;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet({"/login", "/logout"})
public class LoginServlet extends HttpServlet{
	private static final long serialVersionUID = 1L;
	
	private UtilisateurDAO utilisateurDao;
	public void init() {
        this.utilisateurDao = new UtilisateurDAO();
    }
	
	// Affiche le formulaire de connexion
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String path = request.getServletPath();

        if ("/logout".equals(path)) {
            // Si l'utilisateur accède à /logout, on le déconnecte
            logout(request, response);
        } else {
            // Sinon, on affiche le formulaire de connexion
            request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
        }
    }
    
    // Traite la soumission du formulaire de connexion
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String email = request.getParameter("email");
        String motDePasse = request.getParameter("motDePasse");
        
        // 1. Appeler la DAO pour vérifier les identifiants
        Utilisateur utilisateur = utilisateurDao.findByEmailAndPassword(email, motDePasse);

        if (utilisateur != null) {
            // 2. Connexion réussie : Stocker l'utilisateur dans la session
            HttpSession session = request.getSession();
            session.setAttribute("utilisateurConnecte", utilisateur);
            
            // 3. Rediriger vers la page d'accueil ou la liste des cours
            response.sendRedirect(request.getContextPath() + "/cours");
        } else {
            // 4. Échec de la connexion : Afficher un message d'erreur
            request.setAttribute("message", "Email ou mot de passe incorrect.");
            request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
        }
    }

    private void logout(HttpServletRequest request, HttpServletResponse response) 
            throws IOException {
        
        // Récupérer la session existante (sans en créer une nouvelle)
        HttpSession session = request.getSession(false);
        
        if (session != null) {
            session.invalidate();
        }
        response.sendRedirect(request.getContextPath() + "/login");
    }
}
