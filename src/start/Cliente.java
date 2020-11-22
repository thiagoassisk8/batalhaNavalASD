package start;

import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;
import java.net.InetAddress;
import java.net.Inet4Address;
import java.util.Scanner;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.io.DataOutputStream;
import java.io.DataInputStream;
import java.lang.Math; 

import game.*;

public class Cliente {
  
  static Socket servidor;
  static String ip;
  static Scanner in;
  static Jogador jogador;
  static int[] dados = new int[4];
  static Jogador[] jogadores = new Jogador[2];
  static Tabuleiro[] tabs = new Tabuleiro[2];

  static ObjectInputStream istream;
  static ObjectOutputStream ostream;
  static DataInputStream dis;
  static DataOutputStream dos;

  public static void print(String s) {
    System.out.println(s);
  }

  public static void nullArray(Jogador[] j) {
    for (int i = 0; i < j.length; i++)
      j[i] = null;
  }

  // cria o jogador para o jogo
  public static void createPlayer(int[] dados) {
    System.out.print("Digite o seu nome para o jogo: ");
          
    String nome = in.next();


    jogador = new Jogador(nome, dados[0], dados[1], dados[2], dados[3]);
  
    System.out.println("Jogador criado!");
  }

  // configura o mapa do jogador posicionando os barcos na matriz
  public static void configurePlayerMap() {
    int[] listaposicoes = {0,1, 2, 3,4,5,6,7,8,9};
    int x, y;
    System.out.println("Posicione as embarcacoes usando os indices das linhas e colunas.");
    while (jogador.getNumBarcos() < jogador.MAX_BARCOS) {
      jogador.printTab();
      //System.out.print("Linha: ");
      
      
      
        x = (int) (Math.random() * listaposicoes.length);
        
        y = (int) (Math.random() * listaposicoes.length);

        

      

      

      //System.out.print("Coluna: ");
      
      jogador.posicionarBarco(x, y);
    
      System.out.printf("Adicionado! (%d/%d)\n", jogador.getNumBarcos(), jogador.MAX_BARCOS);
    }
    System.out.println("Meu mapa de embarcacoes:");
    jogador.printTab();
  }

  // imprimir os tabuleiros
  public static void mostrarTabs(int i) {
    if (i == 0) {
      print("Seu tabuleiro:");
      jogadores[0].printTab();
      print("Tabuleiro de " + jogadores[1].getNome());
      jogadores[1].printTabSecret();
    } else if (i == 1) {
      print("Seu tabuleiro:");
      jogadores[1].printTab();
      print("Tabuleiro de " + jogadores[0].getNome());
      jogadores[0].printTabSecret();
    }
  }

  public static void main(String[] args) throws UnknownHostException {
    in = new Scanner(System.in);
    
    // conectar: digite a porta em que deseja se conectar
    
    ip = Inet4Address.getLocalHost().getHostAddress();
    print("O IP do servidor eh "+ (ip));
    //numero da porta
    int port = 8080;
    try {
      servidor = new Socket(ip, port);
      print("Conectado a porta "+ port);
      print("Aguardando outro jogador...");

      // ler dados do jogo
      istream = new ObjectInputStream(servidor.getInputStream());
      dados = (int[]) istream.readObject();

      createPlayer(dados); // criar o seu jogador
      configurePlayerMap(); // distribuir embarcações no mapa

      // enviar o obj jogador para o servidor
      ostream = new ObjectOutputStream(servidor.getOutputStream());
      ostream.writeObject(jogador); // escreve o objeto na stream
      ostream.flush();
      print("OK! Espere o outro jogador");

      // recebe índice do jogador no servidor
      int myIndex = istream.readInt();
      int oponentIndex;
      if (myIndex == 0) oponentIndex = 1;
      else oponentIndex = 0;
      print("voce e o jogador "+ (myIndex+1) +".");

      // recebe os vetores com os dois jogadores e os dois tabuleiros
      jogadores = (Jogador[]) istream.readObject();
      tabs = (Tabuleiro[]) istream.readObject();
      print("Voce: "+ jogadores[myIndex].getNome());
      print("Adversario: "+ jogadores[oponentIndex].getNome());

      // iniciar a Batalha
      print("==============================:");
      print(" Iniciando a Batalha");
      print("==============================:");
      int sinal, x, y, indexVencedor;
      int[] coord = new int[2];
      String log, placar;
      boolean end = false;

      dos = new DataOutputStream(servidor.getOutputStream());
      dis = new DataInputStream(servidor.getInputStream());

      // aqui começa o loop ===================================================================================//
      while (!end) {
        // receber o sinal para definir quem ataca
        sinal = (int) istream.readInt();
        //mostrarTabs(myIndex);
        placar = (String)istream.readObject();
        print(placar);

        /* o sinal enviado pelo servidor contém o índice do jogador que ataca nessa rodada
         * logo, o cliente deve verificar se esse sinal é o seu índice no jogo */
        if (sinal == myIndex){
          //print("Sua vez. Digite as coordenadas (linha e coluna) do tiro.");
          /* recebe os valores X e Y do teclado e em seguida envia para o servidor */
          coord = new int[2];
          
          int[] listaposicoes = {0,1, 2, 3,4,5,6,7,8,9};
          

          //Linha
          coord[0] =(int) (Math.random() * listaposicoes.length);
          
          //Coluna
          coord[1] = (int) (Math.random() * listaposicoes.length);

                    
          ostream.writeObject(coord); // envia as coordenadas
        

          // receber log do resultado do tiro
          log = (String)istream.readObject();
          print(log);

          /* se o sinal for igual ao índice do seu oponente significa que é a vez do mesmo */
        } else if (sinal == oponentIndex) {

          print("Vez de "+ jogadores[oponentIndex].getNome() +". Espere...");

          // receber log do resultado do tiro
          log = (String)istream.readObject();
          print(log);

        } else if (sinal == 2){
          /* Caso o loop do servidor termine ele enviará o sinal=2,
           * nesse caso a variável boolean "end" é setada para 'true' e o cliente recebe um sinal
           * com o índice do vencedor ou -1 em caso de empate... */
          print("Acabaram os tiros");
          end = true;
          break;
        }
      }

      // recebe o índice do jogador vencedor: 0 ou 1
      // em caso de empate receberá -1.
      indexVencedor = (int)istream.readInt();
      if (indexVencedor > 0){
        print("+-----------------------------+");
        print("| \tVencedor: "+ jogadores[indexVencedor].getNome());
        print("+-----------------------------+");
      } else if (indexVencedor == -1) {
        print("+-----------------------------+");
        print("| \tEmpate!");
        print("+-----------------------------+");
      }

      System.out.print("Digite CTRL C para desconectar... ");
      String fim = in.next();

      // fechando streams e socket
      istream.close();
      ostream.close();
      servidor.close();
      print("Conexao Encerrada.");

    } catch(Exception e) {
      System.err.println("Erro: "+ e.toString());
    }

  }

}
