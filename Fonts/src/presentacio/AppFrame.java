package presentacio;

import javax.swing.*;
import java.awt.*;
import domini.controladors.CtrlDomini;
import domini.model.Perfil;

public class AppFrame extends JFrame {

    private final CardLayout cardLayout;
    private final JPanel mainPanel;
    private final CtrlDomini ctrlDomini;

    private Perfil usuariActual;
    private VistaMenu vistaMenu;

    public AppFrame() {
        ctrlDomini = new CtrlDomini();

        setTitle("HIDATO");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);
        setResizable(false);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        VistaInicial vistaInicial = new VistaInicial(this);
        mainPanel.add(vistaInicial, "inici");

        add(mainPanel);
        cardLayout.show(mainPanel, "inici");
    }

    public void mostrarPantalla(String nom) {
        if ("menu".equals(nom) && vistaMenu != null)
            vistaMenu.actualitzarEstadistiques(this);
        cardLayout.show(mainPanel, nom);
    }

    public void afegirPantalla(JPanel pantalla, String nom) {
        mainPanel.add(pantalla, nom);
        if (pantalla instanceof VistaMenu)
            vistaMenu = (VistaMenu) pantalla;
    }

    public CtrlDomini getCtrlDomini() {
        return ctrlDomini;
    }

    public Perfil getUsuariActual() {
        return usuariActual;
    }

    public void setUsuariActual(Perfil p) {
        usuariActual = p;
    }
}
