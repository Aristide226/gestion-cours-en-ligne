<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>

<c:set var="userRole" value="${sessionScope.utilisateurConnecte.role}" />
<c:set var="inscriptionStatus" value="${requestScope.inscriptionStatus}" />

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Catalogue de Cours en Ligne</title>
</head>
<body>
    <h1>📚 Catalogue des Cours Disponibles</h1>
    
    <%-- AFFICHAGE DES MESSAGES --%>
    <c:if test="${param.success == 'enrolled'}">
        <p style="color: green;">
            ✅ Félicitations ! Vous êtes inscrit au cours : <strong>${param.titre}</strong>.
        </p>
    </c:if>
    
    <c:if test="${param.error == 'already_enrolled'}">
        <p style="color: orange;">
            ⚠️ Attention : Vous êtes déjà inscrit à ce cours.
        </p>
    </c:if>
    
    <c:if test="${not empty erreur}">
        <p style="color: red;">
            <strong>Erreur :</strong> ${erreur}
        </p>
    </c:if>
    
    <%-- LIEN POUR AJOUTER UN NOUVEAU COURS (PROFESSEUR) --%>
    <c:if test="${userRole == 'PROF'}">
        <p>
            <a href="cours?action=new">Ajouter un nouveau cours</a>
        </p>
    </c:if>
    
    <%-- TABLEAU DES COURS --%>
    <table>
        <thead>
            <tr>
                <th>Titre</th>
                <th>Niveau</th>
                <th>Durée (Heures)</th>
                <th>Description</th>
                <th>Action</th>
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
                                &nbsp;|&nbsp;
                                <a href="cours?action=delete&id=${cours.id}" 
                                   onclick="return confirm('Supprimer ${cours.titre} ?');">
                                    Supprimer
                                </a>
                            </c:when>
                            
                            <%-- CAS 2 : Utilisateur est Étudiant (Inscription) --%>
                            <c:when test="${userRole == 'ETUDIANT'}">
                                <c:set var="inscrit" value="${inscriptionStatus[cours.id]}" />
                                
                                <c:choose>
                                    <c:when test="${inscrit == true}">
                                        <button disabled style="background-color: lightgreen;">
                                            Déjà Inscrit
                                        </button>
                                    </c:when>
                                    <c:otherwise>
                                       
                                        <a href="cours?action=enroll&id=${cours.id}&titre=${cours.titre}">
                                            S'inscrire
                                        </a>
                                    </c:otherwise>
                                </c:choose>
                            </c:when>
                            
                            <c:otherwise>
                                (Accès Restreint)
                            </c:otherwise>
                        </c:choose>
                    </td>
                </tr>
            </c:forEach>
        </tbody>
    </table>
    
    <p>
        <a href="logout">Se Déconnecter</a>
    </p>
</body>
</html>