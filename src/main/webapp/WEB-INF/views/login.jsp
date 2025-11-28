<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Connexion - Gestion des Cours</title>
</head>
<body>
    <h1>🔒 Connexion</h1>
    
    <c:if test="${not empty message}">
        <p style="color: red;">${message}</p>
    </c:if>
    
    <form action="login" method="post"> 
        
        <label for="email">Adresse Email :</label><br>
        <input type="email" id="email" name="email" required><br><br>

        <label for="motDePasse">Mot de Passe :</label><br>
        <input type="password" id="motDePasse" name="motDePasse" required><br><br>

        <input type="submit" value="Se Connecter">
    </form>
    <p>
        <a href="utilisateurs?action=new">Créer un compte</a> (pour les administrateurs)
    </p>
</body>
</html>