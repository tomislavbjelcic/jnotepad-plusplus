package hr.fer.oprpp1.hw08.jnotepadpp.components;

import java.awt.FlowLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Objects;

import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import hr.fer.oprpp1.hw08.jnotepadpp.icons.SaveIcons;
import hr.fer.oprpp1.hw08.jnotepadpp.models.SingleDocumentAdapter;
import hr.fer.oprpp1.hw08.jnotepadpp.models.SingleDocumentListener;
import hr.fer.oprpp1.hw08.jnotepadpp.models.SingleDocumentModel;

public class TabComponent extends JPanel {
	
	private JTabbedPane parent;
	private SingleDocumentModel model;
	private JLabel saveIconLabel;
	private JLabel fileNameLabel;
	private CloseTabButton closeBtn;
	
	public TabComponent(JTabbedPane parent, SingleDocumentModel model, Action closeBtnAction) {
		this.model = Objects.requireNonNull(model);
		this.parent = Objects.requireNonNull(parent);
		saveIconLabel = new JLabel();
		fileNameLabel = new JLabel();
		closeBtn = new CloseTabButton(closeBtnAction);
		initComponent();
		initListeners();
	}
	
	private void initComponent() {
		this.updateIcon(model);
		this.updatePathLabel(model);
		this.setLayout(new FlowLayout(FlowLayout.LEFT, 3, 3));
		this.add(saveIconLabel);
		this.add(fileNameLabel);
		this.add(closeBtn);
		this.setOpaque(false);
	}
	
	private void initListeners() {
		SingleDocumentListener l = new SingleDocumentAdapter() {

			@Override
			public void documentModifyStatusUpdated(SingleDocumentModel model) {
				updateIcon(model);
			}

			@Override
			public void documentFilePathUpdated(SingleDocumentModel model) {
				updatePathLabel(model);
			}
			
		};
		model.addSingleDocumentListener(l);
		MouseListener ml = new MouseAdapter() {

	        @Override
	        public void mouseClicked(MouseEvent e) {
	           int index =  parent.indexOfTabComponent(TabComponent.this);
	           parent.setSelectedIndex(index);
	        }

	    };
	    fileNameLabel.addMouseListener(ml);
	    // zbog toga što sam koristio vlastitu komponentu za prikaz taba
	    // postavljanje tooltipa za JLabel konzumira mouse evente
	    // pa klikom na taj JLabel se tab ne bi promijenio, zato je potrebno
	    // dodati mouselistener na taj JLabel
	}
	
	private void updateIcon(SingleDocumentModel model) {
		ImageIcon saveIcon = getIconFromModel(model);
		saveIconLabel.setIcon(saveIcon);
	}
	
	private void updatePathLabel(SingleDocumentModel model) {
		String filePathStr = model.getFileName();
		fileNameLabel.setText(filePathStr);
		String fullPathStr = model.getFullPathString();
		fileNameLabel.setToolTipText(fullPathStr);
	}
	
	public void setCloseTabAction(Action ac) {
		closeBtn.setAction(ac);
	}
	
	private static ImageIcon getIconFromModel(SingleDocumentModel model) {
		ImageIcon saveIcon = model.isModified() ? SaveIcons.RED_SAVE_ICON : SaveIcons.GREEN_SAVE_ICON;
		return saveIcon;
	}
}
