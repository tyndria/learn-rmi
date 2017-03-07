package wedding.client;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.StringTokenizer;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import wedding.libs.TextPrompt;
import wedding.models.Person;

public class AddPersonDialog extends JDialog {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Boolean isOk;
	JPanel mainPanel;
	Person person;
	Font font;
	JTextField textFieldName;
	JTextField textFieldSurname;
	JTextField textFieldBirthYear;
	JTextField textFieldDescription;
	JTextField textFieldDemand;

	public AddPersonDialog(JFrame owner) {
		super(owner, "Add person", true);
		this.setLayout(new BorderLayout());
		isOk = false;
		font = new Font("Verdana", Font.PLAIN, 20);

		mainPanel = new JPanel();
		mainPanel.setLayout(new GridLayout(5, 2));

		JPanel northPanel = new JPanel();
		northPanel.setLayout(new FlowLayout());

		textFieldName = makeTextField("Name: ");
		textFieldSurname = makeTextField("Surname: ");
		textFieldBirthYear = makeTextField("Birth year: ");
		textFieldDescription = makeTextField("Description: ");

		TextPrompt tp1 = new TextPrompt("Wordspace as delimeter", textFieldDescription);
		tp1.changeAlpha(0.5f);

		textFieldDemand = makeTextField("Demand: ");

		TextPrompt tp2 = new TextPrompt("Wordspace as delimeter", textFieldDemand);
		tp2.changeAlpha(0.5f);

		JButton buttonOk = new JButton("OK");
		northPanel.add(buttonOk);

		buttonOk.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				isOk = true;
				try {
					person = new Person(textFieldBirthYear.getText(), textFieldName.getText(),
							textFieldSurname.getText(), getArrayFromString(textFieldDescription.getText()),
							getArrayFromString(textFieldDemand.getText()), -1);
					clearTextFields();
				} catch (NumberFormatException e) {
					JOptionPane.showMessageDialog(AddPersonDialog.this, "Wrong format");
					isOk = false;
					AddPersonDialog.this.setVisible(false);
				}
				AddPersonDialog.this.setVisible(false);
			}
		});
		this.add(northPanel, BorderLayout.SOUTH);
		this.add(mainPanel, BorderLayout.CENTER);

		this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		this.addWindowListener(new java.awt.event.WindowAdapter() {
			@Override
			public void windowClosing(java.awt.event.WindowEvent windowEvent) {
				clearTextFields();
				dispose();
				;
			}
		});
	}

	private JTextField makeTextField(String s) {
		JTextField textField = new JTextField(20);
		textField.setFont(font);
		JLabel label = new JLabel(s, SwingConstants.RIGHT);
		label.setFont(font);

		mainPanel.add(label);
		mainPanel.add(textField);

		return textField;
	}

	private void clearTextFields() {
		ArrayList<JTextField> textFields = new ArrayList<JTextField>() {
			{
				add(textFieldName);
				add(textFieldSurname);
				add(textFieldBirthYear);
				add(textFieldDescription);
				add(textFieldDemand);
			}
		};
		for (JTextField textField : textFields) {
			textField.setText("");
		}
	}

	private ArrayList<String> getArrayFromString(String str) {
		ArrayList<String> arrayList = new ArrayList<>();
		StringTokenizer stringTokenizer = new StringTokenizer(str);
		while (stringTokenizer.hasMoreTokens()) {
			arrayList.add(stringTokenizer.nextToken());
		}
		return arrayList;
	}
}
