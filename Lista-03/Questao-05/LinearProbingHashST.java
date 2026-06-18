public class LinearProbingHashST<Key, Value> implements ST<Key, Value> {
    private static final int INIT_CAPACITY = 4; // Capacidade inicial mínima

    private int N;           // Número de pares chave-valor guardados
    private int M;           // Tamanho da tabela de dispersão (tamanho dos vetores)
    private Key[] keys;      // Vetor que guarda as chaves
    private Value[] vals;    // Vetor que guarda os valores

    // Construtor sem argumentos (inicia com tamanho padrão)
    public LinearProbingHashST() {
        this(INIT_CAPACITY);
    }

    // Construtor com capacidade específica
    @SuppressWarnings("unchecked")
    public LinearProbingHashST(int capacity) {
        M = capacity;
        N = 0;
        keys = (Key[])   new Object[M];
        vals = (Value[]) new Object[M];
    }

        // Função Hash para mapear chaves aos índices do vetor
    private int hash(Key key) {
        return (key.hashCode() & 0x7fffffff) % M;
    }

    // -------------------------------------------------------------------
    // MÉTODOS DA INTERFACE ST
    // -------------------------------------------------------------------

    public int size() {
        return N;
    }

    public boolean isEmpty() {
        return size() == 0;
    }

    public boolean contains(Key key) {
        if (key == null) throw new IllegalArgumentException("Chave nula!");
        return get(key) != null;
    }

    public Value get(Key key) {
        if (key == null) throw new IllegalArgumentException("Chave nula!");
        
        // Sondagem Linear: procura a chave iterando até encontrar um espaço vazio (null)
        for (int i = hash(key); keys[i] != null; i = (i + 1) % M) {
            if (keys[i].equals(key)) {
                return vals[i]; // Hit
            }
        }
        return null; // Miss
    }

    public void put(Key key, Value val) {
        if (key == null) throw new IllegalArgumentException("Chave nula!");

        if (val == null) {
            delete(key);
            return;
        }

        // REDIMENSIONAMENTO: Se N / M >= 1/2, duplica o tamanho de M
        if (N >= M / 2) {
            resize(2 * M);
        }

        int i;
        // Avança até encontrar a chave (para atualizar) ou um espaço vazio (para inserir)
        for (i = hash(key); keys[i] != null; i = (i + 1) % M) {
            if (keys[i].equals(key)) {
                vals[i] = val; // Chave já existe: atualiza o valor
                return;
            }
        }
        
        // Espaço vazio encontrado: insere nova chave e valor
        keys[i] = key;
        vals[i] = val;
        N++;
    }

    public void delete(Key key) {
        if (key == null) throw new IllegalArgumentException("Chave nula!");
        if (!contains(key)) return; // Se a chave não existe, não há o que apagar

        // Encontra a posição da chave
        int i = hash(key);
        while (!key.equals(keys[i])) {
            i = (i + 1) % M;
        }

        // Apaga a chave e o valor
        keys[i] = null;
        vals[i] = null;

        // Limpeza de Sondagem Linear: Temos de reespalhar todas as chaves 
        // do mesmo agrupamento (cluster) que estavam imediatamente a seguir
        i = (i + 1) % M;
        while (keys[i] != null) {
            Key keyToRehash = keys[i];
            Value valToRehash = vals[i];
            
            // Remove provisoriamente
            keys[i] = null;
            vals[i] = null;
            N--; // Subtrai porque o put() abaixo vai voltar a somar
            
            // Reinsere para que fiquem no local certo sem "buracos" pelo caminho
            put(keyToRehash, valToRehash);
            
            i = (i + 1) % M;
        }

        N--; // Agora sim, subtrai a chave que o utilizador pediu para remover

        // REDIMENSIONAMENTO: Se N / M <= 1/8, reduz para metade o tamanho de M
        if (N > 0 && N <= M / 8) {
            resize(M / 2);
        }
    }

    public Iterable<Key> keys() {
        Queue<Key> queue = new LinkedList<>();
        for (int i = 0; i < M; i++) {
            if (keys[i] != null) {
                queue.add(keys[i]);
            }
        }
        return queue;
    }

    // -------------------------------------------------------------------
    // MÉTODO AUXILIAR DE REDIMENSIONAMENTO (RESIZE)
    // -------------------------------------------------------------------

    private void resize(int capacity) {
        // Cria uma nova tabela temporária com a nova capacidade
        LinearProbingHashST<Key, Value> temp = new LinearProbingHashST<>(capacity);
        
        // Percorre a tabela atual e usa o put() da tabela nova para reespalhar as chaves
        for (int i = 0; i < M; i++) {
            if (keys[i] != null) {
                temp.put(keys[i], vals[i]);
            }
        }
        
        // Atualiza as referências da nossa tabela para os novos arrays
        keys = temp.keys;
        vals = temp.vals;
        M    = temp.M;
    }   
}