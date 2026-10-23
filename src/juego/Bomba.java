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
        
        // Si el enemigo va horizontal, la explosión cubre el largo de la calle (X) y el ancho (Y).
        // Si va vertical, cubre el ancho de la calle (X) y el largo (Y).
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
                this.tiempoExplosion = 30; // ~0.5s de explosión visible
            }
        } else {
            this.tiempoExplosion--;
        }
    }

    // La bomba terminó su ciclo (explosión completada)
    public boolean terminada() {
        return this.explotando && this.tiempoExplosion <= 0;
    }

    // ¿El área de efecto (ya sea la bomba o la explosión) toca a este rectángulo?
    public boolean afectaA(double otroX, double otroY, double otroAncho, double otroAlto) {
        boolean coincideX = Math.abs(this.x - otroX) < (this.anchoExplosion + otroAncho) / 2;
        boolean coincideY = Math.abs(this.y - otroY) < (this.altoExplosion + otroAlto) / 2;
        return coincideX && coincideY;
    }

    public void dibujar(Entorno entorno) {
        if (!this.explotando) {
            // Mientras no explota, dibuja un círculo negro (o un rectángulo si preferís)
            entorno.dibujarCirculo(this.x, this.y, 12, Color.BLACK);
        } else {
            // Cuando explota, dibuja el área de daño en forma de rectángulo naranja
            entorno.dibujarRectangulo(this.x, this.y,
                    this.anchoExplosion, this.altoExplosion, 0, new Color(255, 120, 0));
        }
    }
}