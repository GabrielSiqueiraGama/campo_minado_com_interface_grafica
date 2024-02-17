package br.com.zhant.cm.visao;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;

import br.com.zhant.cm.modelo.Campo;
import br.com.zhant.cm.modelo.CampoEvento;
import br.com.zhant.cm.modelo.CampoObservador;

@SuppressWarnings("serial")
public class BotaoCampo extends JButton implements CampoObservador, MouseListener{

	private Campo campo;
	
	private final Color BG_PADRAO = new Color(184, 184, 184);//cinza
	private final Color BG_MARCADO = new Color(8, 179, 247);//azul
	private final Color BG_EXPLOSAO = new Color(189, 66, 68);//vermelho
	private final Color TEXTO_VERDE = new Color(0, 100, 0);//verde

	public BotaoCampo(Campo campo) {
		this.campo = campo;
		setBackground(BG_PADRAO);//definindo cor de fundo
		setBorder(BorderFactory.createBevelBorder(0));//define tipo de borda
		setOpaque(true);
		addMouseListener(this);
		campo.registrarObservador(this);
	}

	@Override
	public void eventoOcorreu(Campo campo, CampoEvento evento) {
		switch (evento) {
		case ABRIR: 
			aplicaEstiloAbrir();
			break;
		case MARCAR:
			aplicaEstiloMarcar();
			break;
		case EXPLODIR:
			aplicaEstiloExplodir();
			break;
		default:
			aplicarEstiloPadrao();
		}
		
	}

	private void aplicarEstiloPadrao() {
		setBackground(BG_PADRAO);
		
		setText("");
	}
	private void aplicaEstiloMarcar() {
		setBackground(BG_MARCADO);
		setForeground(Color.WHITE);
		setText("M");
	}
	private void aplicaEstiloExplodir() {
		setBackground(BG_EXPLOSAO);
		setForeground(Color.WHITE);
		setText("X");
	}
	private void aplicaEstiloAbrir() {
		
		setBorder(BorderFactory.createLineBorder(Color.GRAY));
		
		if(campo.isMinado()) {
			setBackground(BG_EXPLOSAO);
			return;
		}
		setBackground(BG_PADRAO);
		
		switch (campo.minasNaVizinhanca()) {
		case 1:
			setForeground(TEXTO_VERDE);
			break;
		case 2:
			setForeground(Color.BLUE);
			break;
		case 3:
			setForeground(Color.YELLOW);
			break;
		case 4:
		case 5:
		case 6:
			setForeground(Color.RED);
			break;
		default:
			setForeground(Color.PINK);
		}
		String valor = !campo.vizinhacaSegura() ? campo.minasNaVizinhanca() + "" : "";
		setText(valor);
	}
	@Override
	public void mousePressed(MouseEvent e) {
		if(e.getButton() == 1) {
			campo.abrir();
		}else {
			campo.alterarMarcacao();
		}
		
	}
	@Override
	public void mouseClicked(MouseEvent e) {}
	@Override
	public void mouseReleased(MouseEvent e) {	}
	@Override
	public void mouseEntered(MouseEvent e) {}
	@Override
	public void mouseExited(MouseEvent e) {}

	
}
