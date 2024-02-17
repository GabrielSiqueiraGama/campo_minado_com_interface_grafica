package br.com.zhant.cm.modelo;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;


public class Tabuleiro implements CampoObservador{

	private final int linhas;
	private final int colunas;
	private final int minas;
	
	private final List<Campo> campos = new ArrayList<>();//Cria um array de campos, fazendo assim o tabuleiro
	private final List<Consumer<ResultadoEvento>> observadores = new ArrayList<Consumer<ResultadoEvento>>();
	
	
	public Tabuleiro(int linhas, int colunas, int minas) {
		this.linhas = linhas;
		this.colunas = colunas;
		this.minas = minas;
	
		gerarCampos();
		associarVizinhos();
		sortearMinas();
	}
	public void paraCadaCampo(Consumer<Campo> funcao) {
		campos.forEach(funcao);
	}
	public int getLinhas() {
		return linhas;
	}
	public int getColunas() {
		return colunas;
	}
	public int getMinas() {
		return minas;
	}
	public List<Campo> getCampos() {
		return campos;
	}

	public List<Consumer<ResultadoEvento>> getObservadores() {
		return observadores;
	}



	public void registrarObservador(Consumer<ResultadoEvento> observador) {
		observadores.add(observador);
	}
	
	private void notificarObservadores(boolean resultado) {
		observadores.stream()
			.forEach(obs -> obs.accept(new ResultadoEvento(resultado)));
	}
	
	public void abrir(int linha, int coluna) {
		campos.stream()
			.filter(c -> c.getLinha() == linha && c.getColuna() == coluna)
				.findFirst()
					.ifPresent(c -> c.abrir());;

	}
	
	
	
	public void alterarMarcacao(int linha, int coluna) {//Mesma função acima, porém realiza a marcação no lugar de abrir
		campos.parallelStream()
			.filter(c -> c.getLinha() == linha && c.getColuna() == coluna)
				.findFirst()
					.ifPresent(c -> c.alterarMarcacao());;
	}
	
	private void gerarCampos() {//Gera campos de acordo com a quantidade de linhas e colunas. 
		for(int linha = 0; linha < linhas; linha++) {
			for(int coluna = 0; coluna < colunas; coluna++) {
				Campo campo = new Campo(linha, coluna);
				campo.registrarObservador(this);
				campos.add(campo);
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

	@Override
	public void eventoOcorreu(Campo campo, CampoEvento evento) {
		if(evento == CampoEvento.EXPLODIR) {
			mostrarMinas();
			System.out.println("Perdeu haha");
			notificarObservadores(false);
		}else if(objetivoAlcancado()){
			System.out.println("Nao perdeu :(((((");
			notificarObservadores(true);
		}
		
	}
	
	private void mostrarMinas() {
		campos.stream()
			.filter(c -> c.isMinado())
				.forEach(c -> c.setAberto(true));
	}
	
}
