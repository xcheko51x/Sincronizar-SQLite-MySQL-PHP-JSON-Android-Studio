<?php

    require "conexion.php";
    
    $sql = "SELECT * FROM ventas";
    $query = $mysqli->query($sql);
    
    $datos = array();
    
    while($resultado = $query->fetch_assoc()) {
        $datos[] = $resultado;
    }
    
    echo json_encode(array("Ventas" => $datos));
    
?>
