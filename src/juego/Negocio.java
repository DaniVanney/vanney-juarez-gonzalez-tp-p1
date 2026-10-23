package juego;

import java.awt.Color;
import entorno.Entorno;

public class Negocio {
    double x;
    double y;
    double ancho;
    double alto;



    //método constructor Neogcio
    public Negocio(double x, double y, double ancho, double alto){
        this.x = x;
        this.y =y;
        this.ancho = ancho;
        this.alto = alto;
    }

    public void dibujar(Entorno entorno){
        entorno.dibujarRectangulo(this.x, this.y, this.ancho, this.alto, 0, new Color(70, 130, 200));
    }

    public boolean contienePunto(double puntoX, double puntoY) {
        return Math.abs(this.x - puntoX) <= this.ancho / 2 && Math.abs(this.y - puntoY) <= this.alto / 2;
    }


    
}
