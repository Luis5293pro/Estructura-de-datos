import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Damas {

     // Muestra el tablero
    public static void recorrerTablero(String[][] tablero) {
        System.out.println("   A   B   C   D   E   F   G   H");
        for (int i = 0; i < tablero.length; i++) { 
            System.out.print(i + " "); // Muestra el número de fila
            for (int j = 0; j < tablero[i].length; j++) {   
                System.out.print("[" + tablero[i][j] + "] "); // Muestra el contenido de cada casilla
            }
            System.out.println(); 
        }
    }

     //Valida que las coordenadas proporcionadas estén dentro del rango permitido del tablero (0 a 7).
    private static boolean casillaValida(int Y, int X) {
        return Y >= 0 && Y <= 7 && X >= 0 && X <= 7;
    } 

    // convierte la ficha a reina si llega a la última fila opuesta
    private static void verificarCoronacion(String[][] tablero, int Y, int X) {
        if (tablero[Y][X].equals("B") && Y == 0) {
            tablero[Y][X] = "b"; // Transforma en Reina Blanca
            System.out.println("¡Tu ficha Blanca se ha convertido en REINA ('b')!");
        } else if (tablero[Y][X].equals("N") && Y == 7) {
            tablero[Y][X] = "n"; // Transforma en Reina Negra
            System.out.println("¡Tu ficha Negra se ha convertido en REINA ('n')!");
        }
    }

private static boolean puedeMoverReina(String[][] tablero, int Y, int X) {
    char turno = tablero[Y][X].toUpperCase().charAt(0);
    int[] direccionesY = {-1, 1};
    int[] direccionesX = {-1, 1};

    for (int dY : direccionesY) {
        for (int dX : direccionesX) {
            int ny = Y + dY, nx = X + dX;
            boolean rivalEncontrado = false;

            while (casillaValida(ny, nx)) {
                if (tablero[ny][nx].equals(" ")) {
                    // Movimiento libre o caída tras captura
                    return true;
                } else if (tablero[ny][nx].toUpperCase().charAt(0) != turno) {
                    if (rivalEncontrado) break; // dos rivales seguidos bloquean
                    rivalEncontrado = true;
                } else {
                    break; // ficha propia bloquea
                }
                ny += dY;
                nx += dX;
            }
        }
    }
    return false;
}
private static boolean puedeComerReina(String[][] tablero, int Y, int X) {
    char turno = tablero[Y][X].toUpperCase().charAt(0);
    int[] direccionesY = {-1, 1};
    int[] direccionesX = {-1, 1};

    for (int dY : direccionesY) {
        for (int dX : direccionesX) {
            int ny = Y + dY, nx = X + dX;
            boolean rivalEncontrado = false;

            while (casillaValida(ny, nx)) {
                if (tablero[ny][nx].equals(" ")) {
                    if (rivalEncontrado) return true; // hay captura
                } else if (tablero[ny][nx].toUpperCase().charAt(0) != turno) {
                    if (rivalEncontrado) break; // dos rivales seguidos bloquean
                    rivalEncontrado = true;
                } else {
                    break; // ficha propia bloquea
                }
                ny += dY;
                nx += dX;
            }
        }
    }
    return false;
}
     //Evalúa si una ficha específica en la posición (Y, X) tiene la posibilidad de comer una ficha del rival.
    private static boolean puedeComerFicha(String[][] tablero, int Y, int X, char turno) {
        int pasoY = (turno == 'B') ? -1 : 1; // Las fichas 'B' suben (-1), las 'N' bajan (+1)
        String rival = (turno == 'B') ? "N" : "B";
        int[] direccionesX = {-1, 1}; // Izquierda (-1) y Derecha (1)

        // Revisa ambas diagonales frontales
        for (int pasoX : direccionesX) {
            int dY = Y + pasoY, dX = X + pasoX;             // Posicion a donde ir
            int sY = Y + (pasoY * 2), sX = X + (pasoX * 2);     // Posicion si hay salto(si se come una ficha rival)
            String ficha = tablero[Y][X];
            if (ficha.equals("b") || ficha.equals("n")) {
                return puedeComerReina(tablero, Y, X);
            }
            // Es válido si el rival está al lado y la casilla siguiente está libre
            if (casillaValida(dY, dX) && tablero[dY][dX].equals(rival) &&
                casillaValida(sY, sX) && tablero[sY][sX].equals(" ")) {
                return true;
            }

        }
        return false;
    }

     //Recorre todo el tablero buscando si el jugador en turno tiene al menos una jugada de captura disponible.
    private static boolean hayCapturaDisponible(String[][] tablero, char turno) {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (tablero[i][j].equals(String.valueOf(turno)) || 
                    tablero[i][j].equals(turno == 'B' ? "b" : "n")) {
                    if (puedeComerFicha(tablero, i, j, turno)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private static boolean puedeMover(String[][] tablero, int Y, int X) {
    String ficha = tablero[Y][X];

    if (ficha.equals(" ")) return false;

    if (ficha.equals("b") || ficha.equals("n")) {
        return puedeMoverReina(tablero, Y, X) ||
               puedeComerReina(tablero, Y, X);
    }

    char turno = ficha.charAt(0);

    int pasoY = (turno == 'B') ? -1 : 1;
    int[] direccionesX = {-1, 1};

    for (int dX : direccionesX) {
        int ny = Y + pasoY;
        int nx = X + dX;

        if (casillaValida(ny, nx) && tablero[ny][nx].equals(" ")) {
            return true;
        }

        int sy = Y + 2 * pasoY;
        int sx = X + 2 * dX;

        if (casillaValida(ny, nx) &&
            tablero[ny][nx].toUpperCase().charAt(0) != turno &&
            casillaValida(sy, sx) &&
            tablero[sy][sx].equals(" ")) {
            return true;
        }
    }

    return false;
}
private static boolean moverReina(
        String[][] tablero,
        int Y, int X,
        int destinoY, int destinoX) {

    int pasoY = Integer.compare(destinoY, Y);
    int pasoX = Integer.compare(destinoX, X);

    int y = Y + pasoY;
    int x = X + pasoX;

    boolean rivalEncontrado = false;
    int rivalY = -1;
    int rivalX = -1;

    char turno = tablero[Y][X].toUpperCase().charAt(0);

    while (y != destinoY || x != destinoX) {

        if (!casillaValida(y, x)) {
            return false;
        }

        if (!tablero[y][x].equals(" ")) {

            // Si encontramos una segunda pieza, no se puede pasar
            if (rivalEncontrado) {
                return false;
            }

            // Comprobar si es rival
            if (tablero[y][x].toUpperCase().charAt(0) != turno) {
                rivalEncontrado = true;
                rivalY = y;
                rivalX = x;
            } else {
                // Es una pieza propia
                return false;
            }
        }

        y += pasoY;
        x += pasoX;
    }

    // El destino debe estar vacío
    if (!tablero[destinoY][destinoX].equals(" ")) {
        return false;
    }

    // Si hay rival, es una captura
    if (rivalEncontrado) {
        tablero[rivalY][rivalX] = " ";
    }

    tablero[destinoY][destinoX] = tablero[Y][X];
    tablero[Y][X] = " ";

    return true;
}
// Verifica si un jugador tiene fichas o movimientos
private static boolean jugadorBloqueado(String[][] tablero, char turno) {
    for (int i = 0; i < 8; i++) {
        for (int j = 0; j < 8; j++) {
            if (tablero[i][j].equals(String.valueOf(turno)) ||
                tablero[i][j].equals(turno == 'B' ? "b" : "n")) {
                if (puedeMover(tablero, i, j)) return false;
            }
        }
    }
    return true;
}
    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        boolean juegoActivo = true;
        char turno = 'B', rival = 'N';
        String direccion;
        int X, Y, pasoX = 0, pasoY = 0;

        // Variables de control para encadenar capturas múltiples con la misma ficha
        boolean capturaEnCadena = false;
        int fichaObligadaY = -1, fichaObligadaX = -1;

        // Matriz del tablero de 8x8 con la posición inicial de las piezas
        String[][] tablero = {
            {"N", " ", "N", " ", "N", " ", "N", " "},
            {" ", "N", " ", "N", " ", "N", " ", "N"},
            {"N", " ", "N", " ", "N", " ", "N", " "},
            {" ", " ", " ", " ", " ", " ", " ", " "},
            {" ", " ", " ", " ", " ", " ", " ", " "},
            {" ", "B", " ", "B", " ", "B", " ", "B"},
            {"B", " ", "B", " ", "B", " ", "B", " "},
            {" ", "B", " ", "B", " ", "B", " ", "B"}
        };

        // Bucle principal del juego
        while (juegoActivo) {

            // Limpieza de pantalla mediante código ANSI
            System.out.print("\033[H\033[2J");
            System.out.flush();
            recorrerTablero(tablero);

            // Revisa si existe la regla de captura obligatoria activa en este turno
            boolean obligatorioComer = hayCapturaDisponible(tablero, turno);
            
            System.out.println("\nTurno del jugador " + turno);
            if (capturaEnCadena) {
                System.out.println("¡CAPTURA MULTIPLE!");
            } else if (obligatorioComer) {
                System.out.println("Es OBLIGATORIO comer.");
            }

            try {
                // Lectura de coordenadas
                System.out.print("Coordenada Y (0-7): ");
                Y = Integer.parseInt(reader.readLine().trim());
                System.out.print("Coordenada X (A-H): ");
                X = Character.toUpperCase(reader.readLine().trim().charAt(0)) - 'A';

                // Si está en medio de una ráfaga de capturas, no puede seleccionar otra ficha
                if (capturaEnCadena && (Y != fichaObligadaY || X != fichaObligadaX)) {
                    System.out.println("Debes mover la misma ficha con la que comiste anteriormente");
                    reader.readLine();
                    continue;
                }

                // Verifica que las coordenadas estén dentro del tablero
                if (!casillaValida(Y, X)) {
                    System.out.println("Coordenada inválida");
                    reader.readLine();
                    continue;
                }

                // Verifica si la ficha seleccionada es del jugador de turno
                String ficha = tablero[Y][X];

                if (!(ficha.equals(String.valueOf(turno)) ||
                      ficha.equals(turno == 'B' ? "b" : "n"))) {
                    System.out.println("Ficha inválida");
                    reader.readLine();
                    continue;
                }

                boolean esReina = ficha.equals("b") || ficha.equals("n");
                rival = (turno == 'B') ? 'N' : 'B';

                // Si hay captura obligatoria, la ficha seleccionada debe poder comer
                if (!capturaEnCadena && obligatorioComer &&
                    !puedeComerFicha(tablero, Y, X, turno)) {
                    System.out.println("Debes seleccionar una ficha que pueda comer");
                    reader.readLine();
                    continue;
                }

                // MOVIMIENTO DE REINA
                if (esReina) {

                    System.out.print("Dirección horizontal (q = Izquierda / e = Derecha): ");
                    direccion = reader.readLine().trim().toLowerCase();

                    pasoX = direccion.equals("q") ? -1 :
                            direccion.equals("e") ? 1 : 0;

                    if (pasoX == 0) {
                        System.out.println("Dirección inválida");
                        reader.readLine();
                        continue;
                    }

                    System.out.print("Dirección vertical (w = arriba / s = abajo): ");
                    String vertical = reader.readLine().trim().toLowerCase();

                    int pasoReinaY;

                    if (vertical.equals("w")) {
                        pasoReinaY = -1;
                    } else if (vertical.equals("s")) {
                        pasoReinaY = 1;
                    } else {
                        System.out.println("Dirección vertical inválida");
                        reader.readLine();
                        continue;
                    }

                    System.out.print("Número de casillas: ");
                    int distancia = Integer.parseInt(reader.readLine().trim());

                    if (distancia <= 0) {
                        System.out.println("La distancia debe ser mayor que 0");
                        reader.readLine();
                        continue;
                    }

                    int DestinoY = Y + pasoReinaY * distancia;
                    int DestinoX = X + pasoX * distancia;

                    if (!casillaValida(DestinoY, DestinoX)) {
                        System.out.println("Movimiento fuera del tablero");
                        reader.readLine();
                        continue;
                    }

                    // Comprobar si existe exactamente una ficha rival
                    // en el camino hacia el destino.
                    boolean capturaReina = false;
                    int rivalY = -1;
                    int rivalX = -1;

                    int yTemp = Y + pasoReinaY;
                    int xTemp = X + pasoX;

                    while (yTemp != DestinoY || xTemp != DestinoX) {

                        if (!tablero[yTemp][xTemp].equals(" ")) {

                            if (tablero[yTemp][xTemp].toUpperCase().charAt(0) == rival) {
                                if (capturaReina) {
                                    // Hay dos rivales en el camino.
                                    capturaReina = false;
                                    rivalY = -1;
                                    rivalX = -1;
                                    break;
                                }

                                capturaReina = true;
                                rivalY = yTemp;
                                rivalX = xTemp;

                            } else {
                                // Una ficha propia bloquea el camino.
                                capturaReina = false;
                                rivalY = -1;
                                rivalX = -1;
                            }

                            // Una pieza encontrada bloquea el resto del camino.
                            int siguienteY = yTemp + pasoReinaY;
                            int siguienteX = xTemp + pasoX;

                            while (siguienteY != DestinoY || siguienteX != DestinoX) {
                                if (!tablero[siguienteY][siguienteX].equals(" ")) {
                                    capturaReina = false;
                                    rivalY = -1;
                                    rivalX = -1;
                                }
                                siguienteY += pasoReinaY;
                                siguienteX += pasoX;
                            }

                            break;
                        }

                        yTemp += pasoReinaY;
                        xTemp += pasoX;
                    }

                    // Si el destino no está vacío, moverReina() lo rechazará.
                    // Si existe captura obligatoria, la reina tiene que capturar.
                    if ((obligatorioComer || capturaEnCadena) && !capturaReina) {
                        System.out.println("Movimiento inválido. Estás obligado a comer");
                        reader.readLine();
                        continue;
                    }

                    if (moverReina(tablero, Y, X, DestinoY, DestinoX)) {

                        if (capturaReina) {
                            System.out.println("La reina ha capturado una ficha");
                        }

                        // La misma reina continúa si todavía puede capturar.
                        if (capturaReina &&
                            puedeComerReina(tablero, DestinoY, DestinoX)) {

                            capturaEnCadena = true;
                            fichaObligadaY = DestinoY;
                            fichaObligadaX = DestinoX;

                        } else {

                            capturaEnCadena = false;
                            fichaObligadaY = -1;
                            fichaObligadaX = -1;
                            turno = rival;
                        }

                    } else {
                        System.out.println("Movimiento de reina inválido");
                        reader.readLine();
                    }

                    // No ejecutar la lógica de ficha normal.
                    continue;
                }

                // =====================================================
                // MOVIMIENTO DE FICHA NORMAL
                // =====================================================

                System.out.print("Dirección (q = Izquierda / e = Derecha): ");
                direccion = reader.readLine().trim().toLowerCase();

                pasoY = (turno == 'B') ? -1 : 1;
                pasoX = direccion.equals("q") ? -1 :
                        direccion.equals("e") ? 1 : 0;

                if (pasoX == 0) {
                    System.out.println("Dirección inválida");
                    reader.readLine();
                    continue;
                }

                int DestinoX = X + pasoX;
                int DestinoY = Y + pasoY;

                int SaltoX = X + (pasoX * 2);
                int SaltoY = Y + (pasoY * 2);

                // movimiento sin comer, de una casilla
                if (casillaValida(DestinoY, DestinoX) && tablero[DestinoY][DestinoX].equals(" ")) {
                    if (obligatorioComer || capturaEnCadena) {
                        System.out.println("Movimiento inválido. Estás obligado a comer");
                        reader.readLine();
                    } else {
                        // Realizar movimiento simple
                        tablero[DestinoY][DestinoX] = String.valueOf(turno);
                        tablero[Y][X] = " ";
                        verificarCoronacion(tablero,DestinoY,DestinoX);
                        turno = rival; // Cambiar turno
                        
                    }
                } 
                // Comer pieza
                else if (casillaValida(DestinoY, DestinoX) && tablero[DestinoY][DestinoX].equals(String.valueOf(rival)) &&
                         casillaValida(SaltoY, SaltoX) && tablero[SaltoY][SaltoX].equals(" ")) {
                    
                    // Actualizar tablero (mover ficha actual y retirar ficha rival comida)
                    tablero[SaltoY][SaltoX] = String.valueOf(turno);
                    tablero[DestinoY][DestinoX] = " ";
                    tablero[Y][X] = " ";
                    verificarCoronacion(tablero,SaltoY,SaltoX);
                    
                    // Comprobar si la ficha puede encadenar otro salto desde su nueva ubicación
                    if (puedeComerFicha(tablero, SaltoY, SaltoX, turno)) {
                        capturaEnCadena = true;
                        fichaObligadaY = SaltoY;
                        fichaObligadaX = SaltoX;
                    } else {
                        capturaEnCadena = false; // Finaliza la captura en cadena
                        turno = rival;           // Cambiar turno
                    }
                } 
                else {
                    System.out.println("Movimiento no permitido");
                    reader.readLine();
                }

                if (jugadorBloqueado(tablero, rival)) {
            System.out.println("¡El jugador " + rival + " no tiene movimientos!");
            System.out.println(" Gana el jugador " + rival);
            juegoActivo = false;
            }

            } catch (Exception e) {
                System.out.println("Entrada invalida");
                reader.readLine();
            }
        }
    }
}