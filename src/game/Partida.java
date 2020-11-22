package game;
/*
  A classe partida é usada para manipular as informações do jogo durante
  uma partida/batalha. Os métodos serão usados para prover serviços ás
  solicitações dos clientes (jogadores).
*/
import java.io.Serializable;

public class Partida implements Serializable {
  // constantes:
  private final String BARCO_DESTRUIDO = "-------------------\nBarco destruido!\n--------------------";
  private final String TIRO_NA_AGUA = "-------------------\nTiro na Agua!\n--------------------";
  // vars:
  private Jogador[] jogador = new Jogador[2];

  // construtor
  public Partida(Jogador j1, Jogador j2) {
    this.jogador[0] = j1;
    this.jogador[1] = j2;
  }

  public Jogador getJogador1(){
    return this.jogador[0];
  }
  public Jogador getJogador2(){
    return this.jogador[1];
  }
  public String getBarcoDestruido(){
    return BARCO_DESTRUIDO;
  }
  public String getTiroNaAgua(){
    return TIRO_NA_AGUA;
  }

  // verificar se os dois jogadores ainda possuem tiros disponiveis
  public boolean jogadoresPossuemTiros(){
    int tiros=0;
    for (Jogador j : jogador)
      tiros += j.getTiros(); // soma os tiros dos 2 jogadores
    // se a soma for maior que zero, é pq ainda há tiros disponiveis
    if (tiros > 0)
      return true;
    return false;
  }


  // jogador ataca o outro jogador nas coordenadas passadas
  // retorna o caractere alvo do tiro
  public char jogadorAtaca(int ij, int x, int y) {
    char alvo = '_';
    if (ij > 0 && ij < 3){
      Jogador atk, def=null;
      atk = jogador[ij-1];
      if (ij == 1){
        def = this.jogador[1];
      } else if (ij == 2){
        def = this.jogador[0];
      }
      alvo = def.getTab(x,y);
      atk.setTiros(atk.getTiros()-1); // decrementa o num de tiros do atacante
      if (alvo == 'B') {
        // if o alvo tiver um B, atingiu um barco: troca por X
        def.setTab(x,y,'X');
      } else if(alvo == '~'){
        // se o alvo tiver um ~ ... tiro na água:
        // troca por '*' para indicar que aquele já foi atirado
        def.setTab(x,y,'*');
      }

    }
    return alvo;
  }

  // métodos de print:
  public void printBarcoDestruido(){
    System.out.println(BARCO_DESTRUIDO);
  }
  public void printTiroNaAgua(){
    System.out.println(TIRO_NA_AGUA);
  }

  public void printNomesJogadores(){
    System.out.printf("Batalha: %s vs %s\n", this.jogador[0].getNome(), this.jogador[1].getNome());
  }

  // imprime os tabuleiros dos dois jogadores
	public void printClientTab(){
    // jogador[1] é o cliente
    System.out.println("Seu tabuleiro:");
    jogador[1].printTab();
    // jogador[0] é o servidor
		System.out.println("Tabuleiro de "+ jogador[0].getNome());
		jogador[0].printTabSecret();
	}

  public void printServerTab(){
    // jogador[0] é o servidor
    System.out.println("Seu tabuleiro:");
    jogador[0].printTab();
    // jogador[1] é o cliente
		System.out.println("Tabuleiro de "+ jogador[1].getNome());
		jogador[1].printTabSecret();
	}

}