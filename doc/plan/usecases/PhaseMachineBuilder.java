package ooga.apiexamples;

import ooga.cardtable.ICell;
import ooga.rules.IPhase;
import ooga.rules.IPhaseMachine;
import ooga.rules.IRule;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents the use case for phase donors/receivers, providing a skeleton for implementation
 * Builds PhaseMachine implementation class.
 */
public class PhaseMachineBuilder implements XMLParser {

    public PhaseMachineBuilder() {

    }

    /**
     * Parsese through an XML skeleton to extract information essential to building a PhaseMachine
     *
     * @param xmlFile the file which should be parsed
     * @return the IPhaseMachine to govern game rules
     */
    public IPhaseMachine parseXML(File xmlFile) {
        List<ICell> myCells = new ArrayList<>();
        List<ICell> myPhases = new ArrayList<>();
        for (Node c: doc.getCells()) {
            myCells.add(new ICell(c));
        }
        for (Node p: doc.getPhases()) {
            IRule donor = new IRule(p.getAttribute("donorRegex"));
            IRule acceptor = new IRule(p.getAttribute("acceptorRegex"));
            IRule transfer = new IRule(p.getAttribute("transferRegex"));
            myPhases.add(new IPhase(donor, acceptor, transfer));
        }
        return new IPhaseMachine(myCells, myPhases);
    }

}
