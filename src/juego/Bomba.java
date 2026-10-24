package juego;

import java.awt.Color;
import entorno.Entorno;

public class Bomba {
    double x;
    double y;
    double anchoExplosion;
    double altoExplosion;

    int temporizador;
    boolean explotando;
    int tiempoExplosion;

    public Bomba(double x, double y, double anchoCalle, boolean esHorizontal) {
        this.x = x;
        this.y = y;
        
 
        if (esHorizontal) {
            this.anchoExplosion = anchoCalle * 3; // Largo de la calle
            this.altoExplosion = anchoCalle;      // Ancho de la calle
        } else {
            this.anchoExplosion = anchoCalle;      // Ancho de la calle
            this.altoExplosion = anchoCalle * 3;   // Largo de la calle
        }

        this.temporizador = 120; // ~2 segundos
        this.explotando = false;
        this.tiempoExplosion = 0;
    }

    public void actualizar() {
        if (!this.explotando) {
            this.temporizador--;
            if (this.temporizador <= 0) {
                this.explotando = true;
                this.tiempoExplosion = 30; // 
            }
        } else {
            this.tiempoExplosion--;
        }
    }


    public boolean terminada() {
        return this.explotando && this.tiempoExplosion <= 0;
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