package juego;

import java.awt.Color;
import entorno.Entorno;

public class Mapa {
    
    double x;
    double y;
    double ancho;
    double alto;



    public Mapa(double x, double y, double ancho, double alto){
        this.x = x;
        this.y = y;
        this.ancho = ancho;
        this.alto = alto;
    }


    public void dibujar(Entorno entorno){
        entorno.dibujarRectangulo(this.x + this.ancho/2, this.y + this.alto/2, this.ancho, this.alto, 0, new Color(70, 70, 70));
        
    }
}
