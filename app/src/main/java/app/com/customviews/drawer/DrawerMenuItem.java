package app.com.customviews.drawer;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mindorks.placeholderview.annotations.Click;
import com.mindorks.placeholderview.annotations.Layout;
import com.mindorks.placeholderview.annotations.NonReusable;
import com.mindorks.placeholderview.annotations.Resolve;
import com.mindorks.placeholderview.annotations.View;

import app.com.customviews.R;

@NonReusable
@Layout(R.layout.drawer_item)
public class DrawerMenuItem {

    public static final int DRAWER_MENU_ITEM_PROFILE = 1;
    public static final int DRAWER_MENU_ITEM_REQUESTS = 2;
    public static final int DRAWER_MENU_ITEM_GROUPS = 3;
    public static final int DRAWER_MENU_ITEM_MESSAGE = 4;
    public static final int DRAWER_MENU_ITEM_NOTIFICATIONS = 5;
    public static final int DRAWER_MENU_ITEM_SETTINGS = 6;
    public static final int DRAWER_MENU_ITEM_TERMS = 7;
    public static final int DRAWER_MENU_ITEM_LOGOUT = 8;

    private int mMenuPosition;
    private Context context;
    private DrawerCallback mCallback;

    @View(R.id.itemNameTxt)
    private TextView itemNameTxt;

    @View(R.id.itemIcon)
    private ImageView itemIcon;


    public DrawerMenuItem(int mMenuPosition, Context context) {
        this.mMenuPosition = mMenuPosition;
        this.context = context;
    }

    @Resolve
    private void onResolved() {
        switch (mMenuPosition) {
            case DRAWER_MENU_ITEM_PROFILE:
                itemNameTxt.setText("Profile");
                itemIcon.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_profile));
                break;
            case DRAWER_MENU_ITEM_REQUESTS:
                itemNameTxt.setText("Requests");
                itemIcon.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_request));
                break;
            case DRAWER_MENU_ITEM_GROUPS:
                itemNameTxt.setText("Groups");
                itemIcon.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_group));
                break;
            case DRAWER_MENU_ITEM_MESSAGE:
                itemNameTxt.setText("Message");
                itemIcon.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_message));
                break;
            case DRAWER_MENU_ITEM_NOTIFICATIONS:
                itemNameTxt.setText("Notifications");
                itemIcon.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_notif));
                break;
            case DRAWER_MENU_ITEM_SETTINGS:
                itemNameTxt.setText("Settings");
                itemIcon.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_settings));
                break;
            case DRAWER_MENU_ITEM_TERMS:
                itemNameTxt.setText("Terms");
                itemIcon.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_terms));
                break;
            case DRAWER_MENU_ITEM_LOGOUT:
                itemNameTxt.setText("Logout");
                itemIcon.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_logout));
                break;
        }
    }

    @Click(R.id.mainView)
    private void onMenuItemClick() {
        switch (mMenuPosition) {
            case DRAWER_MENU_ITEM_PROFILE:
                Toast.makeText(context, "Profile", Toast.LENGTH_SHORT).show();
                if(mCallback != null) mCallback.onProfileMenuSelected();
                break;
            case DRAWER_MENU_ITEM_REQUESTS:
                Toast.makeText(context, "Requests", Toast.LENGTH_SHORT).show();
                if(mCallback != null) mCallback.onRequestMenuSelected();
                break;
            case DRAWER_MENU_ITEM_GROUPS:
                Toast.makeText(context, "Groups", Toast.LENGTH_SHORT).show();
                if(mCallback != null) mCallback.onGroupsMenuSelected();
                break;
            case DRAWER_MENU_ITEM_MESSAGE:
                Toast.makeText(context, "Message", Toast.LENGTH_SHORT).show();
                if(mCallback != null) mCallback.onMessageMenuSelected();
                break;
            case DRAWER_MENU_ITEM_NOTIFICATIONS:
                Toast.makeText(context, "Notifications", Toast.LENGTH_SHORT).show();
                if(mCallback != null) mCallback.onNotificationMenuSelected();
                break;
            case DRAWER_MENU_ITEM_SETTINGS:
                Toast.makeText(context, "Settings", Toast.LENGTH_SHORT).show();
                if(mCallback != null) mCallback.onSettingsMenuSelected();
                break;
            case DRAWER_MENU_ITEM_TERMS:
                Toast.makeText(context, "Terms", Toast.LENGTH_SHORT).show();
                if(mCallback != null) mCallback.onTermsMenuSelected();
                break;
            case DRAWER_MENU_ITEM_LOGOUT:
                Toast.makeText(context, "Logout", Toast.LENGTH_SHORT).show();
                if(mCallback != null) mCallback.onLogoutMenuSelected();
                break;
        }
    }

    private void setDrawerCallback(DrawerCallback callback) {
        mCallback = callback;
    }

    public interface DrawerCallback {
        void onProfileMenuSelected();
        void onRequestMenuSelected();
        void onGroupsMenuSelected();
        void onMessageMenuSelected();
        void onNotificationMenuSelected();
        void onSettingsMenuSelected();
        void onTermsMenuSelected();
        void onLogoutMenuSelected();
    }

}
