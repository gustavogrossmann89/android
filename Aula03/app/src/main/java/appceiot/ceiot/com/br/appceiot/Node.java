package appceiot.ceiot.com.br.appceiot;

/**
 * Created by leona on 05/03/2018.
 */

/**
 * TODO: (5) Montar base
 * Estrutura a ser salva no banco de dados
 *
 * Será utilizada firebase como exemplo:
 *
 * Para salvar dados
 * PUT https://ceiot-android-app.firebaseio.com/nodes/node1.json
 * ou
 * POST https://ceiot-android-app.firebaseio.com/nodes.json
 *
 * {
 *    "nome" : "Node1",
 *    "descricao" : "Exemplo de no 1",
 *    "localizacao" : "UTFPR"
 * }
 *
 *
 * PUT	Grave ou substitua dados em um caminho definido, como messages/users/user1/<data>.
 * PATCH	Atualize algumas chaves de um caminho específico sem substituir todos os dados.
 * POST	Adicione a uma lista de dados do banco de dados do Firebase. Sempre que uma solicitação POST é enviada, o cliente Firebase gera uma chave exclusiva, como messages/users//.
 * DELETE	Remova os dados da referência especificada de banco de dados do Firebase.
 * Fonte:https://firebase.google.com/docs/database/rest/save-data?authuser=0
 *
 * Para leitura:
 * https://ceiot-android-app.firebaseio.com/nodes.json?orderBy="nome"&equalTo="Node1"
 * https://ceiot-android-app.firebaseio.com/nodes.json
 *
 * https://firebase.google.com/docs/database/rest/retrieve-data?authuser=0
 */
public class Node {
    private String nome;
    private String descricao;
    private String localizacao;

    public Node(String nome, String descricao, String localizacao) {

        this.nome = nome;
        this.descricao = descricao;
        this.localizacao = localizacao;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getLocalizacao() {
        return localizacao;
    }

    public void setLocalizacao(String localizacao) {
        this.localizacao = localizacao;
    }

    @Override
    public String toString() {
        return "Node{" +
                "nome='" + nome + '\'' +
                ", descricao='" + descricao + '\'' +
                ", localizacao='" + localizacao + '\'' +
                '}';
    }
}
