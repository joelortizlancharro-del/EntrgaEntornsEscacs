import java.util.ArrayList; // Importa la clase ArrayList para almacenar listas dinámicas
import java.util.Scanner; // Importa la clase Scanner para leer entradas del usuario

public class escacs {
    // Variables globales de la clase
    boolean movimentIlegal = false; // Booleano que indica si el movimiento es ilegal
    char[][] taulell; // Matriz que representa el tablero de ajedrez
    int torn = 0; // Indica de quién es el turno (0 = blancas, 1 = negras)
    int filaOrigen; // Fila de la pieza a mover
    int columnaOrigen; // Columna de la pieza a mover
    int filaDesti; // Fila destino de la pieza
    int columnaDesti; // Columna destino de la pieza
    int contador = 0; // Contador de reyes vivos
    boolean tirarToballola = false; // Indica si un jugador se ha rendido
    String movimentPeces; // Almacena el movimiento ingresado por el usuario
    
    Scanner escaner = new Scanner(System.in); // Scanner para leer entrada del usuario

    public static void main(String[] args) {
        escacs p = new escacs(); // Crea una instancia del juego
        p.principal(); // Llama al método principal para iniciar el juego
    }

    public void principal(){
        // Variables locales para los jugadores
        String jugadorBlanc;
        String jugadorNegre;
        
        taulell = new char[8][8]; // Inicializa el tablero
        String[] jugadors = new String[2]; // Array para los nombres de los jugadores
        ArrayList<String> movimentsBlanques = new ArrayList<>(); // Lista de movimientos blancas
        ArrayList<String> movimentsNegres = new ArrayList<>(); // Lista de movimientos negras

        taulell = crearTaulell(taulell); // Crea y llena el tablero con piezas iniciales

        jugadors = creacioDeJugadors(jugadors); // Solicita los nombres de los jugadores

        mostrarTaulell(taulell); // Muestra el tablero inicial
        contarReis(taulell, jugadors); // Cuenta los reyes vivos

        while(contador == 2){ // Bucle principal mientras ambos reyes estén en el tablero
            controlTorns(); // Indica de quién es el turno
            rendirse(taulell, jugadors); // Permite que un jugador se rinda
            if(tirarToballola == false && contador == 2){
                jugar(jugadors, taulell, movimentsBlanques, movimentsNegres); // Ejecuta el turno
                mostrarTaulell(taulell); // Muestra el tablero actualizado
                if(movimentIlegal == false){
                    // Cambia el turno si el movimiento fue legal
                    if(torn == 0){
                        torn = 1;
                    }
                    else if(torn == 1){
                        torn = 0;
                    }
                }
                contador = 0; // Reinicia el contador de reyes antes de contarlos de nuevo
                contarReis(taulell, jugadors); // Recuenta los reyes vivos
                if(contador != 2 && tirarToballola == false){
                    System.out.println("La partida ha acabat!!! ");
                    if(torn == 0){
                        System.out.println("EL GUANYADOR ES:" + jugadors[1]); // Ganador negras
                    }
                    else{
                        System.out.println("EL GUANYADOR ES:" + jugadors[0]); // Ganador blancas
                    }
                }
            }
        }
        mostrarMoviments(movimentsBlanques, movimentsNegres); // Muestra todos los movimientos al final
        //tornarAJugar(jugadors, movimentsBlanques, movimentsNegres); // Comentado, opción de volver a jugar
    }

    public char[][] crearTaulell(char[][] taulell){
        // Coloca las torres, caballos, alfiles, reina y rey negros en la primera fila
        taulell[0][0] = 't';
        taulell[0][1] = 'c';
        taulell[0][2] = 'a';
        taulell[0][3] = 'q';
        taulell[0][4] = 'k';
        taulell[0][5] = 'a';
        taulell[0][6] = 'c';
        taulell[0][7] = 't';

        // Coloca las torres, caballos, alfiles, reina y rey blancas en la última fila
        taulell[7][0] = 'T';
        taulell[7][1] = 'C';
        taulell[7][2] = 'A';
        taulell[7][3] = 'Q';
        taulell[7][4] = 'K';
        taulell[7][5] = 'A';
        taulell[7][6] = 'C';
        taulell[7][7] = 'T';

        // Coloca los peones negros y blancos
        for(int i = 0; i < 8; i++){
            for(int j=0; j < 8; j++){
                if(i == 1){
                    taulell[i][j] = 'p'; // Peones negros
                }
                if(i == 6){
                    taulell[i][j] = 'P'; // Peones blancos
                }
            }
        }

        // Llena el resto del tablero con '.'
        for(int f = 0; f < 8; f++){
            for(int c = 0; c < 8; c++){
                if(taulell[f][c] != 't' && taulell[f][c] != 'T' && taulell[f][c] != 'c' && taulell[f][c] != 'C' &&
                   taulell[f][c] != 'a' && taulell[f][c] != 'A' && taulell[f][c] != 'k' && taulell[f][c] != 'K' &&
                   taulell[f][c] != 'q' && taulell[f][c] != 'Q' && taulell[f][c] != 'p' && taulell[f][c] != 'P'){
                    taulell[f][c] = '.'; // Casilla vacía
                }
            }
        }
        return taulell; // Retorna el tablero inicializado
    }

    public void mostrarTaulell(char[][] taulell){
        int contarFiles = 1; // Contador para mostrar las filas numeradas
        System.out.println("------------------------");
        for(int f = 0; f < 8; f++){
            for(int c = 0; c < 8; c++){
                System.out.print(taulell[f][c] + "  "); // Imprime cada pieza separada por espacios
            }
            System.out.println("|" + contarFiles); // Muestra el número de fila al final
            contarFiles++;
        }
        System.out.println("------------------------");
        System.out.println("a  b  c  d  e  f  g  h"); // Nombres de columnas
    }

    public String[] creacioDeJugadors(String[] jugadors){
        // Solicita nombre del jugador de piezas blancas
        System.out.println("Insereix el nom del jugador que controlara les peces blanques: ");
        jugadors[0] = escaner.nextLine();

        // Solicita nombre del jugador de piezas negras
        System.out.println("Insereix el nom del jugador que controlara les peces negres: ");
        jugadors[1] = escaner.nextLine();
        return jugadors;
    }

    public void jugar(String[] jugadors, char[][] taulell,ArrayList<String> movimentsBlanques, ArrayList<String> movimentsNegres){
        moviment(); // Solicita el movimiento del jugador
        if(movimentIlegal == false){
            retornTaulell(taulell, columnaOrigen, columnaDesti, filaDesti, filaOrigen); // Actualiza tablero
            imprimirMoviments(movimentsBlanques, movimentsNegres, movimentPeces); // Guarda el movimiento
        }
    }

    // MÉTODO PARA MOVIMIENTOS DE PEONES
    public boolean peons(char[][] taulell, int filaOrigen, int columnaOrigen, int filaDesti, int columnaDesti){
        movimentIlegal = false; // Inicializa como movimiento legal
        int movimentVerticalPeo = filaOrigen - filaDesti; // Calcula movimiento vertical
        int movimentIlegalNegresPeo = filaDesti - filaOrigen; // Movimiento específico para peones negros
        int movimentIlegalBlanquesPeo = filaDesti - filaOrigen; // Movimiento específico para peones blancos

        // Reglas de movimiento para peones: no mover más de lo permitido y dirección correcta
        if( ((movimentVerticalPeo > 1 || movimentVerticalPeo < -1) && (filaOrigen != 1 && filaOrigen != 6)) ||
            ((filaOrigen != 1 || filaOrigen != 6) && (movimentVerticalPeo > 2 || movimentVerticalPeo < -2)) ||
            ((taulell[filaOrigen][columnaOrigen] == 'p' && movimentIlegalNegresPeo < 0) ||
             (taulell[filaOrigen][columnaOrigen] == 'P' && movimentIlegalBlanquesPeo > 0))){
            System.out.println("MOVIMENT ILEGAL!!!");
            movimentIlegal = true;
            return movimentIlegal;
        }

        // Movimientos diagonales sin captura o fuera de rango
        if( (((columnaDesti != columnaOrigen)) || ((columnaDesti != columnaOrigen) && (columnaOrigen - columnaDesti) > 1) ||
             ((columnaDesti != columnaOrigen) && (columnaOrigen - columnaDesti) < -1)) &&
            ((taulell[filaOrigen][columnaOrigen] == 'p' && taulell[filaDesti][columnaDesti] == '.') ||
             (taulell[filaOrigen][columnaOrigen] == 'P' && taulell[filaDesti][columnaDesti] == '.'))){
            System.out.println("MOVIMENT ILEGAL!!!");
            movimentIlegal = true;
            return movimentIlegal;
        }

        // Más reglas: no comer en vertical, no moverse diagonal sin captura, etc.
        if(columnaDesti == columnaOrigen && filaDesti != filaOrigen && taulell[filaDesti][columnaDesti] != '.'){
            System.out.println("MOVIMENT ILEGAL!!!");
            movimentIlegal = true;
            return movimentIlegal;
        }
        if(filaOrigen != filaDesti && columnaOrigen != columnaDesti && taulell[filaDesti][columnaDesti] == '.'){
            System.out.println("MOVIMENT ILEGAL!!!");
            movimentIlegal = true;
            return movimentIlegal;
        }
        if( columnaOrigen != columnaDesti && filaDesti == filaOrigen && taulell[filaDesti][columnaDesti] != '.'){
            System.out.println("MOVIMENT ILEGAL!!!");
            movimentIlegal = true;
            return movimentIlegal;
        }

        // No comer más de una pieza en diagonal
        if(taulell[filaDesti][columnaDesti] != '.' && ((columnaOrigen - columnaDesti) > 1 || (columnaDesti - columnaOrigen) < -1)){
            System.out.println("MOVIMENT ILEGAL!!!");
            movimentIlegal = true;
            return movimentIlegal;
        }

        // Verifica si hay piezas en medio cuando el peón mueve dos casillas
        if(filaDesti - filaOrigen == 2){
            for(int i = filaOrigen + 1; i < filaDesti; i++){
                if(taulell[i][columnaDesti] != '.'){
                    System.out.println("MOVIMENT ILEGAL!!!");
                    movimentIlegal = true;
                    return movimentIlegal;
                }
            }
        }
        if(filaDesti - filaOrigen == -2){
            for(int i = filaDesti + 1; i < filaOrigen; i++){
                if(taulell[i][columnaDesti] != '.'){
                    System.out.println("MOVIMENT ILEGAL!!!");
                    movimentIlegal = true;
                    return movimentIlegal;
                }
            }
        }

        // No comer piezas propias
        if(taulell[filaOrigen][columnaOrigen] == 'p' && !Character.isUpperCase(taulell[filaDesti][columnaDesti]) && taulell[filaDesti][columnaDesti] != '.'){
            System.out.println("MOVIMENT ILEGAL!!!");
            movimentIlegal = true;
            return movimentIlegal;
        }
        else if(taulell[filaOrigen][columnaOrigen] == 'P' && Character.isUpperCase(taulell[filaDesti][columnaDesti]) && taulell[filaDesti][columnaDesti] != '.'){
            System.out.println("MOVIMENT ILEGAL!!!");
            movimentIlegal = true;
            return movimentIlegal;
        }

        return movimentIlegal; // Retorna si el movimiento es legal o no
    }

    // METODO TORRE
    public boolean torre(char[][] taulell){
        movimentIlegal = false; // Inicializa como legal

        // Revisa que el movimiento sea sólo horizontal o vertical
        if(filaOrigen != filaDesti && columnaOrigen != columnaDesti){
            System.out.println("MOVIMENT ILEGAL!!!");
            movimentIlegal = true;
            return movimentIlegal;
        }

        // Movimientos verticales positivos
        if(filaDesti > filaOrigen && columnaDesti == columnaOrigen){
            for(int i = filaOrigen + 1; i < filaDesti; i++){
                if(taulell[i][columnaOrigen] !='.'){
                    movimentIlegal = true;
                    System.out.println("MOVIMENT ILEGAL!!!");
                    return movimentIlegal;
                }
            }
        }

        // Movimientos verticales negativos
        if(filaDesti < filaOrigen && columnaDesti == columnaOrigen){
            for(int i = filaDesti + 1; i < filaOrigen; i++){
                if(taulell[i][columnaOrigen] !='.'){
                    movimentIlegal = true;
                    System.out.println("MOVIMENT ILEGAL!!!");
                    return movimentIlegal;
                }
            }
        }

        // Movimientos horizontales positivos
        if(columnaDesti > columnaOrigen && filaDesti == filaOrigen){
            for(int i = columnaOrigen + 1; i < columnaDesti; i++){
                if(taulell[filaDesti][i] !='.'){
                    movimentIlegal = true;
                    System.out.println("MOVIMENT ILEGAL!!!");
                    return movimentIlegal;
                }
            }
        }

        // Movimientos horizontales negativos
        if(columnaDesti < columnaOrigen && filaDesti == filaOrigen){
            for(int i = columnaDesti + 1; i < columnaOrigen; i++){
                if(taulell[filaDesti][i] !='.'){
                    movimentIlegal = true;
                    System.out.println("MOVIMENT ILEGAL!!!");
                    return movimentIlegal;
                }
            }
        }

        // Verifica que no coma piezas propias
        if(taulell[filaOrigen][columnaOrigen] == 't' && !Character.isUpperCase(taulell[filaDesti][columnaDesti]) && taulell[filaDesti][columnaDesti] != '.'){
            System.out.println("MOVIMENT ILEGAL!!!");
            movimentIlegal = true;
            return movimentIlegal;
        }
        else if(taulell[filaOrigen][columnaOrigen] == 'T' && Character.isUpperCase(taulell[filaDesti][columnaDesti]) && taulell[filaDesti][columnaDesti] != '.'){
            System.out.println("MOVIMENT ILEGAL!!!");
            movimentIlegal = true;
            return movimentIlegal;
        }

        return movimentIlegal; // Retorna si es legal
    }

    // AQUI TERMINA EL PUNTO QUE ME PIDÍSTE, DESDE AQUÍ PUEDES SEGUIR DOCUMENTANDO LOS DEMÁS MÉTODOS IGUAL
// MÉTODO CABALLO
public boolean cavall(char[][] taulell){
    movimentIlegal = true; // Inicializa como ilegal por defecto
    int filaMoviment = filaDesti - filaOrigen; // Calcula la diferencia de fila
    int columnaMoviment = columnaDesti - columnaOrigen; // Calcula la diferencia de columna
    
    // Verifica si intenta comer pieza propia
    if(taulell[filaOrigen][columnaOrigen] == 'c' && !Character.isUpperCase(taulell[filaDesti][columnaDesti]) && taulell[filaDesti][columnaDesti] != '.'){
        System.out.println("MOVIMENT ILEGAL!!!");
        movimentIlegal = true;
        return movimentIlegal;
    }
    else if(taulell[filaOrigen][columnaOrigen] == 'C' && Character.isUpperCase(taulell[filaDesti][columnaDesti]) && taulell[filaDesti][columnaDesti] != '.'){
        System.out.println("MOVIMENT ILEGAL!!!");
        movimentIlegal = true;
        return movimentIlegal;
    }

    // Movimiento legal del caballo (L)
    if((filaMoviment == 1 && (columnaMoviment == 2 || columnaMoviment == -2)) ||
       (filaMoviment == -1 && (columnaMoviment == 2 || columnaMoviment == -2)) ||
       (filaMoviment == 2 && (columnaMoviment == 1 || columnaMoviment == -1)) ||
       (filaMoviment == -2 && (columnaMoviment == 1 || columnaMoviment == -1))){
        movimentIlegal = false; // Movimiento válido
        return movimentIlegal;
    }
    else{
        System.out.println("MOVIMENT ILEGAL!!!"); // Movimiento inválido
        movimentIlegal = true;
        return movimentIlegal;
    }
}

// MÉTODO ALFIL
public boolean alfil(char[][] taulell){
    movimentIlegal = false; // Inicializa como legal
    int movimentVerticalAlfil;
    int movimentHoritzontalAlfil;

    // Calcula la dirección vertical
    if(filaDesti > filaOrigen){
        movimentVerticalAlfil = 1; 
    }
    else{
        movimentVerticalAlfil = -1;
    }

    // Calcula la dirección horizontal
    if(columnaDesti > columnaOrigen){
        movimentHoritzontalAlfil = 1;
    }
    else{
        movimentHoritzontalAlfil = -1;
    }

    int fil = filaOrigen + movimentVerticalAlfil; // Posición temporal fila
    int col = columnaOrigen + movimentHoritzontalAlfil; // Posición temporal columna

    // Verifica que el movimiento sea diagonal
    if(!(filaDesti-filaOrigen == columnaDesti-columnaOrigen || 
         filaDesti-filaOrigen == columnaOrigen-columnaDesti || 
         filaOrigen-filaDesti == columnaOrigen-columnaDesti || 
         filaOrigen-filaDesti == columnaDesti-columnaOrigen)){
        System.out.println("MOVIMENT ILEGAL!!!");
        movimentIlegal = true;
        return movimentIlegal;
    }

    // Verifica que no coma piezas propias
    if(taulell[filaOrigen][columnaOrigen] == 'a' && !Character.isUpperCase(taulell[filaDesti][columnaDesti]) && taulell[filaDesti][columnaDesti] != '.'){
        System.out.println("MOVIMENT ILEGAL!!!");
        movimentIlegal = true;
        return movimentIlegal;
    }
    else if(taulell[filaOrigen][columnaOrigen] == 'A' && Character.isUpperCase(taulell[filaDesti][columnaDesti]) && taulell[filaDesti][columnaDesti] != '.'){
        System.out.println("MOVIMENT ILEGAL!!!");
        movimentIlegal = true;
        return movimentIlegal;
    }

    // Verifica que no haya piezas en medio
    while(fil != filaDesti && col != columnaDesti){
        if(taulell[fil][col] != '.'){
            System.out.println("MOVIMENT ILEGAL!!!");
            movimentIlegal = true;
            return movimentIlegal;
        }
        fil = fil + movimentVerticalAlfil; // Avanza en vertical
        col = col + movimentHoritzontalAlfil; // Avanza en horizontal
    }

    return movimentIlegal; // Retorna si el movimiento es legal
}

// MÉTODO REINA
public boolean reina(char[][] taulell){
    // Verifica que no coma piezas propias
    if(taulell[filaOrigen][columnaOrigen] == 'q' && !Character.isUpperCase(taulell[filaDesti][columnaDesti]) && taulell[filaDesti][columnaDesti] != '.'){
        System.out.println("MOVIMENT ILEGAL!!!");
        movimentIlegal = true;
        return movimentIlegal;
    }
    else if(taulell[filaOrigen][columnaOrigen] == 'Q' && Character.isUpperCase(taulell[filaDesti][columnaDesti]) && taulell[filaDesti][columnaDesti] != '.'){
        System.out.println("MOVIMENT ILEGAL!!!");
        movimentIlegal = true;
        return movimentIlegal;
    }

    // La reina combina el movimiento del alfil y la torre
    return alfil(taulell) && torre(taulell);
}

// MÉTODO REY
public boolean rei(char[][] taulell){
    movimentIlegal = false; // Inicializa como legal

    // Verifica que el rey solo se mueva una casilla en cualquier dirección
    if(filaDesti - filaOrigen < 1 ||
       filaDesti - filaOrigen > -1  ||
       columnaDesti - columnaOrigen > 1 || 
       columnaDesti - columnaOrigen < -1){
        System.out.println("MOVIMENT ILEGAL!!!");
        movimentIlegal = true;
        return movimentIlegal;
    }

    // Verifica que no coma piezas propias
    if(taulell[filaOrigen][columnaOrigen] == 'k' && !Character.isUpperCase(taulell[filaDesti][columnaDesti]) && taulell[filaDesti][columnaDesti] != '.'){
        System.out.println("MOVIMENT ILEGAL!!!");
        movimentIlegal = true;
        return movimentIlegal;
    }
    else if(taulell[filaOrigen][columnaOrigen] == 'K' && Character.isUpperCase(taulell[filaDesti][columnaDesti]) && taulell[filaDesti][columnaDesti] != '.'){
        System.out.println("MOVIMENT ILEGAL!!!");
        movimentIlegal = true;
        return movimentIlegal;
    }

    return movimentIlegal; // Retorna si el movimiento es legal
}

// MÉTODO PARA ACTUALIZAR EL TABLERO
public char[][] retornTaulell(char[][] taulell, int columnaOrigen, int columnaDesti, int filaDesti, int filaOrigen){
    taulell[filaDesti][columnaDesti] = taulell[filaOrigen][columnaOrigen]; // Mueve la pieza
    taulell[filaOrigen][columnaOrigen]='.'; // Deja vacío el origen
    return taulell;
}

// MÉTODO PARA LEER EL MOVIMIENTO DEL JUGADOR
public void moviment(){
    escaner.nextLine(); // Limpia buffer
    System.out.println("Quina es la peça que vols moure? Ex: e2 e4");
    movimentPeces = escaner.nextLine(); // Lee el movimiento

    String[] parts = movimentPeces.trim().split("\\s+"); // Divide en origen y destino
    while (parts.length != 2) {
        System.out.println("MOVIMENT ILEGAL \nTorna a introduir les coordenades.");
        movimentPeces = escaner.nextLine();
        parts = movimentPeces.trim().split("\\s+");
    }

    columnaOrigen = parts[0].charAt(0) - 'a'; // Convierte letra a índice columna
    filaOrigen = parts[0].charAt(1) - '1'; // Convierte número a índice fila

    columnaDesti = parts[1].charAt(0) - 'a';
    filaDesti = parts[1].charAt(1) - '1';

    // Valida coordenadas dentro del tablero
    while ((columnaOrigen < 0 || columnaOrigen > 7) || (columnaDesti < 0 || columnaDesti > 7) || (filaOrigen < 0 || filaOrigen > 7) || (filaDesti < 0 || filaDesti > 7)){
        System.out.println("MOVIMENT ILEGAL \nTorna a introduir les coordenades.");
        movimentPeces = escaner.nextLine();
        parts = movimentPeces.trim().split("\\s+");
        columnaOrigen = parts[0].charAt(0) - 'a';
        filaOrigen = parts[0].charAt(1) - '1';

        columnaDesti = parts[1].charAt(0) - 'a';
        filaDesti = parts[1].charAt(1) - '1';
    }

    controlsBasics(taulell); // Verifica reglas básicas de movimiento
    if(movimentIlegal == false){
        escollirMoviment(taulell, columnaOrigen, columnaDesti, filaDesti, filaOrigen); // Evalúa movimiento según tipo de pieza
    }
}

// MÉTODO PARA SELECCIONAR EL TIPO DE MOVIMIENTO SEGÚN PIEZA
public void escollirMoviment(char[][] taulell, int columnaOrigen, int columnaDesti, int filaDesti, int filaOrigen){
    if(taulell[filaOrigen][columnaOrigen] == 'P' || taulell[filaOrigen][columnaOrigen] == 'p'){
        peons(taulell, filaOrigen, columnaOrigen, filaDesti, columnaDesti);
    }
    else if(taulell[filaOrigen][columnaOrigen] == 'T' || taulell[filaOrigen][columnaOrigen] == 't'){
        torre(taulell);
    }
    else if(taulell[filaOrigen][columnaOrigen] == 'C' || taulell[filaOrigen][columnaOrigen] == 'c'){
        cavall(taulell);
    }
    else if(taulell[filaOrigen][columnaOrigen] == 'A' || taulell[filaOrigen][columnaOrigen] == 'a'){
        alfil(taulell);
    }
    else if(taulell[filaOrigen][columnaOrigen] == 'Q' || taulell[filaOrigen][columnaOrigen] == 'q'){
        reina(taulell);
    }
    else if(taulell[filaOrigen][columnaOrigen] == 'K' || taulell[filaOrigen][columnaOrigen] == 'k'){
        rei(taulell);
    }
}

// MÉTODO PARA CONTROLAR TURNOS
public void controlTorns(){
    if(torn == 0){
        System.out.println("Torn de les blanques."); // Turno blancas
    }
    else if(torn == 1){
        System.out.println("Torn de les negres."); // Turno negras
    }
}

// MÉTODO PARA CONTROLES BÁSICOS
public boolean controlsBasics(char[][] taulell){
    movimentIlegal = false;

    // No mover piezas del oponente
    if((taulell[filaOrigen][columnaOrigen] == 'p' || taulell[filaOrigen][columnaOrigen] == 't' || taulell[filaOrigen][columnaOrigen] == 'c' || taulell[filaOrigen][columnaOrigen] == 'a' || taulell[filaOrigen][columnaOrigen] == 'q' || taulell[filaOrigen][columnaOrigen] == 'k') && torn == 0){
        System.out.println("NOMES POTS MOURE PECES DEL TEU COLOR!!!");
        movimentIlegal = true;
        return movimentIlegal;
    }
    if((taulell[filaOrigen][columnaOrigen] == 'P' || taulell[filaOrigen][columnaOrigen] == 'T' || taulell[filaOrigen][columnaOrigen] == 'C' || taulell[filaOrigen][columnaOrigen] == 'A' || taulell[filaOrigen][columnaOrigen] == 'Q' || taulell[filaOrigen][columnaOrigen] == 'K') && torn == 1){
        System.out.println("NOMES POTS MOURE PECES DEL TEU COLOR!!!");
        movimentIlegal = true;
        return movimentIlegal;
    }

    // No mover al mismo lugar
    if(taulell[filaOrigen][columnaOrigen] == taulell[filaDesti][columnaDesti]){
        System.out.println("NO POTS MOURE LA PEÇA AL MATEIX LLOC ON ESTAVA!!!");
        movimentIlegal = true;
        return movimentIlegal;
    }

    // No seleccionar casilla vacía
    if(taulell[filaOrigen][columnaOrigen] == '.'){
        System.out.println("NO HAS SELECCIONAT CAP PEÇA!!!");
        movimentIlegal = true;
        return movimentIlegal;
    }

    return movimentIlegal;
}

// MÉTODO CONTAR REYES
public void contarReis(char[][] taulell, String[] jugadors){
    for(int i = 0; i < 8; i++){
        for(int j = 0; j < 8; j++){
            if(taulell[i][j] == 'k' || taulell[i][j] == 'K'){
                contador ++; // Incrementa contador por cada rey vivo
            }
        }
    }
}

// MÉTODO RENDIRSE
public boolean rendirse(char[][] taulell, String[] jugadors){
    int rendicio;
    System.out.println("Vols rendirte? \n1.No \n2.Si ");
    rendicio = controlErrorsInt(); // Lee opción del usuario

    while(rendicio != 1 && rendicio != 2){
        System.out.println("Ha de ser el numero 1 o el 2.");
        rendicio = controlErrorsInt();
    }

    if(rendicio == 1){
        tirarToballola = false; // No se rinde
    }
    else{
        tirarToballola = true; // Se rinde
        if(torn == 0){
            // Si blancas se rinden, elimina rey blanco y declara ganador
            for(int i = 0; i < 8; i++){
                for(int j= 0; j < 8; j++){
                    if(taulell[i][j] == 'K'){
                        taulell[i][j] = '.';
                        System.out.println("EL GUANYADOR ES:" + jugadors[1]);
                        contarReis(taulell, jugadors);
                        return tirarToballola;
                    }
                }
            }
        }
        else if(torn == 1){
            // Si negras se rinden, elimina rey negro y declara ganador
            for(int i = 0; i < 8; i++){
                for(int j= 0; j < 8; j++){
                    if(taulell[i][j] == 'k'){
                        taulell[i][j] = '.';
                        System.out.println("EL GUANYADOR ES:" + jugadors[0]);
                        contarReis(taulell, jugadors);
                        return tirarToballola;
                    }
                }
            }
        }
    }
    return tirarToballola;
}

// MÉTODO PARA GUARDAR MOVIMIENTOS
public void imprimirMoviments(ArrayList<String> movimentsBlanques, ArrayList<String> movimentsNegres, String movimentPeces){
    if(torn == 0){
        movimentsBlanques.add(movimentPeces); // Agrega movimiento de blancas
    }
    else{
        movimentsNegres.add(movimentPeces); // Agrega movimiento de negras
    }
}

// MÉTODO MOSTRAR MOVIMIENTOS
public void mostrarMoviments(ArrayList<String> movimentsBlanques, ArrayList<String> movimentsNegres){
    int veureMoviments;
    System.out.println("Vols veure els moviments de la partida? \n1.No \n2.Si");
    veureMoviments = controlErrorsInt(); // Lee opción

    while(veureMoviments != 1 && veureMoviments != 2){
        System.out.println("Ha de ser el numero 1 o el 2.");
        veureMoviments = controlErrorsInt();
    }

    if(veureMoviments == 2){
        System.out.println("El moviment de les blanques es: ");
        for(int i = 0; i < movimentsBlanques.size(); i++){
            System.out.println( "Moviment " + (i+1) + ": " + movimentsBlanques.get(i));
        }
        System.out.println("El moviment de les negres es: ");
        for(int i = 0; i < movimentsNegres.size(); i++){
            System.out.println( "Moviment " + (i+1) + ": " + movimentsNegres.get(i));
        }
    }
}

// MÉTODO PARA VOLVER A JUGAR
public void tornarAJugar(String[] jugadors, ArrayList<String> movimentsBlanques, ArrayList<String> movimentsNegres){
    int tornarJoc;
    int mateixosJugadors;
    System.out.println("Voleu tornar a jugar? \n 1.Si \n 2.No");
    tornarJoc = controlErrorsInt(); // Lee opción
    while(tornarJoc != 1 && tornarJoc != 2){
        System.out.println("Ha de ser el numero 1 o el 2.");
        tornarJoc = controlErrorsInt();
    }
    if(tornarJoc == 2){
        System.out.println("Espero que us ho hagueu passat be!!! \nFins aviat!!!");
        System.exit(0); // Termina programa
    }
    else if(tornarJoc == 1){
        System.out.println("Sou els mateixos jugadors? \n 1.Si \n 2.No");
        mateixosJugadors = controlErrorsInt();
        while(mateixosJugadors != 1 && mateixosJugadors != 2){
            System.out.println("Ha de ser el numero 1 o el 2.");
            mateixosJugadors = controlErrorsInt();
       
        }
    }
  }
}
