package raven.menu;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import java.awt.Color;
import java.awt.Component;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;

import raven.drawer.component.DrawerPanel;
import raven.drawer.component.SimpleDrawerBuilder;
import raven.drawer.component.footer.SimpleFooterData;
import raven.drawer.component.header.SimpleHeaderData;
import raven.drawer.component.header.SimpleHeaderStyle;
import raven.drawer.component.menu.MenuAction;
import raven.drawer.component.menu.MenuEvent;
import raven.drawer.component.menu.MenuValidation;
import raven.drawer.component.menu.SimpleMenuOption;
import raven.drawer.component.menu.SimpleMenuStyle;
import raven.drawer.component.menu.data.Item;
import raven.drawer.component.menu.data.MenuItem;

import raven.forms.*;
import raven.model.ModelUser;
import raven.swing.AvatarIcon;

public class MyDrawerBuilder extends SimpleDrawerBuilder {

    private ModelUser user;
    private final ThemesChange themesChange;

    public MyDrawerBuilder() {
        themesChange = new ThemesChange();
    }

    public void setUser(ModelUser user) {
        this.user = user;
        SimpleHeaderData headerData = header.getSimpleHeaderData();
        headerData.setTitle(user.getUserName());
        header.setSimpleHeaderData(headerData);
        rebuildMenu();
    }

    @Override
    public Component getFooter() {
        return themesChange;
    }

    @Override
    public SimpleHeaderData getSimpleHeaderData() {
        AvatarIcon icon = new AvatarIcon(
                getClass().getResource("/raven/resources/image/profile.png"),
                60, 60, 999
        );
        icon.setBorder(2);

        return new SimpleHeaderData()
                .setIcon(icon)
                .setTitle("Ra Ven")
                .setDescription("raven@gmail.com")
                .setHeaderStyle(new SimpleHeaderStyle() {
                    @Override
                    public void styleTitle(JLabel label) {
                        label.putClientProperty(
                                FlatClientProperties.STYLE,
                                "[light]foreground:#FAFAFA"
                        );
                    }

                    @Override
                    public void styleDescription(JLabel label) {
                        label.putClientProperty(
                                FlatClientProperties.STYLE,
                                "[light]foreground:#E1E1E1"
                        );
                    }
                });
    }

    @Override
    public SimpleFooterData getSimpleFooterData() {
        return new SimpleFooterData();
    }

    @Override
    public SimpleMenuOption getSimpleMenuOption() {

        MenuItem items[] = new MenuItem[]{
            new Item("Dashboard", "dashboard.svg"),
            new Item("Transactions", "transactions.svg"),
            new Item("Budgets", "wallet.svg"),
            new Item("Goals", "target.svg"),
            new Item("Analytics", "analytics.svg"),
            new Item("AI Insights", "lightbulb.svg"),
            new Item("Family Budget", "users.svg"),
            new Item("Notifications", "bell.svg"),
            new Item("Logout", "logout.svg"),
        };

        SimpleMenuOption option = new SimpleMenuOption() {
            @Override
            public Icon buildMenuIcon(String path, float scale) {
                FlatSVGIcon icon = new FlatSVGIcon(path, scale);

                FlatSVGIcon.ColorFilter filter = new FlatSVGIcon.ColorFilter();
                filter.add(Color.decode("#969696"), Color.decode("#FAFAFA"), Color.decode("#969696"));

                icon.setColorFilter(filter);
                return icon;
            }
        };

        // صلاحيات المستخدم
        option.setMenuValidation(new MenuValidation() {

            private boolean checkMenu(int[] index, int[] hide) {
                if (index.length == hide.length) {
                    for (int i = 0; i < index.length; i++) {
                        if (index[i] != hide[i]) {
                            return true;
                        }
                    }
                    return false;
                }
                return true;
            }

            @Override
            public boolean menuValidation(int[] index) {
                if (user == null) return false;

                if (!user.isAdmin()) {
                    return checkMenu(index, new int[]{1, 2, 3})
                        && checkMenu(index, new int[]{1, 2, 5})
                        && checkMenu(index, new int[]{1, 2, 2, 3})
                        && checkMenu(index, new int[]{4, 1});
                }
                return true;
            }
        });

        // الشكل
        option.setMenuStyle(new SimpleMenuStyle() {

            @Override
            public void styleMenuItem(JButton menu, int[] index) {
                menu.putClientProperty(
                        FlatClientProperties.STYLE,
                        "[light]foreground:#FAFAFA;arc:10"
                );
            }

            @Override
            public void styleMenu(JComponent component) {
                component.putClientProperty(
                        FlatClientProperties.STYLE,
                        "background:$Drawer.background"
                );
            }

            @Override
            public void styleLabel(JLabel label) {
                label.putClientProperty(
                        FlatClientProperties.STYLE,
                        "[light]foreground:darken(#FAFAFA,15%);"
                        + "[dark]foreground:darken($Label.foreground,30%)"
                );
            }
        });

        // الأحداث
        option.addMenuEvent(new MenuEvent() {
            @Override
            public void selected(MenuAction action, int[] index) {

                if (index.length == 1) {
                    switch (index[0]) {
                        case 0:
                            FormManager.showForm(new DashboardForm());
                            break;
                        case 1:
                            FormManager.showForm(new TransactionForm());
                            break;
                        case 2:
                            FormManager.showForm(new BudgetForm());
                            break;   
                        case 3:
                            FormManager.showForm(new GoalsForm());
                            break;
                        case 8:
                            FormManager.gotoLogin();
                            break;
                    }
                }
            }
        });

        option.setMenus(items)
              .setBaseIconPath("raven/resources/menu")
              .setIconScale(0.45f);

        return option;
    }

    @Override
    public void build(DrawerPanel drawerPanel) {
        drawerPanel.putClientProperty(
                FlatClientProperties.STYLE,
                "background:$Drawer.background"
        );
    }

    @Override
    public int getDrawerWidth() {
        return 270;
    }
}