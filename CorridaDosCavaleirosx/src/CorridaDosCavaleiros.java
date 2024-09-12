import java.util.Random;
import java.util.concurrent.Semaphore;

public class CorridaDosCavaleiros {

    private static final double DISTANCIA_TOTAL = 2000.0; 
    private static final double DISTANCIA_TOCHA = 500.0; 
    private static final double DISTANCIA_PEDRA = 1500.0; 
    private static final double AUMENTO_TOCHE = 80.0; 
    private static final double AUMENTO_PEDRA = 80.0; 
    private static final int NUM_CAVALEIROS = 4;

    private static Random random = new Random();

   
    private static Semaphore semaforoTocha = new Semaphore(1);
    private static Semaphore semaforoPedra = new Semaphore(1);

    public static void main(String[] args) throws InterruptedException {
        Thread[] cavaleiros = new Thread[NUM_CAVALEIROS];
        double[] velocidadesIniciais = new double[NUM_CAVALEIROS];
        double[] temposTotais = new double[NUM_CAVALEIROS];
        
        
        for (int i = 0; i < NUM_CAVALEIROS; i++) {
            velocidadesIniciais[i] = 80 + random.nextDouble() * 80; 
        }
        
        for (int i = 0; i < NUM_CAVALEIROS; i++) {
            int cavaleiroId = i;
            double velocidadeInicial = velocidadesIniciais[i];
            cavaleiros[i] = new Thread(() -> {
                try {
                    temposTotais[cavaleiroId] = correr(velocidadeInicial);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
            cavaleiros[i].start();
        }
        
        
        for (int i = 0; i < NUM_CAVALEIROS; i++) {
            cavaleiros[i].join();
        }

        double menorTempo = Double.MAX_VALUE;
        int vencedor = -1;
        for (int i = 0; i < NUM_CAVALEIROS; i++) {
            if (temposTotais[i] < menorTempo) {
                menorTempo = temposTotais[i];
                vencedor = i;
            }
        }
        
    
        System.out.println("Velocidades Iniciais:");
        for (int i = 0; i < NUM_CAVALEIROS; i++) {
            System.out.printf("Cavaleiro %d: %.2f m/s%n", i + 1, velocidadesIniciais[i]);
        }
        
        System.out.println("\nTempos Totais:");
        for (int i = 0; i < NUM_CAVALEIROS; i++) {
            System.out.printf("Cavaleiro %d: %.2f segundos%n", i + 1, temposTotais[i]);
        }
        
        System.out.printf("\nCavaleiro %d é o vencedor e conseguiu escapar!%n", vencedor + 1);
    }
    
    private static double correr(double velocidadeInicial) throws InterruptedException {
      
        double tempoParaTocha = DISTANCIA_TOCHA / velocidadeInicial;
        
        
        semaforoTocha.acquire();
        double velocidadeComTocha = velocidadeInicial + AUMENTO_TOCHE;
        semaforoTocha.release();
        
    
        double tempoParaPedra = DISTANCIA_PEDRA / velocidadeComTocha;
        
     
        semaforoPedra.acquire();
        double velocidadeComPedra = velocidadeComTocha + AUMENTO_PEDRA;
        semaforoPedra.release();
        

        double tempoRestante = (DISTANCIA_TOTAL - DISTANCIA_TOCHA - DISTANCIA_PEDRA) / velocidadeComPedra;
        

        return tempoParaTocha + tempoParaPedra + tempoRestante;
    }
}
