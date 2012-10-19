package info.guardianproject.mrappuitest;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SceneEditorNoSwipe extends SherlockFragmentActivity implements ActionBar.TabListener {

    private static final String STATE_SELECTED_NAVIGATION_ITEM = "selected_navigation_item";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scene_editor_no_swipe);

        // Set up the action bar.
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // For each of the sections in the app, add a tab to the action bar.
        actionBar.addTab(actionBar.newTab().setText(R.string.tab_add_clips).setTabListener(this));
        actionBar.addTab(actionBar.newTab().setText(R.string.tab_order).setTabListener(this));
        actionBar.addTab(actionBar.newTab().setText(R.string.tab_publish).setTabListener(this));
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState.containsKey(STATE_SELECTED_NAVIGATION_ITEM)) {
            getSupportActionBar().setSelectedNavigationItem(
                    savedInstanceState.getInt(STATE_SELECTED_NAVIGATION_ITEM));
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(STATE_SELECTED_NAVIGATION_ITEM,
                getSupportActionBar().getSelectedNavigationIndex());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getSupportMenuInflater().inflate(R.menu.activity_scene_editor_no_swipe, menu);
        return true;
    }

    

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        // When the given tab is selected, show the tab contents in the container
        int layout = R.layout.fragment_add_clips;
        if (tab.getPosition() == 1) {
            layout = R.layout.fragment_order_clips;
        } else if (tab.getPosition() == 2) {
            layout = R.layout.fragment_story_publish;
        } 
        Fragment fragment = new DummySectionFragment(layout, getSupportFragmentManager());
        Bundle args = new Bundle();
        args.putInt(DummySectionFragment.ARG_SECTION_NUMBER, tab.getPosition() + 1);
        fragment.setArguments(args);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, fragment)
                .commit();
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    /**
     * A dummy fragment representing a section of the app, but that simply displays dummy text.
     */
    public static class DummySectionFragment extends Fragment {
        int layout;
        ViewPager mClipViewPager;
        ClipPagerAdapter mClipPagerAdapter;
        
        public DummySectionFragment(int layout, FragmentManager fm) {
            this.layout = layout;
            mClipPagerAdapter = new ClipPagerAdapter(fm);
        }

        public static final String ARG_SECTION_NUMBER = "section_number";

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View view = inflater.inflate(layout, null);
            if (this.layout == R.layout.fragment_add_clips) {

              // Set up the clip ViewPager with the clip adapter.
              mClipViewPager = (ViewPager) view.findViewById(R.id.viewPager);
              (new AsyncTask<Void, Void, Void>() {
                  @Override
                  protected void onPostExecute(Void result) {
                      mClipViewPager.setAdapter(mClipPagerAdapter);
                  }

                @Override
                protected Void doInBackground(Void... params) {
                    // TODO Auto-generated method stub
                    return null;
                }
              }).execute();
              
            } else if (this.layout == R.layout.fragment_order_clips) {
            } else if (this.layout == R.layout.fragment_story_publish) {
                
            }
            return view;
        }
        
        /**
         * A {@link FragmentPagerAdapter} that returns a fragment corresponding to the clips we are editing
         */
        public class ClipPagerAdapter extends FragmentPagerAdapter {

            public ClipPagerAdapter(FragmentManager fm) {
                super(fm);
            }

            @Override
            public Fragment getItem(int i) {
                Fragment fragment = new ClipThumbnailFragment();
                Bundle args = new Bundle();
                args.putInt(DummySectionFragment.ARG_SECTION_NUMBER, i);
                fragment.setArguments(args);
                return fragment;
            }

            @Override
            public int getCount() {
                return 5;
            }
        }
    }
    
    /**
     * A dummy fragment representing a section of the app, but that simply displays dummy text.
     */
    public static class ClipThumbnailFragment extends Fragment {
        public ClipThumbnailFragment() {
        }

        public static final String ARG_CLIP_TYPE_ID = "clip_type_id";

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            int i = getArguments().getInt(ARG_CLIP_TYPE_ID, 0);
            
            LinearLayout ll = new LinearLayout(getActivity());
            ImageView iv = new ImageView(getActivity());

            TypedArray drawableIds = getActivity().getResources().obtainTypedArray(R.array.cliptype_thumbnails);
            int drawableId = drawableIds.getResourceId(i, -1); // FIXME handle -1
            Drawable d = getActivity().getResources().getDrawable(drawableId);
            
            iv.setImageDrawable(d);
            ll.addView(iv);
            
            return (View) ll;
        }
    }
}
