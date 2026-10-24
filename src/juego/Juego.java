package juego;

import java.awt.Color;
import java.awt.Image;
import entorno.Entorno;
import entorno.InterfaceJuego;

public class Juego extends InterfaceJuego {
    private Entorno entorno;

    Mapa mapa;
    Repartidor repartidor;
    Pedido pedido;

    Enemigo[] enemigos;
    Bomba[] bombas;
    Proyectil proyectil;

    int pedidosEntregados;
    int pedidosPerdidos;
    int enemigosEliminados;
    int vidas;
    int ticks;
    int tiempoLimitePedido;
    int ticksPedidoActual;

    int pedidosParaGanar;
    int pedidosPerdidosParaPerder;
    int vidasIniciales;

	String estadoJuego;

    Juego() {
        this.entorno = new Entorno(this, "Delivery Rush", 800, 600);
        this.mapa = new Mapa(0, 60, 800, 540);

        // --- CARGAR IMAGEN CON ImageIO (Método infalible de Java) ---
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
        this.pedidosPerdidosParaPerder = 5;
        this.tiempoLimitePedido = 60 * 30; // 30 seg

        this.pedidosEntregados = 0;
        this.pedidosPerdidos = 0;
        this.enemigosEliminados = 0;
        this.ticks = 0;
        this.ticksPedidoActual = 0;

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

    private void generarEnemigo(int indice) {
        boolean horizontal = Math.random() < 0.5;
        boolean dejaBombas = Math.random() < 0.4;
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
        this.enemigos[indice] = new Enemigo(x, y, horizontal, dejaBombas);
    }

    private void crearNuevoPedido() {
        int indiceNegocio = (int) (Math.random() * this.mapa.negocios.length);
        int indiceCasa = (int) (Math.random() * this.mapa.casas.length);
        this.pedido = new Pedido(this.mapa.negocios[indiceNegocio], this.mapa.casas[indiceCasa]);
        this.pedido.recibirEnNegocio();
        this.ticksPedidoActual = 0;
    }

    public void tick() {

		if (!this.estadoJuego.equals("Jugando")){
			this.dibujarPantallaFinal(this.estadoJuego);
			return;
		}

        this.ticks++;
        this.ticksPedidoActual++;

        // Movimiento del repartidor
        if (this.entorno.estaPresionada('d') || this.entorno.estaPresionada(this.entorno.TECLA_DERECHA)) {
            double xAnterior = this.repartidor.x;
            this.repartidor.moverDerecha(this.mapa.x + this.mapa.ancho);
            if (this.mapa.hayColision(this.repartidor)) this.repartidor.x = xAnterior;
        }
        if (this.entorno.estaPresionada('a') || this.entorno.estaPresionada(this.entorno.TECLA_IZQUIERDA)) {
            double xAnterior = this.repartidor.x;
            this.repartidor.moverIzquierda(this.mapa.x);
            if (this.mapa.hayColision(this.repartidor)) this.repartidor.x = xAnterior;
        }
        if (this.entorno.estaPresionada('w') || this.entorno.estaPresionada(this.entorno.TECLA_ARRIBA)) {
            double yAnterior = this.repartidor.y;
            this.repartidor.moverArriba(this.mapa.y);
            if (this.mapa.hayColision(this.repartidor)) this.repartidor.y = yAnterior;
        }
        if (this.entorno.estaPresionada('s') || this.entorno.estaPresionada(this.entorno.TECLA_ABAJO)) {
            double yAnterior = this.repartidor.y;
            this.repartidor.moverAbajo(this.mapa.y + this.mapa.alto);
            if (this.mapa.hayColision(this.repartidor)) this.repartidor.y = yAnterior;
        }

        // Retirar pedido (COLISIÓN DIRECTA CON EL NEGOCIO)
        // ---- Retirar pedido del negocio ----
        if (this.pedido.estado.equals("Esperando") && this.entorno.sePresionoBoton(this.entorno.BOTON_DERECHO)) {
            Negocio negocio = this.pedido.origen;
            
            // Verifica si el mouse está sobre el negocio
            boolean clicEnNegocio = negocio.contienePunto(this.entorno.mouseX(), this.entorno.mouseY());
            
            // Calcula la distancia real del repartidor al centro del negocio
            double dist = Math.sqrt(Math.pow(this.repartidor.x - negocio.x, 2) + Math.pow(this.repartidor.y - negocio.y, 2));
            
            // Si el clic fue en el negocio Y el repartidor está a menos de 60 px de su centro
            if (clicEnNegocio && dist < 60) {
                this.pedido.retirar();
            }
        }

        // ---- Entregar pedido ----
        if (this.pedido.estado.equals("En camino") && this.entorno.sePresionoBoton(this.entorno.BOTON_DERECHO)) {
            Casa casa = this.pedido.destino;
            
            boolean clicEnCasa = casa.contienePunto(this.entorno.mouseX(), this.entorno.mouseY());
            
            double dist = Math.sqrt(Math.pow(this.repartidor.x - casa.x, 2) + Math.pow(this.repartidor.y - casa.y, 2));
            
            if (clicEnCasa && dist < 60) {
                this.pedido.entregar();
                this.pedidosEntregados++;
                this.crearNuevoPedido();
            }
        }

        // Entregar pedido (COLISIÓN DIRECTA CON LA CASA)
        if (this.pedido.estado.equals("En camino") && this.entorno.sePresionoBoton(this.entorno.BOTON_DERECHO)) {
            Casa casa = this.pedido.destino;
            // Verifica si está tocando la casa
            if (this.repartidor.colisionaCon(casa.x, casa.y, casa.ancho, casa.alto)) {
                this.pedido.entregar();
                this.pedidosEntregados++;
                this.crearNuevoPedido();
            }
        }

        // Pedido perdido por tiempo
        if (this.pedido.estaActivo() && this.ticksPedidoActual > this.tiempoLimitePedido) {
            this.pedido.perder();
            this.pedidosPerdidos++;
            this.crearNuevoPedido();
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
            if (e.dejaBombas) {
                e.temporizadorBomba--;
                if (e.temporizadorBomba <= 0) {
                	this.agregarBomba(new Bomba(e.x, e.y, this.mapa.anchoCalle, e.horizontal));
                    e.temporizadorBomba = e.intervaloBomba;
                }
            }
            if (e.colisionaCon(this.repartidor.x, this.repartidor.y, this.repartidor.ancho, this.repartidor.alto)) {
                this.vidas--;
                if (this.pedido.estado.equals("En camino")) {
                    this.pedido.perder();
                    this.pedidosPerdidos++;
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
            b.actualizar();
            if (b.afectaA(this.repartidor.x, this.repartidor.y, this.repartidor.ancho, this.repartidor.alto)) {
                this.vidas = 0; 
            }
            if (b.terminada()) this.bombas[i] = null;
        }

        // Victoria / Derrota
        if (this.vidas <= 0|| this.pedidosPerdidos >= this.pedidosPerdidosParaPerder) {
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
        if (this.pedido.estado.equals("Esperando") || this.pedido.estado.equals("En camino")) {
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

    private void agregarBomba(Bomba b) {
        Bomba[] nuevo = new Bomba[this.bombas.length + 1];
        for (int i = 0; i < this.bombas.length; i++) nuevo[i] = this.bombas[i];
        nuevo[this.bombas.length] = b;
        this.bombas = nuevo;
    }

    private void dibujarPanel() {
        this.entorno.dibujarRectangulo(400, 30, 800, 60, 0, new Color(30, 30, 30));
        this.entorno.cambiarFont("Arial", 14, Color.WHITE);

        String tiempo = "Tiempo: " + (this.ticks / 60) + "s";
        String vidasStr = "Vidas: " + this.vidas;
        String entregados = "Entregados: " + this.pedidosEntregados;
        String eliminados = "Eliminados: " + this.enemigosEliminados;
        String estado = "Pedido: " + this.pedido.estado;

        this.entorno.escribirTexto(tiempo, 15, 38);
        this.entorno.escribirTexto(vidasStr, 120, 38);
        this.entorno.escribirTexto(entregados, 210, 38);
        this.entorno.escribirTexto(eliminados, 360, 38);
        this.entorno.escribirTexto(estado, 530, 38);
    }

    private void dibujarPantallaFinal(String mensaje) {
        this.entorno.dibujarRectangulo(400, 300, 800, 600, 0, new Color(0, 0, 0, 200));
        this.entorno.cambiarFont("Arial", 48, Color.WHITE);
        this.entorno.escribirTexto(mensaje, 300, 300);
    }

    @SuppressWarnings("unused")
    public static void main(String[] args) {
        Juego juego = new Juego();
    }
}