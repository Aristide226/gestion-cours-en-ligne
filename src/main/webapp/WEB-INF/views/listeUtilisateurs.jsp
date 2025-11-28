<?xml version="1.0" encoding="UTF-8" ?>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>Liste des Utilisateurs</title>
</head>
<body>
	<h1>👥 Gestion des Utilisateurs</h1>
    <p><a href="utilisateurs?action=new">Ajouter un nouvel utilisateur</a></p>

    <table>
        <thead>
            <tr><th>Nom</th><th>Prénom</th><th>Email</th><th>Rôle</th><th>Actions</th></tr>
        </thead>
        <tbody>
            <c:forEach items="${listeUtilisateurs}" var="user">
                <tr>
                    <td>${user.nom}</td>
                    <td>${user.prenom}</td>
                    <td>${user.email}</td>
                    <td>${user.role}</td>
                    <td>
                        <a href="utilisateurs?action=delete&id=${user.id}" 
                           onclick="return confirm('Supprimer ${user.nom} ${user.prenom} ?');">
                            Supprimer
                        </a>
                    </td>
                </tr>
            </c:forEach>
        </tbody>
    </table>
    <a href="logout">Se Déconnecter</a>
</body>
</html>