package model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity(name = "votos")
public class Voto {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@ManyToOne(cascade = CascadeType.PERSIST)
	@JoinColumn(name = "id_eleitor", foreignKey = @ForeignKey(name ="fk_eleitor"))
	private Usuario eleitor;
	
	@Column(nullable = false)
	private boolean voto;
	
	public Voto() {
		
	}

	public Voto(Usuario eleitor, boolean voto) {
		this.eleitor = eleitor;
		this.voto = voto;
	}
	
	public boolean deletar() {
		PVoto persist = new PVoto();
		return persist.deletar(this);
	}
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public Usuario getEleitor() {
		return eleitor;
	}
	public void setEleitor(Usuario eleitor) {
		this.eleitor = eleitor;
	}
	public boolean getVoto() {
		return voto;
	}
	public void setVoto(boolean voto) {
		this.voto = voto;
	}
}
