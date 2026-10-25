package juego;

import java.awt.Color;
import java.awt.Image;
import entorno.Entorno;

public class Negocio {
    double x;
    double y;
    double ancho;
    double alto;
    Image img; 

    public Negocio(double x, double y, double ancho, double alto, Image img){
        this.x = x;
        this.y = y;
        this.ancho = ancho;
        this.alto = alto;
        this.img = img;
        if (img != null) {
            this.img = img.getScaledInstance((int) this.ancho, (int) this.alto, Image.SCALE_SMOOTH);
        }
    }

    public void dibujar(Entorno entorno){
        if (this.img != null) {
            entorno.dibujarImagen(this.img, this.x, this.y, 0, 1); 
        } else {
            entorno.dibujarRectangulo(this.x, this.y, this.ancho, this.alto, 0, new Color(70, 130, 200));
        }
    }

    public boolean contienePunto(double puntoX, double puntoY) {
        return Math.abs(this.x - puntoX) <= this.ancho / 2 && Math.abs(this.y - puntoY) <= this.alto / 2;
    }
}