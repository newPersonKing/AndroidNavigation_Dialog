package me.listenzz.navigation;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.ashokvarma.bottomnavigation.TextBadgeItem;

import java.util.ArrayList;
import java.util.List;

public class DefaultTabBarProvider implements TabBarProvider {

    private TabBar tabBar;

    private TabBarFragment tabBarFragment;

    @Override
    public View onCreateTabBar(@NonNull List<TabBarItem> tabBarItems, @NonNull TabBarFragment tabBarFragment, @Nullable Bundle savedInstanceState) {
        TabBar tabBar = new TabBar(tabBarFragment.requireContext());
        this.tabBarFragment = tabBarFragment;
        this.tabBar = tabBar;
        initialise(tabBarItems);
        return tabBar;
    }

    @Override
    public void onDestroyTabBar() {
        tabBar = null;
        tabBarFragment = null;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

    }

    @Override
    public void setSelectedIndex(int index) {
        if (tabBar != null) {
            tabBar.selectTab(index, false);
        }
    }

    private void initialise(List<TabBarItem> tabBarItems) {
        tabBar.setMode(BottomNavigationBar.MODE_FIXED);
        tabBar.setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC);
        tabBar.setTabSelectedListener(new BottomNavigationBar.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int position) {
                Log.i("Navigation", "tab position:" + position);
                tabBarFragment.setSelectedIndex(position);
            }

            @Override
            public void onTabUnselected(int position) {

            }

            @Override
            public void onTabReselected(int position) {

            }
        });

        Context context = tabBarFragment.requireContext();

        List<TextBadgeItem> textBadgeItems = new ArrayList<>();

        for (int i = 0, size = tabBarItems.size(); i < size; i++) {
            TabBarItem tabBarItem = tabBarItems.get(i);
            BottomNavigationItem bottomNavigationItem;
            if (tabBarItem != null) {
                Drawable icon = new ColorDrawable();
                if (tabBarItem.iconRes != -1) {
                    icon = ContextCompat.getDrawable(context, tabBarItem.iconRes);
                } else if (tabBarItem.iconUri != null) {
                    icon = DrawableUtils.fromUri(context, tabBarItem.iconUri);
                }

                bottomNavigationItem = new BottomNavigationItem(icon, tabBarItem.title);

                if (tabBarItem.inactiveIconRes != -1) {
                    bottomNavigationItem.setInactiveIconResource(tabBarItem.inactiveIconRes);
                } else if (tabBarItem.inactiveIconUri != null) {
                    bottomNavigationItem.setInactiveIcon(DrawableUtils.fromUri(context, tabBarItem.inactiveIconUri));
                }
            } else {
                bottomNavigationItem = new BottomNavigationItem(new ColorDrawable(), "Tab");
            }

            TextBadgeItem textBadgeItem = new TextBadgeItem();
            textBadgeItem.setBackgroundColor("#FF3B30");
            textBadgeItems.add(textBadgeItem);
            bottomNavigationItem.setBadgeItem(textBadgeItem);
            tabBar.addItem(bottomNavigationItem);
        }

        onInitialise(tabBar);
        tabBar.initialise();

        for (TextBadgeItem item : textBadgeItems) {
            item.hide(false);
        }
    }

    private void onInitialise(TabBar tabBar) {
        Style style = tabBarFragment.style;
        tabBar.setBarBackgroundColor(style.getTabBarBackgroundColor());

        if (style.getTabBarActiveColor() != null) {
            tabBar.setActiveColor(style.getTabBarActiveColor());
        }

        if (style.getTabBarInActiveColor() != null) {
            tabBar.setInActiveColor(style.getTabBarInActiveColor());
        }

        tabBar.setShadow(style.getTabBarShadow());
    }

}
