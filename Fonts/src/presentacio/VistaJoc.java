package presentacio;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import domini.controladors.CtrlDomini;
import domini.controladors.CtrlConfiguracio.TPartida;
import domini.model.*;

public class VistaJoc extends JPanel {

    private static final Color BG_TOP     = new Color(10, 20, 55);
    private static final Color BG_BOT     = new Color(3, 8, 25);
    private static final Color BTN_NOR    = new Color(50, 120, 220);
    private static final Color BTN_HOV    = new Color(80, 155, 255);
    private static final Color C_INIT     = new Color(25, 65, 155);
    private static final Color C_EMPTY    = new Color(12, 28, 78);
    private static final Color C_FILL     = new Color(18, 52, 128);
    private static final Color C_SEL      = new Color(10, 35, 100);
    private static final Color C_HOLE     = new Color(4, 8, 22);
    private static final Color C_BORDER   = new Color(50, 90, 180, 140);
    private static final Color C_GLOW     = new Color(255, 220, 60, 200);
    private static final Color C_FLASH_OK = new Color(40, 170, 80);
    private static final Color C_FLASH_ER = new Color(190, 45, 45);
    private static final Color NUM_AVA    = new Color(12, 28, 78);
    private static final Color NUM_USED   = new Color(8, 16, 48);

    private final AppFrame frame;
    private final TPartida mode;
    private Partida partida;
    private Taulell taulell;
    private int[] cellaSeleccionada = null;
    private String digitBuffer = "";
    private JButton[][] cellBtns;
    private JLabel lblTimer, lblInfo;
    private javax.swing.Timer swingTimer;
    private final Deque<int[]> undoStack = new ArrayDeque<>();
    private boolean partidaAcabada = false;

    VistaJoc(AppFrame frame, Partida partidaCarregada) {
        this.partida = partidaCarregada;
        frame.getCtrlDomini().setPartidaActiva(partidaCarregada);
        this.taulell = partida.getTaulell();
        int tempsMins = 0;
        if (partidaCarregada instanceof Contrarrellotge) {
            mode = TPartida.CONTRARRELLOTGE;
            tempsMins = (int)((Contrarrellotge)partidaCarregada).getTempsLimitSegons() / 60;
        }
        else if (partidaCarregada instanceof Supervivencia)
            mode = TPartida.SUPERVIVENCIA;
        else
            mode = TPartida.NORMAL;
    
        this.frame = frame;
        partida.resetInicio(partida.getTpartida());
        setOpaque(false);
        setLayout(new BorderLayout());
        add(crearTop(tempsMins), BorderLayout.NORTH);
        add(crearCentre(),       BorderLayout.CENTER);
        add(crearBottom(),       BorderLayout.SOUTH);
        configurarTecles();
        startTimer(mode, tempsMins);
    }

    public VistaJoc(AppFrame frame) {
        this.frame = frame;
        this.mode = frame.getCtrlDomini().getModePartida();
        setOpaque(false);
        setLayout(new BorderLayout());
    
        if (!initPartida()) {
            JLabel err = new JLabel("No hi ha hidatos disponibles al sistema.", SwingConstants.CENTER);
            err.setForeground(Color.WHITE);
            err.setFont(new Font("SansSerif", Font.BOLD, 20));
            add(err, BorderLayout.CENTER);
            JPanel bot = new JPanel(new FlowLayout(FlowLayout.LEFT));
            bot.setOpaque(false);
            bot.setBorder(BorderFactory.createEmptyBorder(0, 30, 20, 0));
            JButton b = VistaInicial.crearBoto("← Enrere", BTN_NOR, BTN_HOV);
            b.addActionListener(e -> frame.mostrarPantalla("partidaInicial"));
            bot.add(b);
            add(bot, BorderLayout.SOUTH);
            return;
        }
        
        int tempsMins = 0;
        if (frame.getCtrlDomini().getPartidaActiva() instanceof Contrarrellotge) {
            tempsMins = (int)((Contrarrellotge)frame.getCtrlDomini().getPartidaActiva()).getTempsLimitSegons() / 60;
        }
        add(crearTop(tempsMins), BorderLayout.NORTH);
        add(crearCentre(), BorderLayout.CENTER);
        add(crearBottom(), BorderLayout.SOUTH);
        configurarTecles();
        startTimer(mode, tempsMins);
    }

    // ── Domain init ──────────────────────────────────────────────────────────

    private boolean initPartida() {
        partida = frame.getCtrlDomini().iniciarPartida();
        if (partida == null) return false;
        taulell = partida.getTaulell();
        return true;
    }

    // ── Top bar ──────────────────────────────────────────────────────────────

    private JPanel crearTop(int tempsMins) {
        JPanel top = new JPanel(new GridLayout(1, 3));
        top.setOpaque(false);
        top.setBorder(BorderFactory.createEmptyBorder(18, 30, 8, 30));

        JLabel lblTitol = new JLabel("HIDATO  #" + taulell.getCodi() + "  —  " + nomMode());
        lblTitol.setFont(new Font("Serif", Font.BOLD, 22));
        lblTitol.setForeground(Color.WHITE);

        lblInfo = new JLabel("", SwingConstants.CENTER);
        lblInfo.setFont(new Font("SansSerif", Font.BOLD, 16));
        lblInfo.setForeground(new Color(160, 190, 255));
        actualitzarInfo();

        String timerInit = (mode == TPartida.CONTRARRELLOTGE) ? formatTime(tempsMins * 60L) : "00:00";
        lblTimer = new JLabel(timerInit, SwingConstants.RIGHT);
        lblTimer.setFont(new Font("Monospaced", Font.BOLD, 22));
        lblTimer.setForeground(new Color(100, 200, 255));

        top.add(lblTitol);
        top.add(lblInfo);
        top.add(lblTimer);
        return top;
    }

    // ── Centre: grid ─────────────────────────────────────────────────────────

    private JPanel crearCentre() {
        int files = taulell.getFiles();
        int cols  = taulell.getColumnes();
        cellBtns  = new JButton[files][cols];

        JPanel grid = new JPanel(new GridLayout(files, cols, 3, 3)) {
            @Override public boolean isOpaque() { return false; }
        };
        grid.setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));

        for (int i = 0; i < files; i++)
            for (int j = 0; j < cols; j++) {
                cellBtns[i][j] = crearCellButton(i, j);
                grid.add(cellBtns[i][j]);
            }

        JPanel wrapper = new JPanel(new GridBagLayout());
        wrapper.setOpaque(false);
        wrapper.add(grid);
        return wrapper;
    }

    private java.awt.Shape formaCell(Taulell.TForma forma, int w, int h, int fi, int fj) {
        int pad = 3;
        switch (forma) {
            case TRIANGLE: {
                boolean baseABaix = ((fi + fj) % 2 == 0);
                int[] xs = baseABaix ? new int[]{w/2, w-pad, pad}   : new int[]{pad, w-pad, w/2};
                int[] ys = baseABaix ? new int[]{pad,  h-pad, h-pad} : new int[]{pad, pad,   h-pad};
                return new java.awt.Polygon(xs, ys, 3);
            }
            case HEXAGON: {
                java.awt.Polygon hex = new java.awt.Polygon();
                int cx = w/2, cy = h/2, rx = w/2-pad, ry = h/2-pad;
                for (int k = 0; k < 6; k++) {
                    double a = Math.PI / 180.0 * (60*k - 30);
                    hex.addPoint(cx + (int)(rx*Math.cos(a)), cy + (int)(ry*Math.sin(a)));
                }
                return hex;
            }
            default:
                return new java.awt.geom.RoundRectangle2D.Float(pad, pad, w-2*pad, h-2*pad, 10, 10);
        }
    }

    private JButton crearCellButton(final int fi, final int fj) {
        Cella c0 = taulell.getCella(fi, fj);

        JButton btn = new JButton() {
            java.awt.Shape lastShape = null;
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Cella cel = taulell.getCella(fi, fj);
                boolean sel = Boolean.TRUE.equals(getClientProperty("sel"));
                String buf  = (String) getClientProperty("digitBuf");
                Color flash = (Color) getClientProperty("flashColor");

                Color bg;
                if      (cel == null)      bg = new Color(0,0,0,0);
                else if (cel.esForat())    bg = C_HOLE;
                else if (flash != null)    bg = flash;
                else if (sel)              bg = C_SEL;
                else if (cel.esInicial())  bg = C_INIT;
                else if (cel.esBuida())    bg = C_EMPTY;
                else                       bg = C_FILL;

                java.awt.Shape shape = formaCell(taulell.getForma(), getWidth(), getHeight(), fi, fj);
                lastShape = shape;
                g2.setColor(bg);
                g2.fill(shape);

                if (cel != null && !cel.esForat()) {
                    // Glow border si seleccionada
                    if (sel) {
                        g2.setColor(C_GLOW);
                        g2.setStroke(new BasicStroke(3f));
                        g2.draw(shape);
                    }
                    g2.setColor(C_BORDER);
                    g2.setStroke(new BasicStroke(sel ? 1f : 1.5f));
                    g2.draw(shape);

                    // Text: buffer en curs > valor actual > res
                    String txt = (buf != null && !buf.isEmpty()) ? buf
                               : (!cel.esBuida() ? String.valueOf(cel.getValor()) : null);
                    if (txt != null) {
                        int fs = Math.max(10, getHeight() / 3);
                        g2.setFont(new Font("SansSerif", Font.BOLD, fs));
                        boolean isInitial = cel.esInicial();
                        boolean isBuf = (buf != null && !buf.isEmpty());
                        g2.setColor(isBuf ? new Color(255,220,60)
                                   : isInitial ? Color.WHITE
                                   : new Color(185, 215, 255));
                        FontMetrics fm = g2.getFontMetrics();
                        g2.drawString(txt, (getWidth()-fm.stringWidth(txt))/2,
                                          (getHeight()+fm.getAscent()-fm.getDescent())/2);
                    }
                }
                g2.dispose();
            }
            @Override public boolean isOpaque() { return false; }
            @Override public boolean contains(int x, int y) {
                Cella cel = taulell.getCella(fi, fj);
                if (cel == null || cel.esForat()) return false;
                java.awt.Shape s = lastShape != null ? lastShape
                        : formaCell(taulell.getForma(), getWidth(), getHeight(), fi, fj);
                return s.contains(x, y);
            }
        };
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setPreferredSize(new Dimension(52, 52));

        if (c0 != null && !c0.esForat()) {
            btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            btn.addActionListener(e -> handleCellClick(fi, fj));
        }
        return btn;
    }

    // ── Bottom bar ───────────────────────────────────────────────────────────

    private JPanel crearBottom() {
        JPanel bot = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        bot.setOpaque(false);
        bot.setBorder(BorderFactory.createEmptyBorder(4, 20, 20, 20));

        int nPistes = partida.getDificultat().getNpistes();
        JButton btnPista = VistaInicial.crearBoto("Pista (" + nPistes + ")", BTN_NOR, BTN_HOV);
        btnPista.setEnabled(nPistes > 0);
        btnPista.addActionListener(e -> donarPista(btnPista));

        JButton btnDesfer = VistaInicial.crearBoto("Desfer", BTN_NOR, BTN_HOV);
        btnDesfer.setEnabled(partida.getDificultat().checkUndos());
        btnDesfer.addActionListener(e -> desferMoviment());

        JButton btnConfirmar = VistaInicial.crearBoto("Confirmar", new Color(30, 130, 60), new Color(45, 175, 85));
        btnConfirmar.addActionListener(e -> commitDigitBuffer());

        JButton btnPausa = VistaInicial.crearBoto("Pausa", BTN_NOR, BTN_HOV);
        btnPausa.addActionListener(e -> mostrarDialogPausa());

        bot.add(btnPista);
        bot.add(btnDesfer);
        bot.add(btnConfirmar);
        bot.add(btnPausa);
        return bot;
    }

    // ── Keyboard input ───────────────────────────────────────────────────────

    private void configurarTecles() {
        InputMap im = getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap am = getActionMap();
        for (int d = 0; d <= 9; d++) {
            final int digit = d;
            im.put(KeyStroke.getKeyStroke((char)('0' + d)), "digit" + d);
            am.put("digit" + d, new AbstractAction() {
                public void actionPerformed(ActionEvent e) { afegirDigit(digit); }
            });
        }
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_BACK_SPACE, 0), "backspace");
        am.put("backspace", new AbstractAction() {
            public void actionPerformed(ActionEvent e) { esborrarDigit(); }
        });
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0), "delete");
        am.put("delete", new AbstractAction() {
            public void actionPerformed(ActionEvent e) { esborrarCasella(); }
        });
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "escape");
        am.put("escape", new AbstractAction() {
            public void actionPerformed(ActionEvent e) { desseleccionar(); }
        });
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "enter");
        am.put("enter", new AbstractAction() {
            public void actionPerformed(ActionEvent e) { commitDigitBuffer(); }
        });
    }

    private void afegirDigit(int d) {
        if (cellaSeleccionada == null || partidaAcabada) return;
        int maxDigits = String.valueOf(taulell.getNCaselles()).length();
        if (digitBuffer.length() >= maxDigits) return;
        digitBuffer += d;
        int i = cellaSeleccionada[0], j = cellaSeleccionada[1];
        cellBtns[i][j].putClientProperty("digitBuf", digitBuffer);
        cellBtns[i][j].repaint();
    }

    private void commitDigitBuffer() {
        if (cellaSeleccionada == null || digitBuffer.isEmpty()) return;
        int i = cellaSeleccionada[0], j = cellaSeleccionada[1];
        String buf = digitBuffer;
        digitBuffer = "";
        cellBtns[i][j].putClientProperty("digitBuf", null);
        try {
            int n = Integer.parseInt(buf);
            colocarNumero(i, j, n);
        } catch (NumberFormatException ex) {
            repaintCell(i, j);
        }
    }

    private void esborrarDigit() {
        if (digitBuffer.isEmpty()) { esborrarCasella(); return; }
        digitBuffer = "";
        if (cellaSeleccionada != null) {
            cellBtns[cellaSeleccionada[0]][cellaSeleccionada[1]].putClientProperty("digitBuf", null);
            repaintCell(cellaSeleccionada[0], cellaSeleccionada[1]);
        }
    }

    private void esborrarCasella() {
        if (cellaSeleccionada == null || partidaAcabada) return;
        int i = cellaSeleccionada[0], j = cellaSeleccionada[1];
        Cella c = taulell.getCella(i, j);
        if (c != null && c.esEditable() && !c.esBuida()) {
            undoStack.push(new int[]{i, j, c.getValor()});
            partida.buidarCasella(i, j);
            repaintCell(i, j);
            repaintNums();
            actualitzarInfo();
        }
    }

    private void desseleccionar() {
        digitBuffer = "";
        if (cellaSeleccionada != null) {
            cellBtns[cellaSeleccionada[0]][cellaSeleccionada[1]].putClientProperty("sel", null);
            cellBtns[cellaSeleccionada[0]][cellaSeleccionada[1]].putClientProperty("digitBuf", null);
            repaintCell(cellaSeleccionada[0], cellaSeleccionada[1]);
            cellaSeleccionada = null;
        }
    }

    // ── Game logic ───────────────────────────────────────────────────────────

    private void handleCellClick(int i, int j) {
        if (partidaAcabada) return;
        Cella c = taulell.getCella(i, j);
        if (c == null || !c.esEditable()) return;
        desseleccionar();
        cellaSeleccionada = new int[]{i, j};
        cellBtns[i][j].putClientProperty("sel", Boolean.TRUE);
        repaintCell(i, j);
    }

    private void colocarNumero(int i, int j, int n) {
        if (n < 1 || n > taulell.getNCaselles()) { repaintCell(i, j); return; }
        Cella c = taulell.getCella(i, j);
        if (c == null || !c.esEditable()) return;
    
        // Si la cel·la ja tenia un valor, el treiem primer
        if (!c.esBuida()) {
            undoStack.push(new int[]{i, j, c.getValor()});
            partida.buidarCasella(i, j);
        }
    
        undoStack.push(new int[]{i, j, Cella.BUIDA});
        boolean correcte = frame.getCtrlDomini().processarMoviment(n, i, j);
        
        repaintCell(i, j);
        repaintNums();
    
        if (!correcte) flashCell(i, j, C_FLASH_ER);
        else           flashCell(i, j, C_FLASH_OK);
        
        actualitzarInfo();
        checkEnd();
    }

    private void checkEnd() {
        if (mode == TPartida.SUPERVIVENCIA && !((Supervivencia) partida).teVides()) {
            acabarPartida("Has perdut totes les vides!"); return;
        }
        if (partida.checkCompletat()) { partida.finalizarTiempo(); acabarPartida(null); }
    }

    private void donarPista(JButton btnPista) {
        if (partidaAcabada || partida.getDificultat().getNpistes() <= 0) return;
        desseleccionar();
        partida.donarPistes();
        repaintGrid(); repaintNums(); actualitzarInfo();
        btnPista.setText("Pista (" + partida.getDificultat().getNpistes() + ")");
        if (partida.getDificultat().getNpistes() == 0) btnPista.setEnabled(false);
        if (partida.checkCompletat()) { partida.finalizarTiempo(); acabarPartida(null); }
    }

    private void desferMoviment() {
        if (partidaAcabada || undoStack.isEmpty() || !partida.getDificultat().checkUndos()) return;
        desseleccionar();
        int[] mov = undoStack.pop();
        int i = mov[0], j = mov[1], prevVal = mov[2];
        Cella c = taulell.getCella(i, j);
        if (c == null || !c.esEditable()) return;
        if (!c.esBuida()) partida.buidarCasella(i, j);
        if (prevVal > 0 && taulell.potPosarNumero(i, j, prevVal)) partida.modificaHidato(prevVal, i, j);
        repaintCell(i, j); repaintNums(); actualitzarInfo();
    }

    private void mostrarDialogPausa() {
        if (swingTimer != null) swingTimer.stop();

        Window owner = SwingUtilities.getWindowAncestor(this);
        JDialog dlg = new JDialog(owner instanceof Frame ? (Frame) owner : null, "Pausa", true);
        dlg.setResizable(false);
        int[] result = {0}; // 0=continuar, 1=guardar, 2=sortir

        JPanel panel = new JPanel() {
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setPaint(new GradientPaint(0, 0, BG_TOP, 0, getHeight(), BG_BOT));
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.dispose();
            }
        };
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(35, 70, 35, 70));

        JLabel title = new JLabel("Pausa");
        title.setFont(new Font("SansSerif", Font.BOLD, 38));
        title.setForeground(new Color(100, 200, 255));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(title);
        panel.add(Box.createVerticalStrut(8));

        long secs = partida.getTempsPartida().toSeconds();
        JLabel lblT = new JLabel("Temps: " + formatTime(secs));
        lblT.setFont(new Font("SansSerif", Font.PLAIN, 15));
        lblT.setForeground(new Color(160, 190, 255));
        lblT.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(lblT);
        panel.add(Box.createVerticalStrut(30));

        Dimension btnSz = new Dimension(200, 40);

        JButton btnContinuar = VistaInicial.crearBoto("Continuar", new Color(30, 130, 60), new Color(45, 175, 85));
        btnContinuar.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnContinuar.setPreferredSize(btnSz); btnContinuar.setMaximumSize(btnSz);
        btnContinuar.addActionListener(e -> { result[0] = 0; dlg.dispose(); });

        JButton btnGuardar = VistaInicial.crearBoto("Guardar partida", BTN_NOR, BTN_HOV);
        btnGuardar.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnGuardar.setPreferredSize(btnSz); btnGuardar.setMaximumSize(btnSz);
        btnGuardar.addActionListener(e -> { result[0] = 1; dlg.dispose(); });

        JButton btnSortir = VistaInicial.crearBoto("Sortir", new Color(150, 35, 35), new Color(195, 55, 55));
        btnSortir.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnSortir.setPreferredSize(btnSz); btnSortir.setMaximumSize(btnSz);
        btnSortir.addActionListener(e -> { result[0] = 2; dlg.dispose(); });

        panel.add(btnContinuar);
        panel.add(Box.createVerticalStrut(10));
        panel.add(btnGuardar);
        panel.add(Box.createVerticalStrut(10));
        panel.add(btnSortir);

        dlg.setContentPane(panel);
        dlg.pack();
        dlg.setLocationRelativeTo(this);
        dlg.setVisible(true);

        if (result[0] == 2) {
            partidaAcabada = true;
            if (swingTimer != null) swingTimer.stop();
            frame.mostrarPantalla("menu");
        } else if (result[0] == 1) {
            JOptionPane.showMessageDialog(this, "Partida guardada!", "Guardar partida", JOptionPane.INFORMATION_MESSAGE);
            frame.getCtrlDomini().guardarPartida(partida);
            if (swingTimer != null) swingTimer.start();
        } else {
            if (swingTimer != null) swingTimer.start();
        }
    }

    private void acabarPartida(String extra) {
        partidaAcabada = true;
        if (swingTimer != null) swingTimer.stop();
        partida.calcularPuntuacio();
        float punts = partida.getPuntuacio();
        frame.getCtrlDomini().partidaFinalitzada(punts);
        mostrarDialogFi(punts, extra);
        frame.mostrarPantalla("menu");
    }

    // ── Timer ────────────────────────────────────────────────────────────────

    private void startTimer(TPartida mode, int tempsMins) {
        swingTimer = new javax.swing.Timer(1000, e -> {
            if (partidaAcabada) return;
            if (mode == TPartida.CONTRARRELLOTGE) {
                long restant = ((Contrarrellotge) partida).getTempsRestantSegons();
                lblTimer.setText(formatTime(restant));
                lblTimer.setForeground(restant <= 30 ? new Color(255, 80, 80) : new Color(100, 200, 255));
                if (restant <= 0) { ((Contrarrellotge) partida).marcarTempsEsgotat(); acabarPartida("Temps esgotat!"); }
            } else {
                lblTimer.setText(formatTime(partida.getTempsPartida().toSeconds()));
            }
        });
        swingTimer.start();
    }

    // ── Helpers ──────────────────────────────────────────────────────────────

    private String nomMode() {
        if (mode == TPartida.CONTRARRELLOTGE) return "Contrarrellotge";
        if (mode == TPartida.SUPERVIVENCIA)   return "Supervivència";
        return "Normal";
    }

    private void actualitzarInfo() {
        if (mode == TPartida.SUPERVIVENCIA)      lblInfo.setText("Vides: " + ((Supervivencia)partida).getNvides());
        else if (partida instanceof Contrarrellotge) lblInfo.setText("Errors: " + ((Contrarrellotge)partida).getErrors());
        else if (partida instanceof Normal)          lblInfo.setText("Errors: " + ((Normal)partida).getErrors());
    }

    private void repaintGrid() { for (JButton[] row : cellBtns) for (JButton b : row) b.repaint(); }
    private void repaintCell(int i, int j) { cellBtns[i][j].repaint(); }
    private void repaintNums() { }

    private void flashCell(int i, int j, Color col) {
        cellBtns[i][j].putClientProperty("flashColor", col);
        cellBtns[i][j].repaint();
        new javax.swing.Timer(500, ev -> {
            cellBtns[i][j].putClientProperty("flashColor", null);
            cellBtns[i][j].repaint();
            ((javax.swing.Timer)ev.getSource()).stop();
        }).start();
    }

    private void mostrarDialogFi(float punts, String extra) {
        boolean victoria = (extra == null);
        Window owner = SwingUtilities.getWindowAncestor(this);
        JDialog dlg = new JDialog(owner instanceof Frame ? (Frame) owner : null,
                victoria ? "Felicitats!" : "Fi de la partida", true);
        dlg.setResizable(false);

        JPanel panel = new JPanel() {
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setPaint(new GradientPaint(0, 0, BG_TOP, 0, getHeight(), BG_BOT));
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.dispose();
            }
        };
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(35, 55, 35, 55));

        JLabel title = new JLabel(victoria ? "Felicitats!" : "Fi de la partida");
        title.setFont(new Font("SansSerif", Font.BOLD, 36));
        title.setForeground(victoria ? new Color(255, 215, 0) : new Color(220, 80, 80));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(title);
        panel.add(Box.createVerticalStrut(16));

        if (!victoria) {
            JLabel reason = new JLabel(extra);
            reason.setFont(new Font("SansSerif", Font.PLAIN, 16));
            reason.setForeground(new Color(200, 200, 200));
            reason.setAlignmentX(Component.CENTER_ALIGNMENT);
            panel.add(reason);
            panel.add(Box.createVerticalStrut(14));
        }


        JLabel lblScore = new JLabel("Puntuació:  " + String.format("%.0f", punts) + " pts");
        lblScore.setFont(new Font("SansSerif", Font.BOLD, 24));
        lblScore.setForeground(Color.WHITE);
        lblScore.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(lblScore);
        panel.add(Box.createVerticalStrut(8));

        long secs = partida.getTempsPartida().toSeconds();
        JLabel lblTime = new JLabel("Temps:  " + formatTime(secs));
        lblTime.setFont(new Font("SansSerif", Font.PLAIN, 17));
        lblTime.setForeground(new Color(120, 170, 255));
        lblTime.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(lblTime);
        panel.add(Box.createVerticalStrut(28));

        JButton btnOk = VistaInicial.crearBoto("Tornar al menú", BTN_NOR, BTN_HOV);
        btnOk.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnOk.addActionListener(e -> dlg.dispose());
        panel.add(btnOk);

        dlg.setContentPane(panel);
        dlg.pack();
        dlg.setLocationRelativeTo(this);
        dlg.setVisible(true);
    }

    private String formatTime(long totalSec) {
        return String.format("%02d:%02d", totalSec/60, totalSec%60);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setPaint(new GradientPaint(0, 0, BG_TOP, 0, getHeight(), BG_BOT));
        g2.fillRect(0, 0, getWidth(), getHeight());
    }
}
