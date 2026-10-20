package presentacio;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.HashMap;
import domini.model.Taulell;

public class VistaHidato extends JPanel {

    private static final Color BG_TOP = new Color(10, 20, 55);
    private static final Color BG_BOT = new Color(3, 8, 25);
    private static final Color CARD_BG = new Color(18, 32, 75);
    private static final Color CARD_HOV = new Color(28, 50, 110);
    private static final Color CARD_BORDER = new Color(60, 100, 180, 120);
    private static final Color TITOL = Color.WHITE;
    private static final Color TEXT_SEC = new Color(160, 185, 230);
    private static final Color ACCENT = new Color(100, 160, 255);
    private static final Color BTN_NOR = new Color(50, 120, 220);
    private static final Color BTN_HOV = new Color(80, 155, 255);
    private static final Color BTN_SORT = new Color(160, 45, 45);
    private static final Color BTN_SORT_HOV = new Color(205, 65, 65);
    private static final Color FIELD_BG = new Color(12, 25, 65);
    private static final Color FIELD_BORDER = new Color(60, 100, 180);
    private static final Color ERR_COLOR = new Color(255, 100, 100);
    private static final Color OK_COLOR = new Color(100, 220, 130);
    private static final Color WARN_COLOR = new Color(255, 200, 80);
    private static final Color DIALOG_BG = new Color(8, 18, 50);
    private static final Color RES_BG = new Color(14, 26, 66);

    public VistaHidato(AppFrame frame) {
        setOpaque(false);
        setLayout(new BorderLayout());

        add(crearCapcalera(), BorderLayout.NORTH);
        add(crearCos(frame), BorderLayout.CENTER);
        add(crearPeu(frame), BorderLayout.SOUTH);
    }

    // ── Capçalera ────────────────────────────────────────────────────────────

    private JPanel crearCapcalera() {
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(50, 0, 0, 0));

        JLabel lblTitol = new JLabel("Gestió d'Hidatos", SwingConstants.CENTER);
        lblTitol.setFont(new Font("Serif", Font.BOLD, 48));
        lblTitol.setForeground(TITOL);
        lblTitol.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblSub = new JLabel("Selecciona una operació", SwingConstants.CENTER);
        lblSub.setFont(new Font("SansSerif", Font.PLAIN, 17));
        lblSub.setForeground(TEXT_SEC);
        lblSub.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(lblTitol);
        panel.add(Box.createVerticalStrut(8));
        panel.add(lblSub);
        return panel;
    }

    // ── Cos: dues targetes ───────────────────────────────────────────────────

    private JPanel crearCos(AppFrame frame) {
        JPanel panel = new JPanel(new GridLayout(1, 2, 50, 0));
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createEmptyBorder(55, 130, 40, 130));

        panel.add(crearTargeta(
                "✓", "Validar Hidato",
                "Comprova si un hidato té\nestructura vàlida i solució única",
                e -> mostrarPopupValidar(frame)));
        panel.add(crearTargeta(
                "?", "Comprovar Hidato",
                "Verifica si un hidato completat\nés una solució correcta",
                e -> mostrarPopupComprovar(frame)));

        return panel;
    }

    private JPanel crearTargeta(String simbol, String titol, String desc, ActionListener onClick) {
        JPanel card = new JPanel() {
            private boolean hover = false;
            {
                addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseEntered(MouseEvent e) {
                        hover = true;
                        repaint();
                    }

                    @Override
                    public void mouseExited(MouseEvent e) {
                        hover = false;
                        repaint();
                    }

                    @Override
                    public void mouseClicked(MouseEvent e) {
                        onClick.actionPerformed(null);
                    }
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

            @Override
            public boolean isOpaque() {
                return false;
            }
        };
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(BorderFactory.createEmptyBorder(50, 30, 50, 30));

        JLabel lblSimbol = new JLabel(simbol, SwingConstants.CENTER);
        lblSimbol.setFont(new Font("SansSerif", Font.BOLD, 56));
        lblSimbol.setForeground(ACCENT);
        lblSimbol.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblTitol = new JLabel(titol, SwingConstants.CENTER);
        lblTitol.setFont(new Font("SansSerif", Font.BOLD, 24));
        lblTitol.setForeground(TITOL);
        lblTitol.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Descripció multilinea
        for (String linia : desc.split("\n")) {
            JLabel lblLinia = new JLabel(linia, SwingConstants.CENTER);
            lblLinia.setFont(new Font("SansSerif", Font.PLAIN, 14));
            lblLinia.setForeground(TEXT_SEC);
            lblLinia.setAlignmentX(Component.CENTER_ALIGNMENT);
            card.add(Box.createVerticalGlue());
            card.add(lblSimbol);
            card.add(Box.createVerticalStrut(18));
            card.add(lblTitol);
            card.add(Box.createVerticalStrut(10));
            // afegir línies descripció
            break;
        }
        // Afegir línies de descripció correctament
        card.removeAll();
        card.add(Box.createVerticalGlue());
        card.add(lblSimbol);
        card.add(Box.createVerticalStrut(18));
        card.add(lblTitol);
        card.add(Box.createVerticalStrut(10));
        for (String linia : desc.split("\n")) {
            JLabel l = new JLabel(linia, SwingConstants.CENTER);
            l.setFont(new Font("SansSerif", Font.PLAIN, 14));
            l.setForeground(TEXT_SEC);
            l.setAlignmentX(Component.CENTER_ALIGNMENT);
            card.add(l);
        }
        card.add(Box.createVerticalGlue());

        return card;
    }

    // ── Pop-up: Validar ───────────────────────────────────────────────────────

    private void mostrarPopupValidar(AppFrame frame) {
        JDialog dialog = crearDialog(frame, "Validar Hidato", 420, 340);
        JPanel panel = crearPanelDialog();

        JLabel lblTitol = crearTitolDialog("Validar Hidato");

        JLabel lblInfo = new JLabel("Introdueix el codi de l'hidato a validar:");
        lblInfo.setFont(new Font("SansSerif", Font.PLAIN, 14));
        lblInfo.setForeground(TEXT_SEC);
        lblInfo.setAlignmentX(Component.LEFT_ALIGNMENT);

        JTextField fCodi = crearCamp();
        poblarCodiCombo(fCodi, frame);

        JPanel panelResultat = crearPanelResultat();
        JLabel lblResultat = (JLabel) panelResultat.getComponent(0);

        JButton btnValidar = VistaInicial.crearBoto("Validar", BTN_NOR, BTN_HOV);
        btnValidar.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnValidar.addActionListener(e -> {
            String text = fCodi.getText().trim();
            if (text.isEmpty()) {
                mostrarResultat(lblResultat, panelResultat, "Introdueix un codi.", ERR_COLOR);
                return;
            }
            try {
                int codi = Integer.parseInt(text);
                String res = frame.getCtrlDomini().validarHidato(codi);
                Color color = res.contains("vàlid i té") ? OK_COLOR
                        : res.contains("vàlid però") ? WARN_COLOR : ERR_COLOR;
                mostrarResultat(lblResultat, panelResultat, res, color);
            } catch (NumberFormatException ex) {
                mostrarResultat(lblResultat, panelResultat, "El codi ha de ser un número.", ERR_COLOR);
            }
        });

        panel.add(lblTitol);
        panel.add(Box.createVerticalStrut(20));
        panel.add(lblInfo);
        panel.add(Box.createVerticalStrut(6));
        panel.add(fCodi);
        panel.add(Box.createVerticalStrut(12));
        panel.add(panelResultat);
        panel.add(Box.createVerticalStrut(14));
        panel.add(btnValidar);

        dialog.setContentPane(panel);
        dialog.setVisible(true);
    }

    // ── Pop-up: Comprovar ─────────────────────────────────────────────────────

    private void mostrarPopupComprovar(AppFrame frame) {
        JDialog dialog = crearDialog(frame, "Comprovar Hidato", 470, 340);
        JPanel panel = crearPanelDialog();

        JLabel lblTitol = crearTitolDialog("Comprovar Hidato");

        JLabel lblInfo = new JLabel("Introdueix el codi de l'hidato a comprovar:");
        lblInfo.setFont(new Font("SansSerif", Font.PLAIN, 14));
        lblInfo.setForeground(TEXT_SEC);
        lblInfo.setAlignmentX(Component.LEFT_ALIGNMENT);

        JTextField fCodi = crearCamp();
        poblarCodiCombo(fCodi, frame);

        JPanel panelResultat = crearPanelResultat();
        JLabel lblResultat = (JLabel) panelResultat.getComponent(0);

        JButton btnComprovar = VistaInicial.crearBoto("Comprovar", BTN_NOR, BTN_HOV);
        btnComprovar.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnComprovar.addActionListener(e -> {
            String text = fCodi.getText().trim();
            if (text.isEmpty()) {
                mostrarResultat(lblResultat, panelResultat, "Introdueix un codi.", ERR_COLOR);
                return;
            }
            try {
                int codi = Integer.parseInt(text);
                String res = frame.getCtrlDomini().comprovarHidato(codi);
                Color color = res.contains("correcta") ? OK_COLOR
                        : res.contains("complet") ? WARN_COLOR : ERR_COLOR;
                mostrarResultat(lblResultat, panelResultat, res, color);
            } catch (NumberFormatException ex) {
                mostrarResultat(lblResultat, panelResultat, "El codi ha de ser un número.", ERR_COLOR);
            }
        });

        panel.add(lblTitol);
        panel.add(Box.createVerticalStrut(20));
        panel.add(lblInfo);
        panel.add(Box.createVerticalStrut(6));
        panel.add(fCodi);
        panel.add(Box.createVerticalStrut(12));
        panel.add(panelResultat);
        panel.add(Box.createVerticalStrut(14));
        panel.add(btnComprovar);

        dialog.setContentPane(panel);
        dialog.setVisible(true);
    }

    // ── Peu de pàgina ─────────────────────────────────────────────────────────

    private JPanel crearPeu(AppFrame frame) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createEmptyBorder(0, 30, 25, 0));
        JButton btnEnrere = VistaInicial.crearBoto("← Enrere", BTN_NOR, BTN_HOV);
        btnEnrere.addActionListener(e -> frame.mostrarPantalla("menu"));
        panel.add(btnEnrere);
        return panel;
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private void poblarCodiCombo(JTextField camp, AppFrame frame) {
        HashMap<Integer, Taulell> hidatos = frame.getCtrlDomini().getHidatos();
        if (!hidatos.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            hidatos.keySet().stream().sorted().forEach(k -> {
                if (sb.length() > 0)
                    sb.append(", ");
                sb.append(k);
            });
            camp.setToolTipText("Codis disponibles: " + sb);
        }
    }

    private JPanel crearPanelResultat() {
        JPanel p = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(RES_BG);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                g2.dispose();
            }

            @Override
            public boolean isOpaque() {
                return false;
            }
        };
        p.setBorder(BorderFactory.createEmptyBorder(10, 14, 10, 14));
        p.setMaximumSize(new Dimension(Integer.MAX_VALUE, 46));
        p.setAlignmentX(Component.LEFT_ALIGNMENT);
        p.setVisible(false);

        JLabel lbl = new JLabel(" ");
        lbl.setFont(new Font("SansSerif", Font.PLAIN, 13));
        lbl.setForeground(TEXT_SEC);
        p.add(lbl, BorderLayout.CENTER);
        return p;
    }

    private void mostrarResultat(JLabel lbl, JPanel panel, String text, Color color) {
        lbl.setText(text);
        lbl.setForeground(color);
        panel.setVisible(true);
        panel.revalidate();
        panel.repaint();
    }

    private JDialog crearDialog(AppFrame frame, String titol, int w, int h) {
        JDialog d = new JDialog(frame, titol, true);
        d.setSize(w, h);
        d.setLocationRelativeTo(frame);
        d.setResizable(false);
        d.getContentPane().setBackground(DIALOG_BG);
        return d;
    }

    private JPanel crearPanelDialog() {
        JPanel p = new JPanel();
        p.setBackground(DIALOG_BG);
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBorder(BorderFactory.createEmptyBorder(30, 38, 30, 38));
        return p;
    }

    private JLabel crearTitolDialog(String text) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("Serif", Font.BOLD, 26));
        l.setForeground(TITOL);
        l.setAlignmentX(Component.CENTER_ALIGNMENT);
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

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setPaint(new GradientPaint(0, 0, BG_TOP, 0, getHeight(), BG_BOT));
        g2.fillRect(0, 0, getWidth(), getHeight());
    }
}
