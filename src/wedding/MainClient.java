package wedding;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

/*7.	«Электронная сваха». На основе анализа файла возможных женихов и невест, 
где хранится информация о себе и требования к партнеру, выдать список «идеальных» пар.*/

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

import javax.swing.DefaultListModel;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import wedding.client.AddPersonDialog;
import wedding.models.Couple;
import wedding.models.Person;
import wedding.models.Request;
import wedding.server.ServerAssistantI;

public class MainClient implements NetworkConstants {
	ServerAssistantI serverAssistant;

	public static void main(String[] args) throws FileNotFoundException, ClassNotFoundException, IOException, SQLException {
		MainClient mainClient = new MainClient();
		
		try {
			Registry registry = LocateRegistry.getRegistry(HOST, REGISTRY_PORT);
			String addres = "rmi://" + HOST + ":" + APP_PORT + "/ServerAssistantI";
			mainClient.serverAssistant = (ServerAssistantI)registry.lookup(addres);
			FrameAssistant frameAssistant = new FrameAssistant("Wedding!", mainClient.serverAssistant);
			frameAssistant.setSize(1300, 900);
			frameAssistant.setVisible(true);
		} catch (RemoteException re) {
		    System.out.println("RemoteException: "+re);
		} catch (NotBoundException nbe) {
		    System.out.println("NotBoundException: "+nbe);
		} catch (MalformedURLException mfe) {
		    System.out.println("NotBoundException: "+mfe);
		}
	}
}

class FrameAssistant extends JFrame implements ActionListener {
	DefaultListModel<Person> brideListModel, groomListModel;
	DefaultListModel<Couple> coupleListModel;
	JList<Person> brideJList, groomJList;
	JList<Couple> coupleJList;
	java.util.List<Person> selectedBrides, selectedGrooms;
	JMenuItem checkItem, brideAddButton, groomAddButton, brideSaveButton, groomSaveButton,
			brideDeleteButton, groomDeleteButton;
	Font font;
	AddPersonDialog dialog;
	JPanel checkPanel, textPanel;
	JTextArea textArea, wordWrapArea;
	ServerAssistantI serverAssistant;
	Boolean isBrideListChanged, isGroomListChanged, isNewFileCreated;
	ArrayList<Request> requests = new ArrayList<>();

	public FrameAssistant(String s, ServerAssistantI serverAssistantI) throws FileNotFoundException, ClassNotFoundException, IOException, SQLException {
		super(s);
		this.serverAssistant = serverAssistantI;

		getData();
		selectedBrides = new ArrayList<Person>();
		selectedGrooms = new ArrayList<Person>();

		font = new Font("Verdana", Font.PLAIN, 20);
		this.setLayout(new GridLayout(3, 1));
		this.setJMenuBar(createMenuBar());

		this.getContentPane().add(createListPanel("Brides", brideJList));
		this.getContentPane().add(createListPanel("Ideal couples ^^", coupleJList));
		this.getContentPane().add(createListPanel("Grooms", groomJList));

		closeWindow();
	}

	private void getData() throws FileNotFoundException, ClassNotFoundException, IOException, SQLException {
		ArrayList<Person> brides = serverAssistant.doRequest(new Request("get", "bride", null));
		ArrayList<Person> grooms = serverAssistant.doRequest(new Request("get", "groom", null));

		isBrideListChanged = false;
		isGroomListChanged = false;
		isNewFileCreated = false;

		coupleListModel = new DefaultListModel<Couple>();
		brideListModel = new DefaultListModel<Person>();
		groomListModel = new DefaultListModel<Person>();

		for (Person bride : brides) {
			brideListModel.addElement(bride);
		}
		for (Person groom : grooms) {
			groomListModel.addElement(groom);
		}
		for (Couple couple : serverAssistant.getBestCouples(brides, grooms)) {
			coupleListModel.addElement(couple);
		}

		brideJList = new JList<Person>(brideListModel);
		groomJList = new JList<Person>(groomListModel);
		coupleJList = new JList<Couple>(coupleListModel);

		selectionProcess();
		listModelChangingProcess();
	}

	private void listModelChangingProcess() {

		brideListModel.addListDataListener(new ListDataListener() {

			@Override
			public void intervalRemoved(ListDataEvent e) {
				isBrideListChanged = true;
			}

			@Override
			public void intervalAdded(ListDataEvent e) {
				isBrideListChanged = true;
			}

			@Override
			public void contentsChanged(ListDataEvent e) {
				isBrideListChanged = true;
			}
		});

		groomListModel.addListDataListener(new ListDataListener() {

			@Override
			public void intervalRemoved(ListDataEvent e) {
				isGroomListChanged = true;
			}

			@Override
			public void intervalAdded(ListDataEvent e) {
				isGroomListChanged = true;
			}

			@Override
			public void contentsChanged(ListDataEvent e) {
				isGroomListChanged = true;
			}
		});
	}

	private void selectionProcess() {
		brideJList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		groomJList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

		brideJList.addListSelectionListener(new ListSelectionListener() {

			@Override
			public void valueChanged(ListSelectionEvent e) {
				if (!e.getValueIsAdjusting()) {
					selectedBrides = brideJList.getSelectedValuesList();
				}
			}
		});

		groomJList.addListSelectionListener(new ListSelectionListener() {

			@Override
			public void valueChanged(ListSelectionEvent e) {
				if (!e.getValueIsAdjusting()) {
					selectedGrooms = groomJList.getSelectedValuesList();
				}
			}
		});
	}

	private void closeWindow() {
		this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		this.addWindowListener(new java.awt.event.WindowAdapter() {
			@Override
			public void windowClosing(java.awt.event.WindowEvent windowEvent) {
				try {
					if (isBrideListChanged) {
						showSaveDialog("Do you want to save brides?", "bride");
					}
					if (isGroomListChanged) {
						showSaveDialog("Do you want to save grooms?", "groom");
					}
				} catch(RemoteException e) {
					System.out.println(e.getClass() + ": " + e.getMessage());
				}
				System.exit(0);
			}
		});
	}
	
	private void showSaveDialog(String dialogText, String tableName) throws RemoteException {
		int reply = JOptionPane.showConfirmDialog(null, dialogText,
				"Really Closing?", JOptionPane.YES_NO_OPTION);
		if (reply == JOptionPane.YES_OPTION) {
			doRequests(tableName);
		}
	}

	private JPanel createListPanel(String label, JList list) {
		textPanel = new JPanel(new BorderLayout());

		JLabel textLabel = new JLabel(label);
		textLabel.setFont(font);
		textPanel.add(textLabel, BorderLayout.NORTH);

		list.setFont(font);

		JScrollPane areaScrollPane = new JScrollPane(list, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		areaScrollPane.setPreferredSize(new Dimension(700, 500));
		textPanel.add(areaScrollPane);

		return textPanel;
	}

	private JMenuBar createMenuBar() {
		JMenuBar menuBar = new JMenuBar();
		menuBar.setFont(font);

		JMenu analyseMenu = new JMenu("Analyse");
		analyseMenu.setFont(font);

		JMenu brideMenu = new JMenu("Bride");
		brideMenu.setFont(font);

		JMenu groomMenu = new JMenu("Groom");
		groomMenu.setFont(font);

		checkItem = createMenuItem("Check", font);
		analyseMenu.add(checkItem);

		brideAddButton = createMenuItem("Add", font);
		brideSaveButton = createMenuItem("Save", font);
		brideDeleteButton = createMenuItem("Delete", font);
		brideMenu.add(brideAddButton);
		brideMenu.add(brideDeleteButton);
		brideMenu.add(brideSaveButton);

		groomAddButton = createMenuItem("Add", font);
		groomDeleteButton = createMenuItem("Delete", font);
		groomSaveButton = createMenuItem("Save", font);
		groomMenu.add(groomAddButton);
		groomMenu.add(groomDeleteButton);
		groomMenu.add(groomSaveButton);

		menuBar.add(brideMenu);
		menuBar.add(groomMenu);
		menuBar.add(analyseMenu);

		return menuBar;
	}

	private JMenuItem createMenuItem(String s, Font font) {
		JMenuItem menuItem = new JMenuItem(s);
		menuItem.addActionListener(this);
		menuItem.setFont(font);

		return menuItem;
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		try {
			if (e.getSource() == brideAddButton) {
				openDialog();
				if (dialog.getStatus()) {
					Person newPerson = dialog.getPerson();
					brideListModel.addElement(newPerson);
					requests.add(new Request("post", "bride", newPerson));
				}
			} else if (e.getSource() == groomAddButton) {
				openDialog();
				if (dialog.getStatus()) {
					Person newPerson = dialog.getPerson();
					groomListModel.addElement(newPerson);
					requests.add(new Request("post", "groom", newPerson));
				}
			} else if (e.getSource() == checkItem) {
				coupleListModel.clear();
				for (Couple couple : serverAssistant.getBestCouples(Collections.list(brideListModel.elements()),
						Collections.list(groomListModel.elements()))) {
					coupleListModel.addElement(couple);
				}
			} else if (e.getSource() == brideSaveButton) {
				doRequests("bride");
			} else if (e.getSource() == groomSaveButton) {
				doRequests("groom");
			} else if (e.getSource() == brideDeleteButton) {
				if (selectedBrides.size() != 0) {
					for (Person bride : selectedBrides) {
						brideListModel.removeElement(bride);
						requests.add(new Request("delete", "bride", bride));
					}
				}
				selectedBrides.clear();
			} else if (e.getSource() == groomDeleteButton) {
				if (selectedGrooms.size() != 0) {
					for (Person groom : selectedGrooms) {
						groomListModel.removeElement(groom);
						requests.add(new Request("delete", "groom", groom));
					}
				}
				selectedGrooms.clear();
			} 
		} catch (RemoteException e1) {
			System.out.println(e1.getMessage());
		}
	}

	private void openDialog() {
		if (dialog == null)
			dialog = new AddPersonDialog(this);
		dialog.setSize(300, 300);
		dialog.setVisible(true);
	}

	private void doRequests(String tableName) throws RemoteException {
		Iterator<Request> iterator = requests.iterator();
		while (iterator.hasNext()) {
			Request request = iterator.next();
			if (request.getTableName().equals(tableName)) {
				serverAssistant.doRequest(request);
				iterator.remove();
			}
		}
		
		if (tableName.equals("bride")) {
			isBrideListChanged = false;
		} else {
			isGroomListChanged = false;
		}
		
		JOptionPane.showMessageDialog(null, "Save!");
	}
}
