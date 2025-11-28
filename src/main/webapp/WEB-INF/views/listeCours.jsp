<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<c:set var="userRole" value="${sessionScope.utilisateurConnecte.role}" />
<c:set var="inscriptionStatus" value="${requestScope.inscriptionStatus}" />

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title> Catalogue de Cours en Ligne </title>
</head>
<body>
	<h1>📚 Catalogue des Cours Disponibles</h1>
	
	<%-- AFFICHAGE DES MESSAGES--%>
    <c:if test="${param.success == 'enrolled'}">
        <p style="color: green;">✅ Félicitations ! Vous êtes inscrit au cours : **${param.titre}**.</p>
    </c:if>
    <c:if test="${param.error == 'already_enrolled'}">
        <p style="color: orange;">⚠️ Attention : Vous êtes déjà inscrit à ce cours.</p>
    </c:if>
    <c:if test="${not empty erreur}">
        <p style="color: red;"><strong>Erreur:</strong> ${erreur}</p>
    </c:if>
	
	<c:if test="${userRole == 'PROF'}">
    	<p><a href="cours?action=new">Ajouter un nouveau cours</a></p>
	</c:if>
      
    <table>
        <thead>
            <tr>
                <th>Titre</th>
                <th>Niveau</th>
                <th>Durée (Heures)</th>
                <th>Description</th>
                <th>Action</th> <%-- AJOUTER CETTE ENTÊTE --%>
            </tr>
        </thead>
        <tbody>
            <c:forEach items="${listeCours}" var="cours">
                <tr>
                    <td><strong>${cours.titre}</strong></td>
                    <td>${cours.niveau}</td>
                    <td>${cours.dureeHeures}</td>
                    <td>${cours.description}</td>
                    
                    <td>
                        <c:choose>
                            <%-- CAS 1 : Utilisateur est Professeur (Actions CUD) --%>
                            <c:when test="${userRole == 'PROF'}">
                                <a href="cours?action=edit&id=${cours.id}">Modifier</a>
                                &nbsp; | &nbsp;
                                <a href="cours?action=delete&id=${cours.id}" onclick="return confirm('Supprimer ${cours.titre} ?');">
                                    Supprimer
                                </a>
                            </c:when>
                            
                            <%-- CAS 2 : Utilisateur est Étudiant (Inscription) --%>
                            <c:when test="${userRole == 'ETUDIANT'}">
                                <c:set var="inscrit" value="${inscriptionStatus[cours.id]}" />
                                
                                <c:choose>
                                    <c:when test="${inscrit == true}">
                                        <button disabled style="background-color: lightgreen;">Déjà Inscrit</button>
                                        <%-- Ici, vous pourriez ajouter un lien de désinscription (unenroll) --%>
                                    </c:when>
                                    <c:otherwise>
                                        <%-- Lien vers la nouvelle action 'enroll' --%>
                                        <a href="cours?action=enroll&id=${cours.id}&titre=${cours.titre}">
                                            S'inscrire
                                        </a>
                                    </c:otherwise>
                                </c:choose>
                            </c:when>
                            
                            <%-- CAS 3 : Non connecté ou autre rôle (Rien ou un message) --%>
                            <c:otherwise>
                                (Accès Restreint)
                            </c:otherwise>
                        </c:choose>
                    </td>
                </tr>
            </c:forEach>
        </tbody>
    </table>
    <a href="logout">Se Déconnecter</a>
</body>
</html>