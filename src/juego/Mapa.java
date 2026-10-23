package juego;

import java.awt.Color;
import entorno.Entorno;

public class Mapa {
    //variables de la clase
    double x;
    double y;
    double ancho;
    double alto;

    int filas;
    int columnas;
    int anchoCalle;

     //referencia a un arreglo de objetos (casa)
    Casa[] casas;

    //referencia a un arreglo de objetos (negocio)
    Negocio[] negocios;


    //Método constructor de Mapa
    public Mapa(double x, double y, double ancho, double alto){
        this.x = x;
        this.y = y;
        this.ancho = ancho;
        this.alto = alto;

        this.filas =4;
        this.columnas=4;
        this.anchoCalle=30;
        /*
        this.casas = new Casa[3];
        this.casas[0] = new Casa(this.x + this.anchoCalle +40, this.y + this.anchoCalle + 25, 50, 30);
        this.casas[1] = new Casa(this.x + this.anchoCalle + 120, this.y + this.anchoCalle + 25, 50, 30);
        this.casas[2] = new Casa(this.x + this.anchoCalle + 40, this.y + this.anchoCalle + 70, 50, 30);
        */
        this.casas = new Casa[this.filas * this.columnas * 3];

        this.negocios = new Negocio[this.filas * this.columnas];

        double anchoCuadra = (this.ancho - (this.columnas +1) * this.anchoCalle) / this.columnas;

        /* 
        for(int columna =0; columna < this.columnas; columna ++){
            double inicioX = this.x + this.anchoCalle + columna * (anchoCuadra + this.anchoCalle);

            int indiceCasa = columna * 3;
            this.casas[indiceCasa] = new Casa(inicioX + 40, this.y + this.anchoCalle + 25, 50, 30);
            this.casas[indiceCasa + 1] = new Casa(inicioX + 120, this.y + this.anchoCalle + 25, 50, 30);
            this.casas[indiceCasa+2] = new Casa(inicioX + 40, this.y + this.anchoCalle + 70, 50, 30);

        }
        */

        double altoCuadra =(this.alto - (this.filas + 1) * this.anchoCalle)/ this.filas;

        for (int fila = 0; fila < this.filas; fila++) {
            double inicioY = this.y + this.anchoCalle + fila * (altoCuadra + this.anchoCalle);

            for (int columna = 0; columna < this.columnas; columna++) {
                double inicioX = this.x + this.anchoCalle+ columna * (anchoCuadra + this.anchoCalle);

                int indiceNegocio = fila * this.columnas + columna;

                int indiceCasa = indiceNegocio * 3;

                this.casas[indiceCasa] = new Casa(inicioX + 40, inicioY + 25, 50, 30);
                this.casas[indiceCasa + 1] = new Casa(inicioX + 120, inicioY + 25, 50, 30);
                this.casas[indiceCasa + 2] = new Casa(inicioX + 40, inicioY + 70, 50, 30);
                
                this.negocios[indiceNegocio] = new Negocio(inicioX + 120, inicioY + 70, 50, 30);
            }
        }
    }


    public boolean hayColision(Repartidor repartidor) {
        for (int i = 0; i < this.casas.length; i++) {
            Casa casa = this.casas[i];

            if (repartidor.colisionaCon(
                    casa.x, casa.y, casa.ancho, casa.alto)) {
                return true;
            }
        }

        for (int i = 0; i < this.negocios.length; i++) {
            Negocio negocio = this.negocios[i];

            if (repartidor.colisionaCon(negocio.x, negocio.y, negocio.ancho, negocio.alto)) {
                return true;
            }
        }

        return false;
    }

    public void dibujarFlechaNegocio(Entorno entorno, Negocio negocio) {
        double altoCuadra =(this.alto - (this.filas + 1) * this.anchoCalle)/this.filas;
        int fila = (int) ((negocio.y - this.y) / (altoCuadra + this.anchoCalle));
        double xFlecha = negocio.x;
        double yFlecha = this.y+ (fila + 1) * (altoCuadra + this.anchoCalle)+ this.anchoCalle / 2.0;

        entorno.dibujarRectangulo(xFlecha, yFlecha + 5,6, 8, 0, Color.YELLOW);
        entorno.dibujarTriangulo(xFlecha, yFlecha - 3,12, 18, -Math.PI / 2, Color.YELLOW);
    }


    public void dibujar(Entorno entorno){
        entorno.dibujarRectangulo(this.x + this.ancho/2, this.y + this.alto/2, this.ancho, this.alto, 0, new Color(70, 70, 70));

        double anchoCuadra =(this.ancho - (this.columnas +1) * this.anchoCalle) / this.columnas;

        double altoCuadra = (this.alto - (this.filas + 1) * this.anchoCalle) /this.filas;


        for(int fila =0; fila < this.filas; fila ++){
            double centroY = this.y + this.anchoCalle + fila *(altoCuadra + this.anchoCalle) + altoCuadra /2;

        for(int columna =0; columna < this.columnas ; columna ++ ){
            double centroX = this.x + this.anchoCalle + columna * (anchoCuadra + this.anchoCalle) + anchoCuadra /2;
            
            entorno.dibujarRectangulo(centroX, centroY, anchoCuadra, altoCuadra, 0, new Color(90, 140, 80));
        }
        }

        for(int i = 0; i< this.casas.length; i++){
            this.casas[i].dibujar(entorno);
        }

        
        for(int i =0; i< this.negocios.length; i++){
            this.negocios[i].dibujar(entorno);
        }
    }
}
