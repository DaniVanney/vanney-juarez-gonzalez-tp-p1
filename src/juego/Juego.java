package juego;

import java.awt.Color;
import entorno.Entorno;
import entorno.InterfaceJuego;

public class Juego extends InterfaceJuego {
    Entorno entorno;

    Mapa mapa;
    Repartidor repartidor;
    Pedido pedido;

    Enemigo[] enemigos;
    Bomba[] bombas;
    Proyectil proyectil;

    int pedidosEntregados;
    int pedidosPerdidos;
    int pedidosRobados;
    int enemigosEliminados;
    int vidas;
    long tiempoActual;
    long inicioPedido;
    int tiempoLimitePedido;

    int pedidosParaGanar;
    int pedidosRobadosParaPerder;
    int vidasIniciales;

    String estadoJuego;

    Juego() {
        this.entorno = new Entorno(this, "Delivery Rush", 800, 600);
        this.mapa = new Mapa(0, 60, 800, 540);

        // Cargar la imagen; si no se encuentra, se dibuja un rectángulo.
        java.awt.Image imgRepartidor = null;
        try {
            imgRepartidor = javax.imageio.ImageIO.read(new java.io.File("repartidor.png"));
        } catch (Exception e) {
            System.out.println("No se pudo cargar repartidor.png");
        }
        
        // Pasamos la imagen (si es null, dibuja el cuadrado amarillo)
        this.repartidor = new Repartidor(400, 585, imgRepartidor);

        this.vidasIniciales = 3;
        this.vidas = this.vidasIniciales;
        this.pedidosParaGanar = 5;
        this.pedidosRobadosParaPerder = 5;
        this.tiempoLimitePedido = 30000; // Milisegundos: 30 segundos.

        this.pedidosEntregados = 0;
        this.pedidosPerdidos = 0;
        this.pedidosRobados = 0;
        this.enemigosEliminados = 0;
        this.tiempoActual = 0;
        this.inicioPedido = 0;

        this.bombas = new Bomba[0];
        this.proyectil = null;

        this.enemigos = new Enemigo[4];
        this.generarEnemigo(0);
        this.generarEnemigo(1);
        this.generarEnemigo(2);
        this.generarEnemigo(3);

        this.crearNuevoPedido();
        this.estadoJuego = "Jugando";
        this.entorno.iniciar();
    }

    void generarEnemigo(int indice) {
        boolean horizontal = Math.random() < 0.5;
        // El lugar cero garantiza al menos un enemigo que deja bombas.
        boolean dejaBombas = indice == 0 || Math.random() < 0.4;
        double x, y;
        if (horizontal) {
            int filaCalle = (int) (Math.random() * (this.mapa.filas + 1));
            y = this.mapa.obtenerYCentroCalleHorizontal(filaCalle);
            x = this.mapa.x + 20 + Math.random() * (this.mapa.ancho - 40);
        } else {
            int colCalle = (int) (Math.random() * (this.mapa.columnas + 1));
            x = this.mapa.obtenerXCentroCalleVertical(colCalle);
            y = this.mapa.y + 20 + Math.random() * (this.mapa.alto - 40);
        }
        // Evitar que un reemplazo aparezca encima del repartidor.
        if (Math.abs(x - this.repartidor.x) < 80 && Math.abs(y - this.repartidor.y) < 80) {
            if (horizontal) {
                if (this.repartidor.x < this.mapa.x + this.mapa.ancho / 2) {
                    x = this.mapa.x + this.mapa.ancho - 20;
                } else {
                    x = this.mapa.x + 20;
                }
            } else {
                if (this.repartidor.y < this.mapa.y + this.mapa.alto / 2) {
                    y = this.mapa.y + this.mapa.alto - 20;
                } else {
                    y = this.mapa.y + 20;
                }
            }
        }
        this.enemigos[indice] = new Enemigo(x, y, horizontal, dejaBombas, this.tiempoActual);
    }

    void crearNuevoPedido() {
        int indiceNegocio = (int) (Math.random() * this.mapa.negocios.length);
        int indiceCasa = (int) (Math.random() * this.mapa.casas.length);
        this.pedido = new Pedido(this.mapa.negocios[indiceNegocio], this.mapa.casas[indiceCasa]);
        this.pedido.recibirEnNegocio();
        this.inicioPedido = this.tiempoActual;
    }

    public void tick() {

        if (!this.estadoJuego.equals("Jugando")) {
            this.dibujarPantallaFinal(this.estadoJuego);
            return;
        }

        // El reloj del entorno mide milisegundos, sin suponer una cantidad de ticks.
        this.tiempoActual = this.entorno.tiempo();
        boolean pedidoVencido = this.pedido.estaActivo()
            && this.tiempoActual - this.inicioPedido >= this.tiempoLimitePedido;
        if (pedidoVencido) {
            this.pedido.perder();
            this.pedidosPerdidos++;
            this.crearNuevoPedido();
        }

        // Movimiento del repartidor
        if (this.entorno.estaPresionada('d') || this.entorno.estaPresionada(this.entorno.TECLA_DERECHA)) {
            double xAnterior = this.repartidor.x;
            this.repartidor.moverDerecha(this.mapa.x + this.mapa.ancho);
            if (this.mapa.hayColision(this.repartidor)) {
                this.repartidor.x = xAnterior;
            } else if (this.repartidor.x != xAnterior) {
                this.repartidor.dirX = 1;
                this.repartidor.dirY = 0;
            }
        }
        if (this.entorno.estaPresionada('a') || this.entorno.estaPresionada(this.entorno.TECLA_IZQUIERDA)) {
            double xAnterior = this.repartidor.x;
            this.repartidor.moverIzquierda(this.mapa.x);
            if (this.mapa.hayColision(this.repartidor)) {
                this.repartidor.x = xAnterior;
            } else if (this.repartidor.x != xAnterior) {
                this.repartidor.dirX = -1;
                this.repartidor.dirY = 0;
            }
        }
        if (this.entorno.estaPresionada('w') || this.entorno.estaPresionada(this.entorno.TECLA_ARRIBA)) {
            double yAnterior = this.repartidor.y;
            this.repartidor.moverArriba(this.mapa.y);
            if (this.mapa.hayColision(this.repartidor)) {
                this.repartidor.y = yAnterior;
            } else if (this.repartidor.y != yAnterior) {
                this.repartidor.dirX = 0;
                this.repartidor.dirY = -1;
            }
        }
        if (this.entorno.estaPresionada('s') || this.entorno.estaPresionada(this.entorno.TECLA_ABAJO)) {
            double yAnterior = this.repartidor.y;
            this.repartidor.moverAbajo(this.mapa.y + this.mapa.alto);
            if (this.mapa.hayColision(this.repartidor)) {
                this.repartidor.y = yAnterior;
            } else if (this.repartidor.y != yAnterior) {
                this.repartidor.dirX = 0;
                this.repartidor.dirY = 1;
            }
        }

        // Retiro y entrega: estar sobre la flecha y hacer clic en el edificio.
        if (!pedidoVencido && this.entorno.sePresionoBoton(this.entorno.BOTON_DERECHO)) {
            if (this.pedido.estado.equals("Esperando")) {
                Negocio negocio = this.pedido.origen;
                double yFlecha = this.mapa.obtenerYFlechaNegocio(negocio);

                boolean sobreFlecha =
                    Math.abs(this.repartidor.x - negocio.x) <= this.repartidor.ancho / 2
                    && Math.abs(this.repartidor.y - yFlecha) <= this.repartidor.alto / 2;

                boolean clicCorrecto = negocio.contienePunto(
                    this.entorno.mouseX(), this.entorno.mouseY()
                );

                if (sobreFlecha && clicCorrecto) {
                    this.pedido.retirar();
                }
            } else if (this.pedido.estado.equals("En camino")) {
                Casa casa = this.pedido.destino;
                double yFlecha = this.mapa.obtenerYFlechaCasa(casa);

                boolean sobreFlecha =
                    Math.abs(this.repartidor.x - casa.x) <= this.repartidor.ancho / 2
                    && Math.abs(this.repartidor.y - yFlecha) <= this.repartidor.alto / 2;

                boolean clicCorrecto = casa.contienePunto(
                    this.entorno.mouseX(), this.entorno.mouseY()
                );

                if (sobreFlecha && clicCorrecto) {
                    this.pedido.entregar();
                    this.pedidosEntregados++;
                    this.crearNuevoPedido();
                }
            }
        }

        // Disparo
        if (this.entorno.sePresionoBoton(this.entorno.BOTON_IZQUIERDO) && this.proyectil == null) {
            this.proyectil = new Proyectil(this.repartidor.x, this.repartidor.y, this.repartidor.dirX, this.repartidor.dirY);
        }

        // Actualizar proyectil
        if (this.proyectil != null) {
            this.proyectil.mover();
            for (int i = 0; i < this.enemigos.length; i++) {
                Enemigo e = this.enemigos[i];
                if (e != null && this.proyectil != null && this.proyectil.colisionaCon(e.x, e.y, e.ancho, e.alto)) {
                    this.enemigos[i] = null;
                    this.proyectil = null;
                    this.enemigosEliminados++;
                    this.generarEnemigo(i);
                    break;
                }
            }
            if (this.proyectil != null && this.proyectil.fueraDe(this.mapa.x, this.mapa.x + this.mapa.ancho, this.mapa.y, this.mapa.y + this.mapa.alto)) {
                this.proyectil = null;
            }
        }

        // Actualizar enemigos
        for (int i = 0; i < this.enemigos.length; i++) {
            Enemigo e = this.enemigos[i];
            if (e == null) continue;
            e.mover(this.mapa.x, this.mapa.x + this.mapa.ancho, this.mapa.y, this.mapa.y + this.mapa.alto);
            if (e.debeDejarBomba(this.tiempoActual)) {
                this.agregarBomba(new Bomba(
                    e.x, e.y, this.mapa.anchoCalle, e.horizontal, this.tiempoActual
                ));
            }
            if (e.colisionaCon(this.repartidor.x, this.repartidor.y, this.repartidor.ancho, this.repartidor.alto)) {
                this.vidas--;
                if (this.pedido.estado.equals("En camino")) {
                    this.pedido.perder();
                    this.pedidosPerdidos++;
                    this.pedidosRobados++;
                    this.crearNuevoPedido();
                }
                this.enemigos[i] = null;
                this.generarEnemigo(i);
            }
        }

        // Actualizar bombas
        for (int i = 0; i < this.bombas.length; i++) {
            Bomba b = this.bombas[i];
            if (b == null) continue;
            b.actualizar(this.tiempoActual);
            if (b.afectaA(this.repartidor.x, this.repartidor.y, this.repartidor.ancho, this.repartidor.alto)) {
                this.vidas = 0; 
            }
            if (b.terminada()) this.bombas[i] = null;
        }

        // Una derrota tiene prioridad si ocurre al mismo tiempo que la última entrega.
        if (this.vidas <= 0 || this.pedidosRobados >= this.pedidosRobadosParaPerder) {
            this.estadoJuego = "PERDISTE";
        } else if (this.pedidosEntregados >= this.pedidosParaGanar) {
            this.estadoJuego = "GANASTE";
        }

        if (!this.estadoJuego.equals("Jugando")) {
            this.dibujarPantallaFinal(this.estadoJuego);
            return;
        }

        // ---- DIBUJAR ----
        this.mapa.dibujar(this.entorno);

        // Dibujar flechas
        if (this.pedido.estado.equals("En camino")) {
            this.mapa.dibujarFlechaCasaRoja(this.entorno, this.pedido.destino);
        }
        if (this.pedido.estado.equals("Esperando")) {
            this.mapa.dibujarFlechaNegocio(this.entorno, this.pedido.origen);
        }

        for (int i = 0; i < this.enemigos.length; i++) {
            if (this.enemigos[i] != null) this.enemigos[i].dibujar(this.entorno);
        }
        for (int i = 0; i < this.bombas.length; i++) {
            if (this.bombas[i] != null) this.bombas[i].dibujar(this.entorno);
        }
        if (this.proyectil != null) this.proyectil.dibujar(this.entorno);

        this.repartidor.dibujar(this.entorno);
        this.dibujarPanel();
    }

    void agregarBomba(Bomba b) {
        // Reutilizar los lugares de bombas que ya terminaron.
        for (int i = 0; i < this.bombas.length; i++) {
            if (this.bombas[i] == null || this.bombas[i].terminada()) {
                this.bombas[i] = null;
                this.bombas[i] = b;
                return;
            }
        }
        Bomba[] nuevo = new Bomba[this.bombas.length + 1];
        for (int i = 0; i < this.bombas.length; i++) nuevo[i] = this.bombas[i];
        nuevo[this.bombas.length] = b;
        this.bombas = nuevo;
    }

    void dibujarPanel() {
        this.entorno.dibujarRectangulo(400, 30, 800, 60, 0, new Color(30, 30, 30));
        this.entorno.cambiarFont("Arial", 14, Color.WHITE);

        String tiempo = "Tiempo: " + (this.tiempoActual / 1000) + "s";
        String vidasStr = "Vidas: " + this.vidas;
        String entregados = "Entregados: " + this.pedidosEntregados;
        String eliminados = "Eliminados: " + this.enemigosEliminados;
        String estado = "Pedido: " + this.pedido.estado;

        long restante = this.tiempoLimitePedido - (this.tiempoActual - this.inicioPedido);
        long segundosRestantes = Math.max(0, (restante + 999) / 1000);

        this.entorno.escribirTexto(tiempo, 15, 24);
        this.entorno.escribirTexto(vidasStr, 160, 24);
        this.entorno.escribirTexto(entregados, 280, 24);
        this.entorno.escribirTexto(eliminados, 460, 24);
        this.entorno.escribirTexto(estado, 15, 48);
        this.entorno.escribirTexto("Plazo: " + segundosRestantes + "s", 280, 48);
        this.entorno.escribirTexto("Robados: " + this.pedidosRobados, 460, 48);
    }

    void dibujarPantallaFinal(String mensaje) {
        this.entorno.dibujarRectangulo(400, 300, 800, 600, 0, Color.BLACK);
        this.entorno.cambiarFont("Arial", 48, Color.WHITE);
        this.entorno.escribirTexto(mensaje, 300, 300);
    }

    @SuppressWarnings("unused")
    public static void main(String[] args) {
        Juego juego = new Juego();
    }
}