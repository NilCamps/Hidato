package presentacio;

import domini.model.*;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;

public class VistaPartidaInicial extends JPanel {

    private static final Color BG_TOP      = new Color(10, 20, 55);
    private static final Color BG_BOT      = new Color(3, 8, 25);
    private static final Color CARD_BG     = new Color(18, 32, 75);
    private static final Color CARD_HOV    = new Color(28, 50, 110);
    private static final Color CARD_BORDER = new Color(60, 100, 180, 120);
    private static final Color TITOL       = Color.WHITE;
    private static final Color TEXT_SEC    = new Color(160, 185, 230);
    private static final Color ACCENT      = new Color(100, 160, 255);
    private static final Color BTN_NOR     = new Color(50, 120, 220);
    private static final Color BTN_HOV     = new Color(80, 155, 255);
    private static final Color DIALOG_BG   = new Color(8, 18, 50);
    private static final Color RES_BG      = new Color(14, 26, 66);
    private static final Color ERR_COLOR   = new Color(255, 100, 100);
    private static final Color ROW_ALT     = new Color(20, 38, 90);

    public VistaPartidaInicial(AppFrame frame) {
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

        JLabel lblTitol = new JLabel("Partida", SwingConstants.CENTER);
        lblTitol.setFont(new Font("Serif", Font.BOLD, 48));
        lblTitol.setForeground(TITOL);
        lblTitol.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblSub = new JLabel("Selecciona una opció", SwingConstants.CENTER);
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
                "▶", "Partida Nova",
                "Configura una nova partida\ntriant dificultat i paràmetres",
                e -> {
                    VistaConfigPartida vcp = new VistaConfigPartida(frame);
                    frame.afegirPantalla(vcp, "configPartida");
                    frame.mostrarPantalla("configPartida");
                }));

        panel.add(crearTargeta(
                "↺", "Carregar Partida",
                "Repren una partida guardada\nanteriorment",
                e -> mostrarPopupCarregar(frame)));

        return panel;
    }

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
        lblSimbol.setForeground(ACCENT);
        lblSimbol.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblTitol = new JLabel(titol, SwingConstants.CENTER);
        lblTitol.setFont(new Font("SansSerif", Font.BOLD, 24));
        lblTitol.setForeground(TITOL);
        lblTitol.setAlignmentX(Component.CENTER_ALIGNMENT);

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

    // ── Pop-up: Carregar Partida ──────────────────────────────────────────────

    private void mostrarPopupCarregar(AppFrame frame) {
        java.util.List<String> partides = frame.getCtrlDomini().getPartidesGuardades();

        JDialog dialog = new JDialog(frame, "Carregar Partida", true);
        dialog.setSize(460, partides.isEmpty() ? 280 : 400);
        dialog.setLocationRelativeTo(frame);
        dialog.setResizable(false);

        JPanel panel = new JPanel();
        panel.setBackground(DIALOG_BG);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 38, 30, 38));

        JLabel lblTitol = new JLabel("Carregar Partida");
        lblTitol.setFont(new Font("Serif", Font.BOLD, 26));
        lblTitol.setForeground(TITOL);
        lblTitol.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(lblTitol);
        panel.add(Box.createVerticalStrut(20));

        if (partides.isEmpty()) {
            JPanel panelBuit = new JPanel(new BorderLayout()) {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(RES_BG);
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                    g2.dispose();
                }
                @Override public boolean isOpaque() { return false; }
            };
            panelBuit.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
            panelBuit.setAlignmentX(Component.LEFT_ALIGNMENT);

            JLabel lblBuit = new JLabel("No hi ha partides guardades.", SwingConstants.CENTER);
            lblBuit.setFont(new Font("SansSerif", Font.PLAIN, 14));
            lblBuit.setForeground(ERR_COLOR);
            panelBuit.add(lblBuit, BorderLayout.CENTER);
            panel.add(panelBuit);

        } else {
            JLabel lblInfo = new JLabel("Selecciona una partida guardada:");
            lblInfo.setFont(new Font("SansSerif", Font.PLAIN, 14));
            lblInfo.setForeground(TEXT_SEC);
            lblInfo.setAlignmentX(Component.LEFT_ALIGNMENT);
            panel.add(lblInfo);
            panel.add(Box.createVerticalStrut(10));

            DefaultListModel<String> model = new DefaultListModel<>();
            for (String p : partides) model.addElement(p);

            JList<String> llista = new JList<>(model);
            llista.setBackground(RES_BG);
            llista.setForeground(Color.WHITE);
            llista.setFont(new Font("SansSerif", Font.PLAIN, 14));
            llista.setSelectionBackground(new Color(50, 100, 200));
            llista.setSelectionForeground(Color.WHITE);
            llista.setBorder(BorderFactory.createEmptyBorder(6, 10, 6, 10));

            JScrollPane scroll = new JScrollPane(llista);
            scroll.setAlignmentX(Component.LEFT_ALIGNMENT);
            scroll.setMaximumSize(new Dimension(Integer.MAX_VALUE, 160));
            scroll.setBorder(new LineBorder(new Color(60, 100, 180), 1, true));
            scroll.getViewport().setBackground(RES_BG);
            panel.add(scroll);
            panel.add(Box.createVerticalStrut(14));

            JButton btnCarregar = VistaInicial.crearBoto("Carregar", BTN_NOR, BTN_HOV);
            btnCarregar.setAlignmentX(Component.CENTER_ALIGNMENT);
            btnCarregar.addActionListener(e -> {
                int idx = llista.getSelectedIndex();
                if (idx < 0) return;

                String nomSeleccionat = llista.getSelectedValue();
                Partida p = frame.getCtrlDomini().carregarPartida(frame.getUsuariActual().getUsername(),nomSeleccionat);

                if (p == null) return;

                dialog.dispose();
                VistaJoc vj = new VistaJoc(frame, p);
                frame.afegirPantalla(vj, "joc");
                frame.mostrarPantalla("joc");
            });
            panel.add(btnCarregar);
        }

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

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setPaint(new GradientPaint(0, 0, BG_TOP, 0, getHeight(), BG_BOT));
        g2.fillRect(0, 0, getWidth(), getHeight());
    }
}
