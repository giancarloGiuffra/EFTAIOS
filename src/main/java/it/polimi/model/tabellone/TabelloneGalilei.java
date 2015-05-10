package it.polimi.model.tabellone;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import com.google.common.collect.Iterables;

import it.polimi.model.sector.*;

/**
 * Tabellone Galilei
 *
 */
public class TabelloneGalilei extends Tabellone {
	
    /**
     * Costruttore
     */
    TabelloneGalilei(){
        
        //hard-coded nomi dei settori a seconda del tipo indicato
        final String scialuppeNomi = "B02,V02,B13,V13";
        final String sicuriNomi = "A04,A05,A06,A09,A10,A11,A12,A13,B05,B10,C01,C14,D10,D14,E02,E12,F01,F10,G07,G12,G14,H01,H02,H03,H07,H14,I01,I09,I14,J01,J14,K02,K05,K09,K11,L02,L04,L09,L11,L14,M02,M05,M09,M11,N03,N14,O05,O09,O14,P01,P03,P04,P12,Q01,Q04,Q06,Q11,Q14,R01,R04,R06,R07,R08,R12,T07,T08,T14,U01,U05,U12,V01,V08,W03,W04,W05,W06,W10,W11,W12";
        final String inaccessibiliNomi = "A01,A07,A08,B07,D01,D04,D06,D07,E01,E07,E14,F12,F13,F14,H05,H10,H11,I06,I12,J07,J12,K07,K13,L07,M07,N07,O01,O03,O04,R10,R11,R14,S01,S03,S10,S11,S14,T01,T03,T04,T09,T10,V07,W01,W07,W08";
        final String baseUmanaNome = "L08";
        final String baseALienaNome = "L06";
        final String separatore = ",";
        
        //split dei nomi e creazione delle liste di nomi
        List<Settore> scialuppe = Settore.getListSettoriDiTipo(Arrays.asList(scialuppeNomi.split(separatore)), TipoSettore.SCIALUPPA);
        List<Settore> sicuri = Settore.getListSettoriDiTipo(Arrays.asList(sicuriNomi.split(separatore)), TipoSettore.SICURO);
        List<Settore> inaccessibili = Settore.getListSettoriDiTipo(Arrays.asList(inaccessibiliNomi.split(separatore)), TipoSettore.INACCESSIBILE);
        List<Settore> baseUmana = Settore.getListSettoriDiTipo(Arrays.asList(baseUmanaNome.split(separatore)), TipoSettore.BASE_HUMAN);
        List<Settore> baseAliena = Settore.getListSettoriDiTipo(Arrays.asList(baseALienaNome.split(separatore)), TipoSettore.BASE_ALIEN);
        
        //inserimento in una sola lista
        @SuppressWarnings("unchecked")
		final Iterable<Settore> settori = Iterables.unmodifiableIterable(
        		Iterables.concat(scialuppe,sicuri,inaccessibili,baseUmana, baseAliena));
        
        //inizializzazione della mappa sectors
        this.sectors = new HashMap<String,Settore>();
        for(Settore settore:settori){
        	this.sectors.put(settore.getNome(), settore);
        }
        
        //inizializzazione altri attributi
        this.baseUmana = baseUmana.get(0);
        this.baseAliena = baseAliena.get(0);
    }
}
