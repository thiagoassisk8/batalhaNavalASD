package game;

import java.io.Serializable;

public class Tabuleiro implements Serializable {

	int sizeX;
	int sizeY;
	char[][] tab;
	int n_barcos;
	

	// o tabuleiro recebe os valores de suas dimensões
	public Tabuleiro(int x, int y){
		this.sizeX = 10;
		this.sizeY = 10;
		tab = new char[10][10];
		this.initTab();
	}

	public void initTab() {
		for (int i=0; i<sizeX; i++)
			for (int k=0; k<sizeY; k++)
				tab[i][k] = '~';
	}

	public void setTab(char[][] matriz){
		this.tab = matriz;
	}

	/*
	 * Imprime o tabuleiro com os índices das linhas e colunas
	 */
	public void print() {
		System.out.println("");

		System.out.printf("     ");
		

		for (int j=0; j<sizeY; j++)
			System.out.printf("%d ", j); // imprime índices das colunas
			//System.out.print(letras[j]);
		System.out.println("");

		for (int i=0; i<sizeX; i++) {
			System.out.printf("  %d  ", i); // imprime o numero da linha
			

			for (int k=0; k<sizeY; k++)
				System.out.printf("%c ", tab[i][k]); // imprime linha inteira

			System.out.println(""); // vai pra próxima linha
		}
		System.out.println("");
	}



	public void printSecret() {
		
		System.out.println("");

		System.out.printf("     ");
		for (int j=0; j<sizeY; j++)
			System.out.printf("%d ", j); // imprime índices das colunas
		System.out.println("");

		for (int i=0; i<sizeX; i++) {
			//String letras[]= {"a","b","c","d","e","f","g","h","i","j"};
			
			//System.out.printf("  %d  ", letras[i]); // imprime o numero da linha	
			//Integer.parseInt(String s) 
			
			System.out.printf("  %d  ", i); // imprime o numero da linha

			for (int k=0; k<sizeY; k++){
				if (tab[i][k] == '~' || tab[i][k] == 'B')
					System.out.printf("~ ");
				else if (tab[i][k] == '*' || tab[i][k] == 'X')
					System.out.printf("%c ", tab[i][k]);
			} // imprime linha inteira

			System.out.println(""); // vai pra próxima linha
		}
		System.out.println("");
	}

}
