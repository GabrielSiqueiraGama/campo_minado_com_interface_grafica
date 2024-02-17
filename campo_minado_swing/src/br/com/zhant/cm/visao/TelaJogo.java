package br.com.zhant.cm.visao;

import javax.swing.JFrame;

import br.com.zhant.cm.modelo.Tabuleiro;

@SuppressWarnings("serial")
public class TelaJogo extends JFrame{

	public TelaJogo() {
		Tabuleiro tabuleiro = new Tabuleiro(20, 30, 2);
		 
		add(new PainelTabuleiro(tabuleiro));
		
		setTitle("Campo minado");//Define o titulo que aparecerá na barra do app;
		setSize(690, 438);//Define o tamanho.
		setLocationRelativeTo(null);//Define a posição que o programa abrirá
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);//Encerra o projeto após ser clicado no X do app;
		setVisible(true);//Define a visibilidade
		
	}
	
	public static void main(String[] args) {
		new TelaJogo();
	}
}
