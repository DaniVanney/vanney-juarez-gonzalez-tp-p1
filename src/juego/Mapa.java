package juego;

import java.awt.Color;
import entorno.Entorno;

public class Mapa {
    
    double x;
    double y;
    double ancho;
    double alto;


    int filas;
    int columnas;
    int anchoCalle;



    public Mapa(double x, double y, double ancho, double alto){
        this.x = x;
        this.y = y;
        this.ancho = ancho;
        this.alto = alto;

        this.filas =4;
        this.columnas=4;
        this.anchoCalle=30;
    }


    public void dibujar(Entorno entorno){
        entorno.dibujarRectangulo(this.x + this.ancho/2, this.y + this.alto/2, this.ancho, this.alto, 0, new Color(70, 70, 70));

        double anchoCuadra =(this.ancho - (this.columnas +1) * this.anchoCalle) / this.columnas;

        double altoCuadra = (this.alto - (this.filas + 1) * this.anchoCalle) /this.filas;

        double centroY = this.y + this.anchoCalle +altoCuadra /2;

        for (int columna =0; columna < this.columnas; columna ++){
            double centroX = this.x + this.anchoCalle + columna * (anchoCuadra + this.anchoCalle) + anchoCuadra /2;

            entorno.dibujarRectangulo(centroX, centroY, anchoCuadra, altoCuadra, 0, new Color(90,140,80));
        }
    }
}
