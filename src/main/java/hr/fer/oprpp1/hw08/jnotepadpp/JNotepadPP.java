package hr.fer.oprpp1.hw08.jnotepadpp;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import hr.fer.oprpp1.hw08.jnotepadpp.actions.CloseDocumentAction;
import hr.fer.oprpp1.hw08.jnotepadpp.actions.CreateNewDocumentAction;
import hr.fer.oprpp1.hw08.jnotepadpp.actions.HighlightBasedActions;
import hr.fer.oprpp1.hw08.jnotepadpp.actions.LanguageChangeAction;
import hr.fer.oprpp1.hw08.jnotepadpp.actions.LocalizableAction;
import hr.fer.oprpp1.hw08.jnotepadpp.actions.OpenDocumentAction;
import hr.fer.oprpp1.hw08.jnotepadpp.actions.QuitAction;
import hr.fer.oprpp1.hw08.jnotepadpp.actions.SaveAsDocumentAction;
import hr.fer.oprpp1.hw08.jnotepadpp.actions.SaveDocumentAction;
import hr.fer.oprpp1.hw08.jnotepadpp.actions.StatisticalInfoAction;
import hr.fer.oprpp1.hw08.jnotepadpp.components.LocalizableJToolBar;
import hr.fer.oprpp1.hw08.jnotepadpp.components.StatusBar;
import hr.fer.oprpp1.hw08.jnotepadpp.local.FormLocalizationProvider;
import hr.fer.oprpp1.hw08.jnotepadpp.local.ILocalizationProvider;
import hr.fer.oprpp1.hw08.jnotepadpp.local.LocalizationProvider;
import hr.fer.oprpp1.hw08.jnotepadpp.models.DefaultMultipleDocumentModel;
import hr.fer.oprpp1.hw08.jnotepadpp.models.MultipleDocumentAdapter;
import hr.fer.oprpp1.hw08.jnotepadpp.models.MultipleDocumentListener;
import hr.fer.oprpp1.hw08.jnotepadpp.models.MultipleDocumentModel;
import hr.fer.oprpp1.hw08.jnotepadpp.models.SingleDocumentAdapter;
import hr.fer.oprpp1.hw08.jnotepadpp.models.SingleDocumentListener;
import hr.fer.oprpp1.hw08.jnotepadpp.models.SingleDocumentModel;

public class JNotepadPP extends JFrame {
	
	private static final String APP_NAME = "JNotepad++";
	private static final LocalizationProvider lp = LocalizationProvider.getInstance();
	private FormLocalizationProvider flp;
	private MultipleDocumentModel model;
	
	private Action newDocAction;
	private Action openAction;
	private Action saveAction;
	private Action saveAsAction;
	private Action closeAction;
	private Action quitAction;
	
	private Action statInfoAction;
	
	private JTabbedPane jTabbedPane;
	
	
	private static final String[] languages = {"en", "hr"};
	
	private HighlightBasedActions hlba;
	private SingleDocumentListener titleChanger = new SingleDocumentAdapter() {
		@Override
		public void documentFilePathUpdated(SingleDocumentModel model) {
			String title = model == null ? APP_NAME :
				(model.getFullPathString() + " - " + APP_NAME);
			JNotepadPP.this.setTitle(title);
		};
	};
	
	private static class MenuAction extends LocalizableAction {
		
		public MenuAction(ILocalizationProvider provider, String nameKey) {
			super(provider);
			this.putLocalizedValue(Action.NAME, nameKey);
		}
		@Override
		public void actionPerformed(ActionEvent e) {}
		
		
	}
	
	public JNotepadPP() {
		flp = new FormLocalizationProvider(lp, this);
		this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		this.setSize(900, 600);
		this.setLocationRelativeTo(null);
		this.initGUI();
	}
	
	private void initGUI() {
		Container cp = this.getContentPane();
		cp.setLayout(new BorderLayout());
		
		DefaultMultipleDocumentModel defaultModel = new DefaultMultipleDocumentModel(this, flp);
		this.model = defaultModel;
		this.jTabbedPane = defaultModel;
		MultipleDocumentListener mdl = new MultipleDocumentAdapter() {
			@Override
			public void currentDocumentChanged(SingleDocumentModel previousModel, SingleDocumentModel currentModel) {
				if (previousModel != null)
					previousModel.removeSingleDocumentListener(titleChanger);
				if (currentModel != null)
					currentModel.addSingleDocumentListener(titleChanger);
				titleChanger.documentFilePathUpdated(currentModel);
			}
		};
		mdl.currentDocumentChanged(null, null);
		this.model.addMultipleDocumentListener(mdl);
		StatusBar statusBar = new StatusBar(flp, model);
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowOpened(WindowEvent e) {
				statusBar.startClock();
			}
			
			@Override
			public void windowClosed(WindowEvent e) {
				statusBar.stopClock();
			}
			
			@Override
			public void windowClosing(WindowEvent e) {
				quitAction.actionPerformed(null);
			}
		});
		JPanel centerPnl = new JPanel(new BorderLayout());
		centerPnl.add(jTabbedPane, BorderLayout.CENTER);
		centerPnl.add(statusBar, BorderLayout.PAGE_END);
		cp.add(centerPnl, BorderLayout.CENTER);
		
		this.createActions();
		this.createMenus();
		this.createToolBar();
		
	}
	
	private void createActions() {
		this.newDocAction = new CreateNewDocumentAction(this, flp, model);
		this.openAction = new OpenDocumentAction(this, flp, model);
		this.saveAction = new SaveDocumentAction(this, flp, model);
		this.saveAsAction = new SaveAsDocumentAction(this, flp, model);
		this.closeAction = new CloseDocumentAction(this, flp, model);
		this.quitAction = new QuitAction(this, flp, model);
		this.statInfoAction = new StatisticalInfoAction(this, flp, model);
	}
	
	private void createMenus() {
		JMenuBar menuBar = new JMenuBar();

		JMenu fileMenu = new JMenu(new MenuAction(flp, "file"));
		menuBar.add(fileMenu);
		fileMenu.add(new JMenuItem(newDocAction));
		fileMenu.add(new JMenuItem(openAction));
		fileMenu.addSeparator();
		fileMenu.add(new JMenuItem(saveAction));
		fileMenu.add(new JMenuItem(saveAsAction));
		fileMenu.addSeparator();
		fileMenu.add(new JMenuItem(closeAction));
		fileMenu.addSeparator();
		fileMenu.add(new JMenuItem(quitAction));
		
		JMenu editMenu = new JMenu(new MenuAction(flp, "edit"));
		menuBar.add(editMenu);
		hlba = new HighlightBasedActions(this, flp, model);
		editMenu.add(new JMenuItem(hlba.getCopyAction()));
		editMenu.add(new JMenuItem(hlba.getCutAction()));
		editMenu.add(new JMenuItem(hlba.getPasteAction()));
		
		JMenu toolsMenu = new JMenu(new MenuAction(flp, "tools"));
		menuBar.add(toolsMenu);
		toolsMenu.add(new JMenuItem(statInfoAction));
		JMenu changeCaseSubMenu = new JMenu(hlba.getChangeCaseAction());
		changeCaseSubMenu.add(new JMenuItem(hlba.getToLowerCaseAction()));
		changeCaseSubMenu.add(new JMenuItem(hlba.getToUpperCaseAction()));
		changeCaseSubMenu.add(new JMenuItem(hlba.getInvertCaseAction()));
		toolsMenu.add(changeCaseSubMenu);
		JMenu sortMenu = new JMenu(hlba.getSortLinesAction());
		sortMenu.add(new JMenuItem(hlba.getSortLinesAscendingAction()));
		sortMenu.add(new JMenuItem(hlba.getSortLinesDescendingAction()));
		toolsMenu.add(sortMenu);
		toolsMenu.add(new JMenuItem(hlba.getUniqueLinesAction()));
		
		JMenu langMenu = new JMenu(new MenuAction(flp, "language"));
		menuBar.add(langMenu);
		for (String lang : languages)
			langMenu.add(new JMenuItem(new LanguageChangeAction(flp, lp, lang)));
		
		this.setJMenuBar(menuBar);
	}
	
	public Action getSaveAction() {
		return saveAction;
	}
	
	public JTabbedPane getJTabbedPane() {
		return jTabbedPane;
	}
	
	private void createToolBar() {
		JToolBar toolBar = new LocalizableJToolBar(flp, "toolbar");
		toolBar.setFloatable(true);
		
		toolBar.add(new JButton(newDocAction));
		toolBar.add(new JButton(openAction));
		toolBar.addSeparator();
		toolBar.add(new JButton(saveAction));
		toolBar.add(new JButton(saveAsAction));
		toolBar.addSeparator();
		toolBar.add(new JButton(closeAction));
		toolBar.addSeparator();
		toolBar.add(new JButton(hlba.getCopyAction()));
		toolBar.add(new JButton(hlba.getCutAction()));
		toolBar.add(new JButton(hlba.getPasteAction()));
		toolBar.addSeparator();
		toolBar.add(new JButton(statInfoAction));
		toolBar.add(new JButton(quitAction));
		
		Container cp = this.getContentPane();
		cp.add(toolBar, BorderLayout.PAGE_START);
	}
	
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			JNotepadPP frame = new JNotepadPP();
			frame.setVisible(true);
		});
	}
	
}
