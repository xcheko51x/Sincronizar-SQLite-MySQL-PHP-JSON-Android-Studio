package com.xcheko51x.syncsqlitemysql;

public class Venta {

    String idVenta;
    String producto;
    String fechaVenta;
    String precio;
    String sincronizado;

    public Venta(String idVenta, String producto, String fechaVenta, String precio, String sincronizado) {
        this.idVenta = idVenta;
        this.producto = producto;
        this.fechaVenta = fechaVenta;
        this.precio = precio;
        this.sincronizado = sincronizado;
    }

    public String getIdVenta() {
        return idVenta;
    }

    public void setIdVenta(String idVenta) {
        this.idVenta = idVenta;
    }

    public String getProducto() {
        return producto;
    }

    public void setProducto(String producto) {
        this.producto = producto;
    }

    public String getFechaVenta() {
        return fechaVenta;
    }

    public void setFechaVenta(String fechaVenta) {
        this.fechaVenta = fechaVenta;
    }

    public String getPrecio() {
        return precio;
    }

    public void setPrecio(String precio) {
        this.precio = precio;
    }

    public String getSincronizado() {
        return sincronizado;
    }

    public void setSincronizado(String sincronizado) {
        this.sincronizado = sincronizado;
    }
}
