public class SeparateChainingHashST<Key, Value> implements ST<Key, Value> {
    private static final int initCapacity = 4; // Tamanho inicial da tabela Hash
    private int N; // Numero de pares chave-valor na tabela
    private int M; // Tamanho da tabela de dispersao (Numero de linhas)
    private Node[] st; // Vetor de listas encadeadas

    private static class Node{
        private final Object key; //Chave (tipo generico)
        private Object val; //Valor associado a chave
        private Node next; //Link para o proximo no na lista encadeada

        public Node(Object key, Object val, Node next){
            this.key = key;
            this.val = val;
            this.next = next;
        }
    }

    public SeparateChainingHashST() {
        this(initCapacity); // Chama o construtor com o tamanho inicial
    }

    // Construtor que inicializa a tabela com tamanho especifico
    public SeparateChainingHashST(int M){
        this.M = M;
        st = new Node[M];
    }

    // Metodo auxiliar para redimensionar as listas encandeadas
    private void resize(int chains){
        SeparateChainingHashST<Key, Value> temp = new SeparateChainingHashST<>(chains); // Criamos uma nova tabela temporaria com um novo tamanho

        // Reinsere todas os valores na nova tabela
        for (int i = 0; i < M; i++){
            for (Node x = st[i]; x != null; x = x.next){ //Percorre toda a lista encadeada 
                temp.put((Key) x.key, (Value) x.val); // Insere na nova tabela
            }
        }
        st = temp.st; // Atualiza a referencia da tabela
        M = temp.M; // Atualiza o tamanho da tabela
        N = temp.N; // Atualiza o numero de pares chave-valor
    }

    // Funcao de espalhamento (hash) de acordo com slide
    private int hash(Key key){
        return (key.hashCode() & 0x7fffffff) % M; // Calcula o Hash code e retorna o valor modular
    }

    // Retorna o numero de pares chave-valor    
    public int size(){
        return N;
    }

    // Verifica se a tabela esta vazia
    public boolean isEmpty(){
        return size() == 0;
    }

    // Verifica se a tabela contem uma determinada chave
    public boolean contains(Key key){
        if (key == null) throw new IllegalArgumentException("Chave nao pode ser nula");
        return get(key) != null;
    }

    // Busca o valor associado a uma chave
    public Value get(Key key){
        if (key == null) throw new IllegalArgumentException("Chave nao pode ser nula");
        
        int i = hash(key); // Calcula o hash code e retorna o valor modular

        // Percorre a lista encadeada
        for (Node x = st[i]; x != null; x = x.next){
            if (key.equals(x.key)){
                return (Value) x.val; // Se a chave for igual, retorna o valor
            }   
        }
        return null; // Se a chave nao for encontrada, retorna null
    }

    // Insere um par chave-valor na tabela
    public void put(Key key, Value val){
        if (key == null) throw new IllegalArgumentException("Chave nao pode ser nula");

        if (val == null){
            delete(key); // Se o valor for nulo, deleta a chave
            return;
        }

        if (N >= 8*M) resize(8 *M); // Redimensiona a tabela se estiver muito cheia
        
        int i = hash(key); // Calcula o hash code e retorna o valor modular

        // Percorre a lista encadeada
        for (Node x = st[i]; x != null; x = x.next){
            if (key.equals(x.key)){
                x.val = val; // Se a chave for igual, atualiza o valor
                return;
            }
        }

        N++; // Incrementa o numero de pares chave-valor
        st[i] = new Node(key, val, st[i]); // Insere o novo par chave-valor na lista encadeada
    }

    // Remove uma chave e o seu valor associado da tabela
    public void delete(Key key) {
        if (key == null) throw new IllegalArgumentException("A chave não pode ser nula");

        int i = hash(key);
        st[i] = delete(st[i], key); // Usa o método recursivo auxiliar para deletar

        // Redimensiona se N/M <= 2 (ou seja, N <= 2 * M) e a tabela for maior que o inicial
        if (M > INIT_CAPACITY && N <= 2 * M) {
            resize(M / 2);
        }
    }

    // Método recursivo auxiliar para apagar um nó da lista encadeada
    private Node delete(Node x, Key key) {
        if (x == null) return null;
        if (key.equals(x.key)) {
            N--;
            return x.next; // Remove o nó 'pulando' sobre ele
        }
        x.next = delete(x.next, key);
        return x;
    }
