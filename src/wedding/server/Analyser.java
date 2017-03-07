package wedding.server;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;

import wedding.models.Couple;
import wedding.models.Person;

public class Analyser implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ArrayList<Couple> analyseBridesAndGrooms(ArrayList<Person> brides, ArrayList<Person> grooms) {

		HashMap<String, ArrayList<Person>> groomsSupplies = getSupplies(grooms);

		ArrayList<Couple> couples = new ArrayList<>();
		for (Person bride : brides) {
			ArrayList<String> brideDemands = bride.getDemands();
			ArrayList<String> brideDescription = bride.getPersonalDescription();
			brideDescription.sort(new Comparator<String>() {
				@Override
				public int compare(String o1, String o2) {
					return o1.compareTo(o2);
				}
			});
			
			try {
				HashSet<Person> currentIntersaction = new HashSet<>(groomsSupplies.get(brideDemands.get(0)));
				
				if (brideDemands.size() > 1) {
					for (int i = 1; i < brideDemands.size(); i++) {
						if (groomsSupplies.containsKey(brideDemands.get(i))) {
							currentIntersaction.retainAll(groomsSupplies.get(brideDemands.get(i)));
						}
					}
				} else {
					if (groomsSupplies.containsKey(brideDemands.get(0))) {
						currentIntersaction = new HashSet<>(groomsSupplies.get(brideDemands.get(0)));
					}
				}

				for (Person groom : currentIntersaction) {
					ArrayList<String> groomDemands = groom.getDemands();
					groomDemands.sort(new Comparator<String>() {
						@Override
						public int compare(String o1, String o2) {
							return o1.compareTo(o2);
						}
					});

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
			} catch(NullPointerException e) {
				System.out.println("No couple");
			}
		}
		return couples;
	}

	public HashMap<String, ArrayList<Person>> getSupplies(ArrayList<Person> persons) {

		ArrayList<String> valueArray;
		HashMap<String, ArrayList<Person>> valueMap = new HashMap<String, ArrayList<Person>>();

		for (int i = 0; i < persons.size(); i++) {
			valueArray = persons.get(i).getPersonalDescription();
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
