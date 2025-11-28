<?xml version="1.0" encoding="UTF-8" ?>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>Ajouter un Nouveau Cours</title>
</head>
<body>
	<h1>➕ Ajouter un Nouveau Cours</h1>
    <form action="cours" method="post"> 
        
        <label for="titre">Titre du Cours :</label><br>
        <input type="text" id="titre" name="titre" required><br><br>

        <label for="description">Description :</label><br>
        <textarea id="description" name="description" rows="4" required></textarea><br><br>

        <label for="niveau">Niveau :</label><br>
        <select id="niveau" name="niveau">
            <option value="Débutant">Débutant</option>
            <option value="Intermédiaire">Intermédiaire</option>
            <option value="Avancé">Avancé</option>
        </select><br><br>
        
        <label for="dureeHeures">Durée (en heures) :</label><br>
        <input type="number" id="dureeHeures" name="dureeHeures" min="1" required><br><br>

        <input type="submit" value="Enregistrer le Cours">
        <a href="cours">Annuler et Retour à la Liste</a>
    </form>
</body>
</html>