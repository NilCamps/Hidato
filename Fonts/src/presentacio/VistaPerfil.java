package presentacio;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import domini.controladors.CtrlDomini;
import domini.model.Perfil;

public class VistaPerfil extends JPanel {

    private static final Color BG_TOP       = new Color(10, 20, 55);
    private static final Color BG_BOT       = new Color(3, 8, 25);
    private static final Color CARD_BG      = new Color(18, 32, 75);
    private static final Color CARD_HOV     = new Color(28, 50, 110);
    private static final Color CARD_BORDER  = new Color(60, 100, 180, 120);
    private static final Color TITOL        = Color.WHITE;
    private static final Color TEXT_SEC     = new Color(160, 185, 230);
    private static final Color BTN_NOR      = new Color(50, 120, 220);
    private static final Color BTN_HOV      = new Color(80, 155, 255);
    private static final Color BTN_SORT     = new Color(160, 45, 45);
    private static final Color BTN_SORT_HOV = new Color(205, 65, 65);
    private static final Color FIELD_BG     = new Color(12, 25, 65);
    private static final Color FIELD_BORDER = new Color(60, 100, 180);
    private static final Color ERR_COLOR    = new Color(255, 100, 100);
    private static final Color DIALOG_BG    = new Color(8, 18, 50);

    public VistaPerfil(AppFrame frame) {
        setOpaque(false);
        setLayout(new BorderLayout());

        // --- Capçalera ---
        JPanel top = new JPanel(new BorderLayout());
        top.setOpaque(false);
        top.setBorder(BorderFactory.createEmptyBorder(55, 0, 0, 0));

        JLabel lblTitol = new JLabel("Benvingut a HIDATO", SwingConstants.CENTER);
        lblTitol.setFont(new Font("Serif", Font.BOLD, 48));
        lblTitol.setForeground(TITOL);

        JLabel lblSub = new JLabel("Selecciona una opció per continuar", SwingConstants.CENTER);
        lblSub.setFont(new Font("SansSerif", Font.PLAIN, 17));
        lblSub.setForeground(TEXT_SEC);
        lblSub.setBorder(BorderFactory.createEmptyBorder(8, 0, 0, 0));

        top.add(lblTitol, BorderLayout.CENTER);
        top.add(lblSub, BorderLayout.SOUTH);
        add(top, BorderLayout.NORTH);

        // --- Targetes centrals ---
        JPanel cards = new JPanel(new GridLayout(1, 2, 50, 0));
        cards.setOpaque(false);
        cards.setBorder(BorderFactory.createEmptyBorder(55, 130, 40, 130));

        cards.add(crearTargeta(
            "＋", "Crear Perfil", "Registra't com a nou usuari",
            e -> mostrarDialegCrear(frame)
        ));
        cards.add(crearTargeta(
            "→", "Carregar Perfil", "Accedeix amb el teu compte existent",
            e -> mostrarDialegCarregar(frame)
        ));

        add(cards, BorderLayout.CENTER);

        // --- Botó de sortir ---
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottom.setOpaque(false);
        bottom.setBorder(BorderFactory.createEmptyBorder(0, 0, 45, 0));

        JButton btnSortir = VistaInicial.crearBoto("Sortir del Joc", BTN_SORT, BTN_SORT_HOV);
        btnSortir.addActionListener(e -> System.exit(0));
        bottom.add(btnSortir);

        add(bottom, BorderLayout.SOUTH);
    }

    // --- Targeta clicable ---
    private JPanel crearTargeta(String simbol, String titol, String desc, ActionListener onClick) {
        JPanel card = new JPanel() {
            private boolean hover = false;

            {
                addMouseListener(new MouseAdapter() {
                    @Override public void mouseEntered(MouseEvent e) { hover = true;  repaint(); }
                    @Override public void mouseExited(MouseEvent e)  { hover = false; repaint(); }
                    @Override public void mouseClicked(MouseEvent e) { onClick.actionPerformed(null); }
                });
                setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            }

            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(hover ? CARD_HOV : CARD_BG);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 28, 28);
                g2.setColor(CARD_BORDER);
                g2.setStroke(new BasicStroke(1.5f));
                g2.drawRoundRect(1, 1, getWidth() - 2, getHeight() - 2, 28, 28);
                g2.dispose();
            }

            @Override public boolean isOpaque() { return false; }
        };

        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(BorderFactory.createEmptyBorder(50, 30, 50, 30));

        JLabel lblSimbol = new JLabel(simbol, SwingConstants.CENTER);
        lblSimbol.setFont(new Font("SansSerif", Font.BOLD, 56));
        lblSimbol.setForeground(new Color(100, 160, 255));
        lblSimbol.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblTitol = new JLabel(titol, SwingConstants.CENTER);
        lblTitol.setFont(new Font("SansSerif", Font.BOLD, 26));
        lblTitol.setForeground(TITOL);
        lblTitol.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblDesc = new JLabel(desc, SwingConstants.CENTER);
        lblDesc.setFont(new Font("SansSerif", Font.PLAIN, 15));
        lblDesc.setForeground(TEXT_SEC);
        lblDesc.setAlignmentX(Component.CENTER_ALIGNMENT);

        card.add(Box.createVerticalGlue());
        card.add(lblSimbol);
        card.add(Box.createVerticalStrut(18));
        card.add(lblTitol);
        card.add(Box.createVerticalStrut(10));
        card.add(lblDesc);
        card.add(Box.createVerticalGlue());

        return card;
    }

    // --- Pop-up: Crear Perfil ---
    private void mostrarDialegCrear(AppFrame frame) {
        JDialog dialog = crearDialog(frame, "Crear Nou Perfil", 420, 600);

        JPanel panel = new JPanel();
        panel.setBackground(DIALOG_BG);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

        JLabel titol = new JLabel("Crear Nou Perfil");
        titol.setFont(new Font("Serif", Font.BOLD, 28));
        titol.setForeground(TITOL);
        titol.setAlignmentX(Component.CENTER_ALIGNMENT);

        JTextField fNom      = crearCamp("Nom");
        JTextField fCognoms  = crearCamp("Cognoms");
        JTextField fUsuari   = crearCamp("Nom d'usuari");
        JPasswordField fPass  = crearCampPassword("Contrasenya");
        JPasswordField fPass2 = crearCampPassword("Confirmar contrasenya");

        JLabel lblError = new JLabel(" ");
        lblError.setFont(new Font("SansSerif", Font.PLAIN, 13));
        lblError.setForeground(ERR_COLOR);
        lblError.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton btnCrear = VistaInicial.crearBoto("Crear", BTN_NOR, BTN_HOV);
        btnCrear.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnCrear.addActionListener(e -> {
            String nom     = fNom.getText().trim();
            String cognoms = fCognoms.getText().trim();
            String usuari  = fUsuari.getText().trim();
            String pass    = new String(fPass.getPassword());
            String pass2   = new String(fPass2.getPassword());

            if (nom.isEmpty() || cognoms.isEmpty() || usuari.isEmpty() || pass.isEmpty()) {
                lblError.setText("Omple tots els camps.");
                return;
            }
            if (!pass.equals(pass2)) {
                lblError.setText("Les contrasenyes no coincideixen.");
                return;
            }

            boolean ok = frame.getCtrlDomini().crearPerfil(nom, cognoms, usuari, pass, pass2);
            if (!ok) {
                lblError.setText("El nom d'usuari ja existeix.");
                return;
            }

            frame.getCtrlDomini().cargarPerfil(usuari, pass);
            frame.setUsuariActual(frame.getCtrlDomini().getUsuariActual());
            dialog.dispose();
            anarAMenu(frame);
        });

        panel.add(titol);
        panel.add(Box.createVerticalStrut(22));
        panel.add(crearEtiqueta("Nom"));
        panel.add(Box.createVerticalStrut(4));
        panel.add(fNom);
        panel.add(Box.createVerticalStrut(10));
        panel.add(crearEtiqueta("Cognoms"));
        panel.add(Box.createVerticalStrut(4));
        panel.add(fCognoms);
        panel.add(Box.createVerticalStrut(10));
        panel.add(crearEtiqueta("Nom d'usuari"));
        panel.add(Box.createVerticalStrut(4));
        panel.add(fUsuari);
        panel.add(Box.createVerticalStrut(10));
        panel.add(crearEtiqueta("Contrasenya"));
        panel.add(Box.createVerticalStrut(4));
        panel.add(fPass);
        panel.add(Box.createVerticalStrut(10));
        panel.add(crearEtiqueta("Confirmar contrasenya"));
        panel.add(Box.createVerticalStrut(4));
        panel.add(fPass2);
        panel.add(Box.createVerticalStrut(14));
        panel.add(lblError);
        panel.add(Box.createVerticalStrut(10));
        panel.add(btnCrear);

        dialog.setContentPane(panel);
        dialog.setVisible(true);
    }

    // --- Pop-up: Carregar Perfil ---
    private void mostrarDialegCarregar(AppFrame frame) {
        JDialog dialog = crearDialog(frame, "Accedir al Sistema", 400, 340);

        JPanel panel = new JPanel();
        panel.setBackground(DIALOG_BG);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

        JLabel titol = new JLabel("Accedir al Sistema");
        titol.setFont(new Font("Serif", Font.BOLD, 28));
        titol.setForeground(TITOL);
        titol.setAlignmentX(Component.CENTER_ALIGNMENT);

        JTextField fUsuari  = crearCamp("Nom d'usuari");
        JPasswordField fPass = crearCampPassword("Contrasenya");

        JLabel lblError = new JLabel(" ");
        lblError.setFont(new Font("SansSerif", Font.PLAIN, 13));
        lblError.setForeground(ERR_COLOR);
        lblError.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton btnAccedir = VistaInicial.crearBoto("Accedir", BTN_NOR, BTN_HOV);
        btnAccedir.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnAccedir.addActionListener(e -> {
            String usuari = fUsuari.getText().trim();
            String pass   = new String(fPass.getPassword());

            if (usuari.isEmpty() || pass.isEmpty()) {
                lblError.setText("Omple tots els camps.");
                return;
            }

            boolean ok = frame.getCtrlDomini().cargarPerfil(usuari, pass);
            if (!ok) {
                lblError.setText("Usuari o contrasenya incorrectes.");
                return;
            }

            frame.setUsuariActual(frame.getCtrlDomini().getUsuariActual());
            dialog.dispose();
            anarAMenu(frame);
        });

        panel.add(titol);
        panel.add(Box.createVerticalStrut(24));
        panel.add(crearEtiqueta("Nom d'usuari"));
        panel.add(Box.createVerticalStrut(4));
        panel.add(fUsuari);
        panel.add(Box.createVerticalStrut(12));
        panel.add(crearEtiqueta("Contrasenya"));
        panel.add(Box.createVerticalStrut(4));
        panel.add(fPass);
        panel.add(Box.createVerticalStrut(14));
        panel.add(lblError);
        panel.add(Box.createVerticalStrut(10));
        panel.add(btnAccedir);

        dialog.setContentPane(panel);
        dialog.setVisible(true);
    }

    private void anarAMenu(AppFrame frame) {
        VistaMenu menu = new VistaMenu(frame);
        frame.afegirPantalla(menu, "menu");
        frame.mostrarPantalla("menu");
    }

    // --- Helpers de components ---
    private JDialog crearDialog(AppFrame frame, String titol, int w, int h) {
        JDialog d = new JDialog(frame, titol, true);
        d.setSize(w, h);
        d.setLocationRelativeTo(frame);
        d.setResizable(false);
        d.getContentPane().setBackground(DIALOG_BG);
        return d;
    }

    private JTextField crearCamp(String placeholder) {
        JTextField f = new JTextField();
        estilitzarCamp(f);
        return f;
    }

    private JPasswordField crearCampPassword(String placeholder) {
        JPasswordField f = new JPasswordField();
        estilitzarCamp(f);
        return f;
    }

    private void estilitzarCamp(JTextField f) {
        f.setBackground(FIELD_BG);
        f.setForeground(Color.WHITE);
        f.setCaretColor(Color.WHITE);
        f.setFont(new Font("SansSerif", Font.PLAIN, 15));
        f.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(FIELD_BORDER, 1, true),
            BorderFactory.createEmptyBorder(7, 10, 7, 10)
        ));
        f.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
    }

    private JLabel crearEtiqueta(String text) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("SansSerif", Font.PLAIN, 13));
        l.setForeground(TEXT_SEC);
        l.setAlignmentX(Component.LEFT_ALIGNMENT);
        return l;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setPaint(new GradientPaint(0, 0, BG_TOP, 0, getHeight(), BG_BOT));
        g2.fillRect(0, 0, getWidth(), getHeight());
    }
}
