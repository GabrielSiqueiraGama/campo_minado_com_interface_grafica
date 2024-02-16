package br.com.zhant.cm.modelo;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;


public class Tabuleiro {

	private int linhas;
	private int colunas;
	private int minas;
	
	private final List<Campo> campos = new ArrayList<>();//Cria um array de campos, fazendo assim o tabuleiro

	public Tabuleiro(int linhas, int colunas, int minas) {
		this.linhas = linhas;
		this.colunas = colunas;
		this.minas = minas;
	
		gerarCampos();
		associarVizinhos();
		sortearMinas();
	}
	
	public void abrir(int linha, int coluna) {
		try {//Caso atenda as expectativas ele realiza a função, pegando a linha e a coluna
			campos.stream()
				.filter(c -> c.getLinha() == linha && c.getColuna() == coluna)
					.findFirst()
						.ifPresent(c -> c.abrir());;
		} catch (Exception e) {//no momento da explosão abre todo o quadro do campo minado para identificar as bombas
			campos.forEach(c -> c.setAberto(true));
			throw e;
		}
	}
	public void alterarMarcacao(int linha, int coluna) {//Mesma função acima, porém realiza a marcação no lugar de abrir
		campos.stream()
			.filter(c -> c.getLinha() == linha && c.getColuna() == coluna)
				.findFirst()
					.ifPresent(c -> c.alterarMarcacao());;
	}
	
	private void gerarCampos() {//Gera campos de acordo com a quantidade de linhas e colunas. 
		for(int l = 0; l < linhas; l++) {
			for(int c = 0; c < colunas; c++) {
				campos.add(new Campo(l, c));
			}
		}
	}
	
	private void associarVizinhos() {//Associa todos os vizinhos dos campos
		for(Campo c1: campos) {
			for(Campo c2: campos) {
				c1.adicionarVizinho(c2);
			}
		}
	}
	
	private void sortearMinas() {
		long minasArmadas = 0;
		Predicate<Campo> minado = c-> c.isMinado();
		
		do {
			int aleatorio = (int) (Math.random() * campos.size());//Definindo posição aleatoria das bombas nos campos
			campos.get(aleatorio).minar();//pega o numero definido e mina
			minasArmadas = campos.stream().filter(minado).count();//confere a quantidade de minas já plantadas para encaixar com a definida.
		}while(minasArmadas < minas);
	}
	
	public boolean objetivoAlcancado() {
		return campos.stream().allMatch(c -> c.objetivoAlcancado());
	}
	
	public void reiniciar() {
		campos.stream().forEach(c-> c.reinciar());
		sortearMinas();
	}
	
}
