package juego;

import java.awt.Color;
import entorno.Entorno;

public class Bomba {
    double x;
    double y;
    double anchoExplosion;
    double altoExplosion;

    long tiempoCreacion;
    long tiempoTranscurrido;
    int demoraExplosion;
    int duracionExplosion;
    boolean explotando;

    public Bomba(double x, double y, double anchoCalle, boolean esHorizontal, long tiempoActual) {
        this.x = x;
        this.y = y;
        
 
        if (esHorizontal) {
            this.anchoExplosion = anchoCalle * 3; // Largo de la calle
            this.altoExplosion = anchoCalle;      // Ancho de la calle
        } else {
            this.anchoExplosion = anchoCalle;      // Ancho de la calle
            this.altoExplosion = anchoCalle * 3;   // Largo de la calle
        }

        this.tiempoCreacion = tiempoActual;
        this.tiempoTranscurrido = 0;
        this.demoraExplosion = 2000;
        this.duracionExplosion = 500;
        this.explotando = false;
    }

    public void actualizar(long tiempoActual) {
        this.tiempoTranscurrido = tiempoActual - this.tiempoCreacion;
        this.explotando = this.tiempoTranscurrido >= this.demoraExplosion;
    }

    public boolean terminada() {
        return this.tiempoTranscurrido >= this.demoraExplosion + this.duracionExplosion;
    }

    public boolean afectaA(double otroX, double otroY, double otroAncho, double otroAlto) {
        if (this.terminada()) {
            return false;
        }

        if (!this.explotando) {
            double distanciaX = Math.max(Math.abs(this.x - otroX) - otroAncho / 2, 0);

            double distanciaY = Math.max(Math.abs(this.y - otroY) - otroAlto / 2, 0);

            return distanciaX * distanciaX + distanciaY * distanciaY <= 36;
        }

        boolean coincideX =Math.abs(this.x - otroX) < (this.anchoExplosion + otroAncho) / 2;

        boolean coincideY =Math.abs(this.y - otroY) < (this.altoExplosion + otroAlto) / 2;

        return coincideX && coincideY;
    }

    public void dibujar(Entorno entorno) {
        if (this.terminada()) {
            return;
        }

        if (this.explotando) {
            entorno.dibujarRectangulo(this.x, this.y,this.anchoExplosion, this.altoExplosion,0, new Color(255, 120, 0));
        } 
        else {
            entorno.dibujarCirculo(this.x, this.y, 12, Color.BLACK);
        }
    }
}