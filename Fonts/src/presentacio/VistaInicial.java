package presentacio;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;

public class VistaInicial extends JPanel {

    private static final Color BG_TOP    = new Color(10, 20, 55);
    private static final Color BG_BOT    = new Color(3, 8, 25);
    private static final Color TITOL     = Color.WHITE;
    private static final Color SUBTITOL  = new Color(160, 185, 230);
    private static final Color GRUP      = new Color(120, 150, 200);
    private static final Color LINIA     = new Color(60, 100, 180);
    private static final Color BTN_NOR   = new Color(50, 120, 220);
    private static final Color BTN_HOV   = new Color(80, 155, 255);

    public VistaInicial(AppFrame frame) {
        setOpaque(false);
        setLayout(new GridBagLayout());

        JPanel contingut = new JPanel();
        contingut.setOpaque(false);
        contingut.setLayout(new BoxLayout(contingut, BoxLayout.Y_AXIS));

        JLabel lblTitol = new JLabel("HIDATO");
        lblTitol.setFont(new Font("Serif", Font.BOLD, 110));
        lblTitol.setForeground(TITOL);
        lblTitol.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblSubtitol = new JLabel("Projecte de Programació  ·  PROP");
        lblSubtitol.setFont(new Font("SansSerif", Font.PLAIN, 22));
        lblSubtitol.setForeground(SUBTITOL);
        lblSubtitol.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblGrup = new JLabel("Subgrup 41.4");
        lblGrup.setFont(new Font("SansSerif", Font.ITALIC, 16));
        lblGrup.setForeground(GRUP);
        lblGrup.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel linia = crearLinia();
        linia.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton btnIniciar = crearBoto("INICIAR", BTN_NOR, BTN_HOV);
        btnIniciar.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnIniciar.addActionListener(e -> {
            VistaPerfil vistaPerfil = new VistaPerfil(frame);
            frame.afegirPantalla(vistaPerfil, "perfil");
            frame.mostrarPantalla("perfil");
        });

        contingut.add(lblTitol);
        contingut.add(Box.createVerticalStrut(6));
        contingut.add(lblSubtitol);
        contingut.add(Box.createVerticalStrut(4));
        contingut.add(lblGrup);
        contingut.add(Box.createVerticalStrut(28));
        contingut.add(linia);
        contingut.add(Box.createVerticalStrut(52));
        contingut.add(btnIniciar);

        add(contingut);
    }

    static JButton crearBoto(String text, Color normal, Color hover) {
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isRollover() ? hover : normal);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 32, 32);
                g2.setColor(Color.WHITE);
                g2.setFont(getFont());
                FontMetrics fm = g2.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(getText())) / 2;
                int y = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
                g2.drawString(getText(), x, y);
                g2.dispose();
            }
        };
        btn.setFont(new Font("SansSerif", Font.BOLD, 20));
        btn.setForeground(Color.WHITE);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(220, 55));
        btn.setMaximumSize(new Dimension(220, 55));
        return btn;
    }

    private JPanel crearLinia() {
        JPanel p = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                int w = getWidth(), cy = getHeight() / 2;
                g2.setPaint(new GradientPaint(0, cy, new Color(0, 0, 0, 0), w / 2f, cy, LINIA));
                g2.fillRect(0, cy - 1, w / 2, 2);
                g2.setPaint(new GradientPaint(w / 2f, cy, LINIA, w, cy, new Color(0, 0, 0, 0)));
                g2.fillRect(w / 2, cy - 1, w / 2, 2);
                g2.dispose();
            }
            @Override public boolean isOpaque() { return false; }
        };
        p.setPreferredSize(new Dimension(480, 8));
        p.setMaximumSize(new Dimension(480, 8));
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
