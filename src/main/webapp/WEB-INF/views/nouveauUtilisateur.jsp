<?xml version="1.0" encoding="UTF-8" ?>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>Insert title here</title>
</head>
<body>
	<h1>➕ Ajouter un nouvel Utilisateur</h1>
    <form action="utilisateurs" method="post"> 
        <label for="nom">Nom :</label><br><input type="text" id="nom" name="nom" required><br><br>
        <label for="prenom">Prénom :</label><br><input type="text" id="prenom" name="prenom" required><br><br>
        <label for="email">Email :</label><br><input type="email" id="email" name="email" required><br><br>
        <label for="motDePasse">Mot de Passe :</label><br><input type="password" id="motDePasse" name="motDePasse" required><br><br>
        <label for="role">Rôle :</label><br>
        <select id="role" name="role">
            <option value="PROF">Professeur</option>
            <option value="ETUDIANT">Étudiant</option>
        </select><br><br>

        <input type="submit" value="Enregistrer">
        <a href="utilisateurs">Annuler</a>
    </form>
</body>
</html>