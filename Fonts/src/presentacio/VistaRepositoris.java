package presentacio;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import domini.model.Taulell;

public class VistaRepositoris extends JPanel {

    private static final Color BG_TOP       = new Color(10, 20, 55);
    private static final Color BG_BOT       = new Color(3, 8, 25);
    private static final Color BTN_NOR      = new Color(50, 120, 220);
    private static final Color BTN_HOV      = new Color(80, 155, 255);
    private static final Color TITOL        = Color.WHITE;
    private static final Color TEXT_SEC     = new Color(160, 185, 230);
    private static final Color ACCENT       = new Color(100, 160, 255);
    private static final Color TAULA_HEADER = new Color(18, 35, 88);
    private static final Color TAULA_ROW1   = new Color(14, 26, 66);
    private static final Color TAULA_ROW2   = new Color(10, 20, 52);
    private static final Color TAULA_SEL    = new Color(40, 80, 160);
    private static final Color TAULA_BORDER = new Color(40, 70, 150);
    private static final Color BUIT_COLOR   = new Color(120, 150, 200);

    public VistaRepositoris(AppFrame frame) {
        setOpaque(false);
        setLayout(new BorderLayout());

        add(crearCapcalera(frame), BorderLayout.NORTH);
        add(crearCos(frame), BorderLayout.CENTER);
        add(crearPeuDePagina(frame), BorderLayout.SOUTH);
    }

    // ── Capçalera ────────────────────────────────────────────────────────────

    private JPanel crearCapcalera(AppFrame frame) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createEmptyBorder(45, 55, 20, 55));

        JLabel lblTitol = new JLabel("Repositori d'Hidatos");
        lblTitol.setFont(new Font("Serif", Font.BOLD, 42));
        lblTitol.setForeground(TITOL);

        int total = frame.getCtrlDomini().getHidatos().size();
        JLabel lblCompte = new JLabel(total + " hidato" + (total == 1 ? "" : "s") + " disponibles");
        lblCompte.setFont(new Font("SansSerif", Font.PLAIN, 15));
        lblCompte.setForeground(TEXT_SEC);

        panel.add(lblTitol, BorderLayout.WEST);
        panel.add(lblCompte, BorderLayout.EAST);
        return panel;
    }

    // ── Cos central: taula ───────────────────────────────────────────────────

    private JPanel crearCos(AppFrame frame) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createEmptyBorder(0, 55, 10, 55));

        HashMap<Integer, Taulell> hidatos = frame.getCtrlDomini().getHidatos();

        if (hidatos.isEmpty()) {
            panel.add(crearMissatgeBuit(), BorderLayout.CENTER);
        } else {
            panel.add(crearTaula(hidatos), BorderLayout.CENTER);
        }

        return panel;
    }

    private JPanel crearMissatgeBuit() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setOpaque(false);

        JLabel icon = new JLabel("□");
        icon.setFont(new Font("Serif", Font.PLAIN, 64));
        icon.setForeground(new Color(40, 70, 140));

        JLabel msg = new JLabel("No hi ha hidatos al repositori");
        msg.setFont(new Font("SansSerif", Font.BOLD, 20));
        msg.setForeground(BUIT_COLOR);

        JLabel sub = new JLabel("Els hidatos es carreguen des de la pantalla Hidato");
        sub.setFont(new Font("SansSerif", Font.PLAIN, 14));
        sub.setForeground(new Color(100, 130, 180));

        JPanel contingut = new JPanel();
        contingut.setOpaque(false);
        contingut.setLayout(new BoxLayout(contingut, BoxLayout.Y_AXIS));
        icon.setAlignmentX(Component.CENTER_ALIGNMENT);
        msg.setAlignmentX(Component.CENTER_ALIGNMENT);
        sub.setAlignmentX(Component.CENTER_ALIGNMENT);
        contingut.add(icon);
        contingut.add(Box.createVerticalStrut(12));
        contingut.add(msg);
        contingut.add(Box.createVerticalStrut(6));
        contingut.add(sub);

        panel.add(contingut);
        return panel;
    }

    private JScrollPane crearTaula(HashMap<Integer, Taulell> hidatos) {
        String[] columnes = {"Codi", "Forma", "Adjacència", "Caselles", "Mida (F × C)"};

        List<Taulell> llista = new ArrayList<>(hidatos.values());
        llista.sort((a, b) -> Integer.compare(a.getCodi(), b.getCodi()));

        Object[][] dades = new Object[llista.size()][5];
        for (int i = 0; i < llista.size(); i++) {
            Taulell t = llista.get(i);
            dades[i][0] = "#" + t.getCodi();
            dades[i][1] = formatForma(t.getForma());
            dades[i][2] = formatAdjacencia(t.getAdjacencia());
            dades[i][3] = t.getNCaselles();
            dades[i][4] = t.getFiles() + " × " + t.getColumnes();
        }

        DefaultTableModel model = new DefaultTableModel(dades, columnes) {
            @Override public boolean isCellEditable(int row, int col) { return false; }
        };

        JTable taula = new JTable(model);
        estilitzarTaula(taula);

        JScrollPane scroll = new JScrollPane(taula);
        scroll.setBorder(BorderFactory.createLineBorder(TAULA_BORDER, 1));
        scroll.getViewport().setBackground(TAULA_ROW1);
        scroll.setBackground(TAULA_ROW1);
        estilitzarScrollBar(scroll.getVerticalScrollBar());
        return scroll;
    }

    private void estilitzarTaula(JTable taula) {
        taula.setBackground(TAULA_ROW1);
        taula.setForeground(Color.WHITE);
        taula.setFont(new Font("SansSerif", Font.PLAIN, 14));
        taula.setRowHeight(44);
        taula.setShowGrid(false);
        taula.setIntercellSpacing(new Dimension(0, 0));
        taula.setSelectionBackground(TAULA_SEL);
        taula.setSelectionForeground(Color.WHITE);
        taula.setFillsViewportHeight(true);

        // Capçalera
        JTableHeader header = taula.getTableHeader();
        header.setBackground(TAULA_HEADER);
        header.setForeground(ACCENT);
        header.setFont(new Font("SansSerif", Font.BOLD, 13));
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, TAULA_BORDER));
        header.setReorderingAllowed(false);
        ((DefaultTableCellRenderer) header.getDefaultRenderer()).setHorizontalAlignment(SwingConstants.CENTER);

        // Files alternades
        taula.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object val,
                    boolean sel, boolean foc, int row, int col) {
                super.getTableCellRendererComponent(t, val, sel, foc, row, col);
                setHorizontalAlignment(col == 0 ? SwingConstants.CENTER : SwingConstants.CENTER);
                setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(25, 45, 100)));
                if (sel) {
                    setBackground(TAULA_SEL);
                    setForeground(Color.WHITE);
                } else {
                    setBackground(row % 2 == 0 ? TAULA_ROW1 : TAULA_ROW2);
                    setForeground(col == 0 ? ACCENT : Color.WHITE);
                }
                setFont(col == 0
                    ? new Font("SansSerif", Font.BOLD, 14)
                    : new Font("SansSerif", Font.PLAIN, 14));
                return this;
            }
        });

        // Amplades de columna
        int[] amplades = {80, 130, 160, 110, 140};
        for (int i = 0; i < amplades.length; i++) {
            taula.getColumnModel().getColumn(i).setPreferredWidth(amplades[i]);
        }
    }

    private void estilitzarScrollBar(JScrollBar sb) {
        sb.setBackground(new Color(10, 20, 50));
        sb.setUI(new javax.swing.plaf.basic.BasicScrollBarUI() {
            @Override protected void configureScrollBarColors() {
                thumbColor = new Color(50, 90, 180);
                trackColor = new Color(10, 20, 50);
            }
            @Override protected JButton createDecreaseButton(int o) { return botoInvisible(); }
            @Override protected JButton createIncreaseButton(int o) { return botoInvisible(); }
            private JButton botoInvisible() {
                JButton b = new JButton();
                b.setPreferredSize(new Dimension(0, 0));
                return b;
            }
        });
    }

    // ── Peu de pàgina ────────────────────────────────────────────────────────

    private JPanel crearPeuDePagina(AppFrame frame) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 30, 25, 0));

        JButton btnEnrere = VistaInicial.crearBoto("← Enrere", BTN_NOR, BTN_HOV);
        btnEnrere.addActionListener(e -> frame.mostrarPantalla("menu"));
        panel.add(btnEnrere);
        return panel;
    }

    // ── Helpers de format ─────────────────────────────────────────────────────

    private String formatForma(Taulell.TForma f) {
        switch (f) {
            case QUADRAT:  return "Quadrat";
            case TRIANGLE: return "Triangle";
            case HEXAGON:  return "Hexàgon";
            default:       return f.name();
        }
    }

    private String formatAdjacencia(Taulell.TAdjacencia a) {
        switch (a) {
            case C:  return "Cares";
            case CA: return "Cares i Angles";
            default: return a.name();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setPaint(new GradientPaint(0, 0, BG_TOP, 0, getHeight(), BG_BOT));
        g2.fillRect(0, 0, getWidth(), getHeight());
    }
}
