package com.gdc.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

import com.gdc.model.Utilisateur;

@WebFilter(urlPatterns = {"/cours/*", "/utilisateurs/*", "/cours", "/utilisateurs"})
public class AuthFilter implements Filter{
	
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
		
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        
        HttpSession session = req.getSession(false);
        boolean estConnecte = (session != null && session.getAttribute("utilisateurConnecte") != null);
        
        String path = req.getRequestURI().substring(req.getContextPath().length());
        String method = req.getMethod(); // Récupérer la méthode HTTP
        
        boolean isPublicResource = path.equals("/login") || 
                (path.equals("/utilisateurs") && "new".equals(req.getParameter("action")) && "GET".equalsIgnoreCase(method)) ||
                (path.equals("/utilisateurs") && "POST".equalsIgnoreCase(method)); // L'ACTION POST est publique
        
        
        if (!estConnecte && !isPublicResource) {
            res.sendRedirect(req.getContextPath() + "/login");
            return;
        }
        
        
        if (estConnecte) {
        	Utilisateur utilisateur = (Utilisateur) session.getAttribute("utilisateurConnecte");
            String role = utilisateur.getRole();
            
            if (path.equals("/utilisateurs")) {
                String action = req.getParameter("action");
                
                if (!"PROF".equals(role) && !("new".equals(action) || "POST".equalsIgnoreCase(method))) {
                    res.sendError(HttpServletResponse.SC_FORBIDDEN, "Accès refusé. Seuls les professeurs peuvent gérer les utilisateurs.");
                    return;
                }
            }
        }

        chain.doFilter(request, response);
    }
}