<?xml version="1.0" encoding="UTF-8" ?>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>Modifier le Cours</title>
</head>
<body>
	<h1>✏️ Modifier le Cours : ${cours.titre}</h1>
	<form action="cours" method="post"> 
        
        <input type="hidden" name="id" value="${cours.id}">
        
        <input type="hidden" name="action" value="update">

        <label for="titre">Titre du Cours :</label><br>
        <input type="text" id="titre" name="titre" value="${cours.titre}" required><br><br>

        <label for="description">Description :</label><br>
        <textarea id="description" name="description" rows="4" required>${cours.description}</textarea><br><br>

        <label for="niveau">Niveau :</label><br>
        <select id="niveau" name="niveau">
            <option value="Débutant" ${cours.niveau == 'Débutant' ? 'selected' : ''}>Débutant</option>
            <option value="Intermédiaire" ${cours.niveau == 'Intermédiaire' ? 'selected' : ''}>Intermédiaire</option>
            <option value="Avancé" ${cours.niveau == 'Avancé' ? 'selected' : ''}>Avancé</option>
        </select><br><br>
        
        <label for="dureeHeures">Durée (en heures) :</label><br>
        <input type="number" id="dureeHeures" name="dureeHeures" min="1" value="${cours.dureeHeures}" required><br><br>

        <input type="submit" value="Enregistrer les Modifications">
        <a href="cours">Annuler</a>
    </form>
</body>
</html>