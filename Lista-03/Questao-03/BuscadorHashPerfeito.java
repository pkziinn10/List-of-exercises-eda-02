public class BuscadorHashPerfeito {
    public static void main(String[] args) {
        // As letras fornecidas no exercício
        char[] chaves = {'S', 'E', 'A', 'R', 'C', 'H', 'X', 'M', 'P', 'L'};
        int[] valoresK = new int[chaves.length];
        
        System.out.println("--- MAPEAMENTO DO ALFABETO ---");
        // Passo 1: Converter as letras para a sua posição no alfabeto (A=1, B=2...)
        for (int i = 0; i < chaves.length; i++) {
            // A matemática aqui é: Código ASCII da letra - Código ASCII do 'A' + 1
            valoresK[i] = chaves[i] - 'A' + 1;
            System.out.print(chaves[i] + " = " + valoresK[i] + "  |  ");
        }
        System.out.println("\n\nA procurar a combinação ideal de M e x para zero colisões...");

        // Passo 2: Testar valores de M e x progressivamente
        // Começamos o M a 10 (tamanho do array chaves), pois é o limite físico mínimo.
        for (int M = chaves.length; M < 1000; M++) {
            
            // O x só precisa ser testado até M-1
            for (int x = 1; x < M; x++) {
                
                // Array booleano para registar quais índices já foram ocupados
                boolean[] indiceOcupado = new boolean[M];
                boolean houveColisao = false;

                // Testar a função de hash para cada chave
                for (int k : valoresK) {
                    int hash = (x * k) % M;
                    
                    if (indiceOcupado[hash]) {
                        houveColisao = true; // Oops! Alguém já estava nesta "gaveta"
                        break; // Abandona este 'x' e tenta o próximo imediatamente
                    }
                    indiceOcupado[hash] = true; // Marca a gaveta como ocupada
                }

                // Passo 3: Avaliar o resultado do teste
                if (!houveColisao) {
                    System.out.println("\n=== RESULTADO ENCONTRADO ===");
                    System.out.println("O menor M possível = " + M);
                    System.out.println("O valor de x = " + x);
                    
                    System.out.println("\nProva de que não há colisões com a função (" + x + " * k) % " + M + ":");
                    for (int i = 0; i < chaves.length; i++) {
                        int hash = (x * valoresK[i]) % M;
                        System.out.println("Letra " + chaves[i] + " (k=" + valoresK[i] + ") -> Índice " + hash);
                    }
                    
                    // Como só queremos encontrar o "menor M possível", 
                    // terminamos a execução assim que encontramos o primeiro caso de sucesso!
                    return; 
                }
            }
        }
    }
}