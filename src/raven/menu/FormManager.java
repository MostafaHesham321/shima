package raven.menu;

import com.formdev.flatlaf.extras.FlatAnimatedLafChange;
import java.awt.Image;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import raven.components.MainForm;
import raven.components.SimpleForm;
import raven.login.*;
import raven.model.ModelUser;
import raven.swing.slider.PanelSlider;
import raven.swing.slider.SimpleTransition;
import raven.utils.UndoRedo;

public class FormManager {

    private static FormManager instance;

    private final JFrame frame;
    private final UndoRedo<SimpleForm> forms = new UndoRedo<>();

    private boolean menuShowing = true;

    private final PanelSlider panelSlider;
    private final MainForm mainForm;
    private final Menu menu;
    private final boolean undecorated;

    // init
    public static void install(JFrame frame, boolean undecorated) {
        instance = new FormManager(frame, undecorated);
    }

    private FormManager(JFrame frame, boolean undecorated) {
        this.frame = frame;
        this.undecorated = undecorated;

        panelSlider = new PanelSlider();
        mainForm = new MainForm(undecorated);
        menu = new Menu(new MyDrawerBuilder());
    }

    // show menu
    public static void showMenu() {
        instance.menuShowing = true;

        instance.panelSlider.addSlide(
            instance.menu,
            SimpleTransition.getShowMenuTransition(
                instance.menu.getDrawerBuilder().getDrawerWidth(),
                instance.undecorated
            )
        );
    }

    // show form
    public static void showForm(SimpleForm form) {
        if (isNewFormAble()) {

            instance.forms.add(form);

            if (instance.menuShowing) {
                instance.menuShowing = false;

                Image oldImage = instance.panelSlider.createOldImage();

                instance.mainForm.setForm(form);

                instance.panelSlider.addSlide(
                    instance.mainForm,
                    SimpleTransition.getSwitchFormTransition(
                        oldImage,
                        instance.menu.getDrawerBuilder().getDrawerWidth()
                    )
                );

            } else {
                instance.mainForm.showForm(form);
            }

            instance.forms.getCurrent().formInitAndOpen();
        }
    }

    // navigation
    public static void gotoLogin() {
        FlatAnimatedLafChange.showSnapshot();
        updateTempFormUI();
        instance.frame.getContentPane().removeAll();
        instance.frame.getContentPane().add(new Login());

        instance.frame.repaint();
        instance.frame.revalidate();

        FlatAnimatedLafChange.hideSnapshotWithAnimation();
    }

    public static void gotoRegister() {
        FlatAnimatedLafChange.showSnapshot();

        instance.frame.getContentPane().removeAll();
        instance.frame.getContentPane().add(new Register());
        
        instance.frame.repaint();
        instance.frame.revalidate();

        FlatAnimatedLafChange.hideSnapshotWithAnimation();
    }

    public static void login(ModelUser user) {
        FlatAnimatedLafChange.showSnapshot();

        instance.frame.getContentPane().removeAll();
        instance.frame.getContentPane().add(instance.panelSlider);

        // set user + rebuild menu
        ((MyDrawerBuilder) instance.menu.getDrawerBuilder()).setUser(user);

        instance.frame.repaint();
        instance.frame.revalidate();

        FlatAnimatedLafChange.hideSnapshotWithAnimation();
    }

    public static void hideMenu() {
        instance.menuShowing = false;

        instance.panelSlider.addSlide(
            instance.mainForm,
            SimpleTransition.getHideMenuTransition(
                instance.menu.getDrawerBuilder().getDrawerWidth(),
                instance.undecorated
            )
        );
    }

    // undo / redo
    public static void undo() {
        if (isNewFormAble()) {
            if (!instance.menuShowing && instance.forms.isUndoAble()) {

                instance.mainForm.showForm(
                    instance.forms.undo(),
                    SimpleTransition.getDefaultTransition(true)
                );

                instance.forms.getCurrent().formOpen();
            }
        }
    }

    public static void redo() {
        if (isNewFormAble()) {
            if (!instance.menuShowing && instance.forms.isRedoAble()) {

                instance.mainForm.showForm(instance.forms.redo());
                instance.forms.getCurrent().formOpen();
            }
        }
    }

    // refresh current form
    public static void refresh() {
        if (!instance.menuShowing) {
            instance.forms.getCurrent().formRefresh();
        }
    }

    // helpers
    public static UndoRedo<SimpleForm> getForms() {
        return instance.forms;
    }

    public static boolean isNewFormAble() {
        return instance.forms.getCurrent() == null
            || instance.forms.getCurrent().formClose();
    }

    public static void updateTempFormUI() {
        for (SimpleForm f : instance.forms) {
            SwingUtilities.updateComponentTreeUI(f);
        }
    }
}