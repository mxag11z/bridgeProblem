import java.util.Arrays;
//Esta solucion implementa la solucion al problema de bridge&Torch, usando recursividad.
public class Puente {

    public static int opt(int[] tiempos, int tiempoTotal) {
        Arrays.sort(tiempos);
        int n = tiempos.length;

        System.out.println("\nLista actual: " + Arrays.toString(tiempos));
        System.out.println("Tiempo acumulado hasta ahora: " + tiempoTotal);

        if (n == 0) {
            return tiempoTotal;
        }
        if (n == 1) {
            tiempoTotal += tiempos[0];
            System.out.println("Cruza solo 1 persona: " + Arrays.toString(tiempos) + ", +" + tiempos[0]);
            System.out.println("Tiempo final: " + tiempoTotal);
            return tiempoTotal;
        }
        if (n == 2) {
            tiempoTotal += tiempos[1];
            System.out.println("Cruzan 2 personas: " + Arrays.toString(tiempos) + ", +" + tiempos[1]);
            System.out.println("Tiempo final: " + tiempoTotal);
            return tiempoTotal;
        }
        if (n == 3) {
            int suma = tiempos[0] + tiempos[1] + tiempos[2];
            tiempoTotal += suma;
            System.out.println("Cruzan 3 personas: " + Arrays.toString(tiempos) + ", +" + suma);
            System.out.println("Tiempo final: " + tiempoTotal);
            return tiempoTotal;
        }

        
        int fastest = tiempos[0];
        int secondFastest = tiempos[1];
        int secondSlowest = tiempos[n - 2];
        int slowest = tiempos[n - 1];

        // Paso 1: cruzan los dos más rápidos
        tiempoTotal += secondFastest;
        System.out.println("Cruzan los dos más rápidos: " + fastest + ", " + secondFastest + " +" + secondFastest);

        // Paso 2: regresa el más rápido
        tiempoTotal += fastest;
        System.out.println("Regresa el más rápido: " + fastest + " +" + fastest);

        // Paso 3: cruzan los dos más lentos
        tiempoTotal += slowest;
        System.out.println("Cruzan los dos más lentos: " + secondSlowest + ", " + slowest + " +" + slowest);

        // Paso 4: regresa el segundo más rápido
        tiempoTotal += secondFastest;
        System.out.println("Regresa el segundo más rápido: " + secondFastest + " +" + secondFastest);

        System.out.println("Tiempo acumulado ahora: " + tiempoTotal);

        // Crear nuevo arreglo sin los dos más lentos
        int[] nuevosTiempos = Arrays.copyOf(tiempos, n - 2);

        System.out.println("Se manda a llamada recursiva: " + Arrays.toString(nuevosTiempos));
        return opt(nuevosTiempos, tiempoTotal);
    }

    public static void main(String[] args) {
        int[] times = {1, 2, 5, 10};
        int timeOpt = 0;

        System.out.println("\nEste es el tiempo óptimo: " + opt(times, timeOpt));
    }
}
