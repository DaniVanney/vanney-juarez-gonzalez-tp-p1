package juego;

import java.awt.Color;
import java.awt.Image;
import entorno.Entorno;

public class Mapa {
	 double x;
	 double y;
	 double ancho;
	 double alto;

	 int filas;
	 int columnas;
	 int anchoCalle;
	    
    Casa[] casas;
    Negocio[] negocios;

    public Mapa(double x, double y, double ancho, double alto){
        this.x = x;
        this.y = y;
        this.ancho = ancho;
        this.alto = alto;
        this.filas = 4;
        this.columnas = 4;
        this.anchoCalle = 30;

        //  CARGAR LAS DISTINTAS CASAS Y NEGOCIOS
        Image[] imagenesCasas = new Image[3]; 
        Image imgNegocio = null;
        try {
            imagenesCasas[0] = javax.imageio.ImageIO.read(new java.io.File("casa_dos.png"));
            imagenesCasas[1] = javax.imageio.ImageIO.read(new java.io.File("casa_uno.png"));
            imagenesCasas[2] = javax.imageio.ImageIO.read(new java.io.File("casa_tres.png"));
            imgNegocio = javax.imageio.ImageIO.read(new java.io.File("negocio.png"));
        } catch (Exception e) {
            System.out.println("No se pudieron cargar las imágenes.");
        }
        // ---------------------------------------------

        this.casas = new Casa[this.filas * this.columnas * 3];
        this.negocios = new Negocio[this.filas * this.columnas];

        double anchoCuadra = (this.ancho - (this.columnas +1) * this.anchoCalle) / this.columnas;
        double altoCuadra = (this.alto - (this.filas + 1) * this.anchoCalle) / this.filas;

        for (int fila = 0; fila < this.filas; fila++) {
            double inicioY = this.y + this.anchoCalle + fila * (altoCuadra + this.anchoCalle);
            for (int columna = 0; columna < this.columnas; columna++) {
                double inicioX = this.x + this.anchoCalle + columna * (anchoCuadra + this.anchoCalle);
                int indiceNegocio = fila * this.columnas + columna;
                int indiceCasa = indiceNegocio * 3;

        
                int azar1 = (int)(Math.random() * imagenesCasas.length);
                int azar2 = (int)(Math.random() * imagenesCasas.length);
                int azar3 = (int)(Math.random() * imagenesCasas.length);

         
                this.casas[indiceCasa] = new Casa(inicioX + 40, inicioY + 25, 50, 30, imagenesCasas[azar1]);
                this.casas[indiceCasa + 1] = new Casa(inicioX + 120, inicioY + 25, 50, 30, imagenesCasas[azar2]);
                this.casas[indiceCasa + 2] = new Casa(inicioX + 40, inicioY + 70, 50, 30, imagenesCasas[azar3]);
                
                this.negocios[indiceNegocio] = new Negocio(inicioX + 120, inicioY + 70, 50, 30, imgNegocio);
            }
        }
    }
    // El repartidor debe permanecer por completo sobre las calles.
    public boolean estaEnCalle(Repartidor repartidor) {
        if (repartidor.x - repartidor.ancho / 2 < this.x
                || repartidor.x + repartidor.ancho / 2 > this.x + this.ancho
                || repartidor.y - repartidor.alto / 2 < this.y
                || repartidor.y + repartidor.alto / 2 > this.y + this.alto) {
            return false;
        }
        double anchoCuadra = (this.ancho - (this.columnas + 1) * this.anchoCalle) / this.columnas;
        double altoCuadra = (this.alto - (this.filas + 1) * this.anchoCalle) / this.filas;
        for (int fila = 0; fila < this.filas; fila++) {
            double centroY = this.y + this.anchoCalle + fila * (altoCuadra + this.anchoCalle) + altoCuadra / 2;
            for (int columna = 0; columna < this.columnas; columna++) {
                double centroX = this.x + this.anchoCalle + columna * (anchoCuadra + this.anchoCalle) + anchoCuadra / 2;
                if (repartidor.colisionaCon(centroX, centroY, anchoCuadra, altoCuadra)) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean hayColision(Repartidor repartidor) {
        if (!this.estaEnCalle(repartidor)) {
            return true;
        }
        for (int i = 0; i < this.casas.length; i++) {
            Casa casa = this.casas[i];
            if (repartidor.colisionaCon(casa.x, casa.y, casa.ancho, casa.alto)) return true;
        }
        for (int i = 0; i < this.negocios.length; i++) {
            Negocio negocio = this.negocios[i];
            if (repartidor.colisionaCon(negocio.x, negocio.y, negocio.ancho, negocio.alto)) return true;
        }
        return false;
    }

    public double obtenerYFlechaCasa(Casa casa) {
        double altoCuadra = (this.alto - (this.filas + 1) * this.anchoCalle) / this.filas;
        int fila = (int) ((casa.y - this.y) / (altoCuadra + this.anchoCalle));
        double inicioY = this.y + this.anchoCalle + fila * (altoCuadra + this.anchoCalle);
        double centroCuadraY = inicioY + altoCuadra / 2;
        if (casa.y < centroCuadraY) {
            return inicioY - this.anchoCalle / 2.0;
        } else {
            return inicioY + altoCuadra + this.anchoCalle / 2.0;
        }
    }

    public void dibujarFlechaCasaRoja(Entorno entorno, Casa casa) {
        double xFlecha = casa.x;
        double yFlecha = this.obtenerYFlechaCasa(casa);
        int direccion = 1;
        if (casa.y < yFlecha) direccion = -1;
        entorno.dibujarRectangulo(xFlecha, yFlecha - direccion * 5, 6, 8, 0, Color.RED);
        entorno.dibujarTriangulo(xFlecha, yFlecha + direccion * 3, 12, 18, direccion * Math.PI / 2, Color.RED);
    }

    public double obtenerYFlechaNegocio(Negocio negocio) {
        double altoCuadra = (this.alto - (this.filas + 1) * this.anchoCalle) / this.filas;
        int fila = (int) ((negocio.y - this.y) / (altoCuadra + this.anchoCalle));
        double bordeSuperiorCalleAbajo = this.y + this.anchoCalle + fila * (altoCuadra + this.anchoCalle) + altoCuadra;
        return bordeSuperiorCalleAbajo + this.anchoCalle / 2.0;
    }

    public void dibujarFlechaNegocio(Entorno entorno, Negocio negocio) {
        double xFlecha = negocio.x;
        double yFlecha = this.obtenerYFlechaNegocio(negocio);
        entorno.dibujarRectangulo(xFlecha, yFlecha + 5, 6, 8, 0, Color.YELLOW);
        entorno.dibujarTriangulo(xFlecha, yFlecha - 3, 12, 18, -Math.PI / 2, Color.YELLOW);
    }

    public void dibujar(Entorno entorno){
        entorno.dibujarRectangulo(this.x + this.ancho/2, this.y + this.alto/2, this.ancho, this.alto, 0, new Color(70, 70, 70));
        double anchoCuadra = (this.ancho - (this.columnas +1) * this.anchoCalle) / this.columnas;
        double altoCuadra = (this.alto - (this.filas + 1) * this.anchoCalle) / this.filas;

        for(int fila =0; fila < this.filas; fila ++){
            double centroY = this.y + this.anchoCalle + fila *(altoCuadra + this.anchoCalle) + altoCuadra /2;
            for(int columna =0; columna < this.columnas ; columna ++ ){
                double centroX = this.x + this.anchoCalle + columna * (anchoCuadra + this.anchoCalle) + anchoCuadra /2;
                entorno.dibujarRectangulo(centroX, centroY, anchoCuadra, altoCuadra, 0, new Color(90, 140, 80));
            }
        }
        for(int i = 0; i< this.casas.length; i++) this.casas[i].dibujar(entorno);
        for(int i =0; i< this.negocios.length; i++) this.negocios[i].dibujar(entorno);
    }

    public double obtenerYCentroCalleHorizontal(int indiceCalle) {
        double altoCuadra = (this.alto - (this.filas + 1) * this.anchoCalle) / this.filas;
        return this.y + indiceCalle * (altoCuadra + this.anchoCalle) + this.anchoCalle / 2.0;
    }

    public double obtenerXCentroCalleVertical(int indiceCalle) {
        double anchoCuadra = (this.ancho - (this.columnas + 1) * this.anchoCalle) / this.columnas;
        return this.x + indiceCalle * (anchoCuadra + this.anchoCalle) + this.anchoCalle / 2.0;
    }
}