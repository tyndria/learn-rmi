package wedding.server;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;

import wedding.models.Couple;
import wedding.models.Person;

public class Analyser implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * Returns the list of 'the most suitable' couples 
	 * based on women and men demands and supplies.
	 * 
	 * @param brides list of brides to make suitable couple
	 * @param grooms list of grooms to make suitable couple
	 * @return 		 the list of the most suitable couple
	 * @exception NullPointerException when there is no suitable couples
	 * @see java.lang.NullPointerException
	 * 
	 */
	public ArrayList<Couple> analyseBridesAndGrooms(ArrayList<Person> brides, ArrayList<Person> grooms)
			throws NullPointerException {

		HashMap<String, ArrayList<Person>> groomsSupplies = getSupplies(grooms);

		ArrayList<Couple> couples = new ArrayList<>();
		for (Person bride : brides) {
			ArrayList<String> brideDemands = bride.getDemands();
			ArrayList<String> brideDescription = bride.getPropositions();
			brideDescription.sort(Comparator.naturalOrder());

			HashSet<Person> currentIntersection = new HashSet<>();
			
			if (brideDemands.size() > 1) {
				for (int i = 1; i < brideDemands.size(); i++) {
					if (groomsSupplies.containsKey(brideDemands.get(i))) {
						currentIntersection.retainAll(groomsSupplies.get(brideDemands.get(i)));
					}
				}
			} else {
				if (groomsSupplies.containsKey(brideDemands.get(0))) {
					currentIntersection = new HashSet<>(groomsSupplies.get(brideDemands.get(0)));
				}
			}

			if (currentIntersection.size() > 0) {
				for (Person groom : currentIntersection) {
					ArrayList<String> groomDemands = groom.getDemands();
					groomDemands.sort(Comparator.naturalOrder());

					if (groomDemands.size() < brideDescription.size()) {
						boolean isSuit = true;
						for (String demand : groomDemands) {
							if (!brideDescription.contains(demand)) {
								isSuit = false;
							}
						}
						if (isSuit) {
							couples.add(new Couple(bride, groom));
							break;
						}
					} else if (groomDemands.equals(brideDescription)) {
						couples.add(new Couple(bride, groom));
						break;
					}
				}
			}
		}
		return couples;
	}

	public HashMap<String, ArrayList<Person>> getSupplies(ArrayList<Person> persons) {

		ArrayList<String> valueArray;
		HashMap<String, ArrayList<Person>> valueMap = new HashMap<String, ArrayList<Person>>();

		for (int i = 0; i < persons.size(); i++) {
			valueArray = persons.get(i).getPropositions();
			for (int j = 0; j < valueArray.size(); j++) {
				if (!valueMap.containsKey(valueArray.get(j))) {
					ArrayList<Person> personsArray = new ArrayList<>();
					personsArray.add(persons.get(i));
					valueMap.put(valueArray.get(j), personsArray);
				} else {
					ArrayList<Person> personsArray = valueMap.get(valueArray.get(j));
					personsArray.add(persons.get(i));
				}
			}
		}

		return valueMap;
	}
}
