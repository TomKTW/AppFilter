package dev.kobalt.core.utility;

import android.view.View;
import android.widget.AdapterView;

public class AdapterUtil {

    public static int getPositionForView(View view, AdapterView adapter) {
        {
            View listItem = view;
            try {
                View v;
                while ((v = (View) listItem.getParent()) != null && !v.equals(adapter)) {
                    listItem = v;
                }
            } catch (ClassCastException e) {
                // We made it up to the window without find this list view
                return -1;
            }

            //noinspection ConstantConditions
            if (listItem != null) {
                // Search the children for the list item
                final int childCount = adapter.getChildCount();
                for (int i = 0; i < childCount; i++) {
                    if (adapter.getChildAt(i).equals(listItem)) {
                        return adapter.getFirstVisiblePosition() + i;
                    }
                }
            }

            // Child not found!
            return -1;
        }
    }
}
