package presentacio;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import domini.controladors.CtrlConfiguracio.TPartida;
import domini.model.Taulell.TAdjacencia;
import domini.model.Taulell.TForma;

public class VistaConfigPartida extends JPanel {

    private static final Color BG_TOP       = new Color(10, 20, 55);
    private static final Color BG_BOT       = new Color(3, 8, 25);
    private static final Color TITOL        = Color.WHITE;
    private static final Color TEXT_SEC     = new Color(160, 185, 230);
    private static final Color ACCENT       = new Color(100, 160, 255);
    private static final Color BTN_NOR      = new Color(50, 120, 220);
    private static final Color BTN_HOV      = new Color(80, 155, 255);
    private static final Color TOGGLE_OFF   = new Color(18, 32, 75);
    private static final Color TOGGLE_ON    = new Color(50, 120, 220);
    private static final Color TOGGLE_HOV   = new Color(28, 50, 110);
    private static final Color TOGGLE_BRD   = new Color(60, 100, 180, 150);
    private static final Color CARD_BG      = new Color(14, 26, 66);
    private static final Color CARD_BORDER  = new Color(50, 85, 160, 100);
    private static final Color FIELD_BG     = new Color(12, 25, 65);
    private static final Color FIELD_BORDER = new Color(60, 100, 180);
    private static final Color ERR_COLOR    = new Color(255, 100, 100);
    private static final Color OK_COLOR     = new Color(100, 220, 130);
    private static final Color INFO_COLOR   = new Color(255, 200, 80);

    private static final int MIN_FACIL   = 9;
    private static final int MIN_MITJA   = 16;
    private static final int MIN_DIFICIL = 25;

    public VistaConfigPartida(AppFrame frame) {
        setOpaque(false);
        setLayout(new BorderLayout());

        add(crearCapcalera(), BorderLayout.NORTH);
        add(crearCos(frame), BorderLayout.CENTER);
    }

    // ── Capçalera ────────────────────────────────────────────────────────────

    private JPanel crearCapcalera() {
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(40, 0, 0, 0));

        JLabel lblTitol = new JLabel("Configuració de Partida", SwingConstants.CENTER);
        lblTitol.setFont(new Font("Serif", Font.BOLD, 42));
        lblTitol.setForeground(TITOL);
        lblTitol.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblSub = new JLabel("Tria els paràmetres de la nova partida", SwingConstants.CENTER);
        lblSub.setFont(new Font("SansSerif", Font.PLAIN, 16));
        lblSub.setForeground(TEXT_SEC);
        lblSub.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(lblTitol);
        panel.add(Box.createVerticalStrut(6));
        panel.add(lblSub);
        return panel;
    }

    // ── Cos principal ────────────────────────────────────────────────────────

    private JPanel crearCos(AppFrame frame) {
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setOpaque(false);
        wrapper.setBorder(BorderFactory.createEmptyBorder(30, 80, 0, 80));

        // ── Estat seleccionat ──
        TPartida[]    modeArr = { TPartida.NORMAL };
        int[]         difArr  = { 0 };  // 0=fàcil, 1=mig, 2=difícil
        TForma[]      formaArr= { TForma.QUADRAT };
        TAdjacencia[] adjArr  = { TAdjacencia.C };

        // ── Dues columnes ──
        JPanel cols = new JPanel(new GridLayout(1, 2, 50, 0));
        cols.setOpaque(false);

        // ══ COLUMNA ESQUERRA ══
        JPanel colEsq = new JPanel();
        colEsq.setOpaque(false);
        colEsq.setLayout(new BoxLayout(colEsq, BoxLayout.Y_AXIS));

        // Mode de joc
        JLabel lblMode = crearEtiquetaSeccio("Mode de joc");
        ButtonGroup grupMode = new ButtonGroup();
        JToggleButton[] btnsMode = crearGrupToggle(
                new String[]{"Normal", "Supervivència", "Contrarrellotge"}, grupMode);

        // Panel temps contrarrellotge (ocult per defecte)
        JPanel panelTemps = new JPanel();
        panelTemps.setOpaque(false);
        panelTemps.setLayout(new BoxLayout(panelTemps, BoxLayout.Y_AXIS));
        panelTemps.setVisible(false);

        JLabel lblTemps = crearEtiquetaCamp("Temps límit (minuts)");
        JTextField fTemps = crearCamp();
        fTemps.setText("10");
        panelTemps.add(lblTemps);
        panelTemps.add(Box.createVerticalStrut(4));
        panelTemps.add(fTemps);

        btnsMode[0].addActionListener(e -> { modeArr[0] = TPartida.NORMAL;          panelTemps.setVisible(false); });
        btnsMode[1].addActionListener(e -> { modeArr[0] = TPartida.SUPERVIVENCIA;   panelTemps.setVisible(false); });
        btnsMode[2].addActionListener(e -> { modeArr[0] = TPartida.CONTRARRELLOTGE; panelTemps.setVisible(true);  });

        // Dificultat
        JLabel lblDif = crearEtiquetaSeccio("Dificultat");
        ButtonGroup grupDif = new ButtonGroup();
        JToggleButton[] btnsDif = crearGrupToggle(
                new String[]{"Fàcil", "Mig", "Difícil"}, grupDif);

        // Targeta info dificultat
        JPanel cardInfo = crearCardInfo(difArr);
        JLabel lblInfoDif = (JLabel) cardInfo.getComponent(0);
        actualitzarInfoDificultat(lblInfoDif, 0);

        btnsDif[0].addActionListener(e -> { difArr[0] = 0; actualitzarInfoDificultat(lblInfoDif, 0); });
        btnsDif[1].addActionListener(e -> { difArr[0] = 1; actualitzarInfoDificultat(lblInfoDif, 1); });
        btnsDif[2].addActionListener(e -> { difArr[0] = 2; actualitzarInfoDificultat(lblInfoDif, 2); });

        colEsq.add(lblMode);
        colEsq.add(Box.createVerticalStrut(10));
        colEsq.add(crearFilaToggle(btnsMode));
        colEsq.add(Box.createVerticalStrut(10));
        colEsq.add(panelTemps);
        colEsq.add(Box.createVerticalStrut(24));
        colEsq.add(lblDif);
        colEsq.add(Box.createVerticalStrut(10));
        colEsq.add(crearFilaToggle(btnsDif));
        colEsq.add(Box.createVerticalStrut(14));
        colEsq.add(cardInfo);
        colEsq.add(Box.createVerticalGlue());

        // ══ COLUMNA DRETA ══
        JPanel colDre = new JPanel();
        colDre.setOpaque(false);
        colDre.setLayout(new BoxLayout(colDre, BoxLayout.Y_AXIS));

        // Forma
        JLabel lblForma = crearEtiquetaSeccio("Forma de les cel·les");
        ButtonGroup grupForma = new ButtonGroup();
        JToggleButton[] btnsForma = crearGrupToggle(
                new String[]{"Quadrat", "Triangle", "Hexàgon"}, grupForma);

        btnsForma[0].addActionListener(e -> formaArr[0] = TForma.QUADRAT);
        btnsForma[1].addActionListener(e -> formaArr[0] = TForma.TRIANGLE);
        btnsForma[2].addActionListener(e -> formaArr[0] = TForma.HEXAGON);

        // Adjacència
        JLabel lblAdj = crearEtiquetaSeccio("Adjacència");
        ButtonGroup grupAdj = new ButtonGroup();
        JToggleButton[] btnsAdj = crearGrupToggle(
                new String[]{"Cares", "Cares i Angles"}, grupAdj);

        btnsAdj[0].addActionListener(e -> adjArr[0] = TAdjacencia.C);
        btnsAdj[1].addActionListener(e -> adjArr[0] = TAdjacencia.CA);

        // Nombre de caselles
        JLabel lblCaselles = crearEtiquetaSeccio("Nombre de caselles");
        JTextField fCaselles = crearCamp();
        fCaselles.setText(String.valueOf(MIN_FACIL));
        JLabel lblMinim = new JLabel("Mínim per fàcil: " + MIN_FACIL);
        lblMinim.setFont(new Font("SansSerif", Font.ITALIC, 12));
        lblMinim.setForeground(TEXT_SEC);
        lblMinim.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Codi del hidato (repositori)
        JLabel lblCodi = crearEtiquetaSeccio("Codi del hidato");
        JTextField fCodi = crearCamp();
        fCodi.setText("");
        JLabel lblCodiInfo = new JLabel("Buit = qualsevol disponible");
        lblCodiInfo.setFont(new Font("SansSerif", Font.ITALIC, 12));
        lblCodiInfo.setForeground(TEXT_SEC);
        lblCodiInfo.setAlignmentX(Component.LEFT_ALIGNMENT);

        btnsDif[0].addActionListener(e -> {
            lblMinim.setText("Mínim per fàcil: " + MIN_FACIL);
            if (getCaselles(fCaselles) < MIN_FACIL) fCaselles.setText(String.valueOf(MIN_FACIL));
        });
        btnsDif[1].addActionListener(e -> {
            lblMinim.setText("Mínim per mig: " + MIN_MITJA);
            if (getCaselles(fCaselles) < MIN_MITJA) fCaselles.setText(String.valueOf(MIN_MITJA));
        });
        btnsDif[2].addActionListener(e -> {
            lblMinim.setText("Mínim per difícil: " + MIN_DIFICIL);
            if (getCaselles(fCaselles) < MIN_DIFICIL) fCaselles.setText(String.valueOf(MIN_DIFICIL));
        });

        // Missatge d'error / confirmació
        JLabel lblErr = new JLabel(" ");
        lblErr.setFont(new Font("SansSerif", Font.PLAIN, 13));
        lblErr.setForeground(ERR_COLOR);
        lblErr.setAlignmentX(Component.LEFT_ALIGNMENT);

        colDre.add(lblForma);
        colDre.add(Box.createVerticalStrut(10));
        colDre.add(crearFilaToggle(btnsForma));
        colDre.add(Box.createVerticalStrut(24));
        colDre.add(lblAdj);
        colDre.add(Box.createVerticalStrut(10));
        colDre.add(crearFilaToggle(btnsAdj));
        colDre.add(Box.createVerticalStrut(24));
        colDre.add(lblCaselles);
        colDre.add(Box.createVerticalStrut(6));
        colDre.add(fCaselles);
        colDre.add(Box.createVerticalStrut(4));
        colDre.add(lblMinim);
        colDre.add(Box.createVerticalStrut(18));
        colDre.add(lblCodi);
        colDre.add(Box.createVerticalStrut(6));
        colDre.add(fCodi);
        colDre.add(Box.createVerticalStrut(4));
        colDre.add(lblCodiInfo);
        colDre.add(Box.createVerticalStrut(10));
        colDre.add(lblErr);
        colDre.add(Box.createVerticalGlue());

        cols.add(colEsq);
        cols.add(colDre);

        // ── Botons inferiors ──
        JPanel botons = new JPanel(new BorderLayout());
        botons.setOpaque(false);
        botons.setBorder(BorderFactory.createEmptyBorder(20, 0, 28, 0));

        JButton btnEnrere = VistaInicial.crearBoto("← Enrere", BTN_NOR, BTN_HOV);
        btnEnrere.addActionListener(e -> frame.mostrarPantalla("partidaInicial"));

        JButton btnConfirmar = VistaInicial.crearBoto("Confirmar →", BTN_NOR, BTN_HOV);
        btnConfirmar.addActionListener(e -> {
            int nCaselles = getCaselles(fCaselles);
            int minim = getMinim(difArr[0]);
            if (nCaselles < minim) {
                lblErr.setText("El mínim de caselles per aquesta dificultat és " + minim + ".");
                return;
            }
            if (panelTemps.isVisible()) {
                try {
                    int min = Integer.parseInt(fTemps.getText().trim());
                    if (min <= 0) { lblErr.setText("El temps ha de ser positiu."); return; }
                } catch (NumberFormatException ex) {
                    lblErr.setText("El temps ha de ser un número enter.");
                    return;
                }
            }
            int codiHidato = 0;
            String codiText = fCodi.getText().trim();
            if (!codiText.isEmpty()) {
                try {
                    codiHidato = Integer.parseInt(codiText);
                    if (frame.getCtrlDomini().getHidatos().get(codiHidato) == null) {
                        lblErr.setText("No existeix cap hidato amb el codi " + codiHidato + ".");
                        return;
                    }
                } catch (NumberFormatException ex) {
                    lblErr.setText("El codi del hidato ha de ser un número enter.");
                    return;
                }
            }
            lblErr.setText(" ");
            int tMins = panelTemps.isVisible() ? Integer.parseInt(fTemps.getText().trim()) : 10;
            frame.getCtrlDomini().configurarPartida(modeArr[0], difArr[0], formaArr[0], adjArr[0], getCaselles(fCaselles), codiHidato, tMins);
            VistaJoc vj = new VistaJoc(frame);
            frame.afegirPantalla(vj, "joc");
            frame.mostrarPantalla("joc");
        });

        botons.add(btnEnrere,    BorderLayout.WEST);
        botons.add(btnConfirmar, BorderLayout.EAST);

        wrapper.add(cols,   BorderLayout.CENTER);
        wrapper.add(botons, BorderLayout.SOUTH);
        return wrapper;
    }

    // ── Helpers de UI ────────────────────────────────────────────────────────

    private JToggleButton[] crearGrupToggle(String[] etiquetes, ButtonGroup grup) {
        JToggleButton[] botons = new JToggleButton[etiquetes.length];
        for (int i = 0; i < etiquetes.length; i++) {
            final int idx = i;
            JToggleButton btn = new JToggleButton(etiquetes[i]) {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    if (isSelected()) {
                        g2.setColor(TOGGLE_ON);
                    } else if (getModel().isRollover()) {
                        g2.setColor(TOGGLE_HOV);
                    } else {
                        g2.setColor(TOGGLE_OFF);
                    }
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);
                    g2.setColor(isSelected() ? ACCENT : TOGGLE_BRD);
                    g2.setStroke(new BasicStroke(isSelected() ? 2f : 1f));
                    g2.drawRoundRect(1, 1, getWidth() - 2, getHeight() - 2, 16, 16);
                    g2.setColor(Color.WHITE);
                    g2.setFont(getFont());
                    FontMetrics fm = g2.getFontMetrics();
                    int x = (getWidth() - fm.stringWidth(getText())) / 2;
                    int y = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
                    g2.drawString(getText(), x, y);
                    g2.dispose();
                }
                @Override public boolean isOpaque() { return false; }
            };
            btn.setFont(new Font("SansSerif", Font.BOLD, 14));
            btn.setForeground(Color.WHITE);
            btn.setContentAreaFilled(false);
            btn.setBorderPainted(false);
            btn.setFocusPainted(false);
            btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            btn.setPreferredSize(new Dimension(130, 40));
            btn.setMaximumSize(new Dimension(160, 40));
            grup.add(btn);
            botons[i] = btn;
        }
        botons[0].setSelected(true);
        return botons;
    }

    private JPanel crearFilaToggle(JToggleButton[] botons) {
        JPanel fila = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        fila.setOpaque(false);
        fila.setAlignmentX(Component.LEFT_ALIGNMENT);
        for (JToggleButton b : botons) fila.add(b);
        return fila;
    }

    private JPanel crearCardInfo(int[] difArr) {
        JPanel card = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(CARD_BG);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 14, 14);
                g2.setColor(CARD_BORDER);
                g2.setStroke(new BasicStroke(1f));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 14, 14);
                g2.dispose();
            }
            @Override public boolean isOpaque() { return false; }
        };
        card.setBorder(BorderFactory.createEmptyBorder(12, 16, 12, 16));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 56));
        card.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lbl = new JLabel();
        lbl.setFont(new Font("SansSerif", Font.PLAIN, 13));
        lbl.setForeground(INFO_COLOR);
        card.add(lbl, BorderLayout.CENTER);
        return card;
    }

    private void actualitzarInfoDificultat(JLabel lbl, int dif) {
        switch (dif) {
            case 0: lbl.setText("Fàcil — 5 pistes · Desfer moviments · x1.0");  break;
            case 1: lbl.setText("Mig — 3 pistes · Desfer moviments · x1.5");    break;
            case 2: lbl.setText("Difícil — 0 pistes · Sense desfer · x2.0");    break;
        }
    }

    private JLabel crearEtiquetaSeccio(String text) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("SansSerif", Font.BOLD, 15));
        l.setForeground(TITOL);
        l.setAlignmentX(Component.LEFT_ALIGNMENT);
        return l;
    }

    private JLabel crearEtiquetaCamp(String text) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("SansSerif", Font.PLAIN, 13));
        l.setForeground(TEXT_SEC);
        l.setAlignmentX(Component.LEFT_ALIGNMENT);
        return l;
    }

    private JTextField crearCamp() {
        JTextField f = new JTextField();
        f.setBackground(FIELD_BG);
        f.setForeground(Color.WHITE);
        f.setCaretColor(Color.WHITE);
        f.setFont(new Font("SansSerif", Font.PLAIN, 15));
        f.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(FIELD_BORDER, 1, true),
                BorderFactory.createEmptyBorder(7, 10, 7, 10)));
        f.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        f.setAlignmentX(Component.LEFT_ALIGNMENT);
        return f;
    }

    private int getCaselles(JTextField f) {
        try { return Integer.parseInt(f.getText().trim()); }
        catch (NumberFormatException e) { return 0; }
    }

    private int getMinim(int dif) {
        if (dif == 0) return MIN_FACIL;
        if (dif == 1) return MIN_MITJA;
        return MIN_DIFICIL;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setPaint(new GradientPaint(0, 0, BG_TOP, 0, getHeight(), BG_BOT));
        g2.fillRect(0, 0, getWidth(), getHeight());
    }
}
