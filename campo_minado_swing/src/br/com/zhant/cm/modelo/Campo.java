package br.com.zhant.cm.modelo;

import java.util.ArrayList;
import java.util.List;


public class Campo {

	private boolean minado;
	private boolean aberto;
	private boolean marcado = false;
	
	private final int linha;
	private final int coluna;
	
	private List<Campo> vizinhos = new ArrayList<>(); 
	private List<CampoObservador> observadores = new ArrayList<>();
	//private List<BiConsumer<Campo, CampoEvento>> observadores2 = new ArrayList<BiConsumer<Campo,CampoEvento>>();
	
	
	public Campo(int linha, int coluna) {
		this.linha = linha;
		this.coluna = coluna;
	}
	
	public void registrarObservador(CampoObservador observador) {
		observadores.add(observador);
	}
	
	private void notificarObservadores(CampoEvento evento) {
		observadores.stream()
			.forEach(obs -> obs.eventoOcorreu(this, evento));
	}
	
	boolean adicionarVizinho(Campo vizinho) {
		boolean linhaDiferente = linha != vizinho.linha;
		boolean colunaDiferente = coluna != vizinho.coluna;
		boolean diagonal = colunaDiferente && linhaDiferente;
		
		int deltaLinha = Math.abs(linha - vizinho.linha);
		int deltaColuna = Math.abs(coluna - vizinho.coluna);
		int deltaGeral = deltaLinha + deltaColuna;
		
			if(deltaGeral == 1 && !diagonal) {/*nesta função é conferido se há vizinhos, incluindo a diagonal, ou 
			seja, nos cinco blocos ao redor do bloco selecionado*/
				vizinhos.add(vizinho);
				return true;
			}else if(deltaGeral == 2 && diagonal) {
				vizinhos.add(vizinho);
				return true;
			}else {
				return false;
			}
		}
		
		void alterarMarcacao(){//Confere se o bloco está aberto antes, caso não esteja ele realiza a marcação
			if(!aberto) {
				marcado = !marcado;
				
				if(marcado) {
					notificarObservadores(CampoEvento.MARCAR);
				}else {
					notificarObservadores(CampoEvento.DESMARCAR);
				}
			}
		}

		boolean abrir() {//Confere se está aberto ou marcado, caso não esteja ele realiza a função, caso esteja minado chama a explosão
			if (!aberto && !marcado) {
				if (minado) {
					notificarObservadores(CampoEvento.EXPLODIR);
					return true;
				}
				
				setAberto(true);
				
				if (vizinhacaSegura()) {//Abre enquanto a vizinhança estiver segura
					vizinhos.forEach(v -> v.abrir());
				}
				return true;
			} else {
				return false;
			}
		}

		void minar() {//Função para minar
			minado = true;
		}
		
		boolean objetivoAlcancado() {//Confere se o jogo chegou ao resultado final, conferindo se descobriu o local de todas as bombas
			boolean desvendado = !minado && aberto;
			boolean protegido = minado && marcado;
			return desvendado || protegido;
		}
		long minasNaVizinhanca() {
			return vizinhos.stream().filter(v -> v.minado).count();
		}
		void reinciar() {//seta todas as variaveis iniciais
			aberto = false;
			minado = false;
			marcado = false;
		}
		
		boolean vizinhacaSegura() {//Define como vizinhança segura desde que não esteja minado
			return vizinhos.stream().noneMatch(v -> v.minado);
		}
		
		public boolean isMinado() {
			return minado;
		}
		public boolean isMarcado() {
			return marcado;
		}
		public boolean isAberto() {
			return aberto;
		}
		
		void setAberto(boolean aberto) {
			this.aberto = aberto;
			if(aberto) {
				notificarObservadores(CampoEvento.ABRIR);
			}
		}

		public boolean isFechado() {
			return !aberto;
		}

		public int getLinha() {
			return linha;
		}

		public int getColuna() {
			return coluna;
		}
}
