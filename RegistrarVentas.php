<?php
    
    $server = "127.0.0.1";
    $user = "root";
    $pass = "";
    $bd = "pruebas";
    
    $json = $_POST['json'];
    
    $jsonDecode = json_decode($json, true);
    
    $conexion = mysqli_connect($server, $user, $pass,$bd)
    or die("Ha sucedido un error inexperado en la conexion de la base de datos");
    
    foreach($jsonDecode['Productos'] as $fila) {
        $producto = mysqli_real_escape_string($conexion, $fila['producto']);
        $fechaVenta = mysqli_real_escape_string($conexion, $fila['fechaVenta']);
        $precio = mysqli_real_escape_string($conexion, $fila['precio']);
        $sincronizado = mysqli_real_escape_string($conexion, $fila['sincronizado']);
        
        $sql = "INSERT INTO ventas (producto, fechaVenta, precio, sincronizado) VALUES ('$producto', '$fechaVenta', '$precio', 'true')";
        
        mysqli_query($conexion, $sql);
    }
    
    echo "OK";
    
    mysqli_close($conexion);
    
?>
