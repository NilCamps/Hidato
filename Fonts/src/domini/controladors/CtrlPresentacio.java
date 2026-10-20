package domini.controladors;

import presentacio.AppFrame;
import javax.swing.SwingUtilities;

public class CtrlPresentacio {

    public static void IniciaCtrlPresentacio() {
        SwingUtilities.invokeLater(() -> {
            AppFrame frame = new AppFrame();
            frame.setVisible(true);
        });
    }

}

