/**
 * Sample Skeleton for 'Scene.fxml' Controller Class
 */

package it.polito.tdp.crimes;

import java.net.URL;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.crimes.model.Model;
import it.polito.tdp.crimes.model.Vicino;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FXMLController {
	private Model model;

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="boxAnno"
    private ComboBox<Year> boxAnno; // Value injected by FXMLLoader

    @FXML // fx:id="boxMese"
    private ComboBox<Month> boxMese; // Value injected by FXMLLoader

    @FXML // fx:id="boxGiorno"
    private ComboBox<Integer> boxGiorno; // Value injected by FXMLLoader

    @FXML // fx:id="btnCreaReteCittadina"
    private Button btnCreaReteCittadina; // Value injected by FXMLLoader

    @FXML // fx:id="btnSimula"
    private Button btnSimula; // Value injected by FXMLLoader

    @FXML // fx:id="txtN"
    private TextField txtN; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML
    void doCreaReteCittadina(ActionEvent event) {
    	txtResult.clear();
    	Year anno = boxAnno.getValue();
    	if(anno == null) {
    		txtResult.appendText("Devi selezionare un anno!");
    		return;
    	}
    	model.creaGrafo(anno.getValue());
    	txtResult.appendText(String.format("Grafo creato con %d vertici e %d archi!\n", model.getVertici().size(), model.getNArchi()));
    	
    	txtResult.appendText("Lista vicini\n");
    	for(Integer d: model.getVertici()) {
    		txtResult.appendText(String.format("Vicino del distretto %d:\n", d));
    		List<Vicino> list = model.getVicini(d);
    		for(Vicino v: list)
    			txtResult.appendText(String.format("Distretto: %d. Distanza: %f\n", v.getDistretto(), v.getDistanzaMedia()));
    	}
    }

    @FXML
    void doSimula(ActionEvent event) {
    	txtResult.clear();
    	Year anno = boxAnno.getValue();
    	if(anno == null) {
    		txtResult.appendText("Devi selezionare un anno!");
    		return;
    	}
    	Month mese = boxMese.getValue();
    	if(mese == null) {
    		txtResult.appendText("Devi selezionare un mese!");
    		return;
    	}
    	Integer giorno = boxGiorno.getValue();
    	if(giorno == null) {
    		txtResult.appendText("Devi selezionare un giorno!");
    		return;
    	}
    	try {
    		LocalDate.of(anno.getValue(), mese, giorno);
    	} catch (DateTimeException e) {
        	this.txtResult.clear();
    		txtResult.appendText("Data non corretta\n");
    	}
    	Integer N;
    	try {
    		N = Integer.parseInt(txtN.getText());
    	} catch(NumberFormatException e) {
    		txtResult.appendText("Devi mettere N!");
    		return;
    	}
    	if(!(N>0 && N<11)) {
    		txtResult.appendText("N deve essere compreso tra 1 e 10!");
    		return;
    	}
    	this.model.simula(anno.getValue(), mese.getValue(), giorno, N);
    	txtResult.appendText("Simulo con " + N + " agenti");
    	txtResult.appendText("\nCRIMINI MAL GESTITI: " + this.model.getMalGestiti());
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert boxAnno != null : "fx:id=\"boxAnno\" was not injected: check your FXML file 'Scene.fxml'.";
        assert boxMese != null : "fx:id=\"boxMese\" was not injected: check your FXML file 'Scene.fxml'.";
        assert boxGiorno != null : "fx:id=\"boxGiorno\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnCreaReteCittadina != null : "fx:id=\"btnCreaReteCittadina\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnSimula != null : "fx:id=\"btnSimula\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtN != null : "fx:id=\"txtN\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";

    }
    
    public void setModel(Model model) {
    	this.model = model;
    	setBox();
    }

	private void setBox() {
		List<Integer> list = model.getYears();
		Collections.sort(list);
		for(Integer i: list)
			this.boxAnno.getItems().add(Year.of(i));
		
		List<Integer> mesi = model.getMonths();
		Collections.sort(list);
		for(Integer i: mesi)
			this.boxMese.getItems().add(Month.of(i));
		
		List<Integer> giorni = model.getDays();
		Collections.sort(giorni);
		this.boxGiorno.getItems().addAll(giorni);
		
	}
}
