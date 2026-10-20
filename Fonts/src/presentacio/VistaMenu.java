package presentacio;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import domini.controladors.CtrlDomini;
import domini.model.Perfil;
import domini.model.Estadistiques;

public class VistaMenu extends JPanel {

    private static final Color BG_TOP        = new Color(10, 20, 55);
    private static final Color BG_BOT        = new Color(3, 8, 25);
    private static final Color PANEL_ESQ     = new Color(7, 15, 42);
    private static final Color SEP_COLOR     = new Color(50, 85, 160);
    private static final Color TITOL         = Color.WHITE;
    private static final Color TEXT_SEC      = new Color(160, 185, 230);
    private static final Color TEXT_USUARI   = new Color(120, 155, 215);
    private static final Color CARD_BG       = new Color(16, 30, 72);
    private static final Color CARD_BORDER   = new Color(50, 85, 160, 100);
    private static final Color ACCENT        = new Color(100, 160, 255);
    private static final Color BTN_NOR       = new Color(50, 120, 220);
    private static final Color BTN_HOV       = new Color(80, 155, 255);
    private static final Color BTN_SORT      = new Color(160, 45, 45);
    private static final Color BTN_SORT_HOV  = new Color(205, 65, 65);
    private static final Color FIELD_BG      = new Color(12, 25, 65);
    private static final Color FIELD_BORDER  = new Color(60, 100, 180);
    private static final Color ERR_COLOR     = new Color(255, 100, 100);
    private static final Color OK_COLOR      = new Color(100, 220, 130);
    private static final Color DIALOG_BG     = new Color(8, 18, 50);

    private JLabel lblNomVal;
    private JLabel lblJugatsVal;
    private JLabel lblCompletatsVal;
    private JLabel lblMitjanaVal;
    private JLabel lblMillorVal;

    public VistaMenu(AppFrame frame) {
        setOpaque(false);
        setLayout(new BorderLayout());

        JPanel contingut = new JPanel(new GridLayout(1, 2, 0, 0));
        contingut.setOpaque(false);

        contingut.add(crearPanellEsquerre(frame));
        contingut.add(crearPanellDret(frame));

        add(contingut, BorderLayout.CENTER);

        actualitzarEstadistiques(frame);
    }

    // ── PANELL ESQUERRE: estadístiques ──────────────────────────────────────

    private JPanel crearPanellEsquerre(AppFrame frame) {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setPaint(new GradientPaint(0, 0, PANEL_ESQ, 0, getHeight(), new Color(4, 10, 30)));
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.setColor(SEP_COLOR);
                g2.setStroke(new BasicStroke(1f));
                g2.drawLine(getWidth() - 1, 0, getWidth() - 1, getHeight());
                g2.dispose();
            }
            @Override public boolean isOpaque() { return false; }
        };
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(55, 45, 40, 45));

        // Capçalera usuari
        JLabel lblHola = new JLabel("Estadístiques");
        lblHola.setFont(new Font("Serif", Font.BOLD, 32));
        lblHola.setForeground(TITOL);
        lblHola.setAlignmentX(Component.LEFT_ALIGNMENT);

        lblNomVal = new JLabel();
        lblNomVal.setFont(new Font("SansSerif", Font.PLAIN, 15));
        lblNomVal.setForeground(TEXT_USUARI);
        lblNomVal.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel separador = crearLinia();
        separador.setAlignmentX(Component.LEFT_ALIGNMENT);

        panel.add(lblHola);
        panel.add(Box.createVerticalStrut(4));
        panel.add(lblNomVal);
        panel.add(Box.createVerticalStrut(20));
        panel.add(separador);
        panel.add(Box.createVerticalStrut(28));

        // Targetes d'estadística
        String[][] stats = {
            {"Partides Jugades",   "lblJugats"},
            {"Partides Completades", "lblCompletats"},
            {"Puntuació Mitjana",  "lblMitjana"},
            {"Millor Puntuació",   "lblMillor"}
        };

        JLabel[] valors = new JLabel[4];
        for (int i = 0; i < 4; i++) {
            JLabel[] refs = new JLabel[1];
            JPanel card = crearStatCard(stats[i][0], refs);
            valors[i] = refs[0];
            card.setAlignmentX(Component.LEFT_ALIGNMENT);
            panel.add(card);
            if (i < 3) panel.add(Box.createVerticalStrut(14));
        }

        lblJugatsVal    = valors[0];
        lblCompletatsVal = valors[1];
        lblMitjanaVal   = valors[2];
        lblMillorVal    = valors[3];

        panel.add(Box.createVerticalGlue());
        return panel;
    }

    private JPanel crearStatCard(String etiqueta, JLabel[] valorRef) {
        JPanel card = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(CARD_BG);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);
                g2.setColor(CARD_BORDER);
                g2.setStroke(new BasicStroke(1f));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 16, 16);
                g2.dispose();
            }
            @Override public boolean isOpaque() { return false; }
        };
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
        card.setBorder(BorderFactory.createEmptyBorder(12, 18, 12, 18));

        JLabel lblEtiqueta = new JLabel(etiqueta);
        lblEtiqueta.setFont(new Font("SansSerif", Font.PLAIN, 14));
        lblEtiqueta.setForeground(TEXT_SEC);

        JLabel lblValor = new JLabel("—");
        lblValor.setFont(new Font("SansSerif", Font.BOLD, 22));
        lblValor.setForeground(ACCENT);
        valorRef[0] = lblValor;

        card.add(lblEtiqueta, BorderLayout.WEST);
        card.add(lblValor, BorderLayout.EAST);
        return card;
    }

    private JPanel crearLinia() {
        JPanel p = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setColor(SEP_COLOR);
                g2.fillRect(0, getHeight() / 2 - 1, getWidth(), 1);
                g2.dispose();
            }
            @Override public boolean isOpaque() { return false; }
        };
        p.setMaximumSize(new Dimension(Integer.MAX_VALUE, 6));
        p.setPreferredSize(new Dimension(300, 6));
        return p;
    }

    // ── PANELL DRET: opcions de navegació ───────────────────────────────────

    private JPanel crearPanellDret(AppFrame frame) {
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(55, 60, 40, 60));

        JLabel lblTitol = new JLabel("Menú Principal");
        lblTitol.setFont(new Font("Serif", Font.BOLD, 36));
        lblTitol.setForeground(TITOL);
        lblTitol.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblSub = new JLabel("Què vols fer?");
        lblSub.setFont(new Font("SansSerif", Font.PLAIN, 16));
        lblSub.setForeground(TEXT_SEC);
        lblSub.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(lblTitol);
        panel.add(Box.createVerticalStrut(6));
        panel.add(lblSub);
        panel.add(Box.createVerticalStrut(40));

        String[][] opcions = {
            {"Editar Perfil",      "editar"},
            {"Veure Repositoris",  "repositoris"},
            {"Hidato",             "hidato"},
            {"Partida",            "partida"}
        };

        for (String[] opcio : opcions) {
            JButton btn = crearBotoMenu(opcio[0]);
            btn.addActionListener(e -> {
                switch (opcio[1]) {
                    case "editar":
                        mostrarDialegEditarPerfil(frame);
                        break;
                    case "repositoris":
                        VistaRepositoris vr = new VistaRepositoris(frame);
                        frame.afegirPantalla(vr, "repositoris");
                        frame.mostrarPantalla("repositoris");
                        break;
                    case "hidato":
                        VistaHidato vh = new VistaHidato(frame);
                        frame.afegirPantalla(vh, "hidato");
                        frame.mostrarPantalla("hidato");
                        break;
                    case "partida":
                        VistaPartidaInicial vp = new VistaPartidaInicial(frame);
                        frame.afegirPantalla(vp, "partidaInicial");
                        frame.mostrarPantalla("partidaInicial");
                        break;
                }
            });
            btn.setAlignmentX(Component.CENTER_ALIGNMENT);
            panel.add(btn);
            panel.add(Box.createVerticalStrut(16));
        }

        panel.add(Box.createVerticalGlue());

        JButton btnTancarSessio = VistaInicial.crearBoto("Tancar Sessió",
                new Color(155, 95, 15), new Color(200, 130, 25));
        btnTancarSessio.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnTancarSessio.addActionListener(e -> {
            frame.getCtrlDomini().tancarSessio();
            frame.setUsuariActual(null);
            frame.mostrarPantalla("perfil");
        });
        panel.add(btnTancarSessio);
        panel.add(Box.createVerticalStrut(12));

        JButton btnSortir = VistaInicial.crearBoto("Sortir del Joc", BTN_SORT, BTN_SORT_HOV);
        btnSortir.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnSortir.addActionListener(e -> System.exit(0));
        panel.add(btnSortir);

        return panel;
    }

    private JButton crearBotoMenu(String text) {
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isRollover() ? BTN_HOV : BTN_NOR);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 28, 28);
                g2.setColor(Color.WHITE);
                g2.setFont(getFont());
                FontMetrics fm = g2.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(getText())) / 2;
                int y = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
                g2.drawString(getText(), x, y);
                g2.dispose();
            }
        };
        btn.setFont(new Font("SansSerif", Font.BOLD, 18));
        btn.setForeground(Color.WHITE);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(300, 52));
        btn.setMaximumSize(new Dimension(300, 52));
        return btn;
    }

    // ── Pop-up: Editar Perfil ───────────────────────────────────────────────

    private void mostrarDialegEditarPerfil(AppFrame frame) {
        Perfil p = frame.getUsuariActual();
        if (p == null) return;

        JDialog dialog = crearDialog(frame, "Editar Perfil", 430, 530);

        JPanel panel = new JPanel();
        panel.setBackground(DIALOG_BG);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(28, 38, 28, 38));

        // ── Secció: Dades personals ──
        JLabel secDades = crearTitolSeccio("Dades Personals");
        panel.add(secDades);
        panel.add(Box.createVerticalStrut(6));

        JLabel lblUsuariInfo = new JLabel("Usuari: @" + p.getUsername());
        lblUsuariInfo.setFont(new Font("SansSerif", Font.ITALIC, 13));
        lblUsuariInfo.setForeground(TEXT_USUARI);
        lblUsuariInfo.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(lblUsuariInfo);
        panel.add(Box.createVerticalStrut(10));

        JTextField fNom    = crearCamp();
        JTextField fCognoms = crearCamp();
        fNom.setText(p.getName());
        fCognoms.setText(p.getSurname());

        JLabel lblErrDades = new JLabel(" ");
        lblErrDades.setFont(new Font("SansSerif", Font.PLAIN, 12));
        lblErrDades.setForeground(ERR_COLOR);
        lblErrDades.setAlignmentX(Component.LEFT_ALIGNMENT);

        JButton btnDades = crearBotoPetit("Guardar Nom");
        btnDades.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnDades.addActionListener(e -> {
            boolean ok = frame.getCtrlDomini().editarDadesPersonals(
                fNom.getText().trim(), fCognoms.getText().trim());
            if (ok) {
                lblErrDades.setForeground(OK_COLOR);
                lblErrDades.setText("Nom actualitzat correctament.");
                actualitzarEstadistiques(frame);
            } else {
                lblErrDades.setForeground(ERR_COLOR);
                lblErrDades.setText("Els camps no poden estar buits.");
            }
        });

        panel.add(crearEtiqueta("Nom"));
        panel.add(Box.createVerticalStrut(3));
        panel.add(fNom);
        panel.add(Box.createVerticalStrut(8));
        panel.add(crearEtiqueta("Cognoms"));
        panel.add(Box.createVerticalStrut(3));
        panel.add(fCognoms);
        panel.add(Box.createVerticalStrut(6));
        panel.add(lblErrDades);
        panel.add(Box.createVerticalStrut(4));
        panel.add(btnDades);

        // ── Separador ──
        panel.add(Box.createVerticalStrut(20));
        JPanel sep = crearLiniaH();
        sep.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(sep);
        panel.add(Box.createVerticalStrut(16));

        // ── Secció: Contrasenya ──
        panel.add(crearTitolSeccio("Canviar Contrasenya"));
        panel.add(Box.createVerticalStrut(10));

        JPasswordField fPassActual = crearCampPass();
        JPasswordField fPassNova   = crearCampPass();
        JPasswordField fPassConf   = crearCampPass();

        JLabel lblErrPass = new JLabel(" ");
        lblErrPass.setFont(new Font("SansSerif", Font.PLAIN, 12));
        lblErrPass.setForeground(ERR_COLOR);
        lblErrPass.setAlignmentX(Component.LEFT_ALIGNMENT);

        JButton btnPass = crearBotoPetit("Canviar Contrasenya");
        btnPass.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnPass.addActionListener(e -> {
            String old  = new String(fPassActual.getPassword());
            String nova = new String(fPassNova.getPassword());
            String conf = new String(fPassConf.getPassword());
            if (old.isEmpty() || nova.isEmpty() || conf.isEmpty()) {
                lblErrPass.setForeground(ERR_COLOR);
                lblErrPass.setText("Omple tots els camps.");
                return;
            }
            boolean ok = frame.getCtrlDomini().editarPassword(old, nova, conf);
            if (ok) {
                lblErrPass.setForeground(OK_COLOR);
                lblErrPass.setText("Contrasenya actualitzada.");
                fPassActual.setText(""); fPassNova.setText(""); fPassConf.setText("");
            } else {
                lblErrPass.setForeground(ERR_COLOR);
                lblErrPass.setText("Contrasenya actual incorrecta o no coincideixen.");
            }
        });

        panel.add(crearEtiqueta("Contrasenya actual"));
        panel.add(Box.createVerticalStrut(3));
        panel.add(fPassActual);
        panel.add(Box.createVerticalStrut(8));
        panel.add(crearEtiqueta("Nova contrasenya"));
        panel.add(Box.createVerticalStrut(3));
        panel.add(fPassNova);
        panel.add(Box.createVerticalStrut(8));
        panel.add(crearEtiqueta("Confirmar nova contrasenya"));
        panel.add(Box.createVerticalStrut(3));
        panel.add(fPassConf);
        panel.add(Box.createVerticalStrut(6));
        panel.add(lblErrPass);
        panel.add(Box.createVerticalStrut(4));
        panel.add(btnPass);

        JScrollPane scroll = new JScrollPane(panel);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(DIALOG_BG);
        dialog.setContentPane(scroll);
        dialog.setVisible(true);
    }

    // ── Actualitzar estadístiques en pantalla ───────────────────────────────

    public void actualitzarEstadistiques(AppFrame frame) {
        Perfil p = frame.getUsuariActual();
        if (p == null) return;

        lblNomVal.setText(p.getName() + " " + p.getSurname() + "  ·  @" + p.getUsername());

        Estadistiques e = p.getEstadistiques();
        lblJugatsVal.setText(String.valueOf(e.getHidatosJugats()));
        lblCompletatsVal.setText(String.valueOf(e.getHidatosCompletats()));
        lblMitjanaVal.setText(String.format("%.1f", e.getPuntuacioMitjana()));
        lblMillorVal.setText(String.format("%.1f", e.getMillorPuntuacio()));
    }

    // ── Helpers de components ───────────────────────────────────────────────

    private JDialog crearDialog(AppFrame frame, String titol, int w, int h) {
        JDialog d = new JDialog(frame, titol, true);
        d.setSize(w, h);
        d.setLocationRelativeTo(frame);
        d.setResizable(false);
        d.getContentPane().setBackground(DIALOG_BG);
        return d;
    }

    private JTextField crearCamp() {
        JTextField f = new JTextField();
        estilitzarCamp(f);
        return f;
    }

    private JPasswordField crearCampPass() {
        JPasswordField f = new JPasswordField();
        estilitzarCamp(f);
        return f;
    }

    private void estilitzarCamp(JTextField f) {
        f.setBackground(FIELD_BG);
        f.setForeground(Color.WHITE);
        f.setCaretColor(Color.WHITE);
        f.setFont(new Font("SansSerif", Font.PLAIN, 14));
        f.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(FIELD_BORDER, 1, true),
            BorderFactory.createEmptyBorder(6, 10, 6, 10)
        ));
        f.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        f.setAlignmentX(Component.LEFT_ALIGNMENT);
    }

    private JLabel crearEtiqueta(String text) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("SansSerif", Font.PLAIN, 12));
        l.setForeground(TEXT_SEC);
        l.setAlignmentX(Component.LEFT_ALIGNMENT);
        return l;
    }

    private JLabel crearTitolSeccio(String text) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("SansSerif", Font.BOLD, 16));
        l.setForeground(TITOL);
        l.setAlignmentX(Component.LEFT_ALIGNMENT);
        return l;
    }

    private JButton crearBotoPetit(String text) {
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isRollover() ? BTN_HOV : BTN_NOR);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                g2.setColor(Color.WHITE);
                g2.setFont(getFont());
                FontMetrics fm = g2.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(getText())) / 2;
                int y = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
                g2.drawString(getText(), x, y);
                g2.dispose();
            }
        };
        btn.setFont(new Font("SansSerif", Font.BOLD, 14));
        btn.setForeground(Color.WHITE);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(200, 40));
        btn.setMaximumSize(new Dimension(200, 40));
        return btn;
    }

    private JPanel crearLiniaH() {
        JPanel p = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                g.setColor(SEP_COLOR);
                g.fillRect(0, getHeight() / 2, getWidth(), 1);
            }
            @Override public boolean isOpaque() { return false; }
        };
        p.setMaximumSize(new Dimension(Integer.MAX_VALUE, 4));
        p.setPreferredSize(new Dimension(300, 4));
        return p;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setPaint(new GradientPaint(0, 0, BG_TOP, 0, getHeight(), BG_BOT));
        g2.fillRect(0, 0, getWidth(), getHeight());
    }
}
