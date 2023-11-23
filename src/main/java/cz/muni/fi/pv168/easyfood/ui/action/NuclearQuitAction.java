package cz.muni.fi.pv168.easyfood.ui.action;

import cz.muni.fi.pv168.easyfood.storage.sql.db.DatabaseManager;
import cz.muni.fi.pv168.easyfood.ui.resources.Icons;

import javax.swing.AbstractAction;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

/**
 * Action that terminates the application and deletes a database.
 */
public class NuclearQuitAction extends AbstractAction {

    private final DatabaseManager databaseManager;

    public NuclearQuitAction() {
        this(DatabaseManager.createProductionInstance());
    }

    public NuclearQuitAction(DatabaseManager databaseManager) {
        super("Nuclear Quit", Icons.NUCLEAR_QUIT_ICON);
        this.databaseManager = databaseManager;

        putValue(SHORT_DESCRIPTION, "Terminates the application and deletes a database");
        putValue(MNEMONIC_KEY, KeyEvent.VK_Q);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        databaseManager.destroySchema();

        System.exit(0);
    }
}
